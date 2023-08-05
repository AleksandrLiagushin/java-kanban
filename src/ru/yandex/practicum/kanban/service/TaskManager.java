package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;

import java.util.List;

public interface TaskManager {

    boolean createTask(Task task);
    boolean createEpic(Epic epic);
    boolean createSubtask(Subtask sub);

    Task getTaskById(int taskId);
    Epic getEpicById(int epicId);
    Subtask getSubtaskById(int subId);

    List<Task> getAllTasks();
    List<Subtask> getAllSubtasks();
    List<Epic> getAllEpics();
    List<Task> getTasksPrioritizedByTime();

    boolean updateTask(Task task);
    boolean updateEpic(Epic epic);
    boolean updateSubtask(Subtask sub);

    void deleteTaskById(Integer taskId);
    void deleteEpicById(Integer epicId);
    void deleteSubtaskById(Integer subtaskId);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int epicId);

    List<Task> getHistory();
}