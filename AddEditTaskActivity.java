package com.example.a41p;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.Executors;

public class AddEditTaskActivity extends AppCompatActivity {
    private TaskDao taskDao;
    private EditText inputTitle, inputDescription, inputDueDate;
    private int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        inputTitle = findViewById(R.id.edit_title);
        inputDescription = findViewById(R.id.edit_description);
        inputDueDate = findViewById(R.id.edit_due_date);
        Button saveButton = findViewById(R.id.button_save);

        taskDao = TaskDatabase.getInstance(this).taskDao();

        if (getIntent().hasExtra("taskId")) {
            taskId = getIntent().getIntExtra("taskId", -1);
            Executors.newSingleThreadExecutor().execute(() -> {
                Task task = taskDao.getTaskById(taskId);
                runOnUiThread(() -> {
                    if (task != null) {
                        inputTitle.setText(task.getTitle());
                        inputDescription.setText(task.getDescription());
                        inputDueDate.setText(task.getDueDate());
                    }
                });
            });
        }

        saveButton.setOnClickListener(v -> {
            String title = inputTitle.getText().toString().trim();
            String desc = inputDescription.getText().toString().trim();
            String dueDate = inputDueDate.getText().toString().trim();


            if (title.isEmpty()) {
                inputTitle.setError("The title cannot be empty！");
                inputTitle.requestFocus();
                return;
            }

            if (dueDate.isEmpty()) {
                inputDueDate.setError("The deadline cannot be empty！");
                inputDueDate.requestFocus();
                return;
            }


            if (!dueDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                inputDueDate.setError("The date format should be YYYY-MM-DD！");
                inputDueDate.requestFocus();
                return;
            }


            Task task = new Task();
            task.setTitle(title);
            task.setDescription(desc);
            task.setDueDate(dueDate);
            if (taskId != -1) task.setId(taskId);

            Executors.newSingleThreadExecutor().execute(() -> {
                if (taskId == -1) {
                    taskDao.insert(task);
                } else {
                    taskDao.update(task);
                }
                runOnUiThread(() -> {
                    Toast.makeText(AddEditTaskActivity.this, "Save successfully！", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}
