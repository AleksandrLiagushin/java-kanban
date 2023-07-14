package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.Epic;
import ru.yandex.practicum.kanban.model.Subtask;
import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int uniqueId;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private final Map<Task, Boolean> priorityTasks = new TreeMap<>((t1, t2) ->{
        Optional<LocalDateTime> t1StartTime = t1.getStartTime();
        Optional<LocalDateTime> t2StartTime = t2.getStartTime();

        findCrossings(t1, t2);

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
    public void createTask(Task task) {
        if (task.getId() == 0) {
            task.setId(generateUniqueId());
        }
        if (uniqueId < task.getId()) {
            uniqueId = task.getId();
        }
        tasks.put(task.getId(), task);
        priorityTasks.put(task, true);
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic.getId() == 0) {
            epic.setId(generateUniqueId());
        }
        if (uniqueId < epic.getId()) {
            uniqueId = epic.getId();
        }

        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());

        if (epic == null) {
            return;
        }

        if (subtask.getId() == 0) {
            subtask.setId(generateUniqueId());
        }
        if (uniqueId < subtask.getId()) {
            uniqueId = subtask.getId();
        }

        epic.addSubtaskId(subtask.getId());
        epic.addDuration(subtask.getDuration().orElse(Duration.ZERO));
        subtasks.put(subtask.getId(), subtask);
        changeEpicStatus(subtask.getEpicId());
        setEpicStartTime(epic.getId());
        priorityTasks.put(subtask, true);
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
        priorityTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        priorityTasks.put(task, true);
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
        System.out.println("Found subtask for update");
        Subtask subtaskToRef = subtasks.get(subtask.getId());
        priorityTasks.remove(subtasks.get(subtask.getId()));
        Epic epic = epics.get(subtaskToRef.getEpicId());
        epic.subtractDuration(subtaskToRef.getDuration().orElse(Duration.ZERO));

        if (subtaskToRef.getEpicId() != subtask.getEpicId()) {
            epic.removeSubtaskId(subtask.getId());
            changeEpicStatus(subtaskToRef.getEpicId());
            setEpicStartTime(epic.getId());
            epic = epics.get(subtask.getEpicId());
            epic.addSubtaskId(subtask.getId());
        }

        subtasks.put(subtask.getId(), subtask);
        changeEpicStatus(subtask.getEpicId());
        setEpicStartTime(epic.getId());
        epic.addDuration(subtask.getDuration().orElse(Duration.ZERO));
        priorityTasks.put(subtask, true);
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

        if (tasksByEpic.isEmpty()) {
            epic.setStatus(status); //мне не нравится такой вариант кода из-за дублирования строк (DRY)
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Map<Task, Boolean> getPriorityTasks() {
        return priorityTasks;
    }

    private int generateUniqueId() {
        return ++uniqueId;
    }

    private void setEpicStartTime(int epicId) {
        Optional<Subtask> subtaskWithEarliestStartTime = getSubtasksByEpicId(epicId).stream()
                .filter(subtask -> subtask.getStartTime().isPresent())
                .min(Comparator.comparing(subtask -> subtask.getStartTime().get()));
        subtaskWithEarliestStartTime.flatMap(Task::getStartTime)
                .ifPresent(startTime -> epics.get(epicId).setStartTime(startTime));
    }

    private void findCrossings(Task t1, Task t2) {
        Optional<LocalDateTime> t1StartTime = t1.getStartTime();
        Optional<LocalDateTime> t2StartTime = t2.getStartTime();
        Optional<LocalDateTime> t1EndTime = t1.getEndTime();
        Optional<LocalDateTime> t2EndTime = t2.getEndTime();

        if (t1.getStartTime().isEmpty() || t2.getStartTime().isEmpty()) {
            return;
        }

        if ((t1EndTime.get().isAfter(t2StartTime.get()) && t1StartTime.get().isBefore(t2EndTime.get())) ||
                (t2EndTime.get().isAfter(t1StartTime.get()) && t2StartTime.get().isBefore(t1EndTime.get()))) {
            if (t1.getId() != t2.getId()) {
                t1.addCrossedTask(t2.getId());
                t2.addCrossedTask(t1.getId());
            }
        } else {
            if (t1.getCrossedTasks().isEmpty()) {
                return;
            }
            t1.removeCrossedTask(t2.getId());
            if (t1.getCrossedTasks().isEmpty()) {
                return;
            }
            t2.removeCrossedTask(t1.getId());
        }
    }
}