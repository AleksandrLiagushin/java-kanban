package ru.yandex.practicum.kanban.data.model;

import java.util.Objects;

public class Task {
    private final int id;
    private String taskName;
    private String description;
    private TaskStatus status;

    public Task(int id, String taskName, String description, TaskStatus status) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String taskName, String description) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task task = (Task) obj;
        return (id == task.id
                && taskName.equals(task.taskName)
                && description.equals(task.description)
                && status.equals(task.status));
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
