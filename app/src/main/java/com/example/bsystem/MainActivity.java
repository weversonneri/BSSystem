package com.example.bsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bsystem.activity.AddScheduleActivity;
import com.example.bsystem.activity.ListCustomerActivity;
import com.example.bsystem.activity.ListServiceActivity;
import com.example.bsystem.adapter.ScheduleAdapter;
import com.example.bsystem.model.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();

    ArrayList<Schedule> arrayList;

    private Animation mfabOpen, mfabClose, mfabRotateOpen, mfabRotateClose;

    private boolean isOpen;

    private FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference refSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        refSchedule = database.getReference("schedule");

        this.mViewHolder.toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(this.mViewHolder.toolbar);

        this.mViewHolder.listView = findViewById(R.id.list_schedule);

        this.mViewHolder.buttonMainAdd = findViewById(R.id.button_main_add);
        this.mViewHolder.buttonMainAddSchedule = findViewById(R.id.button_main_add_schedule);
        this.mViewHolder.buttonMainAddCustomer = findViewById(R.id.button_main_add_customer);
        this.mViewHolder.buttonMainAddService = findViewById(R.id.button_main_add_service);

        this.mViewHolder.textMainAddSchedule = findViewById(R.id.text_main_add_schedule);
        this.mViewHolder.textMainAddCustomer = findViewById(R.id.text_main_add_customer);
        this.mViewHolder.textMainAddService = findViewById(R.id.text_main_add_service);


        this.mViewHolder.buttonMainAdd.setOnClickListener(this);
        this.mViewHolder.buttonMainAddSchedule.setOnClickListener(this);
        this.mViewHolder.buttonMainAddCustomer.setOnClickListener(this);
        this.mViewHolder.buttonMainAddService.setOnClickListener(this);


        mfabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        mfabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        mfabRotateOpen = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_open);
        mfabRotateClose = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_close);

        isOpen = false;

        refSchedule.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Schedule schedule = dataSnapshot.getValue(Schedule.class);
                    arrayList.add(schedule);
                }
                ScheduleAdapter arrayAdapter = new ScheduleAdapter(MainActivity.this, R.layout.layout_list_schedule, arrayList);
                MainActivity.this.mViewHolder.listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        this.mViewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Schedule selectSchedule = (Schedule) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, AddScheduleActivity.class);
                intent.putExtra("schedule", selectSchedule);

                startActivity(intent);


            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_main_add:


                if (isOpen) {

                    this.mViewHolder.buttonMainAdd.startAnimation(mfabRotateOpen);

                    this.mViewHolder.buttonMainAddSchedule.startAnimation(mfabClose);
                    this.mViewHolder.buttonMainAddCustomer.startAnimation(mfabClose);
                    this.mViewHolder.buttonMainAddService.startAnimation(mfabClose);

                    this.mViewHolder.textMainAddSchedule.setVisibility(View.INVISIBLE);
                    this.mViewHolder.textMainAddCustomer.setVisibility(View.INVISIBLE);
                    this.mViewHolder.textMainAddService.setVisibility(View.INVISIBLE);

                    isOpen = false;
                } else {

                    this.mViewHolder.buttonMainAdd.startAnimation(mfabRotateClose);

                    this.mViewHolder.buttonMainAddSchedule.startAnimation(mfabOpen);
                    this.mViewHolder.buttonMainAddCustomer.startAnimation(mfabOpen);
                    this.mViewHolder.buttonMainAddService.startAnimation(mfabOpen);

                    this.mViewHolder.textMainAddSchedule.setVisibility(View.VISIBLE);
                    this.mViewHolder.textMainAddCustomer.setVisibility(View.VISIBLE);
                    this.mViewHolder.textMainAddService.setVisibility(View.VISIBLE);

                    isOpen = true;
                }
                break;

            case R.id.button_main_add_schedule:
                startActivity(new Intent(MainActivity.this, AddScheduleActivity.class));
                break;

            case R.id.button_main_add_customer:
                startActivity(new Intent(MainActivity.this, ListCustomerActivity.class));
                break;

            case R.id.button_main_add_service:
                startActivity(new Intent(MainActivity.this, ListServiceActivity.class));
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_item_signOut:
                FirebaseAuth.getInstance().signOut();
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {

        } else {
            finish();
        }
    }

    public static class ViewHolder {
        ListView listView;

        FloatingActionButton buttonMainAdd;
        FloatingActionButton buttonMainAddSchedule;
        FloatingActionButton buttonMainAddCustomer;
        FloatingActionButton buttonMainAddService;

        TextView textMainAddSchedule, textMainAddCustomer, textMainAddService;

        Toolbar toolbar;

    }
}