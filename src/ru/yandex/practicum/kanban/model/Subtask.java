package ru.yandex.practicum.kanban.model;

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

    public static class SubtaskBuilder extends TaskBuilder {
        private final int epicId;

        public SubtaskBuilder(String name, TaskStatus status, int epicId) {
            super(name);
            this.setStatus(status);
            this.epicId = epicId;
        }

        public SubtaskBuilder withDescription(String description) {
            this.setDescription(description);
            return this;
        }

        public SubtaskBuilder withId(int id) {
            this.setId(id);
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
    public String toString() {
        return "SubTask{" + "ID='" + getId() +
                "' Task{" + "Name='" + getName() + '\'' +
                ", Descr='" + getDescription() + '\'' +
                ", Stat='" + getStatus() + '\'' +
                ", Epic='" + getEpicId() + "'}\n";
    }
}
