package com.example.bsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.bsystem.activity.AddCustomerActivity;
import com.example.bsystem.activity.AddScheduleActivity;
import com.example.bsystem.activity.AddServiceActivity;
import com.example.bsystem.activity.ListCustomerActivity;
import com.example.bsystem.activity.ListServiceActivity;
import com.example.bsystem.activity.LoginActivity;
import com.example.bsystem.adapter.ScheduleAdapter;
import com.example.bsystem.model.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private ViewHolder mViewHolder = new ViewHolder();

    ArrayList<Schedule> arrayList;

    private Animation mfabOpen, mfabClose, mfabRotateOpen, mfabRotateClose;

    private boolean isOpen;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseUser user;


    FirebaseDatabase database;
    private Query refSchedule;

    private String userId;

    private String _ID, _NAME, _ROLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        refSchedule = database.getReference("schedule").orderByChild("time");

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

        this.mViewHolder.drawerLayout = findViewById(R.id.drawer_layout_main_activity);
        this.mViewHolder.navigationView = findViewById(R.id.nav_view_main_activity);


        this.mViewHolder.buttonMainAdd.setOnClickListener(this);
        this.mViewHolder.buttonMainAddSchedule.setOnClickListener(this);
        this.mViewHolder.buttonMainAddCustomer.setOnClickListener(this);
        this.mViewHolder.buttonMainAddService.setOnClickListener(this);

        this.mViewHolder.navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.mViewHolder.drawerLayout, this.mViewHolder.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.mViewHolder.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        this.mViewHolder.navigationView.setNavigationItemSelectedListener(this);
        this.mViewHolder.navigationView.setCheckedItem(R.id.nav_home);

        /**************************************************************/
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Usuario logado
                    updateUIData();

                    Log.d(TAG, "onAuthStateChanged:signed in: " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed out: ");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        };


        /**************************************************************/
        mfabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        mfabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        mfabRotateOpen = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_open);
        mfabRotateClose = AnimationUtils.loadAnimation(this, R.anim.fab_rotate_close);

        isOpen = false;
        /****************************************************************/
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
        /**********************************************************************/
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

    private void updateUIData() {
        if (mAuth != null) {
            DatabaseReference check = database.getReference("user").child(mAuth.getUid());

            check.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    _NAME = snapshot.child("name").getValue(String.class);
                    _ROLE = snapshot.child("role").getValue(String.class);
                    Log.d(TAG, "Level access is: " + _NAME);
                    updateHeader();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    /***************************************************************************/

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    /***************************************************************************/

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    /******************************************************************************************************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_main_add:

               /* DatabaseReference check = database.getReference("user").child(mAuth.getUid());

                check.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String value = snapshot.child("role").getValue(String.class);
                        if (value != null && value.equals("admin")) {
                            Log.d(TAG, "Level access is: " + value);*/
                if (_ROLE != null && _ROLE.equals("admin")) {
                    if (isOpen) {

                        MainActivity.this.mViewHolder.buttonMainAdd.startAnimation(mfabRotateOpen);

                        MainActivity.this.mViewHolder.buttonMainAddSchedule.startAnimation(mfabClose);
                        MainActivity.this.mViewHolder.buttonMainAddCustomer.startAnimation(mfabClose);
                        MainActivity.this.mViewHolder.buttonMainAddService.startAnimation(mfabClose);

                        MainActivity.this.mViewHolder.textMainAddSchedule.setVisibility(View.INVISIBLE);
                        MainActivity.this.mViewHolder.textMainAddCustomer.setVisibility(View.INVISIBLE);
                        MainActivity.this.mViewHolder.textMainAddService.setVisibility(View.INVISIBLE);

                        isOpen = false;
                    } else {

                        MainActivity.this.mViewHolder.buttonMainAdd.startAnimation(mfabRotateClose);

                        MainActivity.this.mViewHolder.buttonMainAddSchedule.startAnimation(mfabOpen);
                        MainActivity.this.mViewHolder.buttonMainAddCustomer.startAnimation(mfabOpen);
                        MainActivity.this.mViewHolder.buttonMainAddService.startAnimation(mfabOpen);

                        MainActivity.this.mViewHolder.textMainAddSchedule.setVisibility(View.VISIBLE);
                        MainActivity.this.mViewHolder.textMainAddCustomer.setVisibility(View.VISIBLE);
                        MainActivity.this.mViewHolder.textMainAddService.setVisibility(View.VISIBLE);

                        isOpen = true;
                    }

                } else {
                    if (isOpen) {

                        MainActivity.this.mViewHolder.buttonMainAdd.startAnimation(mfabRotateOpen);

                        MainActivity.this.mViewHolder.buttonMainAddSchedule.startAnimation(mfabClose);
                        MainActivity.this.mViewHolder.buttonMainAddCustomer.startAnimation(mfabClose);
                        // MainActivity.this.mViewHolder.buttonMainAddService.startAnimation(mfabClose);

                        MainActivity.this.mViewHolder.textMainAddSchedule.setVisibility(View.INVISIBLE);
                        MainActivity.this.mViewHolder.textMainAddCustomer.setVisibility(View.INVISIBLE);
                        //MainActivity.this.mViewHolder.textMainAddService.setVisibility(View.INVISIBLE);

                        isOpen = false;
                    } else {

                        MainActivity.this.mViewHolder.buttonMainAdd.startAnimation(mfabRotateClose);

                        MainActivity.this.mViewHolder.buttonMainAddSchedule.startAnimation(mfabOpen);
                        MainActivity.this.mViewHolder.buttonMainAddCustomer.startAnimation(mfabOpen);
                        // MainActivity.this.mViewHolder.buttonMainAddService.startAnimation(mfabOpen);

                        MainActivity.this.mViewHolder.textMainAddSchedule.setVisibility(View.VISIBLE);
                        MainActivity.this.mViewHolder.textMainAddCustomer.setVisibility(View.VISIBLE);
                        //  MainActivity.this.mViewHolder.textMainAddService.setVisibility(View.VISIBLE);

                        isOpen = true;
                    }
                    // Log.d(TAG, "ERRO");

                }

               /*   }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "ERRO");
                    }
                });*/


                break;

            case R.id.button_main_add_schedule:
                startActivity(new Intent(MainActivity.this, AddScheduleActivity.class));
                break;

            case R.id.button_main_add_customer:
                startActivity(new Intent(MainActivity.this, AddCustomerActivity.class));
                break;

            case R.id.button_main_add_service:
                startActivity(new Intent(MainActivity.this, AddServiceActivity.class));
                break;
        }

    }

    /*********************************************************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

 /*   @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_item_signOut:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(this, MainActivity.class));
                break;

       /*     case R.id.nav_schedules:
                startActivity(new Intent(getApplicationContext(), Li.class));
                break;*/

            case R.id.nav_customers:
                startActivity(new Intent(this, ListCustomerActivity.class));
                break;

            case R.id.nav_services:
                startActivity(new Intent(this, ListServiceActivity.class));
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                Log.d(TAG, "Logged out");
                break;
        }

        this.mViewHolder.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateHeader() {
        //View headerView = this.mViewHolder.navigationView.getHeaderView(0);
        this.mViewHolder.imageView = findViewById(R.id.image_header);
        this.mViewHolder.textHeader = findViewById(R.id.text_nav_header);

        this.mViewHolder.textHeader.setText(_NAME);

        Glide.with(this).load(user.getPhotoUrl()).into(this.mViewHolder.imageView);

    }


    public static class ViewHolder {
        ListView listView;

        FloatingActionButton buttonMainAdd;
        FloatingActionButton buttonMainAddSchedule;
        FloatingActionButton buttonMainAddCustomer;
        FloatingActionButton buttonMainAddService;

        TextView textMainAddSchedule, textMainAddCustomer, textMainAddService, textHeader;

        Toolbar toolbar;

        DrawerLayout drawerLayout;
        NavigationView navigationView;

        ImageView imageView;


    }
}