package ru.yandex.practicum.kanban.service;

public interface FileBackedManager extends TaskManager {

    void save();
    void load();
}
