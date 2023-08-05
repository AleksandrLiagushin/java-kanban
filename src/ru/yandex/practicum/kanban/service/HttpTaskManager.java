package ru.yandex.practicum.kanban.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.kanban.exception.KVResponseException;
import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.http.KVTaskClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTaskManager {
    private final Gson gson = Managers.getGson();
    private final KVTaskClient kvTaskClient;

    public HttpTaskManager(String url) {
        super();
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void load() {
        List<Task> tasks = gson.fromJson(kvTaskClient.load("tasks"),
                new TypeToken<List<Task>>() {}.getType());
        List<Epic> epics = gson.fromJson(kvTaskClient.load("epics"),
                new TypeToken<List<Epic>>() {}.getType());
        List<Subtask> subtasks = gson.fromJson(kvTaskClient.load("subtasks"),
                new TypeToken<List<Subtask>>() {}.getType());
        List<Integer> history = gson.fromJson(kvTaskClient.load("history"),
                new TypeToken<List<Integer>>() {}.getType());

        tasks.forEach(this::restoreTask);
        epics.forEach(this::restoreTask);
        subtasks.forEach(this::restoreTask);
        this.restoreHistory(history);
    }

    @Override
    public void save() {
        Gson gson = Managers.getGson();
        try {
            kvTaskClient.put("tasks", gson.toJson(getAllTasks()));
            kvTaskClient.put("epics", gson.toJson(getAllEpics()));
            kvTaskClient.put("subtasks", gson.toJson(getAllSubtasks()));
            kvTaskClient.put("history", gson.toJson(getHistory().stream().map(Task::getId).collect(Collectors.toList())));
        } catch (IOException | InterruptedException e) {
            throw new KVResponseException("Can't save data to server", e.getCause());
        }
    }
}
