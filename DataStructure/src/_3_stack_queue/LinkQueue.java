package _3_stack_queue;

/**
 * 链式队列
 *
 * @author stone
 * @date 2021/05/01
 */
public class LinkQueue<T> implements IQueue<T> {

    private T data;
    private LinkQueue<T> next;

    public LinkQueue() {
    }

    public LinkQueue(T data) {
        this.data = data;
    }

    @Override
    public void enqueue(T data) {
        if (this.data == null) {
            this.data = data;
        } else {
            LinkQueue<T> current = this;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new LinkQueue<T>(data);
        }
    }

    @Override
    public T dequeue() {
        if (this.isEmpty()) {
            return null;
        }
        T result = this.data;
        if (this.next == null) {
            this.data = null;
        } else {
            LinkQueue<T> next = this.next;
            this.data = next.data;
            this.next = next.next;
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        return this.data == null;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public void print() {
        if (this.isEmpty()) {
            System.out.println("打印链式队列元素：空");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        LinkQueue<T> current = this;
        sb.append(current.data);
        if (current.next == null) {
            sb.append(']');
        } else {
            sb.append(", ");
            LinkQueue<T> next = current.next;
            while (next.next != null) {
                sb.append(next.data).append(", ");
                next = next.next;
            }
            sb.append(next.data).append(']');
        }
        System.out.println("打印链式队列元素：" + sb);
    }

}
