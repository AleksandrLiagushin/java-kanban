package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.data.model.Node;
import ru.yandex.practicum.kanban.data.model.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> history = new HashMap<>();
    private final CustomLinkedList<Task> tasks = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        int id = task.getId();

        remove(id);
        history.put(id, tasks.addLast(task));
    }

    @Override
    public void remove(int id) {

        if (history.get(id) == null) {
            return;
        }

        tasks.unlink(history.get(id));
        history.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return tasks.getHistory();
    }

    static class CustomLinkedList<T extends Task>{

        private Node<T> head;
        private Node<T> tail;


        public Node<T> addLast(T element) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;

            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }

            return newNode;
        }

        private void unlink(Node<T> x) {

            final Node<T> next = x.next;
            final Node<T> prev = x.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                x.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                x.next = null;
            }

            x.data = null;
        }

        private List<Task> getHistory() {

            List<Task> tasksHistory = new LinkedList<>();
            Node<T> node = head;

            while (node != null) {
                tasksHistory.add(node.data);
                node = node.next;
            }

            return tasksHistory;
        }
    }
}
