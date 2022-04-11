package com.snake.ai;

import static com.snake.ai.ForegroundService.CHANNEL_ID;
import static com.snake.ai.main.freeze;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.snakeGame.ai.R;

public class AndroidLauncher extends AndroidApplication implements AndroidConnection{

	@RequiresApi(api = Build.VERSION_CODES.S)
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		freeze = true;
		System.out.println("Hello My Service is running = "+isMyServiceRunning());

		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new main(this), config);
	}

	@Override
	public void startService() {
		Intent serviceIntent = new Intent(this, ForegroundService.class);
		ContextCompat.startForegroundService(this, serviceIntent);
	}

	@Override
	public void stopService() {
		Intent serviceIntent = new Intent(this, ForegroundService.class);
		stopService(serviceIntent);
	}

	@RequiresApi(api = Build.VERSION_CODES.S)
	@Override
	public void updateNotification(int population) {
		updateNotification(this,population);
	}

	@RequiresApi(api = Build.VERSION_CODES.S)
	public void updateNotification(Context context, int population) {
		Notification notification = createNotification(context, "Number of Populations: "+population);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(8, notification);
	}

	@RequiresApi(api = Build.VERSION_CODES.S)
	public static Notification createNotification(Context context, String text) {
		Intent notificationIntent = new Intent(context, AndroidLauncher.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context,
				0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

		Intent stopIntent = new Intent(context, ForegroundService.class);
		stopIntent.setAction("stop");
		PendingIntent actionIntent = PendingIntent.getService(context, 1, stopIntent, PendingIntent.FLAG_MUTABLE);

		Intent saveStopIntent = new Intent(context, ForegroundService.class);
		saveStopIntent.setAction("saveStop");
		PendingIntent actionIntent2 = PendingIntent.getService(context, 2, saveStopIntent, PendingIntent.FLAG_MUTABLE);

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Snake Ai", NotificationManager.IMPORTANCE_LOW);
			notificationManager.createNotificationChannel(channel);
		}

		return new NotificationCompat.Builder(context, CHANNEL_ID)
				.setContentTitle("Snake AI")
				.setContentText(text)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pendingIntent)
				.setPriority(Notification.PRIORITY_LOW)
 				.addAction(R.drawable.ic_launcher, "Stop", actionIntent)
				.addAction(R.drawable.ic_launcher, "Save and Stop", actionIntent2)
				.setAutoCancel(true)
				.build();
	}


	public boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (ForegroundService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void toast(final String text) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(AndroidLauncher.this,text,Toast.LENGTH_SHORT).show();
			}
		});
	}
}
