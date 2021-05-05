package _2_liner_list;

/**
 * 不带头节点单链表
 *
 * @author stone
 * @date 2021/04/15
 */
public class SingleLinkNode<T> implements ILinerList<T> {

    // region:成员变量

    private T data;
    private SingleLinkNode<T> next;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public SingleLinkNode<T> getNext() {
        return next;
    }

    public void setNext(SingleLinkNode<T> next) {
        this.next = next;
    }

    // endregion

    // region:基础操作（创销增删改查）

    @Override
    public void insertFirst(T data) {
        if (this.isEmpty()) {
            this.data = data;
            return;
        }
        SingleLinkNode<T> node = new SingleLinkNode<>();
        node.data = this.data;
        node.next = this.next;
        this.next = node;
        this.data = data;
    }

    @Override
    public void insertLast(T data) {
        if (this.isEmpty()) {
            this.data = data;
            return;
        }
        SingleLinkNode<T> current = this;
        while (current.next != null) {
            current = current.next;
        }
        SingleLinkNode<T> node = new SingleLinkNode<>();
        node.data = data;
        current.next = node;
    }

    @Override
    public void deleteByData(T data) {
        if (this.isEmpty()) {
            return;
        }
        if (this.data.equals(data)) {
            if (this.next == null) {
                this.data = null;
            } else {
                this.data = this.next.data;
                this.next = this.next.next;
            }
        } else {
            SingleLinkNode<T> current = this;
            SingleLinkNode<T> next = this.next;
            while (next != null) {
                if (next.data.equals(data)) {
                    current.next = next.next;
                    break;
                }
                current = next;
                next = next.next;
            }
        }
    }

    @Override
    public T deleteByPosition(int position) {
        if (this.isEmpty()) {
            return null;
        }
        T result = null;
        if (position == 1) {
            result = this.data;
            if (this.next == null) {
                this.data = null;
            } else {

                this.data = this.next.data;
                this.next = this.next.next;
            }
        } else {
            int count = 2;
            SingleLinkNode<T> current = this;
            SingleLinkNode<T> next = this.next;
            while (next != null) {
                if (position == count) {
                    result = next.data;
                    current.next = next.next;
                    break;
                }
                current = next;
                next = next.next;
                count++;
            }
        }
        return result;
    }

    @Override
    public T getDataByPosition(int position) {
        if (this.isEmpty()) {
            return null;
        }
        T result = null;
        if (position == 1) {
            result = this.data;
        } else {
            int count = 2;
            SingleLinkNode<T> next = this.next;
            while (next != null) {
                if (position == count) {
                    result = next.data;
                    break;
                }
                next = next.next;
                count++;
            }
        }
        return result;
    }

    @Override
    public int getFirstPositionByData(T data) {
        return this.getPositionByData(data, true);
    }

    @Override
    public int getLastPositionByData(T data) {
        return this.getPositionByData(data, false);
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
            System.out.println("打印单链表数据元素：空");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        SingleLinkNode<T> current = this;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append(']');
        System.out.println("打印单链表数据元素：" + sb);
    }

    // endregion

    // region:扩展算法

    /**
     * 按值查找
     */
    public SingleLinkNode<T> locate(T elem) {
        if (elem == null) {
            return null;
        }
        SingleLinkNode<T> current = this.next;
        while (current != null) {
            if (current.data.equals(elem)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public void insertLast(T[] datas) {
        if (datas == null || datas.length == 0) {
            return;
        }
        for (T data : datas) {
            this.insertLast(data);
        }
    }

    public int getPositionByData(T data, boolean firstOrLast) {
        if (this.isEmpty()) {
            return 0;
        }
        int result = 0;
        if (this.data.equals(data)) {
            return 1;
        } else {
            int count = 2;
            SingleLinkNode<T> next = this.next;
            while (next != null) {
                if (next.data.equals(data)) {
                    result = count;
                    if (firstOrLast) {
                        break;
                    }
                }
                next = next.next;
                count++;
            }
        }
        return result;
    }

    // endregion

}
