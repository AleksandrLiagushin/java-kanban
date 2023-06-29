package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements FileManager {
    private final Path path = Paths.get("resources", "Tasks.csv");

    @Override
    public void save() {
        String HEADER = "id,type,name,description,status,epicId/subtasksIds\n";
        StringBuilder stringBuilder = new StringBuilder(HEADER);

        for (Task task : getAllTasks()) {
            stringBuilder.append(task);
        }
        for (Epic epic : getAllEpics()) {
            stringBuilder.append(epic);
        }
        for (Subtask subtask : getAllSubtasks()) {
            stringBuilder.append(subtask);
        }
        stringBuilder.append("\n");

        for (Task task : getHistory()) {
            stringBuilder.append(task.getId()).append(',');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        try {
            Files.writeString(path, stringBuilder.toString());
        } catch (IOException e) {
            System.out.println("Запись пошла не по плану");
        }
    }

    @Override
    public void load() {
        try {
            List<String> dataFromFile = Files.readAllLines(path);
            deserializeTask(dataFromFile);
        } catch (IOException e) {
            System.out.println("Что-то пошло не так");
        }
    }

    private void deserializeTask(List<String> dataFromFile) {
        int idx;

        if (dataFromFile.isEmpty()) {
            return;
        }

        for (idx = 1; idx < dataFromFile.size(); idx++) {
            if (dataFromFile.get(idx).isEmpty()) {
                break;
            }
            String[] rowData = dataFromFile.get(idx).split(",");
            switch (TaskType.valueOf(rowData[1])) {
                case TASK:
                    super.createTask(new Task.TaskBuilder(rowData[2])
                            .withId(Integer.parseInt(rowData[0]))
                            .withDescription(rowData[3])
                            .withStatus(TaskStatus.valueOf(rowData[4]))
                            .build());
                    break;
                case EPIC:
                    super.createEpic(new Epic.EpicBuilder(rowData[2])
                            .withId(Integer.parseInt(rowData[0]))
                            .withDescription(rowData[3])
                            .withStatus(TaskStatus.valueOf(rowData[4]))
                            .build());
                    break;
                case SUBTASK:
                    super.createSubtask(new Subtask
                            .SubtaskBuilder(rowData[2], TaskStatus.valueOf(rowData[4]), Integer.parseInt(rowData[5]))
                            .withId(Integer.parseInt(rowData[0]))
                            .withDescription(rowData[3])
                            .build());
                    break;

            }
        }

        String[] history = dataFromFile.get(idx + 1).split(",");
        for (String id : history) {
            super.findTaskById(Integer.parseInt(id));
            super.findEpicById(Integer.parseInt(id));
            super.findSubtaskById(Integer.parseInt(id));
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(Integer taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(Integer epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task findTaskById(int taskId) {
        Task task = super.findTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic findEpicById(int epicId) {
        Epic epic = super.findEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask findSubtaskById(int subtaskId) {
        Subtask subtask = super.findSubtaskById(subtaskId);
        save();
        return subtask;
    }
}
