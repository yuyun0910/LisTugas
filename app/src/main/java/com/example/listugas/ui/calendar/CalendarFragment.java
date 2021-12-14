package com.example.listugas.ui.calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

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

public class CalendarFragment extends Fragment{

    CalendarView calendarView;

    FirebaseDatabase DBFirebase;
    DatabaseReference DBreference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String ActiveUID;

    RecyclerView recyclerView;

    private String key = "";
    private String task, description, date, time, priority, category;
    private Boolean completion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

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
            recyclerView = (RecyclerView) getView().findViewById(R.id.calendar_agendaList);
            LinearLayoutManager llm = new LinearLayoutManager(getContext());
            llm.setOrientation(LinearLayoutManager.VERTICAL);

            calendarView = (CalendarView) getView().findViewById(R.id.calendar_calendar);

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                    String curDay, curMonth, curYear;

                    if (day < 10){
                        curDay = "0" + String.valueOf(day);
                    } else {
                        curDay = String.valueOf(day);
                    }

                    if (month+1 < 10){
                        curMonth = "0" + String.valueOf(month+1);
                    } else {
                        curMonth = String.valueOf(month+1);
                    }

                    curYear = String.valueOf(year);

                    String dateToday = curDay+"/"+curMonth+"/"+curYear;

                    FirebaseRecyclerOptions<TaskModel> options = new FirebaseRecyclerOptions.Builder<TaskModel>()
                            .setQuery(DBreference.orderByChild("taskDeadline_date").equalTo(dateToday), TaskModel.class).build();

                    FirebaseRecyclerAdapter<TaskModel, CalendarFragment.NewViewHolder3> adapter = new FirebaseRecyclerAdapter<TaskModel, CalendarFragment.NewViewHolder3>(options) {
                        @Override
                        public void onBindViewHolder(@NonNull CalendarFragment.NewViewHolder3 holder, @SuppressLint("RecyclerView") int position, @NonNull TaskModel model) {
                            holder.setTask(model.getTaskName());
                            holder.setDeadline_date(model.getTaskDeadline_date());
                            holder.setDeadline_time(model.getTaskDeadline_Time());
                            holder.setCategory(model.getTaskCategory());
                            holder.setPriority(model.getTaskPriority());

                            CheckBox cb = (CheckBox) holder.mView.findViewById(R.id.task_checkboxCompletion);
                            completion = model.getTaskCompletion();

                            if (completion == true){
                                cb.setChecked(true);
                            } else {
                                cb.setChecked(false);
                            }

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

                                    showDialog(cb);
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public CalendarFragment.NewViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_data, parent, false);
                            return new CalendarFragment.NewViewHolder3(view);
                        }
                    };
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }
            });

        }
    }


    public static class NewViewHolder3 extends RecyclerView.ViewHolder{

        View mView;

        public NewViewHolder3(@NonNull View itemView) {
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

    private void UncompleteTask(){
        TaskModel model = new TaskModel(key, task, date, time, description, priority, category, false);
        DBreference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Changed successfully!", Toast.LENGTH_SHORT).show();

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

        if (completion == false){
            alertDialogBuilder.setTitle("Mark your task as COMPLETE?");

            alertDialogBuilder
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> CompleteTask())
                    .setNegativeButton("Cancel", (dialog, id) -> {
                        dialog.cancel();
                        cb.setChecked(false);
                    });
        } else {
            alertDialogBuilder.setTitle("Mark your task as UNCOMPLETE?");

            alertDialogBuilder
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> UncompleteTask())
                    .setNegativeButton("Cancel", (dialog, id) -> {
                        dialog.cancel();
                        cb.setChecked(true);
                    });
        }



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}