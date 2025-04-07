package com.example.a41p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

public class TaskDetailActivity extends AppCompatActivity {
    private TaskDao taskDao;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        taskDao = TaskDatabase.getInstance(this).taskDao();
        taskId = getIntent().getIntExtra("taskId", -1);

        TextView titleView = findViewById(R.id.detail_title);
        TextView descView = findViewById(R.id.detail_description);
        TextView dueDateView = findViewById(R.id.detail_due_date);
        Button editButton = findViewById(R.id.button_edit);
        Button deleteButton = findViewById(R.id.button_delete);

        Executors.newSingleThreadExecutor().execute(() -> {
            Task task = taskDao.getTaskById(taskId);
            runOnUiThread(() -> {
                titleView.setText(task.getTitle());
                descView.setText(task.getDescription());
                dueDateView.setText(task.getDueDate());

                editButton.setOnClickListener(v -> {
                    Intent intent = new Intent(TaskDetailActivity.this, AddEditTaskActivity.class);
                    intent.putExtra("taskId", taskId);
                    startActivity(intent);
                });

                deleteButton.setOnClickListener(v -> {
                    Executors.newSingleThreadExecutor().execute(() -> {
                        taskDao.delete(task);
                        finish();
                    });
                });
            });
        });
    }
}

