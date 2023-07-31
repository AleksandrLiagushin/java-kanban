package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.Task;
import ru.yandex.practicum.kanban.user.User;

import java.util.List;

public interface UserManager {
    int add(User user);
    void update(User user);
    User getById(int userId);
    void delete(int userId);
    List<User> getAll();
    List<Task> gerUserTasks(int userId);
    TaskManager getTaskManger();
}
