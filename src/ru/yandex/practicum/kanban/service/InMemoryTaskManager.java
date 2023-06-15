package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int uniqueId;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Override
    public void createTask(Task task) {
        task.setId(generateUniqueId());
        tasks.put(uniqueId, task);
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(generateUniqueId());
        epic.setStatus(TaskStatus.NEW);
        epics.put(uniqueId, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return;
        }
        subtask.setId(generateUniqueId());
        epic.addSubtaskId(uniqueId);
        epics.put(subtask.getEpicId(), epic);
        subtasks.put(uniqueId, subtask);
        changeEpicStatus(subtask.getEpicId());
    }

    @Override
    public Task findTaskById(int taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic findEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask findSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask == null) {
            return null;
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.get(task.getId()) == null) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.get(epic.getId()) == null) {
            return;
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask refSub = subtasks.get(subtask.getId());
        Epic epic = epics.get(refSub.getEpicId());
        if (refSub.getEpicId() != subtask.getEpicId()) {
            epic.removeSubtaskId(subtask.getId());
            changeEpicStatus(refSub.getEpicId());
            epic = epics.get(subtask.getEpicId());
            epic.addSubtaskId(subtask.getId());
        }
        subtasks.put(subtask.getId(), subtask);
        changeEpicStatus(subtask.getEpicId());
    }

    public void deleteTaskById(Integer taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void deleteEpicById(Integer epicId) {
        for (Integer id : epics.get(epicId).getSubtaskIds()) {
            subtasks.remove(id);
        }
        epics.remove(epicId);
    }

    @Override
    public void deleteSubtaskById(Integer subtaskId) {
        int epicID = subtasks.get(subtaskId).getEpicId();
        epics.get(epicID).removeSubtaskId(subtaskId);
        changeEpicStatus(epicID);
        subtasks.remove(subtaskId);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Integer id : epics.keySet()) {
            epics.get(id).clearSubtaskIds();
            epics.get(id).setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>(); //невозможно переименовать subsList --> subtasks
        // конфликт имен с мапой
        for (Integer subID : epics.get(epicId).getSubtaskIds()) {
            subtasksByEpic.add(subtasks.get(subID));
        }
        return subtasksByEpic;
    }

    private void changeEpicStatus(Integer epicId) {
        List<Integer> tasksByEpic = epics.get(epicId).getSubtaskIds();
        Epic epic = epics.get(epicId);
        TaskStatus status = TaskStatus.NEW;
        if (!tasksByEpic.isEmpty()) {
            status = subtasks.get(tasksByEpic.get(0)).getStatus();
            for (Integer integer : tasksByEpic) {
                if (!status.equals(subtasks.get(integer).getStatus())) {
                    status = TaskStatus.IN_PROGRESS;
                    break;
                }
            }
        }
        epic.setStatus(status);
        epics.put(epicId, epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int generateUniqueId() {
        return ++uniqueId;
    }
}