package ru.yandex.practicum.kanban.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;
import ru.yandex.practicum.kanban.service.Managers;
import ru.yandex.practicum.kanban.service.TaskManager;
import ru.yandex.practicum.kanban.user.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpUserServerTest {

    private final Gson gson = Managers.getGson();
    private TaskManager taskManager;
    private UserManager userManager;
    private Task task;
    private User user;

    @BeforeEach
    void init() throws IOException {
        userManager = Managers.getInMemoryUserManager();
        taskManager = userManager.getTaskManger();

        userServer = new HttpUserServer(userManager);

        user = new User("Test", "Testov");

        task = Task.builder()
                .withName("test").withDescription("test description").withStatus(TaskStatus.NEW)
                .withDuration(15).withStartTime(LocalDateTime.now())
                .build();

        taskManager.createTask(task);

        userServer.start();
    }

    @AfterEach
    void tearDown() {
        userServer.stop();
    }

    @Test
    void getUsers() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/api/v1/users");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void stop() {
    }
}