package ru.yandex.practicum.kanban.service;

public interface FileManager extends TaskManager {

    void save();
    void load();
}
