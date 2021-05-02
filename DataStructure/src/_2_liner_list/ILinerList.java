package _2_liner_list;

/**
 * 线性表运算接口
 * 头指针对外位置从1开始
 *
 * @author stone
 * @date 2021/05/01
 */
public interface ILinerList<T> {

    /**
     * 头插法
     */
    void insertFirst(T data);

    /**
     * 尾插法
     */
    void insertLast(T data);

    /**
     * 删除指定值的节点
     *
     * @param data 节点值
     */
    void deleteByData(T data);

    /**
     * 删除指定位序的节点
     *
     * @param position 节点位序
     * @return 节点值
     */
    T deleteByPosition(int position);

    /**
     * 获取指定位序的节点
     *
     * @param position 节点位序
     * @return 节点值
     */
    T getDataByPosition(int position);

    /**
     * 在线性表中找到值相等的第一个元素，返回其位序
     *
     * @param data 节点值
     * @return 返回数据元素的位置，如果找不到则返回0
     */
    int getFirstPositionByData(T data);

    /**
     * 在线性表中找到值相等的最后一个元素，返回其位序
     *
     * @param data 节点值
     * @return 返回数据元素的位置，如果找不到则返回0
     */
    int getLastPositionByData(T data);

    /**
     * 判断线性表是否为空
     *
     * @return true为空，false不为空
     */
    boolean isEmpty();

    /**
     * 打印线性表中所有元素
     */
    void print();

    public static void main(String[] args) {
        ILinerList ll = new StaticLinkList<>();
        //ILinerList ll = new SequenceList(Integer.class);
        ll.insertFirst(4);
        ll.insertFirst(3);
        ll.insertFirst(3);
        ll.insertFirst(2);
        ll.insertFirst(1);
        ll.print();
        ll.insertLast(6);
        ll.insertLast(7);
        ll.insertLast(3);
        ll.insertLast(8);
        ll.insertLast(9);
        ll.insertLast(10);
        ll.print();
        ll.deleteByPosition(10);
        ll.print();
        ll.deleteByPosition(1);
        ll.print();
        ll.deleteByPosition(3);
        ll.print();
        ll.deleteByData(6);
        ll.print();
        System.out.println("3号位序的元素是："+ll.getDataByPosition(3));
        System.out.println("值为3的元素第一个位序是："+ll.getFirstPositionByData(3));
        System.out.println("值为3的元素最后一个位序是："+ll.getLastPositionByData(3));
        //result：
        //打印静态链表元素：[1, 2, 3, 3, 4]
        //静态链表已满，无法插入更多的数据
        //打印静态链表元素：[1, 2, 3, 3, 4, 6, 7, 3, 8, 9]
        //打印静态链表元素：[1, 2, 3, 3, 4, 6, 7, 3, 8]
        //打印静态链表元素：[2, 3, 3, 4, 6, 7, 3, 8]
        //打印静态链表元素：[2, 3, 4, 6, 7, 3, 8]
        //打印静态链表元素：[2, 3, 4, 7, 3, 8]
        //3号位序的元素是：4
        //值为3的元素第一个位序是：2
        //值为3的元素最后一个位序是：5
    }

}
