package liner.list;

/**
 * 第2章 线性表
 * 2.2 线性表的顺序表示
 * 2.2.3 本节试题精选-综合应用题
 *
 * @author stone
 * @date 2021/04/10
 */
public class SequenceListTest {

    public static void main(String[] args) {
        // 1. 从顺序表中删除具有最小值的元素（假设唯一）并由函数返回被删元素的值，空出来的位置由最后一个元素填补，若顺序表为空，则显示出错信息并退出运行
        deleteMinElemTest();
        // 2. 设计一个高效算法，将顺序表L的所有元素逆置，要求算法的空间复杂度为O(1)。
        reverseTest();
        // 3. 对长度为n的顺序表L，编写一个时间复杂度为O(n)、空间复杂度为O(1)的算法，改算法删除线性表中所有值为x的数据元素
        deleteElemByValueTest();
        // 4. 从[有序]顺序表中删除其值在给定值s与t之间（要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行
        deleteElemByRangeValueTest(3, 8);
        // 5. 从顺序表中删除其值在给定值s与t之间（包含s和t，要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行
        deleteElemByRangeValueTest2(3, 8);
        // 6. 从[有序]顺序表中删除所有其值重复的元素，使表中所有元素的值均不同
        deleteRepeatValueTest();
        // 7. 将两个[有序]顺序表合并成一个新的有序顺序表，并有函数返回结果顺序表
        mergeTwoSequenceListTest();
    }

    private static void deleteMinElemTest() {
        System.out.println("1. 从顺序表中删除具有最小值的元素（假设唯一）并由函数返回被删元素的值，空出来的位置由最后一个元素填补，若顺序表为空，则显示出错信息并退出运行");
        SequenceList sl = new SequenceList(Integer.class);
        sl.deleteMinElem();
        sl.insert(6);
        sl.insert(4);
        sl.insert(8);
        sl.insert(2);
        sl.insert(9);
        sl.print();
        sl.deleteMinElem();
        sl.print();
        System.out.println("");
    }

    private static void reverseTest() {
        System.out.println("2. 设计一个高效算法，将顺序表L的所有元素逆置，要求算法的空间复杂度为O(1)");
        SequenceList<String> sl = new SequenceList<>(String.class);
        sl.insert("aaa");
        sl.insert("bbb");
        sl.insert("ccc");
        sl.insert("ddd");
        sl.insert("eee");
        sl.insert("fff");
        sl.insert("ggg");
        sl.print();
        sl.reverse();
        sl.print();
        System.out.println("");
    }

    private static void deleteElemByValueTest() {
        System.out.println("3. 对长度为n的顺序表L，编写一个时间复杂度为O(n)、空间复杂度为O(1)的算法，改算法删除线性表中所有值为x的数据元素");
        SequenceList<String> sl = new SequenceList<>(String.class);
        sl.insert("aaa");
        sl.insert("bbb");
        sl.insert("ccc");
        sl.insert("aaa");
        sl.insert("ddd");
        sl.insert("eee");
        sl.insert("aaa");
        sl.insert("fff");
        sl.insert("aaa");
        sl.insert("ggg");
        sl.insert("aaa");
        sl.print();
        sl.deleteElemByValue("aaa");
        sl.print();
        System.out.println("");
    }

    private static void deleteElemByRangeValueTest(int start, int end) {
        System.out.println("4. 从[有序]顺序表中删除其值在给定值s与t之间（要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行");
        SequenceList<Integer> sl = new SequenceList(Integer.class);
        sl.insert(1);
        sl.insert(2);
        sl.insert(3);
        sl.insert(3);
        sl.insert(4);
        sl.insert(4);
        sl.insert(5);
        sl.insert(5);
        sl.insert(6);
        sl.insert(6);
        sl.insert(6);
        sl.insert(6);
        sl.insert(7);
        sl.insert(8);
        sl.insert(8);
        sl.print();
        sl.deleteElemByRangeValue(2, 6);
        sl.print();
        System.out.println("");
    }

    private static void deleteElemByRangeValueTest2(int start, int end) {
        System.out.println("5. 从顺序表中删除其值在给定值s与t之间（包含s和t，要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行");
        SequenceList<Integer> sl = new SequenceList(Integer.class);
        sl.deleteMinElem();
        sl.insert(6);
        sl.insert(4);
        sl.insert(8);
        sl.insert(2);
        sl.insert(3);
        sl.insert(4);
        sl.insert(5);
        sl.insert(6);
        sl.insert(7);
        sl.print();
        sl.deleteElemByRangeValue2(2, 6);
        sl.print();
        System.out.println("");
    }

    private static void deleteRepeatValueTest() {
        System.out.println("6. 从[有序]顺序表中删除所有其值重复的元素，使表中所有元素的值均不同");
        SequenceList<Integer> sl = new SequenceList(Integer.class);
        sl.insert(1);
        sl.insert(2);
        sl.insert(3);
        sl.insert(3);
        sl.insert(4);
        sl.insert(4);
        sl.insert(5);
        sl.insert(5);
        sl.insert(6);
        sl.insert(6);
        sl.insert(6);
        sl.insert(6);
        sl.insert(7);
        sl.insert(8);
        sl.insert(8);
        sl.print();
        sl.deleteRepeatValue();
        sl.print();
        System.out.println("");
    }

    private static void mergeTwoSequenceListTest() {
        System.out.println("7. 将两个[有序]顺序表合并成一个新的有序顺序表，并有函数返回结果顺序表");
        SequenceList<Integer> sl = new SequenceList(Integer.class);
        sl.insert(2);
        sl.insert(4);
        sl.insert(5);
        sl.insert(7);
        sl.print();
        SequenceList<Integer> sl2 = new SequenceList(Integer.class);
        sl2.insert(1);
        sl2.insert(3);
        sl2.insert(8);
        sl2.insert(9);
        sl2.print();
        sl.merge(sl2);
        sl.print();
        System.out.println("");
    }
}
