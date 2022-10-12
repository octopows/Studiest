import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.resources.Compatibility.Api18Impl.setAutoCancel
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.studiest.MainActivity
import com.example.studiest.R
import com.example.studiest.adicionar_item
import com.example.studiest.tela_principal


object NotificationUtils {

    val CHANNEL_ID = "default"
    @RequiresApi(Build.VERSION_CODES.O)

    private fun createNotificationChannel(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName = context.getString(R.string.notif_channel_name)
        val channelDescription = context.getString(R.string.notif_channel_description)
        val channel = NotificationChannel(
            CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = channelDescription
            enableLights(true)
            lightColor = Color.GREEN
            enableVibration(true)
            vibrationPattern = longArrayOf(0,200,100,200)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun notificationAvaliacao(context: Context, titulo: String?, tipo: Int?, id:Int) {
        var Tipo: String? = null
        if(tipo == 0 ){
            Tipo = "Avaliação"
        } else if (tipo ==1){
            Tipo = "Atividade"
        }else if (tipo ==2){
            Tipo = "Lembrete"
        }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(Tipo)
            .setContentText(titulo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ActivityCompat.getColor(context, R.color.azul))
            .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)


        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id, notificationBuilder.build())
    }

    fun notificationAtividade(context: Context, titulo: String?, tipo: Int?, id:Int) {
        var Tipo: String? = null
        if(tipo == 0 ){
            Tipo = "Avaliação"
        } else if (tipo ==1){
            Tipo = "Atividade"
        }else if (tipo ==2){
            Tipo = "Lembrete"
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(Tipo)
            .setContentText(titulo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ActivityCompat.getColor(context, R.color.azul))
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)


        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id, notificationBuilder.build())
    }

    fun notificationLembrete(context: Context, titulo: String?, tipo: Int?, id:Int) {
        var Tipo: String? = null
        if(tipo == 0 ){
            Tipo = "Avaliação"
        } else if (tipo ==1){
            Tipo = "Atividade"
        }else if (tipo ==2){
            Tipo = "Lembrete"
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context)
        }

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }


        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(Tipo)
            .setContentText(titulo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(ActivityCompat.getColor(context, R.color.azul))
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)


        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(id, notificationBuilder.build())
    }
}