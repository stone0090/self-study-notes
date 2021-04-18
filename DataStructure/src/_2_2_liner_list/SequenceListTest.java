package _2_2_liner_list;

import java.util.Arrays;

import common.Utils;

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
        deleteMinElem();

        // 2. 设计一个高效算法，将顺序表L的所有元素逆置，要求算法的空间复杂度为O(1)。
        reverse();

        // 3. 对长度为n的顺序表L，编写一个时间复杂度为O(n)、空间复杂度为O(1)的算法，改算法删除线性表中所有值为x的数据元素
        deleteElemByValue();

        // 4. 从[有序]顺序表中删除其值在给定值s与t之间（要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行
        deleteElemByRangeValueTest(3, 8);

        // 5. 从顺序表中删除其值在给定值s与t之间（包含s和t，要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行
        deleteElemByRangeValueTest2(3, 8);

        // 6. 从[有序]顺序表中删除所有其值重复的元素，使表中所有元素的值均不同
        deleteRepeatValue();

        // 7. 将两个[有序]顺序表合并成一个新的有序顺序表，并有函数返回结果顺序表
        mergeTwoSequenceList();

        // 8. 将数组中两个顺序表的位置互换
        swapTwoSequenceList();

        // 9. 有序顺序表，用最少时间在表中查找数据为x的元素，若找到，则将其与后继元素位置交换，若找不到，则将其插入表中并是表中的元素仍递增有序
        // 9. 解题思路，本质是有序查找，用二分查找时间复杂度为O(log2n)效率较高

        // 10. 将队列数据元素的循环左移p(0<p<n)个位置
        // 10. 解题思路一：左移可以看做将数组中两个顺序表的位置互换，同第8题思路一
        // 10. 解题思路二：左移可以看做将数组中两个顺序表的位置互换，同第8题思路二
        // 10. 解题思路三：假设从0位置开始，左移p之后的位置是n-p，用一个临时变量记住n-p的值，重复左移操作直至起点，时间复杂度O(n)，空间复杂度O(1)，

        // 11. 有两个等长升序序列A和B，找出两个序列的A和B的中位数。解题思路参考课本
        // 11. 思路一：合并2个有序数组，然后再找中位数，时间复杂度O(n)，空间复杂度O(n)，
        // 11. 思路二：参考王道教材，时间复杂度O(log2n)，空间复杂度O(1)，
        findMedian();

        // 12. 假设A中的n个元素保存在一个以为数组里，找出A中的主元素，若存在主元素则输出主元素，否则输出-1
        findMain();

    }

    private static void deleteMinElem() {
        System.out.println("1. 从顺序表中删除具有最小值的元素（假设唯一）并由函数返回被删元素的值，空出来的位置由最后一个元素填补，若顺序表为空，则显示出错信息并退出运行");
        SequenceList sl = new SequenceList(Integer.class);
        sl.deleteMinElem();
        sl.insert(6);
        sl.insert(2);
        sl.insert(9);
        sl.print();
        sl.deleteMinElem();
        sl.print();
        System.out.println("");
    }

    private static void reverse() {
        System.out.println("2. 设计一个高效算法，将顺序表L的所有元素逆置，要求算法的空间复杂度为O(1)");
        SequenceList<String> sl = new SequenceList<>(String.class);
        sl.insert("aaa");
        sl.insert("bbb");
        sl.insert("ccc");
        sl.insert("ddd");
        sl.insert("eee");
        sl.print();
        sl.reverse();
        sl.print();
        System.out.println("");
    }

    private static void deleteElemByValue() {
        System.out.println("3. 对长度为n的顺序表L，编写一个时间复杂度为O(n)、空间复杂度为O(1)的算法，该算法删除线性表中所有值为x的数据元素");
        SequenceList<String> sl = new SequenceList<>(String.class);
        sl.insert("aaa");
        sl.insert("bbb");
        sl.insert("aaa");
        sl.insert("ddd");
        sl.insert("aaa");
        sl.insert("fff");
        sl.print();
        sl.deleteElemByValue("aaa");
        sl.print();
        System.out.println("");
    }

    private static void deleteElemByRangeValueTest(int start, int end) {
        System.out.println("4. 从[有序]顺序表中删除其值在给定值s与t之间（要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行");
        SequenceList<Integer> sl = new SequenceList(Integer.class);
        sl.deleteElemByRangeValue(2, 5);
        sl.insert(2);
        sl.insert(3);
        sl.insert(4);
        sl.insert(5);
        sl.print();
        sl.deleteElemByRangeValue(5, 4);
        sl.deleteElemByRangeValue(2, 5);
        sl.print();
        System.out.println("");
    }

    private static void deleteElemByRangeValueTest2(int start, int end) {
        System.out.println("5. 从顺序表中删除其值在给定值s与t之间（包含s和t，要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行");
        SequenceList<Integer> sl = new SequenceList(Integer.class);
        sl.insert(5);
        sl.insert(2);
        sl.insert(3);
        sl.insert(4);
        sl.insert(1);
        sl.print();
        sl.deleteElemByRangeValue2(4, 2);
        sl.deleteElemByRangeValue2(2, 4);
        sl.print();
        System.out.println("");
    }

    private static void deleteRepeatValue() {
        System.out.println("6. 从[有序]顺序表中删除所有其值重复的元素，使表中所有元素的值均不同");
        SequenceList<Integer> sl = new SequenceList(Integer.class);
        sl.insert(1);
        sl.insert(2);
        sl.insert(2);
        sl.insert(3);
        sl.insert(3);
        sl.insert(3);
        sl.print();
        sl.deleteRepeatValue();
        sl.print();
        System.out.println("");
    }

    private static void mergeTwoSequenceList() {
        System.out.println("7. 将两个[有序]顺序表合并成一个新的有序顺序表，并有函数返回结果顺序表");
        SequenceList<Integer> sl = new SequenceList(Integer.class);
        sl.insert(2);
        sl.insert(4);
        sl.insert(5);
        sl.print();
        SequenceList<Integer> sl2 = new SequenceList(Integer.class);
        sl2.insert(1);
        sl2.insert(3);
        sl2.insert(8);
        sl2.print();
        sl.merge(sl2);
        sl.print();
        System.out.println("");
    }

    private static void swapTwoSequenceList() {
        System.out.println("8. 将数组中两个顺序表的位置互换");
        int a_start = 0;
        int a_end = 2;
        int b_start = 3;
        int b_end = 5;
        Object[] sl = new Object[] {5, 6, 7, 20, 30, 40};

        System.out.println("思路一，空间换时间，新定义一个顺序表，循环copy，时间复杂度O(n)，空间复杂度O(n)");
        System.out.println(Arrays.toString(sl));
        Object[] newSl = new Object[sl.length];
        for (int i = 0; i < sl.length; i++) {
            if (i < b_start) {
                newSl[i] = sl[i + b_start];
            } else {
                newSl[i] = sl[i - b_start];
            }
        }
        System.out.println(Arrays.toString(newSl));

        System.out.println("思路二，分别反转线性表a和b，再次反转整个线性表，时间复杂度O(n)，空间复杂度O(1)");
        System.out.println(Arrays.toString(sl));
        Utils.reverseArray(sl, a_start, a_end);
        Utils.reverseArray(sl, b_start, b_end);
        Utils.reverseArray(sl, a_start, b_end);
        System.out.println(Arrays.toString(sl));
        System.out.println("");
    }

    private static void findMedian() {
        System.out.println("11. 有两个等长升序序列A和B，找出两个序列的A和B的中位数。");
        int[] arr1 = {5, 8, 13, 16, 20};
        int[] arr2 = {6, 19, 30, 40, 50};
        System.out.println("数组A：" + Arrays.toString(arr1));
        System.out.println("数组B：" + Arrays.toString(arr2));
        int s1 = 0;
        int s2 = 0;
        int d1 = arr1.length - 1;
        int d2 = arr2.length - 1;
        int m1;
        int m2;
        int result;

        while (s1 != d1 || s2 != d2) {
            m1 = (s1 + d1) / 2;
            m2 = (s2 + d2) / 2;
            if (arr1[m1] == arr2[m2]) {
                result = arr1[m1];
                System.out.println("两个序列中位数为：" + result);
                System.out.println("");
                return;
            } else if (arr1[m1] < arr2[m2]) {
                if ((s1 + d1) % 2 == 0) {
                    s1 = m1;
                    d2 = m2;
                } else {
                    s1 = m1 + 1;
                    d2 = m2;
                }
            } else {
                if ((s2 + d2) % 2 == 0) {
                    d1 = m1;
                    s2 = m2;
                } else {
                    d1 = m1;
                    s2 = m2 + 1;
                }
            }
        }
        result = arr1[s1] < arr2[s2] ? arr1[s1] : arr2[s2];
        System.out.println("两个序列中位数为：" + result);
        System.out.println("");
    }

    private static void findMain() {
        System.out.println("12. 假设A中的n个元素保存在一个以为数组里，找出A中的主元素，若存在主元素则输出主元素，否则输出-1");
        int[] arr = {5, 8, 5, 5, 4, 4, 3, 5, 2, 1, 7, 5, 5, 5, 5, 5, 4, 5, 9, 20};
        System.out.println("数组A：" + Arrays.toString(arr));
        int currentValue = arr[0];
        int currentCount = 1;
        int totalCount = 0;
        for (int i = 1; i < arr.length - 1; i++) {
            if (arr[i] == currentValue) {
                currentCount++;
            } else {
                if (currentCount > 0) {
                    currentCount--;
                } else {
                    currentValue = arr[i];
                    currentCount = 1;
                }
            }
        }
        if (currentCount > 0) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == currentValue) {
                    totalCount++;
                }
            }
        }
        if (totalCount >= arr.length / 2) {
            System.out.println("数组A中的主元素为：" + currentValue);
        } else {
            System.out.println("数组A中的不存在主元素：-1");
        }

    }

}
