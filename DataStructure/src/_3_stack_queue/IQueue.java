package _3_stack_queue;

/**
 * 队列的基本操作
 *
 * @author stone
 * @date 2021/05/02
 */
public interface IQueue<T> {

    /**
     * 进队
     *
     * @param data 元素值
     */
    void push(T data);

    /**
     * 出队
     *
     * @return 队头元素
     */
    T pop();

    /**
     * 获取队头元素值
     *
     * @return 队头元素
     */
    T top();

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
            queue.push("aaa");
            queue.push("bbb");
            queue.push("ccc");
            queue.print();
            queue.push("ddd");
            queue.push("eee");
            queue.push("fff");
            queue.print();
            System.out.println("查询队头元素：" + queue.top());
            System.out.println("弹出队头元素：" + queue.pop());
            System.out.println("弹出队头元素：" + queue.pop());
            System.out.println("弹出队头元素：" + queue.pop());
            queue.print();
            System.out.println("弹出队头元素：" + queue.pop());
            System.out.println("弹出队头元素：" + queue.pop());
            System.out.println("弹出队头元素：" + queue.pop());
            queue.print();
            System.out.println("--------------------------");
        }
    }

}
