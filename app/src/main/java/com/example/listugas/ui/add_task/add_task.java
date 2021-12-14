package com.example.listugas.ui.add_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ImageButton;

import com.example.listugas.NavActivity;
import com.example.listugas.R;
import com.example.listugas.ui.add_task.TaskModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class add_task extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] priority = {"Medium", "Low", "High", "Very High"};
    String[] category = {"Task", "Project", "Assignment", "Other"};
    ImageButton add_task;
    TextInputEditText TaskName, TaskDesc;
    TextInputEditText TaskDeadline_Date;
    TextInputEditText TaskDeadline_Time;

    Calendar myCalendar = Calendar.getInstance();
    Calendar myTime = Calendar.getInstance();
    FirebaseDatabase DBFirebase;
    DatabaseReference DBreference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String ActiveUID;

    private int notificationId = 1;

    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_task);

        ImageButton btn = (ImageButton) findViewById(R.id.addTask_close);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TaskName = findViewById(R.id.addTask_inputTaskName);
        TaskDeadline_Date = findViewById(R.id.addTask_inputDeadlineDate);
        TaskDeadline_Time = findViewById(R.id.addTask_inputDeadlineTime);
        TaskDesc = findViewById(R.id.addTask_inputDescTask);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        ActiveUID = mUser.getUid();
        DBFirebase = FirebaseDatabase.getInstance();
        DBreference = DBFirebase.getReference().child("tasks").child(ActiveUID);

        add_task = findViewById(R.id.addTask_submitNewTask);

        Spinner prioritySpinner = findViewById(R.id.dashboard_spinnerPriority);
        Spinner categorySpinner = findViewById(R.id.dashboard_spinnerCategory);
        prioritySpinner.setOnItemSelectedListener(this);
        categorySpinner.setOnItemSelectedListener(this);

        ArrayAdapter priorityArr = new ArrayAdapter(this, android.R.layout.simple_spinner_item, priority);
        ArrayAdapter categoryArr = new ArrayAdapter(this, android.R.layout.simple_spinner_item, category);

        priorityArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityArr);
        categorySpinner.setAdapter(categoryArr);

        DatePickerDialog.OnDateSetListener date = (view1, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            TaskDeadline_Date.setText(sdf.format(myCalendar.getTime()));
        };

        TaskDeadline_Date.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(add_task.this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        });

        TimePickerDialog.OnTimeSetListener time = (view1, hour, minute) -> {
            // TODO Auto-generated method stub
            myTime.set(Calendar.HOUR_OF_DAY, hour);
            myTime.set(Calendar.MINUTE, minute);

            String myFormat2 = "hh:mm a"; //In which you need put here
            SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
            TaskDeadline_Time.setText(sdf2.format(myTime.getTime()));
        };

        TaskDeadline_Time.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new TimePickerDialog(add_task.this, time, myTime
                    .get(Calendar.HOUR_OF_DAY), myTime.get(Calendar.MINUTE), is24HourView()).show();

        });

        add_task.setOnClickListener(view -> {
            String mTaskID = DBreference.push().getKey();
            String mTaskName = TaskName.getText().toString();
            String mTaskDeadline_Date = TaskDeadline_Date.getText().toString();
            String mTaskDeadline_Time = TaskDeadline_Time.getText().toString();
            String mTaskDesc = TaskDesc.getText().toString();
            String mTaskPriority = prioritySpinner.getSelectedItem().toString();
            String mTaskCategory = categorySpinner.getSelectedItem().toString();

            if (TextUtils.isEmpty(mTaskName)){
                TaskName.setError("Name Required");
                return;
            } else if (TextUtils.isEmpty(mTaskDeadline_Date) ){
                TaskDeadline_Date.setError("Date & Time need to be inputted.");
                return;
            } else if (TextUtils.isEmpty(mTaskDeadline_Time)) {
                TaskDeadline_Time.setError("Date & Time need to be inputted.");
                return;
            } else {

                int dayDate = myCalendar.get(Calendar.DAY_OF_MONTH);
                int monthDate = myCalendar.get(Calendar.MONTH);
                int yearDate = myCalendar.get(Calendar.YEAR);


                // Intent
                Intent intent = new Intent(add_task.this, AlarmBrodcastReceiver.class);
                intent.putExtra("notificationId", notificationId);
                intent.putExtra("mTaskName", mTaskName);
                intent.putExtra("mTaskTime", mTaskDeadline_Time);

                // PendingIntent
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        add_task.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                );

                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                myCalendar.set(Calendar.YEAR, yearDate);
                myCalendar.set(Calendar.MONTH, monthDate);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayDate);

                long alarmStartTime = myCalendar.getTimeInMillis();
                long alarmStartTime2 = myTime.getTimeInMillis();

                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime2, pendingIntent);
                TaskModel NewModel = new TaskModel(mTaskID, mTaskName,mTaskDeadline_Date, mTaskDeadline_Time, mTaskDesc, mTaskPriority, mTaskCategory, false);
                Toast.makeText(com.example.listugas.ui.add_task.add_task.this, "Adding Task.", Toast.LENGTH_SHORT).show();

                DBreference.child(mTaskID).setValue(NewModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(com.example.listugas.ui.add_task.add_task.this, "Added Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(add_task.this, NavActivity.class));
                            finish();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(com.example.listugas.ui.add_task.add_task.this, "Failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }

    private boolean is24HourView() {
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}