package ru.yandex.practicum.kanban.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.kanban.service.Managers;
import ru.yandex.practicum.kanban.service.TaskManager;
import ru.yandex.practicum.kanban.service.UserManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpUserServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private Gson gson;
    private TaskManager taskManager;
    private UserManager userManager;

    public HttpUserServer() throws IOException {
        this(Managers.getInMemoryUserManager());
    }

    public HttpUserServer(UserManager userManager) throws IOException {
        this.userManager = userManager;
        this.taskManager = userManager.getTaskManger();
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/api/v1/users", this::taskHandler);
    }

    private void taskHandler(HttpExchange h) {
        try {
            String method = h.getRequestMethod();
            String path = h.getRequestURI().getPath();
            switch (method) {
                case "GET":
                    if (Pattern.matches("^/api/v1/users$", path)) {
                        String response = gson.toJson(userManager.getAllUsers());
                        sendText(h, response);
                        break;
                    }

                    if (Pattern.matches("^/api/v1/users/\\d+$", path)) {
                        String pathId = path.replaceFirst("/api/v1/users/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(userManager.getById(id));
                            System.out.println("User gotten successfully");
                            sendText(h, response);
                        } else {
                            System.out.println("Wrong id= " + pathId);
                            h.sendResponseHeaders(405, 0);
                        }
                        break;
                    }

                    if (Pattern.matches("^/api/v1/users/\\d+/tasks$", path)) {
                        String pathId = path.replaceFirst("/api/v1/users/", "")
                                .replaceFirst("/tasks", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            String response = gson.toJson(userManager.getUserTasks(id));
                            sendText(h, response);
                        } else {
                            System.out.println("Wrong user id= " + pathId);
                            h.sendResponseHeaders(405, 0);
                        }
                    }
                    break;
                case "POST":

                    break;
                case "DELETE":
                    if (Pattern.matches("^/api/v1/users/\\d+$", path)) {
                        String pathId = path.replaceFirst("/api/v1/users/", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            userManager.delete(id);
                            System.out.println("User deleted successfully");
                            h.sendResponseHeaders(200, 0);
                        } else {
                            System.out.println("Wrong id= " + pathId);
                            h.sendResponseHeaders(405, 0);
                        }
                    } else {
                        h.sendResponseHeaders(405, 0);
                    }
                    break;
                default:
                    System.out.println("Bad request. Allowed GET POST and DELETE methods");
                    h.sendResponseHeaders(405, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void start() {
        System.out.println("Server is started at port " + PORT);
        System.out.println("Open http://localhost:" + PORT + "/ in your browser");
//        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped");
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
}
