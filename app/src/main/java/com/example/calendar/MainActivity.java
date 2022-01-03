package com.example.calendar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.calendar.databinding.ActivityMainBinding;
import com.example.calendar.ui.calendar.CalendarFragment;
import com.facebook.stetho.Stetho;
import com.google.android.material.navigation.NavigationView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public static int dateTime[] = new int[5];
    CustomReceiver mReceiver = new CustomReceiver();
    //TestNoti

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;

    public void createNotificationChannel() {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_add)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true);
        return notifyBuilder;
    }

    public void sendNotification() {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        Toast.makeText(this, "Builder:" + String.format("%s", mNotifyManager), Toast.LENGTH_SHORT).show();
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNoteActivity.isUpdateNote = false;
                EditNoteActivity.isEnterbyDialog = false;
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditNoteActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_calendar, R.id.nav_work, R.id.nav_diary)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Stetho.initializeWithDefaults(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        // Register the receiver using the activity context.
        this.registerReceiver(mReceiver, filter);
        createNotificationChannel();

        try {
            sendNotification();
            //每兩秒抓時間
            Timer timer = new Timer();
            System.out.println("Delay：3秒, Period：2秒");
            System.out.println("In testScheduleDelayAndPeriod："
                    + new Date());

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Date d = new Date();
                    System.out.println(d);
                }
            }, 3000, 2000);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            //timer.cancel();
            System.out.println("End testScheduleDelayAndPeriod："
                    + new Date() + "\n");
        } catch (Exception ex) {
            Toast.makeText(this, String.format("%s", ex), Toast.LENGTH_LONG).show();
            System.out.println(String.format("%s", ex));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                DialogFragment newFragment = new LoginDialog();
                newFragment.show(getSupportFragmentManager(), "");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        Toast.makeText(this, "BACKING", Toast.LENGTH_SHORT).show();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getDate() {
        LocalDateTime curTime = LocalDateTime.now();
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy", Locale.TAIWAN);
        dateTime[0] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("MM", Locale.TAIWAN);
        dateTime[1] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("dd", Locale.TAIWAN);
        dateTime[2] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("HH", Locale.TAIWAN);
        dateTime[3] = Integer.parseInt(formmat1.format(curTime));
        formmat1 = DateTimeFormatter.ofPattern("mm", Locale.TAIWAN);
        dateTime[4] = Integer.parseInt(formmat1.format(curTime));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick_monthChange(View view) {
        switch (view.getId()) {
            case R.id.bt_leftArrow:
                if (CalendarFragment.calendarDate[1] != 1) {
                    CalendarFragment.calendarDate[1]--;
                } else {
                    CalendarFragment.calendarDate[0]--;
                    CalendarFragment.monthDays[1] = isLeapYear(CalendarFragment.calendarDate[0]);
                    CalendarFragment.calendarDate[1] = 12;
                }
                CalendarFragment.startDay -= CalendarFragment.monthDays[CalendarFragment.calendarDate[1] - 1] % 7;
                if (CalendarFragment.startDay < 0) {
                    CalendarFragment.startDay += 7;
                }
                break;
            case R.id.bt_rightArrow:
                CalendarFragment.startDay += CalendarFragment.monthDays[CalendarFragment.calendarDate[1] - 1] % 7;
                if (CalendarFragment.startDay > 6) {
                    CalendarFragment.startDay -= 7;
                }
                if (CalendarFragment.calendarDate[1] != 12) {
                    CalendarFragment.calendarDate[1]++;
                    CalendarFragment.monthDays[1] = isLeapYear(CalendarFragment.calendarDate[0]);
                } else {
                    CalendarFragment.calendarDate[0]++;
                    CalendarFragment.calendarDate[1] = 1;
                }
                break;
        }
        CalendarFragment.init();
    }

    public static int isLeapYear(int year) {
        if (year % 4 == 0 && year % 100 != 0) {
            return 29;
        } else {
            return 28;
        }
    }


    @Override
    protected void onDestroy() {
        //Unregister the receiver
        this.unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}