package _2_liner_list;

import java.lang.reflect.Array;

/**
 * 静态链表
 *
 * @author stone
 * @date 2021/04/15
 */
public class StaticLinkList<T> implements ILinerList<T> {

    private StaticLinkNode<T>[] nodes;
    private int head;
    private int last;
    private int length;
    private final int maxLength;

    public StaticLinkList() {
        this.head = -1;
        this.last = -1;
        this.length = 0;
        this.maxLength = 10;
        this.nodes = (StaticLinkNode<T>[])Array.newInstance(StaticLinkNode.class, this.maxLength);
    }

    @Override
    public void insertFirst(T data) {
        if (this.isEmpty()) {
            this.nodes[0] = new StaticLinkNode<>(data, -1);
            this.head = 0;
            this.last = 0;
            this.length++;
        } else {
            if (this.length >= this.maxLength) {
                System.out.println("静态链表已满，无法插入更多的数据");
                return;
            }
            for (int i = 0; i < this.maxLength; i++) {
                StaticLinkNode<T> node = this.nodes[i];
                if (node == null) {
                    this.nodes[i] = new StaticLinkNode<>(data, head);
                    this.head = i;
                    this.length++;
                    return;
                }
            }
        }
    }

    @Override
    public void insertLast(T data) {
        if (this.isEmpty()) {
            this.nodes[0] = new StaticLinkNode<>(data, -1);
            this.head = 0;
            this.last = 0;
            this.length++;
        } else {
            if (this.length >= this.maxLength) {
                System.out.println("静态链表已满，无法插入更多的数据");
                return;
            }
            for (int i = 0; i < maxLength; i++) {
                StaticLinkNode<T> node = this.nodes[i];
                if (node == null) {
                    this.nodes[i] = new StaticLinkNode<>(data, -1);
                    this.nodes[this.last].setNext(i);
                    this.last = i;
                    this.length++;
                    return;
                }
            }
        }
    }

    @Override
    public void deleteByData(T data) {
        if (!this.isEmpty()) {
            int position = this.getFirstPositionByData(data);
            this.deleteByPosition(position);
        }
    }

    @Override
    public T deleteByPosition(int position) {
        if (!this.isEmpty()) {
            StaticLinkNode<T> head = this.nodes[this.head];
            if (position == 1) {
                this.nodes[this.head] = null;
                this.head = head.getNext();
                this.length--;
            } else {
                int positionCount = 2;
                StaticLinkNode<T> current = head;
                StaticLinkNode<T> next = this.nodes[current.getNext()];
                while (next != null) {
                    if (positionCount == position) {
                        current.setNext(next.getNext());
                        if (current.getNext() == this.last) {
                            this.last = current.getNext();
                        }
                        this.length--;
                        return next.getData();
                    }
                    positionCount++;
                    current = next;
                    if (current.getNext() != -1) {
                        next = this.nodes[current.getNext()];
                    } else {
                        next = null;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public T getDataByPosition(int position) {
        if (this.isEmpty()) {
            return null;
        }
        if (this.length < position) {
            return null;
        }
        StaticLinkNode<T> head = this.nodes[this.head];
        if (position == 1) {
            return head.getData();
        } else {
            int positionCount = 2;
            StaticLinkNode<T> current = head;
            StaticLinkNode<T> next = this.nodes[current.getNext()];
            while (next != null) {
                if (positionCount == position) {
                    return next.getData();
                }
                positionCount++;
                current = next;
                if (current.getNext() != -1) {
                    next = this.nodes[current.getNext()];
                } else {
                    next = null;
                }
            }
        }
        return null;
    }

    @Override
    public int getFirstPositionByData(T data) {
        if (this.isEmpty()) {
            return 0;
        }
        int positionCount = 1;
        StaticLinkNode<T> head = this.nodes[this.head];
        if (head.getData().equals(data)) {
            return positionCount;
        } else {
            positionCount++;
            StaticLinkNode<T> current = head;
            StaticLinkNode<T> next = this.nodes[current.getNext()];
            while (next != null) {
                if (next.getData().equals(data)) {
                    return positionCount;
                }
                positionCount++;
                current = next;
                if (current.getNext() != -1) {
                    next = this.nodes[current.getNext()];
                } else {
                    next = null;
                }
            }
        }
        return 0;
    }

    @Override
    public int getLastPositionByData(T data) {
        if (this.isEmpty()) {
            return 0;
        }
        int lastPosition = 0;
        int positionCount = 1;
        StaticLinkNode<T> head = this.nodes[this.head];
        if (head.getData().equals(data)) {
            lastPosition = positionCount;
        } else {
            positionCount++;
            StaticLinkNode<T> current = head;
            StaticLinkNode<T> next = this.nodes[current.getNext()];
            while (next != null) {
                if (next.getData().equals(data)) {
                    lastPosition = positionCount;
                }
                positionCount++;
                current = next;
                if (current.getNext() != -1) {
                    next = this.nodes[current.getNext()];
                } else {
                    next = null;
                }
            }
        }
        return lastPosition;
    }

    @Override
    public boolean isEmpty() {
        return this.head == -1;
    }

    @Override
    public void print() {
        if (this.isEmpty()) {
            System.out.println("静态链表为空");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        StaticLinkNode<T> current = this.nodes[this.head];
        sb.append(current.getData());
        StaticLinkNode<T> next = this.nodes[current.getNext()];
        if (current.getNext() == this.last) {
            sb.append(']');
        } else {
            sb.append(", ");
        }
        while (next != null) {
            sb.append(next.getData());
            if (next.getNext() != -1) {
                sb.append(", ");
                next = this.nodes[next.getNext()];
            } else {
                sb.append(']');
                next = null;
            }
        }
        System.out.println("打印静态链表元素：" + sb);
    }

    public static class StaticLinkNode<T> {
        private T data;
        private int next;

        public StaticLinkNode(T data, int next) {
            this.data = data;
            this.next = next;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public int getNext() {
            return next;
        }

        public void setNext(int next) {
            this.next = next;
        }
    }


}
