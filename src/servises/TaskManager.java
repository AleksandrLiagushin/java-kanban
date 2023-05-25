package servises;

import data.types.EpicTask;
import data.types.SubTask;
import data.types.Task;

import java.util.ArrayList;

public interface TaskManager {

    public void createNewTask(Task task);
    public void createNewEpic(EpicTask epic);
    public void createSubTask(SubTask sub);

    public Task findTaskByID(int taskID);
    public EpicTask findEpicByID(int epicID);
    public SubTask findSubTaskByID(int subID);

    public ArrayList<Task> getAllTasks();
    public ArrayList<SubTask> getAllSubTasks();
    public ArrayList<EpicTask> getAllEpicTasks();

    public void refreshTask(Task task);
    public void refreshEpicTask(EpicTask epic);
    public void refreshSubTask(SubTask sub);

    public void deleteTaskByID(int taskID);
    public void deleteEpicTaskByID(int epicID);
    public void deleteSubTaskByID(int subID);

    public void deleteAllTasks();
    public void deleteAllEpics();
    public void deleteAllSubs();

    public ArrayList<SubTask> getSubTasksListOfEpicByID(int epicID);

    public void changeEpicStatus(int epicID);

    public void generateTaskID();
}