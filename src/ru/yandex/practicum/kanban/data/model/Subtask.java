package ru.yandex.practicum.kanban.data.model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String taskName, String description, TaskStatus status, int epicId) {
        super(id, taskName, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicID) {
        epicId = epicID;
    }

    @Override
    public String toString() {
        return "SubTask{" + "ID='" + getId() +
                "' Task{" + "Name='" + getTaskName() + '\'' +
                ", Descr='" + getDescription() + '\'' +
                ", Stat='" + getStatus() + '\'' +
                ", Epic='" + getEpicId() + "'}\n";
    }
}
