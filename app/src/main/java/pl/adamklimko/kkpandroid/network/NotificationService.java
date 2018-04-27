package pl.adamklimko.kkpandroid.network;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import pl.adamklimko.kkpandroid.R;

public class NotificationService extends Service {

    public NotificationService(Context applicationContext) {
        super();
    }

    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent("RestartNotificationService");
        sendBroadcast(broadcastIntent);
    }

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        timer = new Timer();

        initializeTimerTask();

        //schedule the timer, to wake up every 25 second
        timer.schedule(timerTask, 1000, 25000);
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                showNotification("lol");
            }
        };
    }

    private void showNotification(String message) {

        NotificationManager notif = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder
                (getApplicationContext()).setContentTitle("siema").setContentText("witaj").
                setContentTitle("lol").setSmallIcon(R.mipmap.ic_launcher).build();

        notify.flags = Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}