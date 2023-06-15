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

    public void addSubtaskId(int subID) {
        subtaskIds.add(subID);
    }

    public static class EpicBuilder extends TaskBuilder {

        public EpicBuilder(String name) {
            super(name);
        }

        public EpicBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public EpicBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Epic build() {
            return new Epic(this);
        }
    }


    /*
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
*/
    public void setStatus(TaskStatus status) {
        super.setStatus(status);
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
