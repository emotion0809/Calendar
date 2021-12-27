package com.example.calendar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.calendar.databinding.ActivityMainBinding;
import com.example.calendar.ui.calendar.CalendarFragment;
import com.facebook.stetho.Stetho;
import com.google.android.material.navigation.NavigationView;


import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
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
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, NewRemindActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_calendar, R.id.nav_work, R.id.nav_diary)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        Stetho.initializeWithDefaults(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        Toast.makeText(this, "BACKING", Toast.LENGTH_SHORT).show();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick_monthSel(View view) {
        switch (view.getId()) {
            case R.id.button_lAro:
                if (CalendarFragment.startmonth != 1) {
                    CalendarFragment.startmonth--;
                } else {
                    CalendarFragment.year--;
                    CalendarFragment.month_days[1] = isLeapYear(CalendarFragment.year);
                    CalendarFragment.startmonth = 12;
                }
                CalendarFragment.startdate -= CalendarFragment.month_days[CalendarFragment.startmonth - 1] % 7;
                if (CalendarFragment.startdate < 1) {

                    CalendarFragment.startdate += 7;
                }
                break;
            case R.id.button_rAro:
                CalendarFragment.startdate += CalendarFragment.month_days[CalendarFragment.startmonth - 1] % 7;
                if (CalendarFragment.startdate > 7) {
                    CalendarFragment.startdate -= 7;
                }
                if (CalendarFragment.startmonth != 12) {
                    CalendarFragment.startmonth++;
                    CalendarFragment.month_days[1] = isLeapYear(CalendarFragment.year);
                } else {
                    CalendarFragment.year++;
                    CalendarFragment.startmonth = 1;
                }
                break;
        }
        try {
            CalendarFragment.init(CalendarFragment.froot);
        } catch (Exception eee) {
            System.out.println(eee);
        }
    }


    public static int isLeapYear(int year) {
        if (year % 4 == 0 && year % 100 != 0) {
            return 29;
        } else {
            return 28;
        }
    }

    public void openDDC() {
        DialogFragment newFragment = new SelectColorDialog();
        newFragment.show(getSupportFragmentManager(), "selectColor");
    }
}