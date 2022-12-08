package manager;

import model.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    private final static CustomLinkedList<Task> history = new CustomLinkedList<>();

    @Override
    public List<Task> getHistory() {
        return history.getListTasks();
    }

    public Map<Integer, Node<Task>> getHashMapTask() {
        return history.getHashHistory();
    }

    public void clearAll() {
        history.clear();
    }

    public void add(Task task) {
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(id);
    }


    private static class CustomLinkedList<T> {
        /**
         * Указатель на первый элемент списка. Он же first
         */
        private Node<T> head;
        /**
         * Указатель на последний элемент списка. Он же last
         */
        private Node<T> tail;
        int size = 0;

        private final Map<Integer, Node<T>> historyTasks = new HashMap<>();


        private void linkLast(T element) {
            Integer idTask = ((Task) element).getUid();
            Node<T> iter = head;

            if (iter != null) {
                while (iter.next != null) {
                    iter = iter.next;
                }
            }

            if (historyTasks.containsKey(idTask)) {
                history.removeNode(idTask);
            }

            // Реализуйте метод
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, element, null);
            tail = newNode;
            if (oldTail == null) {
                head = newNode;
            } else {
                oldTail.next = newNode;
            }
            historyTasks.put(idTask, tail);

            size++;
        }

        private T removeNode(Integer idTask) {
            Node<T> node = historyTasks.get(idTask);
            historyTasks.remove(idTask);
            if (node != null) {
                if (node.equals(tail))
                    return unlinkLast(node);
                else if (node.equals(head))
                    return unlinkFirst(node);
                else
                    return unlink(node);
            } else {
                return null;
            }
        }

        private T unlinkFirst(Node<T> f) {
            // assert f == first && f != null;
            final T element = f.data;
            final Node<T> next = f.next;
            f.data = null;
            f.next = null; // help GC
            head = next;
            if (next == null)
                tail = null;
            else
                next.prev = null;
            size--;
            return element;
        }


        private T unlink(Node<T> x) {
            final T element = x.data;
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
            size--;

            return element;
        }

        private T unlinkLast(Node<T> l) {
            // assert l == last && l != null;
            final T element = l.data;
            final Node<T> prev = l.prev;
            l.data = null;
            l.prev = null; // help GC
            tail = prev;
            if (prev == null)
                head = null;
            else
                prev.next = null;
            size--;
            return element;
        }

        private void clear() {
            historyTasks.clear(); //очищаем HashMap
            for (Node<T> x = head; x != null; ) {
                Node<T> next = x.next;
                x.data = null;
                x.next = null;
                x.prev = null;
                x = next;
            }
            head = tail = null;
            size = 0;
        }

        private List<T> getListTasks() {
            Node<T> iter = head;
            List<T> listTask = new ArrayList<>();
            listTask.add(iter.data);

            while (iter.next != null) {
                iter = iter.next;
                listTask.add(iter.data);
            }
            return listTask;
        }

        private Map<Integer, Node<T>> getHashHistory() {
            return historyTasks;
        }

    }

}

