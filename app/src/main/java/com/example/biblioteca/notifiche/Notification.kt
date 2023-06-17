import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.icu.util.Calendar
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.biblioteca.MainActivity
import com.example.biblioteca.R

class NotificationScheduler(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "my_channel"//canale con id unitario per ricevere la notifica
        private const val NOTIFICATION_ID = 1 //id notifica
        private const val ACTION_SHOW_NOTIFICATION = "com.example.MY_CUSTOM_ACTION"
    }

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_SHOW_NOTIFICATION) {
                // Crea la notifica
                val notification = NotificationCompat.Builder(context!!, CHANNEL_ID)
                    .setContentTitle("Consegna Libro prevista per domani")
                    .setContentText("per posticipare entra nell'app")
                    .setSmallIcon(R.drawable.icona)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()

                val notificationManager = NotificationManagerCompat.from(context)
                createNotificationChannel()

                if (ActivityCompat.checkSelfPermission( //permessi notifica
                        context.applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        }
    }

    fun scheduleNotification(anno: Int,mese:Int,giorno: Int) {//setto la data in cui ricevere la notifica
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, anno)
        calendar.set(Calendar.MONTH, Calendar.JUNE)
        calendar.set(Calendar.DAY_OF_MONTH, giorno)
        calendar.set(Calendar.HOUR_OF_DAY, 19) // Ora in formato 24 ore
        calendar.set(Calendar.MINUTE, 35)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(ACTION_SHOW_NOTIFICATION)
        intent.action = "com.example.MY_CUSTOM_ACTION" // Azione personalizzata
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)//alarmManager memorizza la data in cui inviare la notifica

    }


    fun registerNotificationReceiver() { //registratore di notifica
        val filter = IntentFilter(ACTION_SHOW_NOTIFICATION)
        context.registerReceiver(notificationReceiver, filter)
    }

    private fun createNotificationChannel() {  //creo il canale di notifica
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
