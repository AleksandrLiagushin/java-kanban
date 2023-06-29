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
        subtasks.put(uniqueId, subtask);
        changeEpicStatus(subtask.getEpicId());
    }

    @Override
    public Task findTaskById(int taskId) {
        Task task = tasks.get(taskId);

        historyManager.add(task);
        return task;
    }

    @Override
    public Epic findEpicById(int epicId) {
        Epic epic = epics.get(epicId);

        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask findSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);

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
        if (!tasks.containsKey(task.getId())) {
            return;
        }

        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }

        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            return;
        }

        Subtask subtaskRef = subtasks.get(subtask.getId());
        Epic epic = epics.get(subtaskRef.getEpicId());

        if (subtaskRef.getEpicId() != subtask.getEpicId()) {
            epic.removeSubtaskId(subtask.getId());
            changeEpicStatus(subtaskRef.getEpicId());
            epic = epics.get(subtask.getEpicId());
            epic.addSubtaskId(subtask.getId());
        }

        subtasks.put(subtask.getId(), subtask);
        changeEpicStatus(subtask.getEpicId());
    }

    @Override
    public void deleteTaskById(Integer taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void deleteEpicById(Integer epicId) {

        for (Integer id : epics.get(epicId).getSubtaskIds()) {
            subtasks.remove(id);
            historyManager.remove(id);
        }

        historyManager.remove(epicId);
        epics.remove(epicId);
    }

    @Override
    public void deleteSubtaskById(Integer subtaskId) {
        int epicId = subtasks.get(subtaskId).getEpicId();

        epics.get(epicId).removeSubtaskId(subtaskId);
        changeEpicStatus(epicId);
        subtasks.remove(subtaskId);
        historyManager.remove(subtaskId);
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        epics.clear();
        subtasks.keySet().forEach(historyManager::remove);
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().forEach(historyManager::remove);
        subtasks.clear();
        for (Integer id : epics.keySet()) {
            epics.get(id).clearSubtaskIds();
            epics.get(id).setStatus(TaskStatus.NEW);
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        List<Subtask> epicSubtasks = new ArrayList<>();

        for (Integer subtaskId : epics.get(epicId).getSubtaskIds()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }

        return epicSubtasks;
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
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private int generateUniqueId() {
        return ++uniqueId;
    }

    protected void loadTask(Task task) {
        tasks.put(task.getId(), task);
    }

    protected void loadEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    protected void loadSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }
}