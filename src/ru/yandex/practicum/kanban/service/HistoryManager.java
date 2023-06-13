package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.data.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}
