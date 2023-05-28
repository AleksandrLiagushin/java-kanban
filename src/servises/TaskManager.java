package servises;

import data.types.EpicTask;
import data.types.SubTask;
import data.types.Task;

import java.util.ArrayList;

public interface TaskManager {

    void createNewTask(Task task);
    void createNewEpic(EpicTask epic);
    void createSubTask(SubTask sub);

    Task findTaskByID(int taskID);
    EpicTask findEpicByID(int epicID);
    SubTask findSubTaskByID(int subID);

    ArrayList<Task> getAllTasks();
    ArrayList<SubTask> getAllSubTasks();
    ArrayList<EpicTask> getAllEpicTasks();

    void refreshTask(Task task);
    void refreshEpicTask(EpicTask epic);
    void refreshSubTask(SubTask sub);

    void deleteTaskByID(int taskID);
    void deleteEpicTaskByID(int epicID);
    void deleteSubTaskByID(int subID);

    void deleteAllTasks();
    void deleteAllEpics();
    void deleteAllSubs();

    ArrayList<SubTask> getSubTasksListOfEpicByID(int epicID);

    void changeEpicStatus(int epicID);

    ArrayList<Task> getHistory();
}