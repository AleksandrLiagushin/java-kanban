package ru.yandex.practicum.kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(SubtaskBuilder subtaskBuilder) {
        super(subtaskBuilder);
        this.epicId = subtaskBuilder.epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    public static SubtaskBuilder builder() {
        return new SubtaskBuilder();
    }

    public static class SubtaskBuilder extends TaskBuilder {
        private int epicId;

        SubtaskBuilder() {
        }

        public SubtaskBuilder withId(int id) {
            this.setId(id);
            return this;
        }

        public SubtaskBuilder withName(String name) {
            super.setName(name);
            return this;
        }

        public SubtaskBuilder withDescription(String description) {
            this.setDescription(description);
            return this;
        }

        public SubtaskBuilder withStatus(TaskStatus status) {
            this.setStatus(status);
            return this;
        }

        public SubtaskBuilder withStartTime(LocalDateTime startTime) {
            this.setStartTime(startTime);
            return this;
        }

        public SubtaskBuilder withDuration(long duration) {
            this.setDuration(duration);
            return this;
        }

        public SubtaskBuilder withEpicId(int epicId) {
            this.epicId = epicId;
            return this;
        }

        public Subtask build() {
            return new Subtask(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || !super.equals(o)) {
            return false;
        }

        Subtask subtask = (Subtask) o;

        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toCsvLine() {
        return getId() + "," +
                TaskType.SUBTASK + ",'\"" +
                getName() + "\"','\"" +
                getDuration() + "\"'," +
                getStatus() + "," +
                getStartTime().orElse(null) + "," +
                getDuration().orElse(Duration.ZERO).toMinutes() + "," +
                epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime().orElse(null) +
                ", duration=" + getDuration().orElse(Duration.ZERO).toMinutes() +
                "epicId=" + epicId +
                '}';
    }
}
