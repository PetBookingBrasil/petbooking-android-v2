package com.petbooking.UI.Dashboard.Notifications;

import android.databinding.Bindable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.petbooking.R;
import com.petbooking.UI.Dashboard.Notifications.Model.Notification;
import com.petbooking.UI.Dashboard.Notifications.adapter.NotificationAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsActivity extends AppCompatActivity {
    ArrayList<Notification> notifications;
    @BindView(R.id.listNotifications)
    RecyclerView listNotifications;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        notifications = new ArrayList<>();
        listNotifications.setLayoutManager(new LinearLayoutManager(this));
        NotificationAdapter adapter = new NotificationAdapter();
        adapter.setNotifications(notifications);
        listNotifications.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.notification, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //if(id == R.id.clear){
            //notifications.clear();
       if(id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
