package com.example.izracunaj;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "playAgain")
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Da li imate 2 minuta slobodnog vremena?")
                .setContentText("Možda je baš ovo savršen trenutak da oborite rekord!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(200, builder.build());
    }
}
