package ru.yandex.practicum.kanban.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;
import ru.yandex.practicum.kanban.service.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private final HttpClient client = HttpClient.newHttpClient();
    Gson gson = Managers.getGson();


    @BeforeEach
    public void createNewServer() throws IOException {
        server = new HttpTaskServer(Managers.getDefault());
        server.start();
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void shouldCreateNewTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        Task task = Task.builder().withName("Task").withDescription("Task").build();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(server.taskManager.getTaskById(1), Task.builder().withId(1).withName("Task").withDescription("Task").build());
    }

    @Test
    public void shouldUpdateTask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        URI uriUpdate = URI.create("http://localhost:8080/tasks/task/?id=1");
        Task task = Task.builder().withName("Task1").withDescription("Task").build();
        Task taskUpdate = Task.builder()
                .withId(1).withName("Task11").withDescription("Task").withStatus(TaskStatus.DONE).build();
        String json1 = gson.toJson(task);
        String json2 = gson.toJson(taskUpdate);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body1)
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uriUpdate)
                .POST(body2)
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response1.statusCode());
        assertEquals(201, response2.statusCode());
        assertEquals(server.taskManager.getTaskById(1),
                Task.builder().withId(1).withName("Task11").withDescription("Task").withStatus(TaskStatus.DONE).build());
    }

    @Test
    public void shouldUpdateEpic() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        URI uriUpdate = URI.create("http://localhost:8080/tasks/epic/?id=1");
        Epic epic = Epic.builder().withName("Task1").withDescription("Task").build();
        Epic epicUpdate = Epic.builder()
                .withId(1).withName("Task11").withDescription("Task").build();
        String json1 = gson.toJson(epic);
        String json2 = gson.toJson(epicUpdate);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body1)
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uriUpdate)
                .POST(body2)
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response1.statusCode());
        assertEquals(201, response2.statusCode());
        assertEquals(Epic.builder().withId(1).withName("Task11").withDescription("Task").withStatus(TaskStatus.NEW).build(),
                server.taskManager.getEpicById(1));
    }

    @Test
    public void shouldCreateSubtask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        server.taskManager.createEpic(Epic.builder().withName("Task1").withDescription("Task").build());
        Subtask subtask = Subtask.builder().withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.DONE).withEpicId(1).build();
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body1)
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response1.statusCode());
        assertEquals(Subtask.builder().withId(2).withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.DONE).withEpicId(1).build(),
                server.taskManager.getSubtaskById(2));
        assertEquals(TaskStatus.DONE, server.taskManager.getEpicById(1).getStatus());
    }

    @Test
    public void shouldUpdateSubtask() throws IOException, InterruptedException {
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        URI uriUpdate = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        server.taskManager.createEpic(Epic.builder().withName("Task1").withDescription("Task").build());
        Subtask subtask = Subtask.builder().withName("Sub1").withDescription("Task")
                .withStatus(TaskStatus.DONE).withEpicId(1).build();
        Subtask subtaskUpdate = Subtask.builder().withId(2).withName("NotSub").withDescription("Task")
                .withStatus(TaskStatus.NEW).withEpicId(1).build();
        String json1 = gson.toJson(subtask);
        String json2 = gson.toJson(subtaskUpdate);
        final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
        final HttpRequest.BodyPublisher body2 = HttpRequest.BodyPublishers.ofString(json2);

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body1)
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uriUpdate)
                .POST(body2)
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response1.statusCode());
        assertEquals(201, response2.statusCode());
        assertEquals(Subtask.builder().withId(2).withName("NotSub").withDescription("Task")
                        .withStatus(TaskStatus.NEW).withEpicId(1).build(),
                server.taskManager.getSubtaskById(2));
        assertEquals(TaskStatus.NEW, server.taskManager.getEpicById(1).getStatus());
    }

    @Test
    public void shouldGetTaskById() throws IOException, InterruptedException {
        server.taskManager.createTask(Task.builder().withName("Task1").withDescription("Task").build());
        server.taskManager.createTask(Task.builder().withName("Task2").withDescription("Task").build());
        server.taskManager.createTask(Task.builder().withName("Task3").withDescription("Task").build());
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=2");
        URI uri2 = URI.create("http://localhost:8080/tasks/task/?id=4");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uri2)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        Task returned = gson.fromJson(response.body(), Task.class);
        assertEquals(returned, Task.builder().withId(2).withName("Task2").withDescription("Task").build());
        assertEquals(404, response2.statusCode());
    }

    @Test
    public void shouldGetEpicById() throws IOException, InterruptedException {
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic2").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic3").withDescription("Epic").build());
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=2");
        URI uri2 = URI.create("http://localhost:8080/tasks/epic/?id=4");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uri2)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        Epic returned = gson.fromJson(response.body(), Epic.class);
        assertEquals(Epic.builder().withId(2).withName("Epic2").withDescription("Epic").withStatus(TaskStatus.NEW).build(),
                returned);
        assertEquals(404, response2.statusCode());
    }

    @Test
    public void shouldGetSubtaskById() throws IOException, InterruptedException {
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(1).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.DONE).withEpicId(1).build());
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        URI uri2 = URI.create("http://localhost:8080/tasks/subtask/?id=4");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uri2)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        Subtask returned = gson.fromJson(response.body(), Subtask.class);
        assertEquals(Subtask.builder().withId(2).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.NEW).withEpicId(1).build(),
                returned);
        assertEquals(404, response2.statusCode());
    }

    @Test
    public void shouldGetSubtasksById() throws IOException, InterruptedException {
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(1).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.DONE).withEpicId(1).build());
        URI uri = URI.create("http://localhost:8080/tasks/subtasks/?id=1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String subtasks = response.body();
        String expected = gson.toJson(List.of(Subtask.builder().withId(2).withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(1).build(),
                Subtask.builder().withId(3).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.DONE).withEpicId(1).build()));
        assertEquals(expected, subtasks);
    }

    @Test
    public void shouldGetAllTasks() throws IOException, InterruptedException {
        server.taskManager.createTask(Task.builder().withName("Task1").withDescription("Task").build());
        server.taskManager.createTask(Task.builder().withName("Task2").withDescription("Task").build());
        server.taskManager.createTask(Task.builder().withName("Task3").withDescription("Task").build());
        URI uri = URI.create("http://localhost:8080/tasks/task/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String returned = response.body();
        String expected = gson.toJson(List.of(
                Task.builder().withId(1).withName("Task1").withDescription("Task").build(),
                Task.builder().withId(2).withName("Task2").withDescription("Task").build(),
                Task.builder().withId(3).withName("Task3").withDescription("Task").build()
        ));
        assertEquals(expected, returned);
    }

    @Test
    public void shouldGetAllEpics() throws IOException, InterruptedException {
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic2").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic3").withDescription("Epic").build());
        URI uri = URI.create("http://localhost:8080/tasks/epic/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String returned = response.body();
        String expected = gson.toJson(List.of(
                Epic.builder().withId(1).withName("Epic1").withDescription("Epic").withStatus(TaskStatus.NEW).build(),
                Epic.builder().withId(2).withName("Epic2").withDescription("Epic").withStatus(TaskStatus.NEW).build(),
                Epic.builder().withId(3).withName("Epic3").withDescription("Epic").withStatus(TaskStatus.NEW).build()
        ));
        assertEquals(expected, returned);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldGetAllSubtasks() throws IOException, InterruptedException {
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(1).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.DONE).withEpicId(1).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(2).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.DONE).withEpicId(2).build());
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String returned = response.body();
        String expected = gson.toJson(List.of(
                Subtask.builder().withId(3).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.NEW).withEpicId(1).build(),
                Subtask.builder().withId(4).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.DONE).withEpicId(1).build(),
                Subtask.builder().withId(5).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.NEW).withEpicId(2).build(),
                Subtask.builder().withId(6).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.DONE).withEpicId(2).build()
        ));
        assertEquals(expected, returned);
        assertEquals(200, response.statusCode());
    }

    @Test
    public void shouldDeleteAllTasks() throws IOException, InterruptedException {
        server.taskManager.createTask(Task.builder().withName("Task1").withDescription("Task").build());
        server.taskManager.createTask(Task.builder().withName("Task2").withDescription("Task").build());
        server.taskManager.createTask(Task.builder().withName("Task3").withDescription("Task").build());
        URI uri = URI.create("http://localhost:8080/tasks/task/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> returned = server.taskManager.getAllTasks();
        List<Task> expected = Collections.emptyList();

        assertEquals(200, response.statusCode());
        assertEquals(expected, returned);
    }

    @Test
    public void shouldDeleteAllEpics() throws IOException, InterruptedException {
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic2").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic3").withDescription("Epic").build());
        URI uri = URI.create("http://localhost:8080/tasks/epic/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Epic> returned = server.taskManager.getAllEpics();
        List<Epic> expected = Collections.emptyList();
        List<Subtask> subtasksReturned = server.taskManager.getAllSubtasks();
        List<Subtask> subtasksExpected = Collections.emptyList();

        assertEquals(200, response.statusCode());
        assertEquals(expected, returned);
        assertEquals(subtasksExpected, subtasksReturned);
    }

    @Test
    public void shouldDeleteAllSubtasks() throws IOException, InterruptedException {
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(1).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.DONE).withEpicId(1).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(2).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.DONE).withEpicId(2).build());
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Subtask> returned = server.taskManager.getAllSubtasks();
        List<Subtask> expected = Collections.emptyList();
        TaskStatus epic1StatusExpected = server.taskManager.getEpicById(1).getStatus();
        TaskStatus epic2StatusExpected = server.taskManager.getEpicById(2).getStatus();

        assertEquals(200, response.statusCode());
        assertEquals(expected, returned);
        assertEquals(TaskStatus.NEW, epic1StatusExpected);
        assertEquals(TaskStatus.NEW, epic2StatusExpected);
    }

    @Test
    public void shouldDeleteTaskById() throws IOException, InterruptedException {
        server.taskManager.createTask(Task.builder().withName("Task1").withDescription("Task").build());
        server.taskManager.createTask(Task.builder().withName("Task2").withDescription("Task").build());
        server.taskManager.createTask(Task.builder().withName("Task3").withDescription("Task").build());
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=2");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> returned = server.taskManager.getAllTasks();
        List<Task> expected = List.of(
                Task.builder().withId(1).withName("Task1").withDescription("Task").build(),
                Task.builder().withId(3).withName("Task3").withDescription("Task").build()
        );

        assertEquals(200, response.statusCode());
        assertEquals(expected, returned);
    }

    @Test
    public void shouldDeleteEpicById() throws IOException, InterruptedException {
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic2").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic3").withDescription("Epic").build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(1).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.DONE).withEpicId(1).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(2).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.DONE).withEpicId(2).build());
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=2");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Epic> returned = server.taskManager.getAllEpics();
        List<Epic> expected = List.of(
                Epic.builder().withId(1).withName("Epic1").withDescription("Epic").withStatus(TaskStatus.IN_PROGRESS)
                        .withSubtaskIds(List.of(4, 5)).build(),
                Epic.builder().withId(3).withName("Epic3").withDescription("Epic").withStatus(TaskStatus.NEW).build()
        );
        List<Subtask> subtasksReturned = server.taskManager.getAllSubtasks();
        List<Subtask> subtasksExpected = List.of(
                Subtask.builder().withId(4).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.NEW).withEpicId(1).build(),
                Subtask.builder().withId(5).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.DONE).withEpicId(1).build()
        );

        assertEquals(200, response.statusCode());
        assertEquals(expected, returned);
        assertEquals(subtasksExpected, subtasksReturned);
    }

    @Test
    public void shouldDeleteSubtaskById() throws IOException, InterruptedException {
        server.taskManager.createEpic(Epic.builder().withName("Epic1").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic2").withDescription("Epic").build());
        server.taskManager.createEpic(Epic.builder().withName("Epic3").withDescription("Epic").build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(1).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.DONE).withEpicId(1).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.NEW).withEpicId(2).build());
        server.taskManager.createSubtask(Subtask.builder().withName("Epic2").withDescription("Epic")
                .withStatus(TaskStatus.DONE).withEpicId(2).build());
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=4");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Epic> returned = server.taskManager.getAllEpics();
        List<Epic> expected = List.of(
                Epic.builder().withId(1).withName("Epic1").withDescription("Epic").withStatus(TaskStatus.DONE)
                        .withSubtaskIds(List.of(5)).build(),
                Epic.builder().withId(2).withName("Epic2").withDescription("Epic").withStatus(TaskStatus.IN_PROGRESS)
                        .withSubtaskIds(List.of(6, 7)).build(),
                Epic.builder().withId(3).withName("Epic3").withDescription("Epic").withStatus(TaskStatus.NEW).build()
        );
        List<Subtask> subtasksReturned = server.taskManager.getAllSubtasks();
        List<Subtask> subtasksExpected = List.of(
                Subtask.builder().withId(5).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.DONE).withEpicId(1).build(),
                Subtask.builder().withId(6).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.NEW).withEpicId(2).build(),
                Subtask.builder().withId(7).withName("Epic2").withDescription("Epic")
                        .withStatus(TaskStatus.DONE).withEpicId(2).build()
        );

        assertEquals(200, response.statusCode());
        assertEquals(expected, returned);
        assertEquals(subtasksExpected, subtasksReturned);
    }
}