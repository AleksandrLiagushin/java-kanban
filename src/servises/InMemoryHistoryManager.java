package servises;

import data.types.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class InMemoryHistoryManager implements HistoryManager {
    public Queue<Task> historian = new LinkedList<>();

    @Override
    public void add(Task task) {
        historian.add(task);
        if (historian.size() > 10) {
            historian.remove();
        }
    }
    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(historian);
    }
}
