package ru.yandex.practicum.kanban.service;

import ru.yandex.practicum.kanban.model.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> history = new HashMap<>();
    private final CustomLinkedList tasks = new CustomLinkedList();

    @Override
    public void add(Task task) {

        if (task == null) {
            return;
        }

        int id = task.getId();

        remove(id);
        history.put(id, tasks.addLast(task));
    }

    @Override
    public void remove(int id) {

        //данная проверка необходима, т.к. передача несуществующего ID в метод unlink вызывает NPE
        //генерится при попытке вызвать данные поля у нода со значением null:
        //final Node next = x.next;
        //final Node prev = x.prev;
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

    private static class CustomLinkedList {
        private Node head;
        private Node tail;

        public Node addLast(Task element) {
            final Node oldTail = tail;
            final Node newNode = new Node(oldTail, element, null);
            tail = newNode;

            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }

            return newNode;
        }

        private void unlink(Node x) {
            final Node next = x.next;
            final Node prev = x.prev;

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
            Node node = head;

            while (node != null) {
                tasksHistory.add(node.data);
                node = node.next;
            }

            return tasksHistory;
        }
    }

    private static class Node {
        public Task data;
        public Node next;
        public Node prev;

        //не понял в чем заключается удобство отказа от данного конструктора
        //отказ ведет к генерации дополнительных методов и лишнего кода
        //данный список и ноды мы используем только внутри хистрориманагера
        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
