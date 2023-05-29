package ru.yandex.practicum.kanban.servise;

import ru.yandex.practicum.kanban.data.model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public Queue<Task> history = new LinkedList<>();
    /*
        * historian - общепринятое название в промышленной автоматике для хранилищ исторических данных -->
        * переименован в history
        * Queue - полностью отвечает концепции FIFO, что и прописано в ТЗ
        * генерация исключения NullPointerException исключается работой метода add, т.к. размер очереди не может
        * быть менее 1 --> метод .size() выполняется только после добавления элемента в очередь
     */



    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > 10) {
            history.remove();
        }
    }
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
