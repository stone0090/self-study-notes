package _2_liner_list;

/**
 * 带头节点双循环链表
 *
 * @author stone
 * @date 2021/04/15
 */
public class CircularDoubleLinkNode<T> {

    // region: 成员变量

    private T data;
    private CircularDoubleLinkNode<T> prior;
    private CircularDoubleLinkNode<T> next;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public CircularDoubleLinkNode<T> getPrior() {
        return prior;
    }

    public void setPrior(CircularDoubleLinkNode<T> prior) {
        this.prior = prior;
    }

    public CircularDoubleLinkNode<T> getNext() {
        return next;
    }

    public void setNext(CircularDoubleLinkNode<T> next) {
        this.next = next;
    }

    // endregion

    /**
     * 构造函数
     */
    public CircularDoubleLinkNode() {
        this.data = null;
        this.prior = this;
        this.next = this;
    }

    /**
     * 头插
     */
    public void insertFirst(T elem) {
        CircularDoubleLinkNode<T> node = new CircularDoubleLinkNode<>();
        node.data = elem;
        CircularDoubleLinkNode<T> head = this;
        node.prior = head;
        node.next = head.next;
        head.next.prior = node;
        head.next = node;
    }

    /**
     * 尾插
     */
    public void insertLast(T elem) {
        CircularDoubleLinkNode<T> node = new CircularDoubleLinkNode<>();
        node.data = elem;
        CircularDoubleLinkNode<T> head = this;
        CircularDoubleLinkNode<T> last = head.prior;
        node.prior = last;
        node.next = head;
        last.next = node;
        head.prior = node;
    }

    /**
     * 判空
     */
    public boolean isEmpty() {
        return this.next.equals(this);
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
        CircularDoubleLinkNode<T> head = this;
        CircularDoubleLinkNode<T> next = this.next;
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
