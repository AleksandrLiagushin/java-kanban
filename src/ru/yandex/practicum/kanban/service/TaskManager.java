package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.data.model.Epic;
import ru.yandex.practicum.kanban.data.model.Subtask;
import ru.yandex.practicum.kanban.data.model.Task;

import java.util.List;

public interface TaskManager {

    void createTask(Task task);
    void createEpic(Epic epic);
    void createSubtask(Subtask sub);

    Task findTaskById(int taskId);
    Epic findEpicById(int epicId);
    Subtask findSubtaskById(int subId);

    List<Task> getAllTasks();
    List<Subtask> getAllSubtasks();
    List<Epic> getAllEpics();

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask sub);

    void deleteTaskById(Integer taskId);
    void deleteEpicById(Integer epicId);
    void deleteSubtaskById(Integer subtaskId);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubtasks();

    List<Subtask> getSubtasksByEpicId(int epicId);

    List<Task> getHistory();
}