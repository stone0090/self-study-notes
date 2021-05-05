package _3_stack_queue;

/**
 * 队列的基本操作
 *
 * @author stone
 * @date 2021/05/02
 */
public interface IQueue<T> {

    /**
     * 入队
     *
     * @param data 元素值
     */
    void enqueue(T data);

    /**
     * 出队
     *
     * @return 队头元素
     */
    T dequeue();

    /**
     * 判空
     *
     * @return true为空
     */
    boolean isEmpty();

    /**
     * 判满
     *
     * @return true为满
     */
    boolean isFull();

    /**
     * 打印队列元素
     */
    void print();

    public static void main(String[] args) {
        IQueue[] queues = new IQueue[] {new SequenceQueue(), new LinkQueue(), new CircularQueue()};
        for (IQueue queue : queues) {
            queue.enqueue("aaa");
            queue.enqueue("bbb");
            queue.enqueue("ccc");
            queue.print();
            queue.enqueue("ddd");
            queue.enqueue("eee");
            queue.enqueue("fff");
            queue.print();
            System.out.println("弹出队头元素：" + queue.dequeue());
            System.out.println("弹出队头元素：" + queue.dequeue());
            System.out.println("弹出队头元素：" + queue.dequeue());
            queue.print();
            System.out.println("弹出队头元素：" + queue.dequeue());
            System.out.println("弹出队头元素：" + queue.dequeue());
            System.out.println("弹出队头元素：" + queue.dequeue());
            queue.print();
            System.out.println("--------------------------");
        }
    }

}
