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
     * 判断线性表是否已满
     *
     * @return true为满，false为不满
     */
    boolean isFull();

    /**
     * 打印线性表中所有元素
     */
    void print();

    public static void main(String[] args) {
        ILinerList[] lls = new ILinerList[] {new StaticLinkList<>(), new SequenceList<>(Integer.class),
            new SingleLinkNode<>()};
        for (ILinerList ll : lls) {
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
            System.out.println("删除位序为10的元素");
            ll.print();
            System.out.println("删除位序为1的元素");
            ll.deleteByPosition(1);
            ll.print();
            System.out.println("删除位序为3的元素");
            ll.deleteByPosition(3);
            ll.print();
            System.out.println("删除值为6的元素");
            ll.deleteByData(6);
            ll.print();
            System.out.println("3号位序的元素是：" + ll.getDataByPosition(3));
            System.out.println("值为3的元素第一个位序是：" + ll.getFirstPositionByData(3));
            System.out.println("值为3的元素最后一个位序是：" + ll.getLastPositionByData(3));
            System.out.println("--------------------------");
        }
    }

}
