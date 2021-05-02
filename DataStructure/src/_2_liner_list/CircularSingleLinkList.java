package _2_liner_list;

/**
 * 带头节点循环单链表
 *
 * @author stone
 * @date 2021/04/15
 */
public class CircularSingleLinkList<T> {

    // region:成员变量

    private T data;
    private CircularSingleLinkList<T> next;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public CircularSingleLinkList<T> getNext() {
        return next;
    }

    public void setNext(CircularSingleLinkList<T> next) {
        this.next = next;
    }

    // endregion

    /**
     * 构造函数
     */
    public CircularSingleLinkList() {
        this.next = this;
    }

    /**
     * 头插
     */
    public void insertFirst(T elem) {
        CircularSingleLinkList<T> node = new CircularSingleLinkList<>();
        node.data = elem;
        CircularSingleLinkList<T> head = this;
        node.next = head.next;
        head.next = node;
    }

    /**
     * 定位尾节点
     */
    public CircularSingleLinkList<T> locateLast() {
        if (isEmpty()) {
            return null;
        }
        CircularSingleLinkList<T> head = this;
        CircularSingleLinkList<T> next = this.next;
        while (!next.next.equals(head)) {
            next = next.next;
        }
        return next;
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
            System.out.println("循环单链表为空");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        CircularSingleLinkList<T> head = this;
        CircularSingleLinkList<T> next = this.next;
        while (!next.equals(head)) {
            sb.append(next.data);
            if (!next.next.equals(head)) {
                sb.append(", ");
            }
            next = next.next;
        }
        sb.append(']');
        System.out.println("打印循环单链表数据元素：" + sb.toString());
    }

}
