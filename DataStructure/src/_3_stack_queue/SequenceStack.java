package _3_stack_queue;

import java.lang.reflect.Array;

/**
 * 顺序栈
 *
 * @author stone
 * @date 2021/05/01
 */
public class SequenceStack<T> implements IStack<T> {

    private final T[] datas;
    private int length;
    private final int maxLength;
    private final Class<?> dataType;

    public SequenceStack() {
        this.length = 0;
        this.maxLength = 10;
        this.dataType = String.class;
        this.datas = (T[])Array.newInstance(this.dataType, this.maxLength);
    }

    public SequenceStack(Class<?> dataType) {
        this.length = 0;
        this.maxLength = 10;
        this.dataType = dataType;
        this.datas = (T[])Array.newInstance(this.dataType, this.maxLength);
    }

    @Override
    public void push(T data) {
        if (this.isFull()) {
            System.out.println("进栈失败，顺序栈已满");
            return;
        }
        this.datas[length++] = data;
    }

    @Override
    public T pop() {
        if (this.isEmpty()) {
            return null;
        }
        return this.datas[--length];
    }

    @Override
    public T top() {
        if (this.isEmpty()) {
            return null;
        }
        return this.datas[length - 1];
    }

    @Override
    public boolean isEmpty() {
        return this.length <= 0;
    }

    @Override
    public boolean isFull() {
        return this.length >= this.maxLength;
    }

    @Override
    public void print() {
        if (this.isEmpty()) {
            System.out.println("打印顺序栈元素：空");
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
        System.out.println("打印顺序栈元素：" + sb);
    }
}
