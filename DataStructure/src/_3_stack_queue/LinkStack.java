package _3_stack_queue;

/**
 * 不带头节点链栈
 *
 * @author stone
 * @date 2021/05/01
 */
public class LinkStack<T> implements IStack<T> {

    private T data;
    private LinkStack<T> next;

    public LinkStack() {
    }

    public LinkStack(T data) {
        this.data = data;
    }

    @Override
    public void push(T data) {
        if (this.data == null) {
            this.data = data;
        } else {
            LinkStack<T> current = this;
            while (current.next != null) {
                current = current.next;
            }
            current.next = new LinkStack<>(data);
        }
    }

    @Override
    public T pop() {
        if (this.isEmpty()) {
            return null;
        }
        T result;
        LinkStack<T> current = this;
        if (current.next == null) {
            result = current.data;
            current.data = null;
        } else {
            LinkStack<T> next = current.next;
            while (next.next != null) {
                current = next;
                next = next.next;
            }
            result = next.data;
            current.next = null;
        }
        return result;
    }

    @Override
    public T top() {
        if (this.isEmpty()) {
            return null;
        }
        T result;
        LinkStack<T> current = this;
        if (current.next == null) {
            result = current.data;
        } else {
            LinkStack<T> next = current.next;
            while (next.next != null) {
                next = next.next;
            }
            result = next.data;
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
            System.out.println("打印链栈元素：空");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        LinkStack<T> current = this;
        sb.append(current.data);
        if (current.next == null) {
            sb.append(']');
        } else {
            sb.append(", ");
            LinkStack<T> next = current.next;
            while (next.next != null) {
                sb.append(next.data).append(", ");
                next = next.next;
            }
            sb.append(next.data).append(']');
        }
        System.out.println("打印链栈元素：" + sb);
    }
}
