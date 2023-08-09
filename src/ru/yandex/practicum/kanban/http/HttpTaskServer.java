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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    public final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefaultHttpManager());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        gson = Managers.getGson();
        this.taskManager = taskManager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::taskManagerHandler);
    }

    private void taskManagerHandler(HttpExchange h) throws IOException {
        try {
            String method = h.getRequestMethod();
            Map<String, String> params = getQueryMap(h.getRequestURI().getRawQuery());
            String path = h.getRequestURI().getPath().replaceFirst("/tasks", "");

            switch (method) {

                case "GET":
                    if (Pattern.matches("^/task/$", path) && !params.isEmpty()) {
                        getTask(params, "/task/", h);
                        break;
                    }

                    if (Pattern.matches("^/epic/$", path) && !params.isEmpty()) {
                        getTask(params, "/epic/", h);
                        break;
                    }

                    if (Pattern.matches("^/subtask/$", path) && !params.isEmpty()) {
                        getTask(params, "/subtask/", h);
                        break;
                    }

                    if (Pattern.matches("^/task/$", path) && params.isEmpty()) {
                        getAllGenericTasks("/task/", h);
                        break;
                    }

                    if (Pattern.matches("^/epic/$", path) && params.isEmpty()) {
                        getAllGenericTasks("/epic/", h);
                        break;
                    }

                    if (Pattern.matches("^/subtask/$", path) && params.isEmpty()) {
                        getAllGenericTasks("/subtask/", h);
                        break;
                    }

                    if (Pattern.matches("^/priority$", path)) {
                        sendText(h, gson.toJson(taskManager.getTasksPrioritizedByTime()));
                        break;
                    }

                    if (Pattern.matches("^/subtasks/$", path) && !params.isEmpty()) {
                        getTask(params, "/subtasks/", h);
                        break;
                    }

                    if (Pattern.matches("^/history$", path)) {
                        sendText(h, gson.toJson(taskManager.getHistory()));
                        break;
                    }

                    h.sendResponseHeaders(400, 0);
                    break;

                case "POST":
                    if (Pattern.matches("^/task/$", path)) {
                        postTask(params, "/task/", h);
                        break;
                    }

                    if (Pattern.matches("^/epic/$", path)) {
                        postTask(params, "/epic/", h);
                        break;
                    }

                    if (Pattern.matches("^/subtask/$", path)) {
                        postTask(params, "/subtask/", h);
                        break;
                    }

                    h.sendResponseHeaders(400, 0);
                    break;

                case "DELETE":
                    if (Pattern.matches("^/task/$", path) && !params.isEmpty()) {
                        deleteGenericTask(params, "/task/", h);
                        break;
                    }

                    if (Pattern.matches("^/epic/$", path) && !params.isEmpty()) {
                        deleteGenericTask(params, "/epic/", h);
                        break;
                    }

                    if (Pattern.matches("^/subtask/$", path) && !params.isEmpty()) {
                        deleteGenericTask(params, "/subtask/", h);
                        break;
                    }

                    if (Pattern.matches("^/task/$", path) && params.isEmpty()) {

                        taskManager.deleteAllTasks();
                        h.sendResponseHeaders(200, 0);
                        break;
                    }

                    if (Pattern.matches("^/epic/$", path) && params.isEmpty()) {

                        taskManager.deleteAllEpics();
                        h.sendResponseHeaders(200, 0);
                        break;
                    }

                    if (Pattern.matches("^/subtask/$", path) && params.isEmpty()) {

                        taskManager.deleteAllSubtasks();
                        h.sendResponseHeaders(200, 0);
                        break;
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

    public void stop() {
        System.out.println("Server on port " + PORT + " has been stopped.");
        server.stop(0);
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

    private static Map<String, String> getQueryMap(String query) {
        if (query == null) {
            return new HashMap<>();
        }

        Map<String, String> map = new HashMap<>();
        String[] rowParams = query.split("&");

        for (String rowParam : rowParams) {
            String[] paramParts = rowParam.split("=");
            if (paramParts[0].length() > 1) {
                map.put(paramParts[0], paramParts[1]);
            }
        }

        return map;
    }

    private void getTask(Map<String, String> params, String regex, HttpExchange h) throws IOException {

        if (!params.containsKey("id")) {
            h.sendResponseHeaders(400, 0);
            return;
        }
        int id = parsePathId(params.get("id"));
        if (id == -1) {
            h.sendResponseHeaders(400, 0);
            return;
        }
        String stringTask;
        switch (regex) {
            case "/task/":
                stringTask = gson.toJson(taskManager.getTaskById(id));
                break;
            case "/epic/":
                stringTask = gson.toJson(taskManager.getEpicById(id));
                break;
            case "/subtask/":
                stringTask = gson.toJson(taskManager.getSubtaskById(id));
                break;
            case "/subtasks/":
                stringTask = gson.toJson(taskManager.getSubtasksByEpicId(id));
                break;
            default:
                h.sendResponseHeaders(406, -1);
                return;
        }

        if ("null".equals(stringTask)) {
            h.sendResponseHeaders(404, 0);
            return;
        }
        sendText(h, stringTask);
    }

    private void getAllGenericTasks(String regex, HttpExchange h) throws IOException {
        String stringTask;
        switch (regex) {
            case "/task/":
                stringTask = gson.toJson(taskManager.getAllTasks());
                break;
            case "/epic/":
                stringTask = gson.toJson(taskManager.getAllEpics());
                break;
            case "/subtask/":
                stringTask = gson.toJson(taskManager.getAllSubtasks());
                break;
            default:
                h.sendResponseHeaders(406, -1);
                return;
        }

        sendText(h, stringTask);
    }

    private void postTask(Map<String, String> params, String regex, HttpExchange h) throws IOException {
        String rowTask = readText(h);

        if (rowTask.isEmpty()) {
            h.sendResponseHeaders(406, 0);
            return;
        }

        if (!params.isEmpty()) {
            if (!params.containsKey("id")) {
                h.sendResponseHeaders(406, 0);
                return;
            }

            int id = parsePathId(params.get("id"));

            if (id == -1) {
                h.sendResponseHeaders(400, 0);
                return;
            }

            if (updateGenericTask(rowTask, regex)) {
                h.sendResponseHeaders(201, 0);
                return;
            }

            h.sendResponseHeaders(404, 0);
            return;
        }

        if (createGenericTask(rowTask, regex)) {
            h.sendResponseHeaders(201, 0);
            return;
        }

        h.sendResponseHeaders(404, 0);
    }

    private boolean createGenericTask(String stringTask, String regex) {
        switch (regex) {
            case "/task/":
                return taskManager.createTask(gson.fromJson(stringTask, Task.class));
            case "/epic/":
                return taskManager.createEpic(gson.fromJson(stringTask, Epic.class));
            case "/subtask/":
                return taskManager.createSubtask(gson.fromJson(stringTask, Subtask.class));
            default:
                return false;
        }
    }

    private boolean updateGenericTask(String stringTask, String regex) {
        switch (regex) {
            case "/task/":
                return taskManager.updateTask(gson.fromJson(stringTask, Task.class));
            case "/epic/":
                return taskManager.updateEpic(gson.fromJson(stringTask, Epic.class));
            case "/subtask/":
                return taskManager.updateSubtask(gson.fromJson(stringTask, Subtask.class));
            default:
                return false;
        }
    }

    private void deleteGenericTask(Map<String, String> params, String regex, HttpExchange h) throws IOException {
        if (!params.containsKey("id")) {
            h.sendResponseHeaders(406, 0);
            return;
        }

        int id = parsePathId(params.get("id"));

        if (id == -1) {
            h.sendResponseHeaders(400, 0);
            return;
        }

        switch (regex) {
            case "/task/":
                taskManager.deleteTaskById(id);
                h.sendResponseHeaders(200, 0);
                return;
            case "/epic/":
                taskManager.deleteEpicById(id);
                h.sendResponseHeaders(200, 0);
                return;
            case "/subtask/":
                taskManager.deleteSubtaskById(id);
                h.sendResponseHeaders(200, 0);
                return;
            default:
                h.sendResponseHeaders(406, 0);
        }
    }
}
