package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.exception.ManagerLoadException;
import ru.yandex.practicum.kanban.exception.ManagerSaveException;
import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;
import ru.yandex.practicum.kanban.model.TaskType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements FileBackedManager {
    private final Path path;
    private static final String CSV_HEADER = "id,type,name,description,status,epicId/subtasksIds\n";
    private static final int CSV_ID = 0;
    private static final int CSV_TYPE = 1;
    private static final int CSV_NAME = 2;
    private static final int CSV_DESCRIPTION = 3;
    private static final int CSV_STATUS = 4;
    private static final int CSV_EPICID = 5;

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    public static FileBackedManager loadFromFile(Path path) {
        FileBackedManager fileBackedManager = new FileBackedTaskManager(path);
        fileBackedManager.load();
        return fileBackedManager;
    }

    @Override
    public void save() {
        StringBuilder stringBuilder = new StringBuilder(CSV_HEADER);

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

        try {
            Files.writeString(path, stringBuilder.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Запись в файл не удалась", e.getCause());
        }
    }

    @Override
    public void load() {
        try {
            List<String> dataFromFile = Files.readAllLines(path);
            deserializeTask(dataFromFile);
        } catch (IOException e) {
            throw new ManagerLoadException("Чтение из файла не удалось, возможно файл поврежден", e.getCause());
        }
    }

    private void deserializeTask(List<String> csvLines) {
        int index; //idx - наследие промышленной автоматики... там есть ограничения на память выделяемую под имена переменных=)

        if (csvLines.isEmpty()) {
            return;
        }

        for (index = 1; index < csvLines.size(); index++) {
            if (csvLines.get(index).isEmpty()) {
                break;
            }
            String[] rowData = csvLines.get(index).split(",");
            switch (TaskType.valueOf(rowData[CSV_TYPE])) {
                case TASK:
                    super.createTask(new Task.TaskBuilder(rowData[CSV_NAME])
                            .withId(Integer.parseInt(rowData[CSV_ID]))
                            .withDescription(rowData[CSV_DESCRIPTION])
                            .withStatus(TaskStatus.valueOf(rowData[CSV_STATUS]))
                            .build());
                    break;
                case EPIC:
                    super.createEpic(new Epic.EpicBuilder(rowData[CSV_NAME])
                            .withId(Integer.parseInt(rowData[CSV_ID]))
                            .withDescription(rowData[CSV_DESCRIPTION])
                            .withStatus(TaskStatus.valueOf(rowData[CSV_STATUS]))
                            .build());
                    break;
                case SUBTASK:
                    super.createSubtask(new Subtask
                            .SubtaskBuilder(rowData[CSV_NAME], TaskStatus.valueOf(rowData[CSV_STATUS]),
                            Integer.parseInt(rowData[CSV_EPICID]))
                            .withId(Integer.parseInt(rowData[CSV_ID]))
                            .withDescription(rowData[CSV_DESCRIPTION])
                            .build());
                    break;

            }
        }

        if (csvLines.size() == index + 1) {
            return;
        }

        String[] history = csvLines.get(index + 1).split(",");
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
