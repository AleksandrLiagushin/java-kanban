package ru.yandex.practicum.kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    private Epic(EpicBuilder epicBuilder) {
        super(epicBuilder);
        subtaskIds.addAll(epicBuilder.subtaskIds);
    }

    public List<Integer> getSubtaskIds() {
        return Collections.unmodifiableList(subtaskIds);
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public static EpicBuilder builder() {
        return new EpicBuilder();
    }

    public static class EpicBuilder extends TaskBuilder {
        private final List<Integer> subtaskIds = new ArrayList<>();

        //нет возможности поставить private т.к. используется за границами класса
        EpicBuilder() {
        }

        public EpicBuilder withId(int id) {
            this.setId(id);
            return this;
        }

        public EpicBuilder withName(String name) {
            super.setName(name);
            return this;
        }

        public EpicBuilder withDescription(String description) {
            this.setDescription(description);
            return this;
        }

        public EpicBuilder withStatus(TaskStatus status) {
            this.setStatus(status);
            return this;
        }

        public EpicBuilder withStartTime(LocalDateTime startTime) {
            this.setStartTime(startTime);
            return this;
        }

        public EpicBuilder withDuration(long duration) {
            this.setDuration(duration);
            return this;
        }

        public EpicBuilder withSubtaskIds(List<Integer> subtaskIds) {
            this.subtaskIds.addAll(subtaskIds);
            return this;
        }

        public Epic build() {
            return new Epic(this);
        }
    }

    public void setStatus(TaskStatus status) {
        super.setStatus(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass() || !super.equals(o)) {
            return false;
        }

        Epic epic = (Epic) o;

        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toCsvLine() {
        return getId() + "," +
                TaskType.EPIC + ",'\"" +
                getName() + "\"','\"" +
                getDuration() + "\"'," +
                getStatus() + "," +
                getStartTime().orElse(null) + "," +
                getDuration().orElse(Duration.ZERO).toMinutes() + "," +
                subtaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", startTime=" + getStartTime().orElse(null) +
                ", duration=" + getDuration().orElse(Duration.ZERO).toMinutes() +
                "subtaskIds=" + subtaskIds +
                '}';
    }
}
