package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.data.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int HISTORY_SIZE = 10;
    private final LinkedList<Task> history = new LinkedList<>(); //вопрос: почему идея предлагает сделать переменную
    //final, если нет - выдает соответсвующий варнинг?
    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > HISTORY_SIZE) {
            history.remove();
        }
    }
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
