package _2_2_liner_list;

import java.lang.reflect.Array;

import common.Utils;
import javafx.util.Pair;

/**
 * 顺序表
 *
 * @author stone
 * @date 2021/04/10
 */
public class SequenceList<T> {

    // region:成员变量

    private int length;
    private int maxLength;
    private final Class<?> elemType;
    private T[] data;

    // endregion:成员变量

    // region:基础操作（创销增删改查）

    /**
     * 顺序表构造函数
     *
     * @param elemType 数据元素的类型
     */
    public SequenceList(Class<?> elemType) {
        this.length = 0;
        this.maxLength = 10;
        this.elemType = elemType == null ? String.class : elemType;
        this.data = (T[])Array.newInstance(this.elemType, this.maxLength);
        System.out.println("创建顺序表成功，最大长度为" + this.maxLength);
    }

    /**
     * 顺序表构造函数
     *
     * @param elemType 数据元素的类型
     */
    public SequenceList(Class<?> elemType, int maxLength) {
        this.length = 0;
        this.maxLength = maxLength;
        this.elemType = elemType == null ? String.class : elemType;
        this.data = (T[])Array.newInstance(this.elemType, this.maxLength);
        System.out.println("创建顺序表成功，最大长度为" + this.maxLength);
    }

    /**
     * 向顺序表中插入一个数据元素
     *
     * @param elem 数据元素
     */
    public void insert(T elem) {
        if (this.length >= this.maxLength) {
            this.maxLength = this.maxLength * 2;
            T[] temp = (T[])Array.newInstance(this.elemType, this.maxLength);
            for (int i = 0; i < this.length; i++) {
                temp[i] = this.data[i];
            }
            this.data = temp;
            System.out.println("顺序表自动扩容，最大长度为" + this.maxLength + "");
        }
        System.out.println("顺序表插入数据元素：[" + elem + "]");
        this.data[this.length++] = elem;
    }

    /**
     * 删除顺序表中指定位置的数据元素
     *
     * @param index 数据位置
     */
    public T delete(int index) {
        String checkMsg = checkDataPosition(index);
        if (checkMsg != null) {
            System.out.println(checkMsg + "，删除顺序表" + index + "位置数据元素失败");
            return null;
        }
        T elem = this.data[index - 1];
        for (int i = index; i < this.length; i++) {
            this.data[i - 1] = this.data[i];
        }
        this.length--;
        System.out.println("删除顺序表" + index + "位置数据元素：[" + elem + "]");
        return elem;
    }

    /**
     * 删除顺序表中指定范围的数据元素
     *
     * @param start 数据开始位置
     * @param end   数据结束位置
     */
    public void deleteByRange(int start, int end) {
        String checkMsg = checkDataPosition(start);
        if (checkMsg != null) {
            System.out.println(checkMsg + "，删除顺序表" + start + "位置数据元素失败");
            return;
        }
        String checkMsg2 = checkDataPosition(end);
        if (checkMsg != null) {
            System.out.println(checkMsg2 + "，删除顺序表" + end + "位置数据元素失败");
            return;
        }
        for (int i = start; i < this.length; i++) {
            this.data[i - 1] = this.data[i + end - start];
        }
        System.out.println("删除顺序表" + start + "到" + end + "位置数据元素成功");
        this.length = this.length - (end - start + 1);
    }

    /**
     * 获取顺序表中指定位置的数据元素
     *
     * @param index 数据位置
     * @return 数据元素
     */
    public T getElemValue(int index) {
        String checkMsg = checkDataPosition(index);
        if (checkMsg != null) {
            System.out.println(checkMsg + "，获取顺序表" + index + "位置数据元素失败");
            return null;
        }
        T elem = this.data[index - 1];
        System.out.println("获取顺序表" + index + "位置数据元素：[" + elem + "]");
        return elem;
    }

    /**
     * 在顺序表中找到值相等的第一个元素，返回其位置
     *
     * @param value 查找的值
     * @return 返回数据元素的位置，如果找不到则返回0
     */
    public int locateFirstElem(T value) {
        int index = 0;
        for (int i = 0; i < length; i++) {
            if (data[i].equals(value)) {
                index = i + 1;
                break;
            }
        }
        System.out.println("获取顺序表第一个：[" + value + "]，位置为：" + index);
        return index;
    }

    /**
     * 在顺序表中找到值相等的最后一个元素，返回其位置
     *
     * @param value 查找的值
     * @return 返回数据元素的位置，如果找不到则返回0
     */
    public int locateLastElem(T value) {
        int index = 0;
        for (int i = this.length - 1; i >= 0; i--) {
            if (data[i].equals(value)) {
                index = i + 1;
                break;
            }
        }
        System.out.println("获取顺序表最后一个：[" + value + "]，位置为：" + index);
        return index;
    }

