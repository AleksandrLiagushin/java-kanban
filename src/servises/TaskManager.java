package servises;

import data.storage.EpicTasksList;
import data.storage.SubTasksList;
import data.storage.TasksList;
import data.types.EpicTask;
import data.types.SubTask;
import data.types.Task;
import data.types.TaskStatus;

import java.util.ArrayList;

public class TaskManager {

    private int taskID;
    private TasksList tasksList = new TasksList();
    private SubTasksList subTasksList = new SubTasksList();
    private EpicTasksList epicTasksList = new EpicTasksList();


    public void createNewTask(Task task) {
        generateTaskID();
        task.setId(taskID);
        tasksList.addNewTaskToList(taskID, task);
    }

    public void createNewEpic(EpicTask epic) {
        generateTaskID();
        epic.setId(taskID);
        epic.setStatus(TaskStatus.NEW);
        epicTasksList.addEpicTaskToList(taskID, epic);
    }
    public void createSubTask(SubTask sub) {
        generateTaskID();
        sub.setId(taskID);
        if (epicTasksList.getTaskByID(sub.getOwnedByEpic()) != null) {
            EpicTask epic = findEpicByID(sub.getOwnedByEpic());
            epic.addSubIDToSubTasksList(taskID);
            epicTasksList.addEpicTaskToList(sub.getOwnedByEpic(), epic);
            subTasksList.addSubTaskToList(taskID, sub);
            changeEpicStatus(sub.getOwnedByEpic());
        }
    }

    public Task findTaskByID(int taskID) {
        if (tasksList.getTaskByID(taskID) != null) {
            return tasksList.getTaskByID(taskID);
        }
        return null;
    }

    public EpicTask findEpicByID(int epicID) {
        if (epicTasksList.getTaskByID(epicID) != null) {
            return epicTasksList.getTaskByID(epicID);
        }
        return null;
    }

    public SubTask findSubTaskByID(int subID) {
        if (subTasksList.getTaskByID(subID) != null) {
            return subTasksList.getTaskByID(subID);
        }
        return null;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksList.getTasksList().values());
    }

    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasksList.getSubTasksList().values());
    }

    public ArrayList<EpicTask> getAllEpicTasks() {
        return new ArrayList<>(epicTasksList.getEpicTasksList().values());
    }

    public void refreshTask(Task task) {
        if (findTaskByID(task.getId()) == null) {
            return;
        }
        tasksList.addNewTaskToList(task.getId(), task);
    }

    public void refreshEpicTask(EpicTask epic) {
        if (findEpicByID(epic.getId()) == null) {
            return;
        }
        epicTasksList.addEpicTaskToList(epic.getId(), epic);
    }

    public void refreshSubTask(SubTask sub) {
        SubTask refSub = findSubTaskByID(sub.getId());
        EpicTask epic = findEpicByID(refSub.getOwnedByEpic());
        if (refSub.getOwnedByEpic() != sub.getOwnedByEpic()) {
            epic.removeSubIDFromIDsList(sub.getId());
            changeEpicStatus(refSub.getOwnedByEpic());
            epic = findEpicByID(sub.getOwnedByEpic());
            epic.addSubIDToSubTasksList(sub.getId());
        }
        refSub.setTaskName(sub.getTaskName());
        refSub.setDescription(sub.getDescription());
        refSub.setStatus(sub.getStatus());
        refSub.setOwnedByEpic(sub.getOwnedByEpic());
        subTasksList.addSubTaskToList(sub.getId(), refSub);
        changeEpicStatus(sub.getOwnedByEpic());
    }

    public void deleteTaskByID(int taskID) {
        tasksList.deleteTaskByID(taskID);
    }

    public void deleteEpicTaskByID(int epicID) {
        for (Integer id : findEpicByID(epicID).getSubTasksIDsList()) {
            subTasksList.deleteTaskByID(id);
        }
        epicTasksList.deleteTaskByID(epicID);
    }

    public void deleteSubTaskByID(int subID) {
        int epicID = findSubTaskByID(subID).getOwnedByEpic();
        findEpicByID(epicID).removeSubIDFromIDsList(subID);
        changeEpicStatus(epicID);
        subTasksList.deleteTaskByID(subID);
    }

    public void deleteAllTasks() {
        tasksList.deleteAllTasks();
    }

    public void deleteAllEpics() {
        epicTasksList.deleteAllTasks();
        subTasksList.deleteAllTasks();
    }

    public void deleteAllSubs() {
        subTasksList.deleteAllTasks();
        for (Integer id : epicTasksList.getEpicTasksList().keySet()) {
            findEpicByID(id).getSubTasksIDsList().clear();
            findEpicByID(id).setStatus(TaskStatus.NEW);
        }
    }

    public ArrayList<SubTask> getSubTasksListOfEpicByID(int epicID) {
        ArrayList<SubTask> subsList = new ArrayList<>();
        for (Integer subID : findEpicByID(epicID).getSubTasksIDsList()) {
            subsList.add(findSubTaskByID(subID));
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

    public void generateTaskID() {
        this.taskID += 1;
    }
}