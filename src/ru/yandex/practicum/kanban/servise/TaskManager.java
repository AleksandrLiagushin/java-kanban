package ru.yandex.practicum.kanban.servise;

import ru.yandex.practicum.kanban.data.model.EpicTask;
import ru.yandex.practicum.kanban.data.model.Subtask;
import ru.yandex.practicum.kanban.data.model.Task;

import java.util.List;

public interface TaskManager {

    void createNewTask(Task task);
    void createNewEpic(EpicTask epic);
    void createSubTask(Subtask sub);

    Task findTaskByID(int taskId);
    EpicTask findEpicByID(int epicId);
    Subtask findSubTaskByID(int subId);

    List<Task> getAllTasks();
    List<Subtask> getAllSubTasks();
    List<EpicTask> getAllEpicTasks();

    void refreshTask(Task task);
    void refreshEpicTask(EpicTask epic);
    void refreshSubTask(Subtask sub);

    void deleteTaskByID(int taskId);
    void deleteEpicTaskByID(int epicId);
    void deleteSubTaskByID(int subId);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubs();

    List<Subtask> getSubTasksListOfEpicByID(int epicId);

    List<Task> getHistory();

    int generateTaskID();
}