package com.example.bsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bsystem.R;
import com.example.bsystem.model.Schedule;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener {

    Schedule schedule;

    private ViewHolder mViewHolder = new ViewHolder();

    FirebaseDatabase database;
    DatabaseReference referenceSchedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);


        database = FirebaseDatabase.getInstance();
        referenceSchedule = database.getReference("schedule");


        this.mViewHolder.editScheduleAddName = findViewById(R.id.edit_schedule_add_name);
        this.mViewHolder.editScheduleAddPhone = findViewById(R.id.edit_schedule_add_phone);
        this.mViewHolder.editScheduleAddTime = findViewById(R.id.edit_schedule_add_time);

        this.mViewHolder.imageScheduleAddTime = findViewById(R.id.image_schedule_add_time);

        this.mViewHolder.editScheduleAddService = findViewById(R.id.edit_schedule_add_service);

        this.mViewHolder.buttonScheduleAddDelete = findViewById(R.id.button_schedule_add_delete);
        this.mViewHolder.buttonScheduleAddEdit = findViewById(R.id.button_schedule_add_edit);
        this.mViewHolder.buttonScheduleAddSubmit = findViewById(R.id.button_schedule_add_submit);
        this.mViewHolder.buttonScheduleAddCancel = findViewById(R.id.button_schedule_add_cancel);

        this.mViewHolder.editScheduleAddTime.setKeyListener(null);

        this.mViewHolder.editScheduleAddTime.setOnClickListener(this);

        this.mViewHolder.imageScheduleAddTime.setOnClickListener(this);

        this.mViewHolder.buttonScheduleAddDelete.setOnClickListener(this);
        this.mViewHolder.buttonScheduleAddEdit.setOnClickListener(this);
        this.mViewHolder.buttonScheduleAddSubmit.setOnClickListener(this);
        this.mViewHolder.buttonScheduleAddCancel.setOnClickListener(this);

      /*  this.mViewHolder.editScheduleAddPhone.addTextChangedListener(TelefoneMaskUtil.insert("(##)#####-####", this.mViewHolder.editScheduleAddPhone));
        //this.mViewHolder.editScheduleAddTime.addTextChangedListener(TelefoneMaskUtil.insert("##:##", this.mViewHolder.editScheduleAddTime));
*/
/***********************************************************************************************************************************/

        Intent intent = getIntent();
        Schedule schedule = (Schedule) intent.getSerializableExtra("schedule");
        if (schedule != null) {

            this.mViewHolder.buttonScheduleAddSubmit.setVisibility(View.INVISIBLE);
            this.mViewHolder.buttonScheduleAddCancel.setVisibility(View.INVISIBLE);
            this.mViewHolder.buttonScheduleAddEdit.setVisibility(View.VISIBLE);
            this.mViewHolder.buttonScheduleAddDelete.setVisibility(View.VISIBLE);

            this.mViewHolder.imageScheduleAddTime.setEnabled(false);

            this.mViewHolder.editScheduleAddName.setEnabled(false);
            this.mViewHolder.editScheduleAddPhone.setEnabled(false);
            this.mViewHolder.editScheduleAddTime.setEnabled(false);
            this.mViewHolder.editScheduleAddService.setEnabled(false);

            this.mViewHolder.editScheduleAddName.setText(schedule.getName());
            this.mViewHolder.editScheduleAddPhone.setText(schedule.getPhone());
            this.mViewHolder.editScheduleAddTime.setText(schedule.getTime());
            this.mViewHolder.editScheduleAddService.setText(schedule.getService());

        }
    }

    /***********************************************************************************************************************************/


    private int year, month, day, hour, minute;

    private void scheduleTest(View v) {
        initDateTimeData();
        Calendar calendarDefault = Calendar.getInstance();

        calendarDefault.set(year, month, day);
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                AddScheduleActivity.this,
                calendarDefault.get(Calendar.YEAR),
                calendarDefault.get(Calendar.MONTH),
                calendarDefault.get(Calendar.DAY_OF_MONTH)

        );
        Calendar calendarMax = Calendar.getInstance();
        Calendar calendarMin = Calendar.getInstance();

        calendarMax.set(calendarMax.get(Calendar.YEAR), 11, 31);
        datePickerDialog.setMinDate(calendarMin);
        datePickerDialog.setMaxDate(calendarMax);

        List<Calendar> daysList = new LinkedList<>();
        Calendar[] daysArray;
        Calendar calendarAux = Calendar.getInstance();
        while (calendarAux.getTimeInMillis() <= calendarMax.getTimeInMillis()) {
            if (calendarAux.get(Calendar.DAY_OF_WEEK) != 1 && calendarAux.get(Calendar.DAY_OF_WEEK) != 7) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(calendarAux.getTimeInMillis());
                daysList.add(calendar);
            }
            calendarAux.setTimeInMillis(calendarAux.getTimeInMillis() + (24 * 60 * 60 * 1000));
        }
        daysArray = new Calendar[daysList.size()];
        for (int i = 0; i < daysArray.length; i++) {
            daysArray[i] = daysList.get(i);
        }
        datePickerDialog.setSelectableDays(daysArray);
        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show(getSupportFragmentManager(), "DatePickerDialog");
    }

    private void initDateTimeData() {
        if (year == 0) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }
    }

    /***********************************************************************************************************************************/


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_schedule_add_submit) {
            register();
        }
        if (v.getId() == R.id.button_schedule_add_cancel) {
            finish();
        }
        if (v.getId() == R.id.button_schedule_add_edit) {
            scheduleEdit(v);
        }
        if (v.getId() == R.id.image_schedule_add_time) {
            scheduleTest(v);
        }
        if (v.getId() == R.id.edit_schedule_add_time) {
            scheduleTest(v);
        }
        if (v.getId() == R.id.button_schedule_add_delete) {
          /*  refSchedule.child(String.valueOf(schedule.getName())).removeValue();

            DatabaseReference uR = FirebaseDatabase.getInstance().getReference("schedule").child(name);
            uR.removeValue();*/

            finish();
        }

    }

    private void scheduleEdit(View v) {

        this.mViewHolder.buttonScheduleAddSubmit.setVisibility(View.VISIBLE);
        this.mViewHolder.buttonScheduleAddCancel.setVisibility(View.VISIBLE);
        this.mViewHolder.buttonScheduleAddEdit.setVisibility(View.INVISIBLE);
        this.mViewHolder.buttonScheduleAddDelete.setVisibility(View.INVISIBLE);

        this.mViewHolder.buttonScheduleAddSubmit.setText("Salvar");

        this.mViewHolder.imageScheduleAddTime.setEnabled(true);

        this.mViewHolder.editScheduleAddName.setEnabled(true);
        this.mViewHolder.editScheduleAddPhone.setEnabled(true);
        this.mViewHolder.editScheduleAddTime.setEnabled(true);
        this.mViewHolder.editScheduleAddService.setEnabled(true);

    }

    private void register() {
        String name = this.mViewHolder.editScheduleAddName.getText().toString();
        String phone = this.mViewHolder.editScheduleAddPhone.getText().toString();
        String time = this.mViewHolder.editScheduleAddTime.getText().toString();
        String service = this.mViewHolder.editScheduleAddService.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Preencher", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "phone", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(time)) {
            Toast.makeText(this, "time", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(service)) {
            Toast.makeText(this, "service", Toast.LENGTH_SHORT).show();
        } else {
            String id = referenceSchedule.push().getKey();

            Schedule schedule = new Schedule(id, name, phone, time, service);
            referenceSchedule.child(id).setValue(schedule);
            Toast.makeText(this, "Agendamento realizado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /***********************************************************************************************************************************/

    @Override
    public void onCancel(DialogInterface dialog) {
        year = month = day = hour = minute = 0;
        this.mViewHolder.editScheduleAddTime.setText("");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year1, int monthOfYear, int dayOfMonth) {
        Calendar tDefault = Calendar.getInstance();
        tDefault.set(year, month, day, hour, minute);

        year = year1;
        month = monthOfYear;
        day = dayOfMonth;

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                this,
                tDefault.get(Calendar.HOUR_OF_DAY),
                tDefault.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setOnCancelListener(this);
        timePickerDialog.show(getSupportFragmentManager(), "timePickerDialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute1, int second) {
        if (hourOfDay < 9 || hourOfDay > 18) {
            onDateSet(null, year, month, day);
            Toast.makeText(this, "Somente entre 8 e 18", Toast.LENGTH_SHORT).show();
            return;
        }
        hour = hourOfDay;
        minute = minute1;

        this.mViewHolder.editScheduleAddTime.setText((day < 10 ? "0" + day : day) + "/" +
                (month + 1 < 10 ? "0" + (month + 1) : month + 1) + "/" +
                year + " " + (hour < 10 ? "0" + hour : hour) + "h" +
                (minute < 10 ? "0" + minute : minute));

    }

    /***********************************************************************************************************************************/


    public static class ViewHolder {
        TextInputEditText editScheduleAddName;
        TextInputEditText editScheduleAddPhone;
        TextInputEditText editScheduleAddTime;

        TextInputEditText editScheduleAddService;

        ImageView imageScheduleAddTime;

        Button buttonScheduleAddDelete;
        Button buttonScheduleAddEdit;
        Button buttonScheduleAddSubmit;
        Button buttonScheduleAddCancel;

    }
}