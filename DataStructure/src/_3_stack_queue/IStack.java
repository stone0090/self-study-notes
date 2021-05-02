package _3_stack_queue;

/**
 * 栈的基本操作
 *
 * @author stone
 * @date 2021/05/02
 */
public interface IStack<T> {

    /**
     * 进栈
     *
     * @param data 元素值
     */
    void push(T data);

    /**
     * 出栈
     *
     * @return 栈顶元素
     */
    T pop();

    /**
     * 获取栈顶元素值
     *
     * @return 栈顶元素
     */
    T top();

    /**
     * 判空
     *
     * @return true为空栈
     */
    boolean isEmpty();

    /**
     * 判满
     *
     * @return true为满栈
     */
    boolean isFull();

    /**
     * 打印栈元素
     */
    void print();

    public static void main(String[] args) {
        IStack[] stacks = new IStack[] {new SequenceStack<String>(), new LinkStack<String>()};
        for (IStack stack : stacks) {
            stack.push("aaa");
            stack.push("bbb");
            stack.push("ccc");
            stack.print();
            stack.push("ddd");
            stack.push("eee");
            stack.push("fff");
            stack.print();
            System.out.println("查询栈顶元素：" + stack.top());
            System.out.println("弹出栈顶元素：" + stack.pop());
            System.out.println("弹出栈顶元素：" + stack.pop());
            System.out.println("弹出栈顶元素：" + stack.pop());
            stack.print();
            System.out.println("弹出栈顶元素：" + stack.pop());
            System.out.println("弹出栈顶元素：" + stack.pop());
            System.out.println("弹出栈顶元素：" + stack.pop());
            stack.print();
            System.out.println("--------------------------");
        }
    }
}
