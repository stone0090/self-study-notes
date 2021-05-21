package _2_liner_list;

import java.lang.reflect.Array;

import _7_sort.SortUtils;
import javafx.util.Pair;

/**
 * 顺序表
 *
 * @author stone
 * @date 2021/04/10
 */
public class SequenceList<T> implements ILinerList<T> {

    // region:成员变量

    private T[] datas;
    private int length;
    private final int maxLength;
    private final Class<?> dataType;

    // endregion:成员变量

    // region:基础操作（创销增删改查）

    /**
     * 顺序表构造函数
     *
     * @param dataType 数据元素的类型
     */
    public SequenceList(Class<?> dataType) {
        this.length = 0;
        this.maxLength = 10;
        this.dataType = dataType == null ? String.class : dataType;
        this.datas = (T[])Array.newInstance(this.dataType, this.maxLength);
    }

    /**
     * 顺序表构造函数
     *
     * @param dataType 数据元素的类型
     */
    public SequenceList(Class<?> dataType, int maxLength) {
        this.length = 0;
        this.maxLength = maxLength;
        this.dataType = dataType == null ? String.class : dataType;
        this.datas = (T[])Array.newInstance(this.dataType, this.maxLength);
    }

    @Override
    public void insertFirst(T data) {
        if (this.isFull()) {
            System.out.println("顺序表已满，头插失败");
            return;
        }
        if (!this.isEmpty()) {
            for (int i = length; i > 0; i--) {
                this.datas[i] = this.datas[i - 1];
            }
        }
        this.datas[0] = data;
        this.length++;
    }

    @Override
    public void insertLast(T data) {
        if (this.isFull()) {
            System.out.println("顺序表已满，尾插失败");
            return;
        }
        this.datas[this.length++] = data;
    }

    @Override
    public void deleteByData(T data) {
        if (this.isEmpty()) {
            return;
        }
        while (true) {
            int index = this.getFirstPositionByData(data);
            if (index > 0) {
                this.deleteByPosition(index);
            } else {
                break;
            }
        }
    }

    @Override
    public T deleteByPosition(int position) {
        if (this.isEmpty() || position < 1 || position > this.length) {
            return null;
        }
        T data = this.datas[position - 1];
        for (int i = position; i < this.length; i++) {
            this.datas[i - 1] = this.datas[i];
        }
        this.length--;
        return data;
    }

    @Override
    public T getDataByPosition(int position) {
        if (this.isEmpty() || position < 1 || position > this.length) {
            return null;
        }
        return this.datas[position - 1];
    }

    @Override
    public int getFirstPositionByData(T data) {
        int position = 0;
        for (int i = 0; i < length; i++) {
            if (this.datas[i].equals(data)) {
                position = i + 1;
                break;
            }
        }
        return position;
    }

