package _2_liner_list;

/**
 * 带头节点双链表
 *
 * @author stone
 * @date 2021/04/15
 */
public class DoubleLinkList<T> {

    // region: 成员变量

    private T data;
    private int freq;
    private DoubleLinkList<T> prior;
    private DoubleLinkList<T> next;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public DoubleLinkList<T> getPrior() {
        return prior;
    }

    public void setPrior(DoubleLinkList<T> prior) {
        this.prior = prior;
    }

    public DoubleLinkList<T> getNext() {
        return next;
    }

    public void setNext(DoubleLinkList<T> next) {
        this.next = next;
    }

    // endregion

    /**
     * 构造函数
     */
    public DoubleLinkList() {
        this.data = null;
    }

    /**
     * 头插
     */
    public void insertFirst(T elem) {
        DoubleLinkList<T> node = new DoubleLinkList<>();
        node.data = elem;
        DoubleLinkList<T> head = this;
        node.prior = head;
        node.next = head.next;
        if (head.next != null) {
            head.next.prior = node;
        }
        head.next = node;
    }

    /**
     * 定位
     */
    public DoubleLinkList<T> locate(T elem) {
        DoubleLinkList<T> current = this.next;
        while (current != null) {
            if (current.getData().equals(elem)) {
                current.freq++;
                this.resort(current);
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public void resort(DoubleLinkList<T> node) {
        DoubleLinkList<T> current = this.next;
        if (node.equals(current)) {
            return;
        }
        while (current != null) {
            if (node.freq >= current.freq) {
                this.delete(node);
                node.prior = current.prior;
                node.next = current;
                current.prior.next = node;
                current.prior = node;
                return;
            }
            current = current.next;
        }
    }

    public void delete(DoubleLinkList<T> node) {
        node.prior.next = node.next;
        node.next.prior = node.prior;
        node.prior = null;
        node.next = null;
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
        DoubleLinkList<T> next = this.next;
        while (next != null) {
            sb.append(next.data).append("(fred:").append(next.freq).append(")");
            if (next.next != null) {
                sb.append(", ");
            }
            next = next.next;
        }
        sb.append(']');
        System.out.println("打印循环双链表数据元素：" + sb.toString());
    }

}
