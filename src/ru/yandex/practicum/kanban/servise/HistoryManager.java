package ru.yandex.practicum.kanban.servise;

import ru.yandex.practicum.kanban.data.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
