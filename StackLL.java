public class StackLL<T> {

    private SinglyList<T> list;

    public StackLL() {
        list = new SinglyList<>();
    }

    // Top of the stack is the head of the list
    public void push(T x) {
        list.addFirst(x);
    }

    public T pop() {
        return list.removeFirst();
    }

    // Simple peek without exposing head: remove then put back
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
