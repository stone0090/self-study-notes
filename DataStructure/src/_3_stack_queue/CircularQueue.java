package _3_stack_queue;

import java.lang.reflect.Array;

/**
 * 循环队列
 *
 * @author stone
 * @date 2021/05/01
 */
public class CircularQueue<T> implements IQueue<T> {

    private T[] datas;
    private int length;
    private int head;
    private final int maxLength;
    private final Class<?> dataType;

    public CircularQueue() {
        this.length = 0;
        this.maxLength = 10;
        this.head = 0;
        this.dataType = String.class;
        this.datas = (T[])Array.newInstance(this.dataType, this.maxLength);
    }

    @Override
    public void enqueue(T data) {
        if (this.isFull()) {
            System.out.println("入队失败，循环队列已满");
            return;
        }
        this.datas[this.getRear() + 1] = data;
        this.length++;
    }

    @Override
    public T dequeue() {
        if (this.isEmpty()) {
            return null;
        }
        T result = this.datas[this.head++];
        this.length--;
        return result;
    }

    @Override
    public boolean isEmpty() {
        return this.length == 0;
    }

    @Override
    public boolean isFull() {
        return this.length >= this.maxLength;
    }

    @Override
    public void print() {
        if (this.isEmpty()) {
            System.out.println("打印循环队列元素：空");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = this.head; i < this.length + this.head; i++) {
            if (i > this.maxLength) {
                i = i % this.maxLength;
            }
            sb.append(this.datas[i]).append('(').append(i).append(')');
            if (i == this.getRear()) {
                sb.append(']');
            } else {
                sb.append(", ");
            }
        }
        System.out.println("打印循环队列元素：" + sb);
    }

    /**
     * 获取队尾数组下标
     *
     * @return 数组下标
     */
    private int getRear() {
        int rear = this.head + this.length - 1;
        if (rear > this.maxLength) {
            rear = rear % this.maxLength;
        }
        return rear;
    }

}
