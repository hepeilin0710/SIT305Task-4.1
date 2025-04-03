package com.example.a41p;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;
    private TaskDao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ✅ 设置边距适配
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ✅ 初始化数据库
        taskDao = TaskDatabase.getInstance(this).taskDao();

        // ✅ 初始化 RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        taskAdapter = new TaskAdapter(task -> {
            // 点击任务后进入详情页面
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            intent.putExtra("taskId", task.getId());
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        // ✅ 加载任务数据
        taskDao.getAllTasks().observe(this, tasks -> {
            taskAdapter.submitList(tasks);
        });

        // ✅ 设置加号按钮跳转到添加任务页面
        findViewById(R.id.fab_add_task).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });
    }
}
