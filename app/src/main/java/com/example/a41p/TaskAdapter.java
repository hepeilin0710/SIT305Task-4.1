package com.example.a41p;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    private List<Task> taskList = new ArrayList<>();
    private OnItemClickListener listener;

    public TaskAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<Task> tasks) {
        taskList = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.textTitle.setText(task.getTitle());
        holder.textDueDate.setText(task.getDueDate());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDueDate;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textDueDate = itemView.findViewById(R.id.text_due_date);
        }
    }
}
