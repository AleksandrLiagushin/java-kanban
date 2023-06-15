package ru.yandex.practicum.kanban.model;

import java.util.Objects;

public class Task {
    private int id;
    private String name;
    private String description;
    private TaskStatus status;

    public Task(TaskBuilder taskBuilder) {
        this.id = taskBuilder.id;
        this.name = taskBuilder.name;
        this.description = taskBuilder.description;
        this.status = taskBuilder.status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public static class TaskBuilder {
        protected int id;
        protected String name;
        protected String description;
        protected TaskStatus status;

        public TaskBuilder(String name) {
            this.name = name;
        }

        public TaskBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public TaskBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public TaskBuilder setStatus(TaskStatus status) {
            this.status = status;
            return this;
        }

        public Task build() {
            return new Task(this);
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Task)) {
            return false;
        }
        Task task = (Task) obj;
        return id == task.id
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", taskName='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                "}\n";
    }
}
