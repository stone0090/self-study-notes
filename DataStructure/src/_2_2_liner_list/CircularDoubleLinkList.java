package _2_2_liner_list;

/**
 * 带头节点双循环链表
 *
 * @author stone
 * @date 2021/04/15
 */
public class CircularDoubleLinkList<T> {

    private T data;
    private CircularDoubleLinkList<T> prior;
    private CircularDoubleLinkList<T> next;

    public CircularDoubleLinkList() {
        this.data = null;
        this.prior = this;
        this.next = this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public CircularDoubleLinkList<T> getPrior() {
        return prior;
    }

    public void setPrior(CircularDoubleLinkList<T> prior) {
        this.prior = prior;
    }

    public CircularDoubleLinkList<T> getNext() {
        return next;
    }

    public void setNext(CircularDoubleLinkList<T> next) {
        this.next = next;
    }

    /**
     * 头插
     */
    public void insertFirst(T elem) {
        CircularDoubleLinkList<T> node = new CircularDoubleLinkList<>();
        node.data = elem;
        CircularDoubleLinkList<T> head = this;
        node.prior = head;
        node.next = head.next;
        head.next.prior = node;
        head.next = node;
    }

    /**
     * 尾插
     */
    public void insertLast(T elem) {
        CircularDoubleLinkList<T> node = new CircularDoubleLinkList<>();
        node.data = elem;
        CircularDoubleLinkList<T> head = this;
        CircularDoubleLinkList<T> last = head.prior;
        node.prior = last;
        node.next = head;
        last.next = node;
        head.prior = node;
    }

    public boolean isEmpty() {
        if (this.next.equals(this)) {
            return true;
        }
        return false;
    }

    /**
     * 打印
     */
    public void print() {
        if (this.isEmpty()) {
            System.out.println("循环双链表为空");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        CircularDoubleLinkList<T> head = this;
        CircularDoubleLinkList<T> next = this.next;
        while (!next.equals(head)) {
            sb.append(next.data);
            if (!next.next.equals(head)) {
                sb.append(", ");
            }
            next = next.next;
        }
        sb.append(']');
        System.out.println("打印循环双链表数据元素：" + sb.toString());
    }

}
