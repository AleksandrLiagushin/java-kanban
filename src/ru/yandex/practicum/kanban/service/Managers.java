package ru.yandex.practicum.kanban.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.practicum.kanban.adapter.LocalDateTimeAdapter;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class Managers {

    private Managers(){}

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getBackedTaskManager(Path path) {
        return FileBackedTaskManager.loadFromFile(path);
    }

    public static UserManager getInMemoryUserManager() {
        return new InMemoryUserManager();
    }

    //todo переопределить методы для LocalDateTimeAdapter
    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe());
        return gsonBuilder.create();
    }
}
