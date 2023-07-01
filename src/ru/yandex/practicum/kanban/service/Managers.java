package ru.yandex.practicum.kanban.service;

import java.nio.file.Path;

public class Managers {

    private Managers(){}

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getBackedTaskManager(Path path) {
        return new FileBackedTaskManager(path);
    }
}
