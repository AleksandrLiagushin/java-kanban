package ru.yandex.practicum.kanban.model;

import java.util.Objects;

public class Task {
    private int id;
    private final String name;
    private final String description;
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

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public static class TaskBuilder {
        private int id;
        private String name;
        private String description;
        private TaskStatus status;

        TaskBuilder() {
        }

        public TaskBuilder withId(int id) {
            this.id = id;
            return this;
        }

        public TaskBuilder withName(String name) {
            this.name = name;
            return this;
        }
        public TaskBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public TaskBuilder withStatus(TaskStatus status) {
            this.status = status;
            return this;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setStatus(TaskStatus status) {
            this.status = status;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Task task = (Task) o;

        return id == task.id
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    public String toCsvLine() {
        return String.valueOf(id) + '\'' +
                TaskType.TASK + ',' +
                name + ',' +
                description + ',' +
                status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