    /**
     * 在控制台从前往后的顺序打印顺序表中每个数据元素的值
     */
    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < this.length; i++) {
            sb.append(this.data[i]);
            if (i == this.length - 1) {
                sb.append(']');
                System.out.println("打印顺序表数据元素：" + sb.toString());
                return;
            }
            sb.append(", ");
        }
    }

    private T[] getData() {
        return data;
    }

    private int getLength() {
        return this.length;
    }

    /**
     * 检查传入的数据位置，是否在顺序表中存在
     *
     * @param index 数据位置
     */
    private String checkDataPosition(int index) {
        if (index < 1) {
            return "数据位置不能小于1";
        }
        if (this.length < 1) {
            return "顺序表为空";
        }
        if (index > this.length) {
            return "数据位置不能大于顺序表的长度";
        }
        return null;
    }

    // endregion

    // region:扩展算法

    /**
     * 1. 从顺序表中删除具有最小值的元素（假设唯一）并由函数返回被删元素的值，空出来的位置由最后一个元素填补，若顺序表为空，则显示出错信息并退出运行
     *
     * @return 数据元素
     */
    public T deleteMinElem() {

        if (!this.elemType.equals(Integer.class)) {
            System.out.println("执行失败，此算法只支持Integer数据类型");
            return null;
        }
        if (this.length < 1) {
            System.out.println("执行失败，顺序表中数据元素为空");
            return null;
        }

        Pair min = null;
        if (this.length == 1) {
            min = new Pair(1, this.data[0]);
        } else {
            for (int i = 1; i < this.length; i++) {
                if (this.data[i] != null) {
                    Integer current = (Integer)this.data[i];
                    if (min == null) {
                        min = new Pair(i, this.data[i]);
                    } else {
                        if (current < (Integer)min.getValue()) {
                            min = new Pair(i, this.data[i]);
                        }
                    }
                }
            }
        }
        if (min != null) {
            return delete((Integer)min.getKey() + 1);
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

        Utils.reverseArray(this.data, 0, this.length - 1);
    }

    /**
     * 3. 对长度为n的顺序表L，编写一个时间复杂度为O(n)、空间复杂度为O(1)的算法，改算法删除线性表中所有值为x的数据元素
     */
    public void deleteElemByValue(T elem) {
        if (this.length < 1) {
            System.out.println("执行失败，顺序表中数据元素为空");
            return;
        }
        while (true) {
            int index = this.locateFirstElem(elem);
            if (index > 0) {
                this.delete(index);
            } else {
                break;
            }
        }
    }

    /**
     * 4. 从[有序]顺序表中删除其值在给定值s与t之间（要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行
     *
     * @param s 开始元素值
     * @param t 结束元素值
     */
    public void deleteElemByRangeValue(Integer s, Integer t) {
        if (!this.elemType.equals(Integer.class)) {
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
            int index = firstIndex == 0 ? this.locateFirstElem((T)i) : this.locateLastElem((T)i);
            if (index != 0) {
                if (firstIndex == 0) {
                    firstIndex = index;
                }
                lastIndex = index;
            }
        }
        if (firstIndex != 0) {
            if (firstIndex == lastIndex) {
                this.delete(firstIndex);
            } else {
                this.deleteByRange(firstIndex, lastIndex);
            }
        }
    }

    /**
     * 5. 从顺序表中删除其值在给定值s与t之间（包含s和t，要求s<t）的所有元素，若s或t不合理或顺序表为空，则显示出错信息并退出运行
     *
     * @param s 开始元素值
     * @param t 结束元素值
     */
    public void deleteElemByRangeValue2(Integer s, Integer t) {
        if (!this.elemType.equals(Integer.class)) {
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
            this.deleteElemByValue((T)i);
        }
    }

    /**
     * 6. 从[有序]顺序表中删除所有其值重复的元素，使表中所有元素的值均不同
     */
    public void deleteRepeatValue() {
        if (this.length < 1) {
            System.out.println("执行失败，顺序表中数据元素为空");
            return;
        }
        for (int i = 1; i <= this.length; i++) {
            T elem = this.getElemValue(i);
            while (true) {
                int index = this.locateLastElem(elem);
                if (i != index) {
                    this.delete(index);
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
        if (!this.elemType.equals(Integer.class) || !oldSl.elemType.equals(Integer.class)) {
            System.out.println("执行失败，此算法只支持Integer数据类型");
            return;
        }
        SequenceList newSl = new SequenceList(Integer.class, this.length + oldSl.length);
        int i = 1, j = 1;
        while (i <= this.length && j <= oldSl.length) {
            if ((Integer)this.getElemValue(i) < (Integer)oldSl.getElemValue(j)) {
                newSl.insert(this.getElemValue(i++));
            } else {
                newSl.insert(oldSl.getElemValue(j++));
            }
        }
        while (i <= this.length) {
            newSl.insert(this.getElemValue(i++));
        }
        while (j <= oldSl.length) {
            newSl.insert(oldSl.getElemValue(j++));
        }
        this.data = (T[])newSl.getData();
        this.length = newSl.getLength();
    }

    // endregion

}