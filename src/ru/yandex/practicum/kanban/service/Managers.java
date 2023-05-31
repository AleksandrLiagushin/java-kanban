package ru.yandex.practicum.kanban.service;

public class Managers {

    private Managers(){}

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }
}
