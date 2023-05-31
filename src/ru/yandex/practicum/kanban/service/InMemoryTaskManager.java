package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.data.model.Epic;
import ru.yandex.practicum.kanban.data.model.Subtask;
import ru.yandex.practicum.kanban.data.model.Task;
import ru.yandex.practicum.kanban.data.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int taskId;
    private final HashMap<Integer, Task> tasksMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasksMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Override
    public void createTask(Task task) {
        tasksMap.put(taskId, task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setStatus(TaskStatus.NEW);
        epicMap.put(taskId, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (epicMap.get(subtask.getEpicId()) != null) {
            Epic epic = epicMap.get(subtask.getEpicId());
            epic.addSubtaskId(taskId);
            epicMap.put(subtask.getEpicId(), epic);
            subtasksMap.put(taskId, subtask);
            changeEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public Task findTaskById(int taskId) {
        if (tasksMap.get(taskId) != null) {
            historyManager.add(tasksMap.get(taskId));
            return tasksMap.get(taskId);
        }
        return null;
    }

    @Override
    public Epic findEpicById(int epicId) {
        if (epicMap.get(epicId) != null) {
            historyManager.add(epicMap.get(epicId));
            return epicMap.get(epicId);
        }
        return null;
    }

    @Override
    public Subtask findSubtaskById(int subtaskId) {
        if (subtasksMap.get(subtaskId) != null) {
            historyManager.add(subtasksMap.get(subtaskId));
            return subtasksMap.get(subtaskId);
        }
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public void refreshTask(Task task) {
        if (findTaskById(task.getId()) == null) {
            return;
        }
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void refreshEpic(Epic epic) {
        if (findEpicById(epic.getId()) == null) {
            return;
        }
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void refreshSubtask(Subtask subtask) {
        Subtask refSub = findSubtaskById(subtask.getId());
        Epic epic = findEpicById(refSub.getEpicId());
        if (refSub.getEpicId() != subtask.getEpicId()) {
            epic.removeSubtaskId(subtask.getId());
            changeEpicStatus(refSub.getEpicId());
            epic = findEpicById(subtask.getEpicId());
            epic.addSubtaskId(subtask.getId());
        }
        refSub.setName(subtask.getName());
        refSub.setDescription(subtask.getDescription());
        refSub.setStatus(subtask.getStatus());
        refSub.setEpicId(subtask.getEpicId());
        subtasksMap.put(subtask.getId(), refSub);
        changeEpicStatus(subtask.getEpicId());
    }

    public void deleteTaskById(Integer taskId) {
        tasksMap.remove(taskId);
    }

    @Override
    public void deleteEpicById(Integer epicId) {
        for (Integer id : findEpicById(epicId).getSubtaskIds()) {
            subtasksMap.remove(id);
        }
        epicMap.remove(epicId);
    }

    @Override
    public void deleteSubtaskById(Integer subtaskId) {
        int epicID = findSubtaskById(subtaskId).getEpicId();
        findEpicById(epicID).removeSubtaskId(subtaskId);
        changeEpicStatus(epicID);
        subtasksMap.remove(subtaskId);
    }

    @Override
    public void deleteAllTasks() {
        tasksMap.clear();
    }

    @Override
    public void deleteAllEpics() {
        epicMap.clear();
        subtasksMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasksMap.clear();
        for (Integer id : epicMap.keySet()) {
            findEpicById(id).getSubtaskIds().clear();
            findEpicById(id).setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> subsList = new ArrayList<>();
        for (Integer subID : findEpicById(epicId).getSubtaskIds()) {
            subsList.add(findSubtaskById(subID));
        }
        return subsList;
    }


    private void changeEpicStatus(Integer epicId) {
        List<Integer> tasksByEpic = epicMap.get(epicId).getSubtaskIds();
        Epic epic = epicMap.get(epicId);
        TaskStatus status = TaskStatus.NEW;
        if (!tasksByEpic.isEmpty()) {
            status = subtasksMap.get(tasksByEpic.get(0)).getStatus();
            for (Integer integer : tasksByEpic) {
                if (!status.equals(subtasksMap.get(integer).getStatus())) {
                    status = TaskStatus.IN_PROGRESS;
                    break;
                }
            }
        }
        epic.setStatus(status);
        epicMap.put(epicId, epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int generateTaskID() {
        return ++taskId;
    }
}