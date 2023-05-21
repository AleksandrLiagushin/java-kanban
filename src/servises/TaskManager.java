package servises;

import data.storage.EpicTasksList;
import data.storage.SubTasksList;
import data.storage.TasksList;
import data.types.EpicTask;
import data.types.SubTask;
import data.types.Task;
import data.types.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private int taskID;
    private TasksList tasksList = new TasksList();
    private SubTasksList subTasksList = new SubTasksList();
    private EpicTasksList epicTasksList = new EpicTasksList();


    public void createNewTask(Task task) {
        setTaskID();
        task.setCreationDate();
        tasksList.addNewTaskToList(getTaskID(), task);
    }

    public void createNewEpic(EpicTask epic) {
        setTaskID();
        epic.setCreationDate();
        epicTasksList.addEpicTaskToList(getTaskID(), epic);
    }
    public void createSubTask(SubTask sub) {
        setTaskID();
        sub.setCreationDate();
        if (epicTasksList.getTaskByID(sub.getOwnedByEpic()) != null) {
            EpicTask epic = findEpicByID(sub.getOwnedByEpic());
            epic.addSubIDToSubTasksList(getTaskID());
            epicTasksList.addEpicTaskToList(sub.getOwnedByEpic(), epic);
            subTasksList.addSubTaskToList(getTaskID(), sub);
            changeEpicStatus(sub.getOwnedByEpic());
        } else {
            System.out.println("Такого эпика нет. Подзадача переведена в задачи");
            tasksList.addNewTaskToList(getTaskID(), sub);
        }
    }

    public Task findTaskByID(int taskID) {
        if (tasksList.getTaskByID(taskID) != null) {
            return (tasksList.getTaskByID(taskID));
        }
        return null;
    }

    public EpicTask findEpicByID(int epicID) {
        if (epicTasksList.getTaskByID(epicID) != null) {
            return (epicTasksList.getTaskByID(epicID));
        }
        return null;
    }

    public SubTask findSubTaskByID(int subID) {
        if (subTasksList.getTaskByID(subID) != null) {
            return (subTasksList.getTaskByID(subID));
        }
        return null;
    }

    public HashMap<Integer, Task> getAllTasks() {
        return tasksList.getTasksList();
    }

    public HashMap<Integer, SubTask> getAllSubTasks() {
        return subTasksList.getSubTasksList();
    }

    public HashMap<Integer, EpicTask> getAllEpicTasks() {
        return epicTasksList.getEpicTasksList();
    }

    public void refreshTask(Task task, int taskID) {
        Task refTask = findTaskByID(taskID);
        refTask.setTaskName(task.getTaskName());
        refTask.setDescription(task.getDescription());
        refTask.setStatus(task.getStatus());
        tasksList.addNewTaskToList(taskID, refTask);
    }

    public void refreshEpicTask(EpicTask epic, int epicID) {
        EpicTask refEpic = findEpicByID(epicID);
        refEpic.setTaskName(epic.getTaskName());
        refEpic.setDescription(epic.getDescription());
        epicTasksList.addEpicTaskToList(epicID, refEpic);
    }

    public void refreshSubTask(SubTask sub, int subID) {
        SubTask refSub = findSubTaskByID(subID);
        refSub.setTaskName(sub.getTaskName());
        refSub.setDescription(sub.getDescription());
        refSub.setStatus(sub.getStatus());
        refSub.setOwnedByEpic(sub.getOwnedByEpic());
        subTasksList.addSubTaskToList(subID, refSub);
        changeEpicStatus(sub.getOwnedByEpic());
    }

    public void deleteTaskByID(int taskID) {
        tasksList.deleteTaskByID(taskID);
    }

    public void deleteEpicTaskByID(int epicID) {
        for (Integer id : findEpicByID(epicID).getSubTasksIDsList()) {
            deleteSubTaskByID(id);
        }
        epicTasksList.deleteTaskByID(epicID);
    }

    public void deleteSubTaskByID(int subID) {
        int epicID = findSubTaskByID(subID).getOwnedByEpic();
        findEpicByID(findSubTaskByID(subID).getOwnedByEpic()).getSubTasksIDsList().remove(subID);
        changeEpicStatus(epicID);
        subTasksList.deleteTaskByID(subID);
    }

    public void deleteAllTasks() {
        tasksList.deleteAllTasks();
    }

    public void deleteAllEpics() {
        epicTasksList.deleteAllTasks();
        for (Integer id : subTasksList.getSubTasksList().keySet()) {
            findSubTaskByID(id).setOwnedByEpic(0);
        }
    }

    public void deleteAllSubs() {
        subTasksList.deleteAllTasks();
        for (Integer id : getAllEpicTasks().keySet()) {
            findEpicByID(id).getSubTasksIDsList().clear();
            findEpicByID(id).setStatus(TaskStatus.NEW);
        }
    }

    public HashMap<Integer, SubTask> getSubTasksListOfEpicByID(int epicID) {
        HashMap<Integer, SubTask> subsList = new HashMap<>();
        for (Integer subID : findEpicByID(epicID).getSubTasksIDsList()) {
            subsList.put(subID, findSubTaskByID(subID));
        }
        return subsList;
    }

    public void changeEpicStatus(int epicID) {
        ArrayList<Integer> tasksByEpic = epicTasksList.getTaskByID(epicID).getSubTasksIDsList();
        EpicTask epic = epicTasksList.getTaskByID(epicID);
        TaskStatus status = TaskStatus.NEW;
        if (!tasksByEpic.isEmpty()) {
            status = subTasksList.getTaskByID(tasksByEpic.get(0)).getStatus();
            for (Integer integer : tasksByEpic) {
                if (!status.equals(subTasksList.getTaskByID(integer).getStatus())) {
                    status = TaskStatus.IN_PROGRESS;
                    break;
                }
            }
        }
        epic.setStatus(status);
        epicTasksList.addEpicTaskToList(epicID, epic);
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID() {
        this.taskID += 1;
    }
}