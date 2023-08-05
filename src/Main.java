import com.google.gson.Gson;
import ru.yandex.practicum.kanban.http.HttpTaskServer;
import ru.yandex.practicum.kanban.http.KVServer;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.service.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer server = new HttpTaskServer();
        server.start();
        Gson gson = Managers.getGson();

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        Task task = Task.builder().withName("Task").withDescription("Task").build();
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Код ответа " + response.statusCode());
    }
}
