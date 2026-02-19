public class QueueLL<T> {

    private SinglyList<T> list;

    public QueueLL() {
        list = new SinglyList<>();
    }

    // Enqueue goes to the tail, dequeue comes from the head
    public void enqueue(T x) {
        list.addLast(x);
    }

    public T dequeue() {
        return list.removeFirst();
    }

    // Peek at the front without changing the order
    public T peek() {
        T val = list.removeFirst();
        if (val == null) return null;
        list.addFirst(val);
        return val;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        return list.toValueString();
    }
}
