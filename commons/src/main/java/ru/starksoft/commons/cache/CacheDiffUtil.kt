package ru.starksoft.commons.cache

import android.util.Log

object CacheDiffUtil {

    private const val TAG = "CacheDiffUtil"

    @JvmStatic
    fun <T : CacheEntry> calculateDiff(oldList: List<T>, newList: List<T>): DiffResult<T> {
        val startTime = System.currentTimeMillis()
        Log.d(TAG, "calculateDiff: started")
        val fastFindMap: MutableMap<String, T> = HashMap()
        val toAdd: MutableList<T> = ArrayList()
        val toUpdate: MutableList<T> = ArrayList()
        val toRemove: MutableList<String> = ArrayList()
        val exists: MutableList<String> = ArrayList()
        for (t in oldList) {
            val entryId = t.getEntryId()
            fastFindMap[entryId] = t
            toRemove.add(entryId)
        }
        for (originEntry in newList) {
            val entryId = originEntry.getEntryId()

            // Удаление
            exists.add(entryId)

            // Добавим, если нет
            if (!toRemove.contains(entryId)) {
                Log.d(TAG, "calculateDiff: adding")
                toAdd.add(originEntry)
            } else {
                // Изменение
                val item = fastFindMap[entryId]
                val itemContentHashCode = item?.getContentHashCode() ?: 0
                val originEntryContentHashCode = originEntry.getContentHashCode()
                if (itemContentHashCode != originEntryContentHashCode) {
                    Log.d(TAG, "calculateDiff: changing")
                    toUpdate.add(originEntry)
                }
            }
        }

        // Удаляем все существующие значения
        toRemove.removeAll(exists)
        Log.d(TAG, "calculateDiff: toAdd=" + toAdd.size)
        Log.d(TAG, "calculateDiff: toUpdate=" + toUpdate.size)
        Log.d(TAG, "calculateDiff: toRemove=" + toRemove.size)
        Log.d(TAG, "calculateDiff: done in " + (System.currentTimeMillis() - startTime) + " ms")
        return DiffResult(toAdd, toUpdate, toRemove)
    }

    private fun <T : CacheEntry> createFindMap(oldList: List<T>): Map<String, T> {
        val map: MutableMap<String, T> = HashMap()
        for (t in oldList) {
            map[t.getEntryId()] = t
        }
        return map
    }

    private fun <T : CacheEntry> getAllIds(oldList: List<T>): List<String> {
        val result: MutableList<String> = ArrayList()
        for (cacheEntry in oldList) {
            result.add(cacheEntry.getEntryId())
        }
        return result
    }
}
