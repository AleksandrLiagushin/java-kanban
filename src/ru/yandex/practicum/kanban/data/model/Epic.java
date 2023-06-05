package ru.yandex.practicum.kanban.data.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    public List<Integer> getSubtaskIds() {
        return Collections.unmodifiableList(subtaskIds);
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void addSubtaskId(int subID) {
        subtaskIds.add(subID);
    }

    @Override
    public Epic setId(int id) {
        super.setId(id);
        return this;
    }
    @Override
    public Epic setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public Epic setDescription(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public Epic setStatus(TaskStatus status) {
        super.setStatus(status);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Epic)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Epic epic = (Epic) o;
        return subtaskIds.equals(epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        return "EpicTask{" + "ID='" + getId() +
                "' Task{" + "Name='" + getName() +
                "', Descr='" + getDescription() +
                "', Status='" + getStatus() +
                " SubTasksIDs=" +
                getSubtaskIds() + "}\n";
    }
}
