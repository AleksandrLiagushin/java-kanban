package servises;

import data.storage.TasksMap;
import data.types.EpicTask;
import data.types.SubTask;
import data.types.Task;
import data.types.TaskStatus;

import java.util.ArrayList;

public class InMemoryTaskManager implements TaskManager {

    private int taskID;
    private final TasksMap<Task> tasksMap = new TasksMap<>();
    private final TasksMap<SubTask> subTasksMap = new TasksMap<>();
    private final TasksMap<EpicTask> epicTasksMap = new TasksMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Override
    public void createNewTask(Task task) {
        generateTaskID();
        task.setId(taskID);
        tasksMap.addTaskToMap(taskID, task);
    }

    @Override
    public void createNewEpic(EpicTask epic) {
        generateTaskID();
        epic.setId(taskID);
        epic.setStatus(TaskStatus.NEW);
        epicTasksMap.addTaskToMap(taskID, epic);
    }

    @Override
    public void createSubTask(SubTask sub) {
        generateTaskID();
        sub.setId(taskID);
        if (epicTasksMap.getTaskByID(sub.getOwnedByEpic()) != null) {
            EpicTask epic = epicTasksMap.getTaskByID(sub.getOwnedByEpic());
            epic.addSubIDToSubTasksList(taskID);
            epicTasksMap.addTaskToMap(sub.getOwnedByEpic(), epic);
            subTasksMap.addTaskToMap(taskID, sub);
            changeEpicStatus(sub.getOwnedByEpic());
        }
    }

    @Override
    public Task findTaskByID(int taskID) {
        if (tasksMap.getTaskByID(taskID) != null) {
            historyManager.add(tasksMap.getTaskByID(taskID));
            return tasksMap.getTaskByID(taskID);
        }
        return null;
    }

    @Override
    public EpicTask findEpicByID(int epicID) {
        if (epicTasksMap.getTaskByID(epicID) != null) {
            historyManager.add(epicTasksMap.getTaskByID(epicID));
            return epicTasksMap.getTaskByID(epicID);
        }
        return null;
    }

    @Override
    public SubTask findSubTaskByID(int subID) {
        if (subTasksMap.getTaskByID(subID) != null) {
            historyManager.add(subTasksMap.getTaskByID(subID));
            return subTasksMap.getTaskByID(subID);
        }
        return null;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.getTasksMap().values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasksMap.getTasksMap().values());
    }

    @Override
    public ArrayList<EpicTask> getAllEpicTasks() {
        return new ArrayList<>(epicTasksMap.getTasksMap().values());
    }

    @Override
    public void refreshTask(Task task) {
        if (findTaskByID(task.getId()) == null) {
            return;
        }
        tasksMap.addTaskToMap(task.getId(), task);
    }

    @Override
    public void refreshEpicTask(EpicTask epic) {
        if (findEpicByID(epic.getId()) == null) {
            return;
        }
        epicTasksMap.addTaskToMap(epic.getId(), epic);
    }

    @Override
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
        subTasksMap.addTaskToMap(sub.getId(), refSub);
        changeEpicStatus(sub.getOwnedByEpic());
    }

    @Override
    public void deleteTaskByID(int taskID) {
        tasksMap.deleteTaskByID(taskID);
    }

    @Override
    public void deleteEpicTaskByID(int epicID) {
        for (Integer id : findEpicByID(epicID).getSubTasksIDsList()) {
            subTasksMap.deleteTaskByID(id);
        }
        epicTasksMap.deleteTaskByID(epicID);
    }

    @Override
    public void deleteSubTaskByID(int subID) {
        int epicID = findSubTaskByID(subID).getOwnedByEpic();
        findEpicByID(epicID).removeSubIDFromIDsList(subID);
        changeEpicStatus(epicID);
        subTasksMap.deleteTaskByID(subID);
    }

    @Override
    public void deleteAllTasks() {
        tasksMap.deleteAllTasks();
    }

    @Override
    public void deleteAllEpics() {
        epicTasksMap.deleteAllTasks();
        subTasksMap.deleteAllTasks();
    }

    @Override
    public void deleteAllSubs() {
        subTasksMap.deleteAllTasks();
        for (Integer id : epicTasksMap.getTasksMap().keySet()) {
            findEpicByID(id).getSubTasksIDsList().clear();
            findEpicByID(id).setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public ArrayList<SubTask> getSubTasksListOfEpicByID(int epicID) {
        ArrayList<SubTask> subsList = new ArrayList<>();
        for (Integer subID : findEpicByID(epicID).getSubTasksIDsList()) {
            subsList.add(findSubTaskByID(subID));
        }
        return subsList;
    }

    @Override
    public void changeEpicStatus(int epicID) {
        ArrayList<Integer> tasksByEpic = epicTasksMap.getTaskByID(epicID).getSubTasksIDsList();
        EpicTask epic = epicTasksMap.getTaskByID(epicID);
        TaskStatus status = TaskStatus.NEW;
        if (!tasksByEpic.isEmpty()) {
            status = subTasksMap.getTaskByID(tasksByEpic.get(0)).getStatus();
            for (Integer integer : tasksByEpic) {
                if (!status.equals(subTasksMap.getTaskByID(integer).getStatus())) {
                    status = TaskStatus.IN_PROGRESS;
                    break;
                }
            }
        }
        epic.setStatus(status);
        epicTasksMap.addTaskToMap(epicID, epic);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void generateTaskID() {
        this.taskID += 1;
    }
}