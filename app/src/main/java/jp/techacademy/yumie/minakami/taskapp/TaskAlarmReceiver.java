//package jp.techacademy.yumie.minakami.taskapp;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.BitmapFactory;
//import android.support.v7.app.NotificationCompat;
//
//public class TaskAlarmReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        // 通知の設定を行う
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setSmallIcon(R.drawable.small_icon);
//        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.large_icon));
//        builder.setWhen(System.currentTimeMillis());
//        builder.setDefaults(Notification.DEFAULT_ALL);
//        builder.setAutoCancel(true);
//
//        // タスクの情報を設定する
//        Task task = (Task) intent.getSerializableExtra(MainActivity.EXTRA_TASK);
//        builder.setTicker(task.getTitle()); // 5.0以降は表示されない
//        builder.setContentTitle(task.getTitle());
//        builder.setContentText(task.getContents());
//
//        // 通知をタップしたらアプリを起動するようにする
//        Intent startAppIntent = new Intent(context, MainActivity.class);
//        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, 0);
//        builder.setContentIntent(pendingIntent);
//
//        // 通知を表示する
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(task.getId(), builder.build());
//    }
//}

package jp.techacademy.yumie.minakami.taskapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import org.parceler.Parcel;
import org.parceler.Parcels;

import io.realm.RealmQuery;
//import android.util.Log;

/**
 * Created by user on 2016/11/23.
 */

// TaskAlarmReceiver for receiving broadcast
public class TaskAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
//        Log.d("TaskApp", "onReceive");

        // set Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.small_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.large_icon));
        builder.setWhen(System.currentTimeMillis());
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setAutoCancel(true);

        // set Task info
        // measures for close/down app
        Bundle bundle = intent.getBundleExtra(MainActivity.EXTRA_TASK);     // get Bundle of Intent
        Task task = (Task) Parcels.unwrap(bundle.getParcelable(MainActivity.EXTRA_TASK));   // dereference the @Parcel
        // measures for close/down app
//        Task task = (Task) intent.getSerializableExtra(MainActivity.EXTRA_TASK);      // running error on Android 7.0 or later
//        String taskId = (String) intent.getSerializableExtra(MainActivity.EXTRA_TASK);
//        RealmQuery<Task> q = realm.where(Task.class);
//        q.equalTo();
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            builder.setTicker(task.getTitle()); // No display on 5.0 or later
        builder.setContentTitle(task.getTitle());
        builder.setContentText(task.getContents());

        // Start App when tapping Notification
        Intent startAppIntent = new Intent(context, MainActivity.class);
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, 0);
        builder.setContentIntent(pendingIntent);

        // Display Notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(task.getId(), builder.build());
    }
}
