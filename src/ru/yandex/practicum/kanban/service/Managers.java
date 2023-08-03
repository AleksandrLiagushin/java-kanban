package ru.yandex.practicum.kanban.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.kanban.adapter.GsonAdapters;
import ru.yandex.practicum.kanban.adapter.LocalDateTimeAdapter;
import ru.yandex.practicum.kanban.exception.KVResponseException;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    private Managers(){}

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultHttpManager() {
        try {
            return HttpTaskManager.load("localhost:8078");
        } catch (IOException | InterruptedException e) {
            throw new KVResponseException("Problem with TaskManager creation", e.getCause());
        }
    }

    public static FileBackedTaskManager getBackedTaskManager(Path path) {
        return FileBackedTaskManager.loadFromFile(path);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, GsonAdapters.localDateTime().nullSafe())
                .registerTypeAdapter(Duration.class, GsonAdapters.duration().nullSafe());
        return gsonBuilder.create();
    }
}
