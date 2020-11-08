package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bsystem.R;
import com.example.bsystem.model.Schedule;
import com.example.bsystem.utils.TelefoneMaskUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterScheduleActivity extends AppCompatActivity implements View.OnClickListener {

    Schedule schedule;

    private ViewHolder mViewHolder = new ViewHolder();

    FirebaseDatabase database;
    DatabaseReference refSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        database = FirebaseDatabase.getInstance();
        refSchedule = database.getReference("schedule");

        this.mViewHolder.editScheduleAddName = findViewById(R.id.edit_schedule_add_name);
        this.mViewHolder.editScheduleAddPhone = findViewById(R.id.edit_schedule_add_phone);
        this.mViewHolder.editScheduleAddTime = findViewById(R.id.edit_schedule_add_time);
        this.mViewHolder.editScheduleAddService = findViewById(R.id.edit_schedule_add_service);

        this.mViewHolder.buttonScheduleAddSubmit = findViewById(R.id.button_schedule_add_submit);
        this.mViewHolder.buttonScheduleAddCancel = findViewById(R.id.button_schedule_add_cancel);

        this.mViewHolder.buttonScheduleAddSubmit.setOnClickListener(this);
        this.mViewHolder.buttonScheduleAddCancel.setOnClickListener(this);


        this.mViewHolder.editScheduleAddPhone.addTextChangedListener(TelefoneMaskUtil.insert("(##)#####-####", this.mViewHolder.editScheduleAddPhone));
        this.mViewHolder.editScheduleAddTime.addTextChangedListener(TelefoneMaskUtil.insert("##:##", this.mViewHolder.editScheduleAddTime));

        Intent intent = getIntent();
        schedule = (Schedule) intent.getSerializableExtra("schedule");
        if (schedule != null) {
            this.mViewHolder.editScheduleAddName.setText(schedule.getName().toString());
        }


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_schedule_add_submit) {
            register();

        }
        if (v.getId() == R.id.button_schedule_add_cancel) {
            finish();
        }
    }

    private void register() {

        schedule = new Schedule();

        schedule.setName(this.mViewHolder.editScheduleAddName.getText().toString());
        schedule.setPhone(this.mViewHolder.editScheduleAddPhone.getText().toString());
        schedule.setTime(this.mViewHolder.editScheduleAddTime.getText().toString());
        schedule.setService(this.mViewHolder.editScheduleAddService.getText().toString());

        refSchedule.child(schedule.getName()).setValue(schedule);

        Toast.makeText(this, "Agendamento realizado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();

    }

    public static class ViewHolder {
        TextInputEditText editScheduleAddName;
        TextInputEditText editScheduleAddPhone;
        TextInputEditText editScheduleAddTime;
        TextInputEditText editScheduleAddService;

        Button buttonScheduleAddSubmit;
        Button buttonScheduleAddCancel;

    }
}