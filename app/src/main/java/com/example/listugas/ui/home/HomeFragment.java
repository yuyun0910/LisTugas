package com.example.listugas.ui.home;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listugas.ui.history.HistoryActivity;
import com.example.listugas.R;
import com.example.listugas.onboarding_plan;
import com.example.listugas.ui.add_task.TaskModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment {

    FirebaseDatabase DBFirebase;
    DatabaseReference DBreference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String ActiveUID;


    RecyclerView recyclerView;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myTime = Calendar.getInstance();

    String[] priorityArray = {"Medium", "Low", "High", "Very High"};
    String[] categoryArray = {"Task", "Project", "Assignment", "Other"};
    private String key = "";
    private String task;
    private String description;
    private String date;
    private String time;
    private Boolean completion;
    private String priority;
    private String category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if (mUser == null){
            startActivity(new Intent(getActivity(), onboarding_plan.class));
        } else {
            ActiveUID = mUser.getUid();
            DBFirebase = FirebaseDatabase.getInstance();
            DBreference = DBFirebase.getReference().child("tasks").child(ActiveUID);
            recyclerView = (RecyclerView) getView().findViewById(R.id.dashboard_View);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);

            FirebaseRecyclerOptions<TaskModel> options = new FirebaseRecyclerOptions.Builder<TaskModel>()
                    .setQuery(DBreference.orderByChild("taskCompletion").equalTo(false), TaskModel.class).build();

            FirebaseRecyclerAdapter<TaskModel, NewViewHolder> adapter = new FirebaseRecyclerAdapter<TaskModel, NewViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull NewViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull TaskModel model) {
                    holder.setTask(model.getTaskName());
                    holder.setDeadline_date(model.getTaskDeadline_date());
                    holder.setDeadline_time(model.getTaskDeadline_Time());
                    holder.setCategory(model.getTaskCategory());
                    holder.setPriority(model.getTaskPriority());

                    CheckBox cb = (CheckBox) holder.mView.findViewById(R.id.task_checkboxCompletion);

                    cb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            key = getRef(position).getKey();
                            task = model.getTaskName();
                            description = model.getTaskDesc();
                            date = model.getTaskDeadline_date();
                            time = model.getTaskDeadline_Time();
                            priority = model.getTaskPriority();
                            category = model.getTaskCategory();
                            completion = model.getTaskCompletion();

                            showDialog(cb);
                        }
                    });

                    holder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            key = getRef(position).getKey();
                            task = model.getTaskName();
                            description = model.getTaskDesc();
                            date = model.getTaskDeadline_date();
                            time = model.getTaskDeadline_Time();
                            priority = model.getTaskPriority();
                            category = model.getTaskCategory();
                            completion = model.getTaskCompletion();

                            ViewTask();
                        }
                    });
                }

                @NonNull
                @Override
                public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_data, parent, false);
                    return new NewViewHolder(view);
                }
            };

            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        }
    }


    public static class NewViewHolder extends RecyclerView.ViewHolder{

        public View mView;

        public NewViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTask(String task){
            TextView taskTextView = mView.findViewById(R.id.task_txtTaskName);
            taskTextView.setText(task);
        }

        public void setDeadline_date(String Deadline_date){
            TextView dateTextView = mView.findViewById(R.id.task_txtDate);
            dateTextView.setText(Deadline_date);
        }

        public void setDeadline_time(String Deadline_time){
            TextView timeTextView = mView.findViewById(R.id.task_txtTime);
            timeTextView.setText(Deadline_time);
        }

        public void setCategory(String category){
            TextView categoryView = mView.findViewById(R.id.task_txtCategory);
            categoryView.setText(category);
        }

        public void setPriority(String Priority){
            TextView categoryView = mView.findViewById(R.id.task_txtPriority);
            categoryView.setText(Priority);
        }

    }

    private void ViewTask(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View view = inflater.inflate(R.layout.activity_view_task, null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();

        EditText mTask = view.findViewById(R.id.viewTask_inputTaskName);
        EditText mDesc = view.findViewById(R.id.viewTask_inputDescTask);
        EditText mDate = view.findViewById(R.id.viewTask_inputDeadlineDate);
        EditText mTime = view.findViewById(R.id.viewTask_inputDeadlineTime);

        mTask.setText(task);
        mDesc.setText(description);
        mDate.setText(date);
        mTime.setText(time);

        Button EditTask = view.findViewById(R.id.viewTask_EditTask);
        Button DeleteTask = view.findViewById(R.id.viewTask_DeleteTask);

        Spinner prioritySpinner = view.findViewById(R.id.viewTask_spinnerPriority);
        Spinner categorySpinner = view.findViewById(R.id.viewTask_spinnerCategory);
        prioritySpinner.setOnItemSelectedListener(prioritySpinner.getOnItemSelectedListener());
        categorySpinner.setOnItemSelectedListener(categorySpinner.getOnItemSelectedListener());

        ArrayAdapter priorityArr = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, priorityArray);
        ArrayAdapter categoryArr = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, categoryArray);

        priorityArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryArr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityArr);
        categorySpinner.setAdapter(categoryArr);

        DatePickerDialog.OnDateSetListener datePick = (view1, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd/MM/yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            mDate.setText(sdf.format(myCalendar.getTime()));
        };

        mDate.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getContext(), datePick, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();

        });

        TimePickerDialog.OnTimeSetListener timePick = (view1, hour, minute) -> {
            // TODO Auto-generated method stub
            myTime.set(Calendar.HOUR_OF_DAY, hour);
            myTime.set(Calendar.MINUTE, minute);

            String myFormat2 = "hh:mm a"; //In which you need put here
            SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2);
            mTime.setText(sdf2.format(myTime.getTime()));
        };

        mTime.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new TimePickerDialog(getContext(), timePick, myTime
                    .get(Calendar.HOUR_OF_DAY), myTime.get(Calendar.MINUTE), is24HourView()).show();

        });

        EditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = mTask.getText().toString().trim();
                description = mDesc.getText().toString().trim();
                date = mDate.getText().toString().trim();
                time = mTime.getText().toString().trim();
                priority = prioritySpinner.getSelectedItem().toString().trim();
                category = categorySpinner.getSelectedItem().toString().trim();

                TaskModel model = new TaskModel(key, task, date, time, description, priority, category, Boolean.FALSE);

                DBreference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "Changed Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(getActivity(), "Failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        DeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBreference.child(key).removeValue();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void CompleteTask(){
        TaskModel model = new TaskModel(key, task, date, time, description, priority, category, true);
        DBreference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Congratulation for completing your task!", Toast.LENGTH_SHORT).show();

                    // TODO Refresh Fragment

                } else {
                    String error = task.getException().toString();
                    Toast.makeText(getActivity(), "Failed: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialog(CheckBox cb) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        alertDialogBuilder.setTitle("Mark your task as complete?");

        alertDialogBuilder
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> CompleteTask())
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.cancel();
                    cb.setChecked(false);
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private boolean is24HourView() {
        return true;
    }

}