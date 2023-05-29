package ru.yandex.practicum.kanban.servise;

import ru.yandex.practicum.kanban.data.storage.TasksMap;
import ru.yandex.practicum.kanban.data.model.EpicTask;
import ru.yandex.practicum.kanban.data.model.Subtask;
import ru.yandex.practicum.kanban.data.model.Task;
import ru.yandex.practicum.kanban.data.model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int taskId;
    private final TasksMap<Task> tasksMap = new TasksMap<>();
    private final TasksMap<Subtask> subtasksMap = new TasksMap<>();
    private final TasksMap<EpicTask> epicTasksMap = new TasksMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Override
    public void createNewTask(Task task) {
        tasksMap.addTaskToMap(taskId, task);
    }

    @Override
    public void createNewEpic(EpicTask epic) {
        epic.setStatus(TaskStatus.NEW);
        epicTasksMap.addTaskToMap(taskId, epic);
    }

    @Override
    public void createSubTask(Subtask sub) {
        if (epicTasksMap.getTaskByID(sub.getEpicId()) != null) {
            EpicTask epic = epicTasksMap.getTaskByID(sub.getEpicId());
            epic.addSubIDToSubTasksList(taskId);
            epicTasksMap.addTaskToMap(sub.getEpicId(), epic);
            subtasksMap.addTaskToMap(taskId, sub);
            changeEpicStatus(sub.getEpicId());
        }
    }

    @Override
    public Task findTaskByID(int taskId) {
        if (tasksMap.getTaskByID(taskId) != null) {
            historyManager.add(tasksMap.getTaskByID(taskId));
            return tasksMap.getTaskByID(taskId);
        }
        return null;
    }

    @Override
    public EpicTask findEpicByID(int epicId) {
        if (epicTasksMap.getTaskByID(epicId) != null) {
            historyManager.add(epicTasksMap.getTaskByID(epicId));
            return epicTasksMap.getTaskByID(epicId);
        }
        return null;
    }

    @Override
    public Subtask findSubTaskByID(int subId) {
        if (subtasksMap.getTaskByID(subId) != null) {
            historyManager.add(subtasksMap.getTaskByID(subId));
            return subtasksMap.getTaskByID(subId);
        }
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.getTasksMap().values());
    }

    @Override
    public List<Subtask> getAllSubTasks() {
        return new ArrayList<>(subtasksMap.getTasksMap().values());
    }

    @Override
    public List<EpicTask> getAllEpicTasks() {
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
    public void refreshSubTask(Subtask sub) {
        Subtask refSub = findSubTaskByID(sub.getId());
        EpicTask epic = findEpicByID(refSub.getEpicId());
        if (refSub.getEpicId() != sub.getEpicId()) {
            epic.removeSubIdFromIDs(sub.getId());
            changeEpicStatus(refSub.getEpicId());
            epic = findEpicByID(sub.getEpicId());
            epic.addSubIDToSubTasksList(sub.getId());
        }
        refSub.setTaskName(sub.getTaskName());
        refSub.setDescription(sub.getDescription());
        refSub.setStatus(sub.getStatus());
        refSub.setEpicId(sub.getEpicId());
        subtasksMap.addTaskToMap(sub.getId(), refSub);
        changeEpicStatus(sub.getEpicId());
    }

    @Override
    public void deleteTaskByID(int taskId) {
        tasksMap.deleteTaskByID(taskId);
    }

    @Override
    public void deleteEpicTaskByID(int epicId) {
        for (Integer id : findEpicByID(epicId).getSubtasksIds()) {
            subtasksMap.deleteTaskByID(id);
        }
        epicTasksMap.deleteTaskByID(epicId);
    }

    @Override
    public void deleteSubTaskByID(int subId) {
        int epicID = findSubTaskByID(subId).getEpicId();
        findEpicByID(epicID).removeSubIdFromIDs(subId);
        changeEpicStatus(epicID);
        subtasksMap.deleteTaskByID(subId);
    }

    @Override
    public void deleteAllTasks() {
        tasksMap.deleteAllTasks();
    }

    @Override
    public void deleteAllEpics() {
        epicTasksMap.deleteAllTasks();
        subtasksMap.deleteAllTasks();
    }

    @Override
    public void deleteAllSubs() {
        subtasksMap.deleteAllTasks();
        for (Integer id : epicTasksMap.getTasksMap().keySet()) {
            findEpicByID(id).getSubtasksIds().clear();
            findEpicByID(id).setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public List<Subtask> getSubTasksListOfEpicByID(int epicId) {
        ArrayList<Subtask> subsList = new ArrayList<>();
        for (Integer subID : findEpicByID(epicId).getSubtasksIds()) {
            subsList.add(findSubTaskByID(subID));
        }
        return subsList;
    }


    private void changeEpicStatus(int epicId) {
        List<Integer> tasksByEpic = epicTasksMap.getTaskByID(epicId).getSubtasksIds();
        EpicTask epic = epicTasksMap.getTaskByID(epicId);
        TaskStatus status = TaskStatus.NEW;
        if (!tasksByEpic.isEmpty()) {
            status = subtasksMap.getTaskByID(tasksByEpic.get(0)).getStatus();
            for (Integer integer : tasksByEpic) {
                if (!status.equals(subtasksMap.getTaskByID(integer).getStatus())) {
                    status = TaskStatus.IN_PROGRESS;
                    break;
                }
            }
        }
        epic.setStatus(status);
        epicTasksMap.addTaskToMap(epicId, epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int generateTaskID() {
        this.taskId += 1;
        return taskId;
    }
}