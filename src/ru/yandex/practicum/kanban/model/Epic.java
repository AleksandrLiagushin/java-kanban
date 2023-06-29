package ru.yandex.practicum.kanban.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(EpicBuilder epicBuilder) {
        super(epicBuilder);
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

    public static class EpicBuilder extends TaskBuilder {

        public EpicBuilder(String name) {
            super(name);
        }

        public EpicBuilder withId(int id) {
            this.setId(id);
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
    public String toString() {

        return getId() + "," +
                TaskType.EPIC + "," +
                getName() + "," +
                getDescription() + "," +
                getStatus() + "," +
                getSubtaskIds() + "\n";
    }
}
