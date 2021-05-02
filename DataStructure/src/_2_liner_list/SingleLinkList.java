package _2_liner_list;

/**
 * 带头节点单链表
 *
 * @author stone
 * @date 2021/04/15
 */
public class SingleLinkList<T> {

    // region:成员变量

    private T data;
    private SingleLinkList<T> next;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public SingleLinkList<T> getNext() {
        return next;
    }

    public void setNext(SingleLinkList<T> next) {
        this.next = next;
    }

    // endregion

    // region:基础操作（创销增删改查）

    /**
     * 按位序插入
     */
    public void insert(int index, T elem) {
        if (index < 1 || elem == null) {
            return;
        }
        SingleLinkList<T> current = this.next;
        while (--index >= 1) {
            if (current != null) {
                current = current.next;
            } else {
                break;
            }
        }
        insertPrior(current, elem);
    }

    /**
     * 指定节点前插
     */
    public void insertPrior(SingleLinkList<T> node, T elem) {
        if (node == null) {
            return;
        }
        SingleLinkList<T> newNode = new SingleLinkList<>();
        newNode.data = node.data;
        newNode.next = node.next;
        node.data = elem;
        node.next = newNode;
    }

    /**
     * 指定节点后插
     */
    public void insertNext(SingleLinkList<T> node, T elem) {
        if (node == null) {
            return;
        }
        SingleLinkList<T> newNode = new SingleLinkList<>();
        newNode.data = elem;
        if(node.next != null){
            newNode.next = node.next;
        }
        node.next = newNode;
    }

    /**
     * 头插
     */
    public void insertFirst(T[] elems) {
        for (T elem : elems) {
            insertNext(this, elem);
        }
    }

    /**
     * 尾插
     */
    public void insertLast(T[] elems) {
        SingleLinkList<T> current = this;
        while (true) {
            if (current.next == null) {
                for (T elem : elems) {
                    insertNext(current, elem);
                    current = current.next;
                }
                break;
            }
            current = current.next;
        }
    }

    /**
     * 按位查找
     */
    public SingleLinkList<T> locate(int index) {
        if (index < 1) {
            return null;
        }
        SingleLinkList<T> current = this.next;
        while (--index >= 1) {
            if (current.next != null) {
                current = current.next;
            } else {
                break;
            }
        }
        return current;
    }

    /**
     * 按值查找
     */
    public SingleLinkList<T> locate(T elem) {
        if (elem == null) {
            return null;
        }
        SingleLinkList<T> current = this.next;
        while (current != null) {
            if (current.data.equals(elem)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * 定位前序节点
     */
    public SingleLinkList<T> locatePrior(SingleLinkList<T> node) {
        if (node == null) {
            return null;
        }
        SingleLinkList<T> current = this;
        while (current.next != null) {
            if (current.next.equals(node)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * 按位删除
     */
    public void delete(int index) {
        SingleLinkList<T> node = this.locate(index);
        this.delete(node);
    }

    /**
     * 按值删除
     */
    public void delete(T elem) {
        SingleLinkList<T> node = this.locate(elem);
        this.delete(node);
    }

    /**
     * 按节点删除
     */
    public void delete(SingleLinkList<T> node) {
        if (node == null) {
            return;
        }
        if (node.next != null) {
            node.data = node.next.data;
            node.next = node.next.next;
        } else {
            SingleLinkList<T> prior = this.locatePrior(node);
            if (prior != null) {
                prior.next = null;
            }
        }
    }

    /**
     * 打印
     */
    public void print() {
        this.print(true);
    }

    /**
     * 打印
     */
    public void print(boolean isHeadNode) {
        if (this.isEmpty()) {
            System.out.println("单链表为空");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        SingleLinkList<T> current = isHeadNode ? this.next : this;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append(']');
        System.out.println("打印单链表数据元素：" + sb.toString());
    }

    public boolean isEmpty() {
        SingleLinkList<T> head = this;
        return head.next == null;
    }

    // endregion

    // region:扩展算法

    // endregion

    public static void main(String[] args) {
        SingleLinkList<Integer> sLinkList = new SingleLinkList<>();

        System.out.println("1、头插");
        sLinkList.insertFirst(new Integer[] {1, 2, 3, 4, 5});
        sLinkList.print();
        System.out.println("");

        System.out.println("2、尾插");
        sLinkList = new SingleLinkList<>();
        sLinkList.insertLast(new Integer[] {1, 2, 3, 4, 5});
        sLinkList.print();
        System.out.println("");

        System.out.println("3、按位序插入");
        sLinkList.insert(2, 3);
        sLinkList.print();
        System.out.println("");

        System.out.println("4、按位查找");
        SingleLinkList<Integer> node = sLinkList.locate(4);
        node.print(false);
        System.out.println("");

        System.out.println("5、按值查找");
        Integer elem = 4;
        node = sLinkList.locate(elem);
        node.print(false);
        System.out.println("");

        System.out.println("6、按位删除");
        sLinkList.delete(1);
        sLinkList.print();
        System.out.println("");

        System.out.println("7、按值删除");
        elem = 5;
        sLinkList.delete(elem);
        sLinkList.print();
        System.out.println("");

        System.out.println("8、按节点删除");
        sLinkList.delete(node);
        sLinkList.print();
        System.out.println("");
    }

}
