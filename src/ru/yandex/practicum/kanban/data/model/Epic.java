package ru.yandex.practicum.kanban.data.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(int id, String taskName, String description) {
        super(id, taskName, description);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void removeSubtaskId(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    public void addSubtaskId(int subID) {
        subtaskIds.add(subID);
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
