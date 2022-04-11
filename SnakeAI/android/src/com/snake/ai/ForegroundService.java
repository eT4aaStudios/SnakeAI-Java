package com.snake.ai;

import static com.snake.ai.SavedSnakes.prefs;
import static com.snake.ai.SavedSnakes.saveAsJson;
import static com.snake.ai.SnakeGame.gameThread;
import static com.snake.ai.main.bestSnakes;
import static com.snake.ai.main.freeze;
import static com.snake.ai.main.gson;
import static com.snake.ai.main.snakeGameInstance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "SnakeForegroundService";
    NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "saveStop".equals(intent.getAction())) {
            Notification notification = AndroidLauncher.createNotification(this, "Saving...");
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(8, notification);
            freeze = true;
            prefs = Gdx.app.getPreferences("SnakeAiVersion2");
            gson = new Gson();
            saveAsJson(snakeGameInstance, bestSnakes);

            stopForeground(true);
            stopSelf();

            gameThread.interrupt();
            gameThread = null;
            return START_NOT_STICKY;
        }
        if (intent != null && "stop".equals(intent.getAction())) {
            freeze = true;

            stopForeground(true);
            stopSelf();

            gameThread.interrupt();
            gameThread = null;
            return START_NOT_STICKY;
        }

        startForeground(8, AndroidLauncher.createNotification(this,"Start calculating Snakes!"));

        gameThread = new Thread(main.snakeGame);
        gameThread.setPriority(Thread.MAX_PRIORITY);
        gameThread.start();
        gameThread.setName("SnakeAiCalculating");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}