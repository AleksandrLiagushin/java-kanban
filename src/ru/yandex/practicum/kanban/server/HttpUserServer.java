package ru.yandex.practicum.kanban.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.kanban.service.Managers;
import ru.yandex.practicum.kanban.service.TaskManager;
import ru.yandex.practicum.kanban.service.UserManager;

import java.io.IOException;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpUserServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private Gson gson;
    private TaskManager taskManager;
    private UserManager userManager;

    public HttpUserServer() throws IOException{
        this(Managers.getInMemoryUserManager());
    }

    public HttpUserServer(UserManager userManager) throws IOException {
        this.userManager = userManager;
        this.taskManager = userManager.getTaskManger();
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/task", this::taskHandler);
    }

    private void taskHandler(HttpExchange h) {
        try {
            String method = h.getRequestMethod();
            String path = h.getRequestURI().getPath();
            switch (method) {
                case "GET":
                case "POST":
                case "DELETE":
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("Server is started at port " + PORT);
        System.out.println("Open http://localhost: in your browser" + PORT + "/");
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
