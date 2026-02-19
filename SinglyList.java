public class SinglyList<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public SinglyList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void addFirst(T x) {
        Node<T> n = new Node<>(x);
        n.next = head;
        head = n;

        if (tail == null) {
            tail = head;
        }
        size++;
    }

    public void addLast(T x) {
        Node<T> n = new Node<>(x);

        if (head == null) {
            head = n;
            tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    public T removeFirst() {
        if (head == null) return null;

        T removed = head.data;
        head = head.next;
        size--;

        if (head == null) {
            tail = null;
        }
        return removed;
    }

    public boolean remove(T x) {
        Node<T> prev = null;
        Node<T> cur = head;

        while (cur != null) {
            if ((x == null && cur.data == null) || (x != null && x.equals(cur.data))) {
                if (prev == null) {
                    head = cur.next;
                } else {
                    prev.next = cur.next;
                }

                if (cur == tail) {
                    tail = prev;
                }

                size--;
                if (size == 0) {
                    head = null;
                    tail = null;
                }
                return true;
            }

            prev = cur;
            cur = cur.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> cur = head;
        while (cur != null) {
            sb.append(cur.data).append(" -> ");
            cur = cur.next;
        }
        sb.append("null");
        return sb.toString();
    }

    public String toValueString() {
        StringBuilder sb = new StringBuilder();
        Node<T> cur = head;
        while (cur != null) {
            sb.append(cur.data);
            if (cur.next != null) sb.append(" ");
            cur = cur.next;
        }
        return sb.toString();
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
