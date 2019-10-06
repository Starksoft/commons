package ru.starksoft.commons.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlin.LazyThreadSafetyMode.NONE

@Suppress("MemberVisibilityCanBePrivate")
object NotificationHelper {

	@JvmStatic
	fun newBuilder(context: Context): Builder {
		return Builder(context)
	}

	data class Builder constructor(
		private val context: Context,
		private var id: Int = 0,
		private var icon: Int = 0,
		private var largeIcon: Bitmap? = null,
		private var title: String? = null,
		private var message: String? = null,
		private var defaultSound: Boolean = false,
		private var soundUri: Uri? = null,
		private var autoCancel: Boolean = false,
		private var ongoing: Boolean = false,
		private var localOnly: Boolean = false,
		private var pendingIntent: PendingIntent? = null,
		private var channelName: String? = null,
		private var channelId: String? = null,
		private var style: NotificationCompat.Style? = null,
		private var channel: NotificationChannel? = null

	) {
		private val notificationManager by lazy(NONE) { ContextCompat.getSystemService(context, NotificationManager::class.java)!! }

		fun defaultSound(): Builder {
			defaultSound = true
			return this
		}

		fun soundUri(soundUri: Uri): Builder {
			this.soundUri = soundUri
			return this
		}

		fun title(title: String): Builder {
			this.title = title
			return this
		}

		fun message(message: String): Builder {
			this.message = message
			return this
		}

		fun autoCancel(): Builder {
			autoCancel = true
			return this
		}

		fun ongoing(ongoing: Boolean): Builder {
			this.ongoing = ongoing
			return this
		}

		fun localOnly(localOnly: Boolean): Builder {
			this.localOnly = localOnly
			return this
		}

		fun id(id: Int): Builder {
			this.id = id
			return this
		}

		fun icon(@DrawableRes icon: Int): Builder {
			this.icon = icon
			return this
		}

		fun largeIcon(largeIcon: Bitmap): Builder {
			this.largeIcon = largeIcon
			return this
		}

		fun channelName(channelName: String): Builder {
			this.channelName = channelName
			return this
		}

		fun pendingIntent(pendingIntent: PendingIntent): Builder {
			this.pendingIntent = pendingIntent
			return this
		}

		fun channelId(channelId: String): Builder {
			this.channelId = channelId
			return this
		}

		fun style(style: NotificationCompat.Style): Builder {
			this.style = style
			return this
		}

		fun show() {
			notificationManager.notify(id, build())
		}

		fun build(): Notification {
			check(id >= 0) { "Invalid id" }
			check(icon >= 1) { "Invalid icon" }
			check(!title.isNullOrBlank()) { "Empty title" }
			check(!message.isNullOrBlank()) { "Empty message" }
			check(!channelId.isNullOrBlank()) { "Empty channelId" }
			check(!channelName.isNullOrBlank()) { "Empty channelName" }

			val notificationBuilder = NotificationCompat.Builder(context, channelId!!)

			if (soundUri != null) {
				notificationBuilder.setSound(soundUri)
			} else if (defaultSound) {
				val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
				notificationBuilder.setSound(defaultSoundUri)
			}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				if (channel != null) {
					val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
					notificationChannel.enableLights(false)
					notificationChannel.lightColor = Color.BLUE
					notificationChannel.enableVibration(false)

					notificationManager.createNotificationChannel(notificationChannel)
				} else {
					notificationManager.createNotificationChannel(channel!!)
				}
			}

			return notificationBuilder.setSmallIcon(icon)
				.setLargeIcon(largeIcon)
				.setStyle(style)
				.setLocalOnly(localOnly)
				.setContentIntent(pendingIntent)
				.setContentTitle(title)
				.setContentText(message)
				.setAutoCancel(autoCancel)
				.build()
		}
	}
}
