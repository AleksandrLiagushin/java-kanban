package ru.yandex.practicum.kanban.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.kanban.adapter.GsonAdapters;

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

    public static HttpTaskManager getDefaultHttpManager() {
            HttpTaskManager httpTaskManager = new HttpTaskManager("http://localhost:8078");
            httpTaskManager.load();
            return httpTaskManager;
    }

    public static FileBackedTaskManager getBackedTaskManager(Path path) {
        return FileBackedTaskManager.loadFromFile(path);
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, GsonAdapters.localDateTime().nullSafe())
                .registerTypeAdapter(Duration.class, GsonAdapters.duration().nullSafe());
        return gsonBuilder.create();
    }
}
