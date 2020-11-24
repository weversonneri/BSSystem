package com.example.bsystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bsystem.R;
import com.example.bsystem.model.Schedule;
import com.example.bsystem.utils.TelefoneMaskUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener {

    private ViewHolder mViewHolder = new ViewHolder();

    FirebaseDatabase database;
    DatabaseReference referenceSchedule;

    String _ID, _NAME, _PHONE, _TIME, _SERVICE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);


        database = FirebaseDatabase.getInstance();
        referenceSchedule = database.getReference("schedule");

        this.mViewHolder.toolbar = findViewById(R.id.add_schedule_toolbar);
        setSupportActionBar(this.mViewHolder.toolbar);

        this.mViewHolder.editScheduleAddName = findViewById(R.id.edit_schedule_add_name);
        this.mViewHolder.editScheduleAddPhone = findViewById(R.id.edit_schedule_add_phone);
        this.mViewHolder.editScheduleAddTime = findViewById(R.id.edit_schedule_add_time);
        this.mViewHolder.imageScheduleAddTime = findViewById(R.id.image_schedule_add_time);
        this.mViewHolder.editScheduleAddService = findViewById(R.id.edit_schedule_add_service);

        this.mViewHolder.layoutScheduleAddName = findViewById(R.id.layout_schedule_add_name);
        this.mViewHolder.layoutScheduleAddPhone = findViewById(R.id.layout_schedule_add_phone);
        this.mViewHolder.layoutScheduleAddTime = findViewById(R.id.layout_schedule_add_time);
        this.mViewHolder.layoutScheduleAddService = findViewById(R.id.layout_schedule_add_service);

        this.mViewHolder.buttonScheduleAddSubmit = findViewById(R.id.button_schedule_add_submit);
        this.mViewHolder.buttonScheduleAddCancel = findViewById(R.id.button_schedule_add_cancel);

        this.mViewHolder.editScheduleAddTime.setKeyListener(null);

        this.mViewHolder.editScheduleAddTime.setOnClickListener(this);

        this.mViewHolder.imageScheduleAddTime.setOnClickListener(this);


        this.mViewHolder.buttonScheduleAddSubmit.setOnClickListener(this);
        this.mViewHolder.buttonScheduleAddCancel.setOnClickListener(this);

        this.mViewHolder.editScheduleAddPhone.addTextChangedListener(TelefoneMaskUtil.insert("(##)#####-####", this.mViewHolder.editScheduleAddPhone));

        showData();
/***********************************************************************************************************************************/
    }

    private void showData() {
        Intent intent = getIntent();
        Schedule schedule = (Schedule) intent.getSerializableExtra("schedule");
        if (schedule != null) {

            this.mViewHolder.buttonScheduleAddSubmit.setVisibility(View.INVISIBLE);
            this.mViewHolder.buttonScheduleAddCancel.setVisibility(View.INVISIBLE);

            this.mViewHolder.imageScheduleAddTime.setEnabled(false);

            this.mViewHolder.editScheduleAddName.setEnabled(false);
            this.mViewHolder.editScheduleAddPhone.setEnabled(false);
            this.mViewHolder.editScheduleAddTime.setEnabled(false);
            this.mViewHolder.editScheduleAddService.setEnabled(false);

            _ID = schedule.getId();
            _NAME = schedule.getName();
            _PHONE = schedule.getPhone();
            _TIME = schedule.getTime();
            _SERVICE = schedule.getService();

            this.mViewHolder.editScheduleAddName.setText(_NAME);
            this.mViewHolder.editScheduleAddPhone.setText(_PHONE);
            this.mViewHolder.editScheduleAddTime.setText(_TIME);
            this.mViewHolder.editScheduleAddService.setText(_SERVICE);

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

        switch (v.getId()) {
            case R.id.button_schedule_add_submit:
                register();
                break;
            case R.id.button_schedule_add_cancel:
                finish();
                break;
            case R.id.image_schedule_add_time:
                scheduleTest(v);
                break;
        }

    }

    private void scheduleEdit() {

        this.mViewHolder.buttonScheduleAddSubmit.setVisibility(View.VISIBLE);
        this.mViewHolder.buttonScheduleAddCancel.setVisibility(View.VISIBLE);

        this.mViewHolder.buttonScheduleAddSubmit.setText("Salvar");

        this.mViewHolder.imageScheduleAddTime.setEnabled(true);

        this.mViewHolder.editScheduleAddTime.setEnabled(true);
        this.mViewHolder.editScheduleAddService.setEnabled(true);

    }

    private void register() {
        String name = this.mViewHolder.editScheduleAddName.getText().toString();
        String phone = this.mViewHolder.editScheduleAddPhone.getText().toString();
        String time = this.mViewHolder.editScheduleAddTime.getText().toString();
        String service = this.mViewHolder.editScheduleAddService.getText().toString();


        if (TextUtils.isEmpty(name)) {
            this.mViewHolder.layoutScheduleAddName.setError("* Campo obrigatório");
        } else {
            this.mViewHolder.layoutScheduleAddName.setError(null);
        }
        if (TextUtils.isEmpty(phone)) {
            this.mViewHolder.layoutScheduleAddPhone.setError("* Campo obrigatório");
        }else if (phone.length() < 14) {
            this.mViewHolder.layoutScheduleAddPhone.setError("Telefone inválido");
        } else {
            this.mViewHolder.layoutScheduleAddPhone.setError(null);
        }
        if (TextUtils.isEmpty(time)) {
            this.mViewHolder.layoutScheduleAddTime.setError("* Campo obrigatório");
        } else {
            this.mViewHolder.layoutScheduleAddTime.setError(null);
        }
        if (TextUtils.isEmpty(service)) {
            this.mViewHolder.layoutScheduleAddService.setError("* Campo obrigatório");
        } else {
            if (_ID == null) {
                String id = referenceSchedule.push().getKey();

                Schedule schedule = new Schedule(id, name, phone, time, service);
                referenceSchedule.child(id).setValue(schedule).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddScheduleActivity.this, "Agendamento realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Toast.makeText(AddScheduleActivity.this, "ERRO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                update();
            }
        }
    }

    private void update() {
        // Schedule schedule = new Schedule(_ID, _TIME, _SERVICE);
        referenceSchedule.child(_ID).child("service").setValue(this.mViewHolder.editScheduleAddService.getText().toString());
        _SERVICE = this.mViewHolder.editScheduleAddService.getText().toString();
        referenceSchedule.child(_ID).child("time").setValue(this.mViewHolder.editScheduleAddTime.getText().toString());
        _TIME = this.mViewHolder.editScheduleAddTime.getText().toString();

        Toast.makeText(this, "Dados atualizados", Toast.LENGTH_SHORT).show();
        finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit:

                scheduleEdit();
                return true;

            case R.id.menu_item_remove:
                referenceSchedule.child(_ID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddScheduleActivity.this, "Agendamento deletado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddScheduleActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class ViewHolder {
        TextInputEditText editScheduleAddName;
        TextInputEditText editScheduleAddPhone;
        TextInputEditText editScheduleAddTime;

        TextInputEditText editScheduleAddService;

        TextInputLayout layoutScheduleAddName, layoutScheduleAddPhone, layoutScheduleAddTime, layoutScheduleAddService;

        ImageView imageScheduleAddTime;

        Button buttonScheduleAddSubmit;
        Button buttonScheduleAddCancel;

        Toolbar toolbar;


    }
}