    @Override
    public int getLastPositionByData(T data) {
        int position = 0;
        for (int i = this.length - 1; i >= 0; i--) {
            if (this.datas[i].equals(data)) {
                position = i + 1;
                break;
            }
        }
        return position;
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
            System.out.println("打印顺序表数据元素：空");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < this.length; i++) {
            sb.append(this.datas[i]);
            if (i == this.length - 1) {
                sb.append(']');
                System.out.println("打印顺序表数据元素：" + sb);
                return;
            }
            sb.append(", ");
        }
    }

    // endregion

    // region:扩展算法

    /**
     * 1. 从顺序表中删除具有最小值的元素（假设唯一）并由函数返回被删元素的值，空出来的位置由最后一个元素填补，若顺序表为空，则显示出错信息并退出运行
     *
     * @return 数据元素
     */
    public T deleteByMinData() {
        if (!this.dataType.equals(Integer.class)) {
            System.out.println("执行失败，此算法只支持Integer数据类型");
            return null;
        }
        if (this.length < 1) {
            System.out.println("执行失败，顺序表中数据元素为空");
            return null;
        }
        Pair min = null;
        if (this.length == 1) {
            min = new Pair(1, this.datas[0]);
        } else {
            for (int i = 1; i < this.length; i++) {
                if (this.datas[i] != null) {
                    Integer current = (Integer)this.datas[i];
                    if (min == null) {
                        min = new Pair(i, this.datas[i]);
                    } else {
                        if (current < (Integer)min.getValue()) {
                            min = new Pair(i, this.datas[i]);
                        }
                    }
                }
            }
        }
        if (min != null) {
            return deleteByPosition((Integer)min.getKey() + 1);
        } else {
            System.out.println("执行失败，顺序表中的数据全为null");
            return null;
        }
    }

    /**
     * 2. 设计一个高效算法，将顺序表L的所有元素逆置，要求算法的空间复杂度为O(1)
     */
    public void reverse() {
        if (this.length < 1) {
            System.out.println("执行失败，顺序表中数据元素为空");
            return;
        }
        SortUtils.reverseArray(this.datas, 0, this.length - 1);
    }

    /**
     * 4. 从[有序]顺序表中删除其值在给定值s与t之间（要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行
     *
     * @param s 开始元素值
     * @param t 结束元素值
     */
    public void deleteByValueRange(Integer s, Integer t) {
        if (!this.dataType.equals(Integer.class)) {
            System.out.println("执行失败，此算法只支持Integer数据类型");
            return;
        }
        if (this.length < 1) {
            System.out.println("执行失败，顺序表中数据元素为空");
            return;
        }
        if (s == null || t == null || s >= t) {
            System.out.println("执行失败，s和t不能为null，且要求 s<t");
            return;
        }
        System.out.println("开始删除[有序]顺序表中值为[" + s + "]到[" + t + "]之间的数据元素...");
        int firstIndex = 0;
        int lastIndex = 0;
        for (Integer i = s + 1; i < t; i++) {
            int index = firstIndex == 0 ? this.getFirstPositionByData((T)i) : this.getLastPositionByData((T)i);
            if (index != 0) {
                if (firstIndex == 0) {
                    firstIndex = index;
                }
                lastIndex = index;
            }
        }
        if (firstIndex != 0) {
            if (firstIndex == lastIndex) {
                this.deleteByPosition(firstIndex);
            } else {
                this.deleteByPositionRange(firstIndex, lastIndex);
            }
        }
    }

    /**
     * 删除顺序表中指定范围的数据元素
     *
     * @param start 数据开始位置
     * @param end   数据结束位置
     */
    private void deleteByPositionRange(int start, int end) {
        for (int i = start; i <= end; i++) {
            this.deleteByPosition(i);
        }
    }

    /**
     * 5. 从顺序表中删除其值在给定值s与t之间（包含s和t，要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行
     *
     * @param s 开始元素值
     * @param t 结束元素值
     */
    public void deleteByValueRange2(Integer s, Integer t) {
        if (!this.dataType.equals(Integer.class)) {
            System.out.println("执行失败，此算法只支持Integer数据类型");
            return;
        }
        if (this.length < 1) {
            System.out.println("执行失败，顺序表中数据元素为空");
            return;
        }
        if (s == null || t == null || s >= t) {
            System.out.println("执行失败，s和t不能为null，且要求 s<t");
            return;
        }
        System.out.println("开始删除顺序表中值为[" + s + "]到[" + t + "]之间的数据元素...");
        for (Integer i = s; i <= t; i++) {
            this.deleteByData((T)i);
        }
    }

    /**
     * 6. 从[有序]顺序表中删除所有其值重复的元素，使表中所有元素的值均不同
     */
    public void deleteByRepeatValue() {
        if (this.length < 1) {
            System.out.println("执行失败，顺序表中数据元素为空");
            return;
        }
        for (int i = 1; i <= this.length; i++) {
            T elem = this.getDataByPosition(i);
            while (true) {
                int index = this.getLastPositionByData(elem);
                if (i != index) {
                    this.deleteByPosition(index);
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 7. 将两个[有序]顺序表合并成一个新的有序顺序表，并有函数返回结果顺序表
     *
     * @param oldSl [有序]顺序表
     */
    public void merge(SequenceList<T> oldSl) {
        if (!this.dataType.equals(Integer.class) || !oldSl.dataType.equals(Integer.class)) {
            System.out.println("执行失败，此算法只支持Integer数据类型");
            return;
        }
        SequenceList newSl = new SequenceList(Integer.class, this.length + oldSl.length);
        int i = 1, j = 1;
        while (i <= this.length && j <= oldSl.length) {
            if ((Integer)this.getDataByPosition(i) < (Integer)oldSl.getDataByPosition(j)) {
                newSl.insertLast(this.getDataByPosition(i++));
            } else {
                newSl.insertLast(oldSl.getDataByPosition(j++));
            }
        }
        while (i <= this.length) {
            newSl.insertLast(this.getDataByPosition(i++));
        }
        while (j <= oldSl.length) {
            newSl.insertLast(oldSl.getDataByPosition(j++));
        }
        this.datas = (T[])newSl.datas;
        this.length = newSl.length;
    }

    // endregion

}