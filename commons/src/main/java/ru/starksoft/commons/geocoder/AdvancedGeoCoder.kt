package ru.starksoft.commons.geocoder

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class AdvancedGeoCoder constructor(context: Context, private val coderType: GeocoderType) {

    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
    private val googleApiKey = "" //context.getString(R.string.google_map_api_key)
    private val yandexApiKey = "" //context.getString(R.string.yandex_api_key)

    fun tryToGetLocation(lat: Double, lon: Double, priority: Array<GeocoderType>): Address? {
        Log.d(
            TAG,
            "tryToGetLocation() called with: lat = [$lat], lon = [$lon], priority = [$priority]"
        )

        for (geocoderType in priority) {
            Log.d(TAG, "tryToGetLocation: geocoderType=$geocoderType")

            val address = try {
                getFromLocation(lat, lon, geocoderType)
            } catch (e: Exception) {
                Log.d(TAG, "tryToGetLocation: e=$e")
                null
            }

            Log.d(TAG, "tryToGetLocation: address=$address")

            if (address != null) {
                return address
            }
        }

        Log.d(TAG, "tryToGetLocation: no location was found, just returning null")
        return null
    }

    private fun getFromLocation(lat: Double, lon: Double, geocoderType: GeocoderType): Address? {
        return when (geocoderType) {
            GeocoderType.GOOGLE_INTERNAL -> geocoder.getFromLocation(lat, lon, MAX_RESULTS)?.getOrNull(0)
            GeocoderType.GOOGLE_EXTERNAL -> getLocationFromGoogle(lat, lon)
            GeocoderType.YANDEX -> getLocationFromYandex(lat, lon)
        }
    }

    fun getFromLocation(lat: Double, lon: Double): List<Address> {
        val addressList = ArrayList<Address>()
        try {
            when (coderType) {
                GeocoderType.GOOGLE_INTERNAL -> {
                    val fromLocation: List<Address>
                    try {
                        fromLocation = geocoder.getFromLocation(lat, lon, MAX_RESULTS).orEmpty()
                        addressList.addAll(fromLocation)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    if (addressList.isEmpty()) {
                        getLocationFromYandex(lat, lon)?.let { addressList.add(it) }
                    }
                }

                GeocoderType.YANDEX -> getLocationFromYandex(lat, lon)?.let { addressList.add(it) }

                GeocoderType.GOOGLE_EXTERNAL -> getLocationFromGoogle(
                    lat,
                    lon
                )?.let { addressList.add(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Collections.unmodifiableList(addressList)
    }

    fun getFromLocationName(locationName: String): List<Address> {
        val addressList = ArrayList<Address>()
        try {
            when (coderType) {
                GeocoderType.GOOGLE_INTERNAL -> {
                    try {
                        addressList.addAll(geocoder.getFromLocationName(locationName, MAX_RESULTS).orEmpty())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    if (addressList.isEmpty()) {
                        getLocationNameFromYandex(locationName)?.let { addressList.add(it) }
                    }
                }
                GeocoderType.GOOGLE_EXTERNAL -> getLocationNameFromGoogle(locationName)?.let {
                    addressList.add(
                        it
                    )
                }
                GeocoderType.YANDEX -> addressList.addAll(getLocationNameFromYandex2(locationName))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Collections.unmodifiableList(addressList)
    }

    private fun getLocationFromYandex(lat: Double, lon: Double): Address? {
        val response = getLocationInfo(lat, lon, GeocoderType.YANDEX)
        Log.d(TAG, response.toString())
        return parseYandexResponse(response)
    }

    private fun getLocationNameFromYandex(locationName: String): Address? {
        val response = getLocationInfo(locationName, GeocoderType.YANDEX)
        Log.d(TAG, response.toString())
        return parseYandexResponse(response)
    }

    private fun getLocationNameFromYandex2(locationName: String): List<Address> {
        return parseYandexResponse2(getLocationInfo(locationName, GeocoderType.YANDEX))
    }

    private fun parseYandexResponse(response: JSONObject?): Address? {
        if (response == null) {
            return null
        }
        var addressType: Address? = null
        var lat = 0.0
        var lon = 0.0
        try {
            var town: String? = null
            var street: String? = null
            var house: String? = null

            val jsonArray = response.getJSONObject("response").getJSONObject("GeoObjectCollection")
                .getJSONArray("featureMember")
            if (jsonArray.length() > 0) {
                val geoObject = (jsonArray.get(0) as JSONObject).getJSONObject("GeoObject")
                val lonLat = geoObject.getJSONObject("Point").getString("pos")
                try {
                    val result =
                        lonLat.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    lon = java.lang.Double.parseDouble(result[0])
                    lat = java.lang.Double.parseDouble(result[1])
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                val addressJsonObject =
                    geoObject.getJSONObject("metaDataProperty").getJSONObject("GeocoderMetaData")
                        .getJSONObject("Address")
                val locationArray = addressJsonObject.getJSONArray("Components")
                for (i in 0 until locationArray.length()) {
                    val kind = (locationArray.get(i) as JSONObject).getString("kind")
                    val name = (locationArray.get(i) as JSONObject).getString("name")
                    when (kind) {
                        "locality" -> town = name
                        "street" -> street = name
                        "house" -> house = name
                    }
                }

                Log.d(TAG, "Yandex geocoder get response: $town, $street, $house")
                addressType = Address(Locale.getDefault())
                addressType.locality = town
                addressType.thoroughfare = street
                addressType.featureName = house
                addressType.latitude = lat
                addressType.longitude = lon
            }

        } catch (e1: Exception) {
            e1.printStackTrace()
        }

        return addressType
    }

    private fun parseYandexResponse2(response: JSONObject?): List<Address> {
        Log.d(TAG, "parseYandexResponse2() called with: response = [$response]")
        if (response == null) {
            return emptyList()
        }
        val result: MutableList<Address> = ArrayList()

        val jsonArray = response.getJSONObject("response").getJSONObject("GeoObjectCollection")
            .getJSONArray("featureMember")

        val length = jsonArray.length()

        for (i in 0 until length) {
            try {
                val geoObject = (jsonArray.get(i) as JSONObject).getJSONObject("GeoObject")

                val fallBack = geoObject.getString("name")
                val lonLat = geoObject.getJSONObject("Point").getString("pos")

                var lat = 0.0
                var lon = 0.0

                try {
                    val coordinates =
                        lonLat.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    lon = coordinates[0].toDouble()
                    lat = coordinates[1].toDouble()

                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                val addressJsonObject = geoObject
                    .getJSONObject("metaDataProperty")
                    .getJSONObject("GeocoderMetaData")
                    .getJSONObject("Address")

                var town: String? = null
                var street: String? = null
                var house: String? = null
                var province: String? = null
                var country: String? = null

                val locationArray = addressJsonObject.getJSONArray("Components")
                for (o in 0 until locationArray.length()) {
                    val components = locationArray.get(o) as JSONObject
                    val kind = components.getString("kind")
                    val name = components.getString("name")
                    when (kind) {
                        "locality" -> town = name
                        "street" -> street = name
                        "house" -> house = name
                        "province" -> province = name
                        "country" -> country = name
                    }
                }

                val addressType = Address(Locale.getDefault())
                addressType.locality = town ?: fallBack
                addressType.thoroughfare = street
                addressType.featureName = house
                addressType.latitude = lat
                addressType.longitude = lon
                addressType.adminArea = province
                addressType.countryName = country

                result.add(addressType)

                if (i > 10) {
                    break
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return result
    }

    @Throws(Exception::class)
    private fun getLocationFromGoogle(lat: Double, lon: Double): Address? {
        return getLocationInfo(
            lat,
            lon,
            GeocoderType.GOOGLE_EXTERNAL
        )?.let { parseGoogleResponse(it) }
    }

    @Throws(Exception::class)
    private fun getLocationNameFromGoogle(locationName: String): Address? {
        return getLocationInfo(
            locationName,
            GeocoderType.GOOGLE_EXTERNAL
        )?.let { parseGoogleResponse(it) }
    }

    private fun parseGoogleResponse(response: JSONObject): Address? {
        var addressType: Address? = null
        val lon: Double
        val lat: Double
        try {
            var town: String? = null
            var street: String? = null
            var house: String? = null

            val jsonArray = response.getJSONArray("results")
            if (jsonArray.length() > 0) {
                val addressJsonObject = jsonArray.get(0) as JSONObject
                var location = addressJsonObject.getJSONObject("geometry")
                location = location.getJSONObject("location")

                lon = location.getDouble("lng") // долгота
                lat = location.getDouble("lat") // широта

                val addressComponents = addressJsonObject.getJSONArray("address_components")
                for (i in 0 until addressComponents.length()) {

                    val types = (addressComponents.get(i) as JSONObject).getJSONArray("types")
                    for (j in 0 until types.length()) {
                        when (types.get(j) as String) {
                            "street_number" -> house = getComponentValue(addressComponents, i)
                            "route" -> street = getComponentValue(addressComponents, i)
                            "locality" -> town = getComponentValue(addressComponents, i)
                        }
                    }
                }

                Log.d(TAG, "Google geocoder get response: $town, $street, $house")
                addressType = Address(Locale.getDefault())
                addressType.locality = town
                addressType.thoroughfare = street
                addressType.featureName = house
                addressType.latitude = lat
                addressType.longitude = lon
            }

        } catch (exception: Exception) {
            exception.printStackTrace()
            return null

        }

        return addressType
    }

    @Throws(JSONException::class)
    private fun getComponentValue(addressComponents: JSONArray, i: Int): String? {
        return (addressComponents.get(i) as JSONObject).getString("long_name")
    }

    private fun getLocationInfo(lat: Double, lon: Double, coderType: GeocoderType): JSONObject? {
        val url: String = when (coderType) {

            GeocoderType.GOOGLE_EXTERNAL -> createGoogleBaseUrl().appendQueryParameter(
                "latlng",
                "$lat,$lon"
            ).toString()

            GeocoderType.YANDEX -> createYandexBaseUrl().appendQueryParameter(
                "geocode",
                "$lon,$lat"
            ).toString()

            else -> throw IllegalStateException("Unknown geocoder type: $coderType")
        }

        return requestJsonObject(url)
    }

    private fun getLocationInfo(locationName: String, coderType: GeocoderType): JSONObject? {
        val url: String = when (coderType) {

            GeocoderType.GOOGLE_EXTERNAL -> createGoogleBaseUrl().appendQueryParameter(
                "address",
                locationName
            ).toString()

            GeocoderType.YANDEX -> createYandexBaseUrl().appendQueryParameter(
                "geocode",
                locationName
            ).toString()

            else -> throw IllegalStateException("Unknown geocoder type: $coderType")
        }
        return requestJsonObject(url)
    }

    @Synchronized
    private fun requestJsonObject(url: String): JSONObject? {
        val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection

        connection.connectTimeout = 30_000
        connection.readTimeout = 90_000
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-type", "application/json");

        return try {
            val response = if (connection.responseCode in 200..299) {
                connection.inputStream.bufferedReader().readText()
            } else {
                connection.errorStream.bufferedReader().readText()
            }
            JSONObject(response)

        } catch (e: Exception) {
            e.printStackTrace()
            null

        } finally {
            connection.disconnect()
        }
    }

    private fun createYandexBaseUrl(): Uri.Builder {
        return Uri.parse(YANDEX_GEOCODE_URL)
            .buildUpon()
            .appendQueryParameter("lang", "ru_RU")
            .appendQueryParameter("apiKey", yandexApiKey)
    }

    private fun createGoogleBaseUrl(): Uri.Builder {
        return Uri.parse(GOOGLE_GEOCODE_URL)
            .buildUpon()
            .appendQueryParameter("language", "ru")
            .appendQueryParameter("key", googleApiKey)
    }

    enum class GeocoderType {
        GOOGLE_EXTERNAL, GOOGLE_INTERNAL, YANDEX
    }

    companion object {

        private const val TAG = "AdvancedGeoCoder"
        private const val MAX_RESULTS = 1
        private const val YANDEX_GEOCODE_URL = "https://geocode-maps.yandex.ru/1.x/?format=json"
        private const val GOOGLE_GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json"

        @JvmStatic
        fun newYandexInstance(context: Context): AdvancedGeoCoder {
            return AdvancedGeoCoder(context, GeocoderType.YANDEX)
        }
    }
}
