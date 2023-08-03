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
    private final KVTaskClient client;

    public HttpTaskManager(String url) {
        super();
        client = new KVTaskClient(url);
    }

    public static TaskManager load(String url) throws IOException, InterruptedException {
        HttpTaskManager httpTaskManager = new HttpTaskManager(url);
        httpTaskManager.client.register();
        Gson gson = Managers.getGson();

        List<Task> tasks = gson.fromJson(httpTaskManager.client.load("tasks"),
                new TypeToken<List<Task>>() {}.getType());
        List<Epic> epics = gson.fromJson(httpTaskManager.client.load("epics"),
                new TypeToken<List<Epic>>() {}.getType());
        List<Subtask> subtasks = gson.fromJson(httpTaskManager.client.load("subtasks"),
                new TypeToken<List<Subtask>>() {}.getType());
        List<Integer> history = gson.fromJson(httpTaskManager.client.load("history"),
                new TypeToken<List<Integer>>() {}.getType());

        tasks.forEach(httpTaskManager::createTask);
        epics.forEach(httpTaskManager::createEpic);
        subtasks.forEach(httpTaskManager::createSubtask);

        for (Integer id : history) {
            httpTaskManager.getTaskById(id);
            httpTaskManager.getEpicById(id);
            httpTaskManager.getSubtaskById(id);
        }

        return httpTaskManager;
    }

    public void save() {
        Gson gson = Managers.getGson();
        try {
            client.put("tasks", gson.toJson(getAllTasks()));
            client.put("epics", gson.toJson(getAllEpics()));
            client.put("subtasks", gson.toJson(getAllSubtasks()));
            client.put("history", gson.toJson(getHistory().stream().map(Task::getId).collect(Collectors.toList())));
        } catch (IOException | InterruptedException e) {
            throw new KVResponseException("Can't save data to server", e.getCause());
        }
    }
}
