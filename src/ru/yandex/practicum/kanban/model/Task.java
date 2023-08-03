package ru.yandex.practicum.kanban.model;

import ru.yandex.practicum.kanban.user.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private int id;
    private final String name;
    private final String description;
    private TaskStatus status;
    private LocalDateTime startTime;
    private Duration duration;

    public Task(TaskBuilder taskBuilder) {
        this.id = taskBuilder.id;
        this.name = taskBuilder.name;
        this.description = taskBuilder.description;
        this.status = taskBuilder.status;
        this.startTime = taskBuilder.startTime;
        this.duration = taskBuilder.duration;
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

    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }

    public Optional<LocalDateTime> getEndTime() {
        if (getStartTime().isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(getStartTime().get().plus(getDuration().orElse(Duration.ZERO)));
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public static class TaskBuilder {
        private int id;
        private String name;
        private String description;
        private TaskStatus status;
        private LocalDateTime startTime;
        private Duration duration;

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

        public TaskBuilder withStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public TaskBuilder withDuration(long duration) {
            this.duration = Duration.ofMinutes(duration);
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

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public void setDuration(long duration) {
            this.duration = Duration.ofMinutes(duration);
        }

        public Task build() {
            return new Task(this);
        }

    }

    public String toCsvLine() {
        return id + "," +
                TaskType.TASK + ",'\"" +
                name + "\"','\"" +
                description + "\"'," +
                status + "," +
                getStartTime().orElse(null) + "," +
                getDuration().orElse(Duration.ZERO).toMinutes();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(startTime, task.startTime);
               // && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, startTime, duration);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
