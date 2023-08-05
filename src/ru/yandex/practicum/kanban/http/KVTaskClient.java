package ru.yandex.practicum.kanban.http;

import ru.yandex.practicum.kanban.exception.KVResponseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String apiToken;
    private final String url;
    private HttpClient client;

    public KVTaskClient(String url) {
        this.url = url;
    }

    public void register() {
        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url + "/register")).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new KVResponseException("Client has not been registered, status= " + response.statusCode());
            }
            apiToken = response.body();
        } catch (IOException | InterruptedException e) {
            throw new KVResponseException("Client has not load request, something went wrong.", e.getCause());
        }
    }

    public String load(String key) {
        client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken))
                .GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new KVResponseException("Client has not load request, status= " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new KVResponseException("Client has not load request, something went wrong.", e.getCause());
        }
    }

    public void put(String key, String data) throws IOException, InterruptedException {
        client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken))
                .POST(HttpRequest.BodyPublishers.ofString(data)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new KVResponseException("Client has not save request, status= " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new KVResponseException("Client has not load request, something went wrong.", e.getCause());
        }
    }
}
