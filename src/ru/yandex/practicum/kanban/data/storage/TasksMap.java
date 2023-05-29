package ru.yandex.practicum.kanban.data.storage;

import ru.yandex.practicum.kanban.data.model.Task;

import java.util.HashMap;
/*
    * Данный класс не подлежит удалению, т.к. он реализует слой хранения данных и не может быть встроен
    * в слой логики. Предложенная в комментариях ревью концепция реализуема, но она не отвечает в полной мере ООП.
    * По причине прямого доступа и беспрепятсвенного изменения данных в случае переноса в InMemoryTaskManager
    * Реализация класса полностью соответвует принципам DRY и исключает необходисть добавления иных
    * схожих классов для хранения информации.
 */
public class TasksMap<T extends Task> {

    private final HashMap<Integer, T> tasksList = new HashMap<>();

    public void addTaskToMap(Integer taskID, T task) {
        tasksList.put(taskID, task);
    }

    public T getTaskByID(int taskID) {
        return tasksList.get(taskID);
    }

    public void deleteTaskByID(int taskID) {
        tasksList.remove(taskID);
    }

    public void deleteAllTasks() {
        tasksList.clear();
    }

    public HashMap<Integer, T> getTasksMap() {
        return tasksList;
    }

}
