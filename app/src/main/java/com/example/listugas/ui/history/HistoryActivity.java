package com.example.listugas.ui.history;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listugas.R;
import com.example.listugas.ui.add_task.TaskModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HistoryActivity extends Fragment {

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_history, container, false);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        ActiveUID = mUser.getUid();
        DBFirebase = FirebaseDatabase.getInstance();
        DBreference = DBFirebase.getReference().child("tasks").child(ActiveUID);
        recyclerView = (RecyclerView) getView().findViewById(R.id.history_View);
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        llm2.setOrientation(LinearLayoutManager.VERTICAL);

        FirebaseRecyclerOptions<TaskModel> options = new FirebaseRecyclerOptions.Builder<TaskModel>()
                .setQuery(DBreference.orderByChild("taskCompletion").equalTo(true), TaskModel.class).build();

        FirebaseRecyclerAdapter<TaskModel, HistoryActivity.NewViewHolder2> adapter = new FirebaseRecyclerAdapter<TaskModel, HistoryActivity.NewViewHolder2>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryActivity.NewViewHolder2 holder, @SuppressLint("RecyclerView") int position, @NonNull TaskModel model) {
                holder.setTask(model.getTaskName());
                holder.setDeadline_date(model.getTaskDeadline_date());
                holder.setDeadline_time(model.getTaskDeadline_Time());
                holder.setCategory(model.getTaskCategory());
                holder.setPriority(model.getTaskPriority());

                CheckBox cb = (CheckBox) holder.mView.findViewById(R.id.task_checkboxCompletion);
                cb.setChecked(true);

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
            public HistoryActivity.NewViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_data, parent, false);

                return new HistoryActivity.NewViewHolder2(view);
            }
        };

        recyclerView.setLayoutManager(llm2);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public class NewViewHolder2 extends RecyclerView.ViewHolder {

        View mView;

        public NewViewHolder2(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTask(String task) {
            TextView taskTextView = mView.findViewById(R.id.task_txtTaskName);
            taskTextView.setText(task);
        }

        public void setDeadline_date(String Deadline_date) {
            TextView dateTextView = mView.findViewById(R.id.task_txtDate);
            dateTextView.setText(Deadline_date);
        }

        public void setDeadline_time(String Deadline_time) {
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

    private void ViewTask() {
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

        EditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = mTask.getText().toString().trim();
                description = mDesc.getText().toString().trim();
                date = mDate.getText().toString().trim();
                time = mTime.getText().toString().trim();

                TaskModel model = new TaskModel(key, task, date, time, description, priority, category, true);

                DBreference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Changed Successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(getContext(), "Failed: " + error, Toast.LENGTH_SHORT).show();
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

    private void UncompleteTask(){
        TaskModel model = new TaskModel(key, task, date, time, description, priority, category, false);
        DBreference.child(key).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Changed Successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    String error = task.getException().toString();
                    Toast.makeText(getContext(), "Failed: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialog(CheckBox cb) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getContext());

        alertDialogBuilder.setTitle("Mark your task as UNCOMPLETE?");

        alertDialogBuilder
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> UncompleteTask())
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.cancel();
                    cb.setChecked(true);
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}