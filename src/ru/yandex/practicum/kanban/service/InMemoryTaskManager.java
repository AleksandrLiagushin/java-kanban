package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.exception.ManagerIntersectionException;
import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int uniqueId;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private final Set<Task> tasksPrioritizedByTime = new TreeSet<>((t1, t2) -> {
        Optional<LocalDateTime> t1StartTime = t1.getStartTime();
        Optional<LocalDateTime> t2StartTime = t2.getStartTime();

        if (t1StartTime.isEmpty() && t2StartTime.isEmpty()) {
            return t1.getId() - t2.getId();
        }
        if (t1StartTime.equals(t2StartTime)) {
            return t1.getId() - t2.getId();
        }
        if (t1StartTime.isEmpty()) {
            return 1;
        }
        if (t2StartTime.isEmpty()) {
            return -1;
        }
        if (t1StartTime.get().isBefore(t2StartTime.get())) {
            return -1;
        } else {
            return 1;
        }
    });

    @Override
    public boolean createTask(Task task) {
        if (isEmptyTask(task)) {
            return false;
        }

        checkId(task);

        try {
            isCrossing(task);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }

        tasks.put(task.getId(), task);
        tasksPrioritizedByTime.add(task);
        return true;
    }

    @Override
    public boolean createEpic(Epic epic) {
        if (isEmptyTask(epic)) {
            return false;
        }

        checkId(epic);

        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
        return true;
    }

    @Override
    public boolean createSubtask(Subtask subtask) {
        if (isEmptyTask(subtask)) {
            return false;
        }

        Epic epic = epics.get(subtask.getEpicId());

        if (epic == null) {
            return false;
        }

        checkId(subtask);

        try {
            isCrossing(subtask);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }

        if (subtask.getStartTime().isPresent()) {
            epic.addDuration(subtask.getDuration().orElse(Duration.ZERO));
        }

        epic.addSubtaskId(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        changeEpicStatus(subtask.getEpicId());
        setEpicStartTime(epic.getId());
        tasksPrioritizedByTime.add(subtask);
        return true;
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);

        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);

        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
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
    public boolean updateTask(Task task) {
        if (isEmptyTask(task)) {
            return false;
        }

        if (!tasks.containsKey(task.getId())) {
            return false;
        }

        try {
            isCrossing(task);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }

        tasksPrioritizedByTime.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        tasksPrioritizedByTime.add(task);
        return true;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (isEmptyTask(epic)) {
            return false;
        }

        if (!epics.containsKey(epic.getId())) {
            return false;
        }

        epic.setStatus(epics.get(epic.getId()).getStatus());

        epics.put(epic.getId(), epic);
        return true;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (isEmptyTask(subtask)) {
            return false;
        }

        if (!subtasks.containsKey(subtask.getId())) {
            return false;
        }

        try {
            isCrossing(subtask);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return false;
        }

        Subtask subtaskToRef = subtasks.get(subtask.getId());
        tasksPrioritizedByTime.remove(subtaskToRef);
        Epic epic = epics.get(subtaskToRef.getEpicId());

        if (subtaskToRef.getStartTime().isPresent()) {
            epic.subtractDuration(subtaskToRef.getDuration().orElse(Duration.ZERO));
        }

        if (subtaskToRef.getEpicId() != subtask.getEpicId()) {
            epic.removeSubtaskId(subtask.getId());
            changeEpicStatus(subtaskToRef.getEpicId());
            setEpicStartTime(epic.getId());
            epic = epics.get(subtask.getEpicId());
            epic.addSubtaskId(subtask.getId());
        }

        if (subtask.getStartTime().isPresent()) {
            epic.addDuration(subtask.getDuration().orElse(Duration.ZERO));
        }

        subtasks.put(subtask.getId(), subtask);
        changeEpicStatus(subtask.getEpicId());
        setEpicStartTime(epic.getId());
        tasksPrioritizedByTime.add(subtask);
        return true;
    }

    @Override
    public void deleteTaskById(Integer taskId) {
        tasksPrioritizedByTime.remove(tasks.get(taskId));
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
        Epic epic = epics.get(epicId);

        epic.removeSubtaskId(subtaskId);
        changeEpicStatus(epicId);

        if (subtasks.get(subtaskId).getDuration().isPresent()) {
            epic.subtractDuration(subtasks.get(subtaskId).getDuration().orElse(Duration.ZERO));
        }

        tasksPrioritizedByTime.remove(subtasks.get(subtaskId));
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
            Epic epic = epics.get(id);
            epic.clearSubtaskIds();
            epic.setStatus(TaskStatus.NEW);
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO.toMinutes());
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        if (!epics.containsKey(epicId)) {
            return null;
        }

        return epics.get(epicId).getSubtaskIds().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getTasksPrioritizedByTime() {
        return List.copyOf(tasksPrioritizedByTime);
    }

    private int generateUniqueId() {
        return ++uniqueId;
    }

    private void changeEpicStatus(Integer epicId) {
        List<Integer> tasksByEpic = epics.get(epicId).getSubtaskIds();
        Epic epic = epics.get(epicId);
        TaskStatus status = TaskStatus.NEW;

        if (tasksByEpic.isEmpty()) {
            epic.setStatus(status);
            return;
        }

        status = subtasks.get(tasksByEpic.get(0)).getStatus();
        for (Integer integer : tasksByEpic) {
            if (!status.equals(subtasks.get(integer).getStatus())) {
                status = TaskStatus.IN_PROGRESS;
                break;
            }
        }

        epic.setStatus(status);
    }

    private void setEpicStartTime(int epicId) {
        epics.get(epicId).setStartTime(getSubtasksByEpicId(epicId).stream()
                .filter(subtask -> subtask.getStartTime().isPresent())
                .min(Comparator.comparing(subtask -> subtask.getStartTime().get()))
                .flatMap(Task::getStartTime)
                .orElse(null));
    }

    private void isCrossing(Task task) throws RuntimeException {
        Optional<LocalDateTime> t1StartTime = task.getStartTime();
        Optional<LocalDateTime> t1EndTime = task.getEndTime();

        if (t1StartTime.isEmpty()) {
            return;
        }

        for (Task task2 : tasksPrioritizedByTime) {
            Optional<LocalDateTime> t2EndTime = task2.getEndTime();
            Optional<LocalDateTime> t2StartTime = task2.getStartTime();

            if (t2StartTime.isEmpty() || task.getId() == task2.getId()) {
                continue;
            }

            if ((t1EndTime.get().isAfter(t2StartTime.get()) && t1StartTime.get().isBefore(t2EndTime.get())) ||
                    (t2EndTime.get().isAfter(t1StartTime.get()) && t2StartTime.get().isBefore(t1EndTime.get()))) {
                throw new ManagerIntersectionException("Can't add/update task because it is crossing other one");
            }
        }
    }

    private void checkId(Task task) {
        if (task.getId() == 0) {
            task.setId(generateUniqueId());
        }
        if (uniqueId < task.getId()) {
            uniqueId = task.getId();
        }
    }

    private boolean isEmptyTask(Task task) {
        String name = task.getName();

        return name == null || name.isEmpty() || name.isBlank();
    }

    void restoreTask(Task task) {
        if (task.getClass().equals(Task.class)) {
            tasks.put(task.getId(), task);
            tasksPrioritizedByTime.add(task);
        } else if (task.getClass().equals(Epic.class)) {
            epics.put(task.getId(), (Epic) task);
            tasksPrioritizedByTime.add(task);
        } else if (task.getClass().equals(Subtask.class)) {
            subtasks.put(task.getId(), (Subtask) task);
            tasksPrioritizedByTime.add(task);
        }
    }

    void restoreHistory(List<Integer> history) {
        for(Integer taskId : history) {
            if (tasks.containsKey(taskId)) {
                historyManager.add(tasks.get(taskId));
            }
            if (epics.containsKey(taskId)) {
                historyManager.add(epics.get(taskId));
            }
            if (subtasks.containsKey(taskId)) {
                historyManager.add(subtasks.get(taskId));
            }
        }
    }
}
