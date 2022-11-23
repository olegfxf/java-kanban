package manager;

import model.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    public static CustomLinkedList<Task> history = new CustomLinkedList<>();

    @Override
    public List<Task> getHistory() {
        return history.getListTasks();
    }

    public HashMap<Integer, Node<Task>> getHashMapTask() {
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


    public static class CustomLinkedList<T> {
        /**
         * Указатель на первый элемент списка. Он же first
         */
        private Node<T> head;
        /**
         * Указатель на последний элемент списка. Он же last
         */
        public Node<T> tail;
        private int size = 0;

        HashMap<Integer, Node<T>> historyTasks = new HashMap<>();


        public void linkLast(T element) {
            Integer idTask = ((Task) element).getUid();

            System.out.println("History HashMap    (idTask)");
            System.out.println(historyTasks.keySet());

            Node<T> iter = head;
            System.out.println("History LinkedList (idTask)");
            if (iter != null) {
                while (iter.next != null) {
                    System.out.print(" " + ((Task) iter.data).getUid() + " ");
                    iter = iter.next;
                }
                System.out.print(" " + ((Task) tail.data).getUid() + " "); //print tail
            } else {
                System.out.println("[]");
            }
            System.out.println();

            System.out.println("!!!!!!!!! Вставка !!!!!!!!!!!!!!!!!!! " + idTask);
            if (historyTasks.containsKey(idTask)) {
                System.out.println("@@@@@@@@@ с Заменой @@@@@@@@@@@@@@@@@ " + idTask);
                history.removeNode(idTask);
                //historyTasks.remove(idTask); // сам себя перезаписывает
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

        public T removeNode(Integer idTask) {
            Node<T> node = historyTasks.get(idTask);
            historyTasks.remove(idTask);
            if (node.equals(tail))
                return unlinkLast(node);
            else if (node.equals(head))
                return unlinkFirst(node);
            else
                return unlink(node);
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


        public T unlink(Node<T> x) {
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

        public void clear() {
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

        public ArrayList<T> getListTasks() {
            Node<T> iter = head;
            ArrayList<T> listTask = new ArrayList<>();
            listTask.add(iter.data);

            while (iter.next != null) {
                iter = iter.next;
                listTask.add(iter.data);
            }
            return listTask;
        }

        public HashMap<Integer, Node<T>> getHashHistory() {
            return historyTasks;
        }

    }


}

