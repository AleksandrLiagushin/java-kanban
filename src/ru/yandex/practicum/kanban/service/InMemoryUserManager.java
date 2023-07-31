package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.user.User;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryUserManager implements UserManager{
    private final Map<Integer, User> users = new HashMap<>();
    private final FileBackedManager taskManager = Managers.getBackedTaskManager(Paths.get("resources", "Tasks.csv"));
    private int uniqueUserId = 0;

    private int generateId() {
        return ++uniqueUserId;
    }

    @Override
    public int add(User user) {
        int id = generateId();
        user.setId(id);
        users.put(uniqueUserId, user);
        return id;
    }

    @Override
    public void update(User user) {
        int id = user.getId();
        if (!users.containsKey(id)) {
            return;
        }
        users.put(id, user);
    }

    @Override
    public User getById(int userId) {
        return users.get(userId);
    }

    @Override
    public void delete(int userId) {
        users.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<Task> getUserTasks(int userId) {
        return Stream.of(taskManager.getAllTasks(), taskManager.getAllEpics(), taskManager.getAllSubtasks())
                .flatMap(Collection::stream)
                .filter(task -> task.getUser().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public FileBackedManager getTaskManger() {
        return taskManager;
    }
}
