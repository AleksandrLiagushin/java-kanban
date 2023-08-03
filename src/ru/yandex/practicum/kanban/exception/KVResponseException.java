package ru.yandex.practicum.kanban.exception;

public class KVResponseException extends RuntimeException{
    public KVResponseException(String message) {
        super(message);
    }
    public KVResponseException(String message, Throwable cause) {
        super(message);
    }
}
