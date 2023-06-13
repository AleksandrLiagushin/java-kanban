package ru.yandex.practicum.kanban.model;

import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public int getEpicId() {
        return epicId;
    }

    public Subtask setEpicId(int epicId) {
        this.epicId = epicId;
        return this;
    }

    @Override
    public Subtask setId(int id) {
        super.setId(id);
        return this;
    }
    @Override
    public Subtask setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public Subtask setDescription(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public Subtask setStatus(TaskStatus status) {
        super.setStatus(status);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subtask)) {
            return false;
        }
        if (!super.equals(o)) {
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
