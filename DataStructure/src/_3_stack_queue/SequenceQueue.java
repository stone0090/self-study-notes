package _3_stack_queue;

import java.lang.reflect.Array;

/**
 * 顺序队列
 *
 * @author stone
 * @date 2021/05/01
 */
public class SequenceQueue<T> implements IQueue<T> {

    private T[] datas;
    private int length;
    private final int maxLength;
    private final Class<?> dataType;

    public SequenceQueue() {
        this.length = 0;
        this.maxLength = 10;
        this.dataType = String.class;
        this.datas = (T[])Array.newInstance(this.dataType, this.maxLength);
    }

    @Override
    public void enqueue(T data) {
        if (this.isFull()) {
            System.out.println("顺序队列已满，入队失败");
            return;
        }
        this.datas[this.length++] = data;
    }

    @Override
    public T dequeue() {
        if (this.isEmpty()) {
            return null;
        }
        T result = this.datas[0];
        for (int i = 1; i < this.length; i++) {
            this.datas[i - 1] = this.datas[i];
            if (i == this.length - 1) {
                this.datas[i] = null;
            }
        }
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
            System.out.println("打印顺序队列元素：空");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < this.length; i++) {
            sb.append(this.datas[i]);
            if (i == this.length - 1) {
                sb.append(']');
            } else {
                sb.append(", ");
            }
        }
        System.out.println("打印顺序队列元素：" + sb);
    }
}
