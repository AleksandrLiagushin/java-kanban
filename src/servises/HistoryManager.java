package servises;

import data.types.Task;

import java.util.ArrayList;

    public interface HistoryManager {

        void add(Task task);

        ArrayList<Task> getHistory();
}
