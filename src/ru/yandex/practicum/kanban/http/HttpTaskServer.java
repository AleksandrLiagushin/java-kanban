package ru.yandex.practicum.kanban.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.service.Managers;
import ru.yandex.practicum.kanban.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager = Managers.getDefault();

    public HttpTaskServer() throws IOException {
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::taskManagerHandler);
    }

    private void taskManagerHandler(HttpExchange h) throws IOException {
        try {
            String method = h.getRequestMethod();
            String path = h.getRequestURI().getPath().replaceFirst("/tasks", "");
            switch (method) {
                case "GET":
                    if (Pattern.matches("^/$", path)) {
                        List<Task> allTasks = Stream.of(taskManager.getAllTasks(),
                                        taskManager.getAllEpics(), taskManager.getAllSubtasks())
                                .flatMap(Collection::stream)
                                .collect(Collectors.toList());
                        sendText(h, gson.toJson(allTasks));
                        break;
                    }

                    if (Pattern.matches("^/task/\\d+$", path)) {
                        int id = parsePathId(path.replaceFirst("/task/", ""));

                        if (id == -1) {
                            h.sendResponseHeaders(400, 0);
                            break;
                        }

                        Task task = taskManager.getTaskById(id);

                        if (task == null) {
                            h.sendResponseHeaders(404, 0);
                            break;
                        }

                        sendText(h, gson.toJson(task));
                    }

                    if (Pattern.matches("^/epic/\\d+$", path)) {
                        int id = parsePathId(path.replaceFirst("/epic/", ""));

                        if (id == -1) {
                            h.sendResponseHeaders(400, 0);
                            break;
                        }

                        Task task = taskManager.getEpicById(id);

                        if (task == null) {
                            h.sendResponseHeaders(404, 0);
                            break;
                        }

                        sendText(h, gson.toJson(task));
                    }

                    if (Pattern.matches("^/subtask/\\d+$", path)) {
                        int id = parsePathId(path.replaceFirst("/subtask/", ""));

                        if (id == -1) {
                            h.sendResponseHeaders(400, 0);
                            break;
                        }

                        Task task = taskManager.getTaskById(id);

                        if (task == null) {
                            h.sendResponseHeaders(404, 0);
                            break;
                        }

                        sendText(h, gson.toJson(task));
                        break;
                    }

                    if (Pattern.matches("^/priority$", path)) {
                        sendText(h, gson.toJson(taskManager.getTasksPrioritizedByTime()));
                        break;
                    }

                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        int id = parsePathId(path.replaceFirst("/subtasks/", ""));

                        if (id == -1) {
                            h.sendResponseHeaders(400, 0);
                            break;
                        }

                        if (taskManager.getEpicById(id) == null) {
                            h.sendResponseHeaders(404, 0);
                            break;
                        }

                        sendText(h, gson.toJson(taskManager.getSubtaskById(id)));
                    }

                    if (Pattern.matches("^/history$", path)) {
                        sendText(h, gson.toJson(taskManager.getHistory()));
                    }

                    h.sendResponseHeaders(400, 0);
                    break;
                case "POST":
                    if (Pattern.matches("^/task/\\d+$", path)) {
                        int id = parsePathId(path.replaceFirst("/task/", ""));

                        if (id == -1) {
                            h.sendResponseHeaders(400, 0);
                            break;
                        }

                        if (taskManager.getTaskById(id) == null) {
                            h.sendResponseHeaders(404, 0);
                            break;
                        }

                        Task task = gson.fromJson(h.getRequestBody().toString(), Task.class);
                        taskManager.updateTask(task);

                        h.sendResponseHeaders(201, 0);
                    }

                    if (Pattern.matches("^/epic/\\d+$", path)) {
                        int id = parsePathId(path.replaceFirst("/epic/", ""));

                        if (id == -1) {
                            h.sendResponseHeaders(400, 0);
                            break;
                        }

                        if (taskManager.getEpicById(id) == null) {
                            h.sendResponseHeaders(404, 0);
                            break;
                        }

                        Epic epic = gson.fromJson(h.getRequestBody().toString(), Epic.class);
                        taskManager.updateEpic(epic);

                        h.sendResponseHeaders(201, 0);
                    }

                    if (Pattern.matches("^/subtask/\\d+$", path)) {
                        int id = parsePathId(path.replaceFirst("/subtask/", ""));

                        if (id == -1) {
                            h.sendResponseHeaders(400, 0);
                            break;
                        }

                        if (taskManager.getSubtaskById(id) == null) {
                            h.sendResponseHeaders(404, 0);
                            break;
                        }

                        Subtask subtask = gson.fromJson(h.getRequestBody().toString(), Subtask.class);
                        taskManager.updateTask(subtask);

                        h.sendResponseHeaders(201, 0);
                    }

                    if (Pattern.matches("^/task/$", path)) {
                        Task task = gson.fromJson(h.getRequestBody().toString(), Task.class);
                        taskManager.createTask(task);

                        h.sendResponseHeaders(201, 0);
                    }

                    if (Pattern.matches("^/epic$", path)) {
                        Epic epic = gson.fromJson(h.getRequestBody().toString(), Epic.class);
                        taskManager.createTask(epic);

                        h.sendResponseHeaders(201, 0);
                    }

                    if (Pattern.matches("^/subtask/$", path)) {
                        Subtask subtask = gson.fromJson(h.getRequestBody().toString(), Subtask.class);
                        taskManager.createTask(subtask);

                        h.sendResponseHeaders(201, 0);
                    }

                    h.sendResponseHeaders(400, 0);
                    break;
                case "DELETE":
                    if (Pattern.matches("^/task/\\d+$", path)) {
                        int id = parsePathId(path.replaceFirst("/task/", ""));

                        if (id == -1) {
                            h.sendResponseHeaders(400, 0);
                            break;
                        }

                        taskManager.deleteTaskById(id);

                        h.sendResponseHeaders(200, 0);
                    }

                    if (Pattern.matches("^/epic/\\d+$", path)) {
                        int id = parsePathId(path.replaceFirst("/epic/", ""));

                        if (id == -1) {
                            h.sendResponseHeaders(400, 0);
                            break;
                        }

                        taskManager.deleteEpicById(id);

                        h.sendResponseHeaders(200, 0);
                    }

                    if (Pattern.matches("^/subtask/\\d+$", path)) {
                        int id = parsePathId(path.replaceFirst("/subtask/", ""));

                        if (id == -1) {
                            h.sendResponseHeaders(400, 0);
                            break;
                        }

                        taskManager.deleteSubtaskById(id);

                        h.sendResponseHeaders(200, 0);
                    }

                    if (Pattern.matches("^/task$", path)) {

                        taskManager.deleteAllTasks();

                        h.sendResponseHeaders(200, 0);
                    }

                    if (Pattern.matches("^/epic$", path)) {

                        taskManager.deleteAllEpics();

                        h.sendResponseHeaders(200, 0);
                    }

                    if (Pattern.matches("^/subtask$", path)) {

                        taskManager.deleteAllSubtasks();

                        h.sendResponseHeaders(200, 0);
                    }

                    h.sendResponseHeaders(400, 0);
                    break;
                default:
                    h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
