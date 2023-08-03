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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBackedTaskManager extends InMemoryTaskManager implements FileBackedManager {
    private static final String CSV_HEADER = "id,type,name,description,status,startTime,duration,epicId/subtasksIds";
    // описание групп захвата:
    // (^\d+) - определяет id
    // ([A-Z_]+) - определяет type и status
    // ('"(.*?)"') - определяет name и description
    // (\d{4}-\d{2}-\d{2}T\d{2}:?[\d{2}]?:?[\d{2}]?\.?[\d+]?|null) - определяет startTime
    // (\d+) - определяет duration и epicId / subtaskIds
    private static final String CSV_LINE_REGEX =
            "(^\\d+)|([A-Z_]+)|('\"(.*?)\"')|(\\d{4}-\\d{2}-\\d{2}T\\d{2}:?[\\d{2}]?:?[\\d{2}]?\\.?[\\d+]?|null)|(\\d+)";
    private static final int CSV_ID = 0;
    private static final int CSV_TYPE = 1;
    private static final int CSV_NAME = 2;
    private static final int CSV_DESCRIPTION = 3;
    private static final int CSV_STATUS = 4;
    private static final int CSV_START_TIME = 5;
    private static final int CSV_DURATION = 6;
    private static final int CSV_EPIC_ID = 7;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final Path path;

    FileBackedTaskManager() {
        path = null;
    }
    private FileBackedTaskManager(Path path) {
        this.path = path;
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager fileBackedManager = new FileBackedTaskManager(path);
        fileBackedManager.load();
        return fileBackedManager;
    }

    @Override
    public void save() {
        StringBuilder stringBuilder;

        stringBuilder = Stream.of(getAllTasks(), getAllEpics(), getAllSubtasks())
                .flatMap(Collection::stream)
                .collect(StringBuilder::new,
                        (builder, task) -> builder.append(task.toCsvLine()).append('\n'),
                        (x, y) -> x.append("WTF").append(y)); // не понимаю назначение этой строки - она ничего не делает, но при этом обязательна...

        stringBuilder.insert(0, CSV_HEADER + '\n').append("\n");

        stringBuilder.append(getHistory().stream()
                .map(task -> String.valueOf(task.getId()))
                .collect(Collectors.joining(",")))
                .append('\n');

        try {
            Files.writeString(path, stringBuilder.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Error. Data have not been written.", e.getCause());
        }
    }

    @Override
    public void load() {
        try {
            List<String> dataFromFile = Files.readAllLines(path);
            deserializeTask(dataFromFile);
        } catch (IOException e) {
            throw new ManagerLoadException("Error. Can not read the file, it may be damaged", e.getCause());
        }
    }

    private void deserializeTask(List<String> csvLines) {

        int index;
        LocalDateTime startTime;
        Pattern taskPattern = Pattern.compile(CSV_LINE_REGEX);


        if (csvLines.isEmpty()) {
            return;
        }

        for (index = 1; index < csvLines.size(); index++) {

            List<String> parsedCsvLine = taskPattern.matcher(csvLines.get(index))
                    .results()
                    .map(matcher -> matcher.group(0).replaceAll("'\"|\"'", ""))
                    .collect(Collectors.toList());

            if (csvLines.get(index).isEmpty()) {
                break;
            }

            if (!"null".equals(parsedCsvLine.get(CSV_START_TIME))){
                startTime = LocalDateTime.parse(parsedCsvLine.get(CSV_START_TIME), ISO_FORMATTER);
            } else {
                startTime = null;
            }

            switch (TaskType.valueOf(parsedCsvLine.get(CSV_TYPE))) {
                case TASK:
                    super.createTask(Task.builder()
                            .withName(parsedCsvLine.get(CSV_NAME))
                            .withId(Integer.parseInt(parsedCsvLine.get(CSV_ID)))
                            .withDescription(parsedCsvLine.get(CSV_DESCRIPTION))
                            .withStatus(TaskStatus.valueOf(parsedCsvLine.get(CSV_STATUS)))
                            .withStartTime(startTime)
                            .withDuration(Long.parseLong(parsedCsvLine.get(CSV_DURATION)))
                            .build());
                    break;
                case EPIC:
                    super.createEpic(Epic.builder()
                            .withName(parsedCsvLine.get(CSV_NAME))
                            .withId(Integer.parseInt(parsedCsvLine.get(CSV_ID)))
                            .withDescription(parsedCsvLine.get(CSV_DESCRIPTION))
                            .build());
                    break;
                case SUBTASK:
                    super.createSubtask(Subtask.builder()
                            .withId(Integer.parseInt(parsedCsvLine.get(CSV_ID)))
                            .withName(parsedCsvLine.get(CSV_NAME))
                            .withDescription(parsedCsvLine.get(CSV_DESCRIPTION))
                            .withStatus(TaskStatus.valueOf(parsedCsvLine.get(CSV_STATUS)))
                            .withStartTime(startTime)
                            .withDuration(Long.parseLong(parsedCsvLine.get(CSV_DURATION)))
                            .withEpicId(Integer.parseInt(parsedCsvLine.get(CSV_EPIC_ID)))
                            .build());
                    break;
            }
        }

        if (csvLines.size() == index + 1 || csvLines.get(index + 1).isEmpty()) {
            return;
        }

        String[] history = csvLines.get(index + 1).split(",");
        for (String id : history) {
            super.getTaskById(Integer.parseInt(id));
            super.getEpicById(Integer.parseInt(id));
            super.getSubtaskById(Integer.parseInt(id));
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
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = super.getEpicById(epicId);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = super.getSubtaskById(subtaskId);
        save();
        return subtask;
    }
}
