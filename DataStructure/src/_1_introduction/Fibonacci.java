package _1_introduction;

/**
 * 第1章 绪论
 * 思维拓展
 *
 * 斐波那契数列（Fibonacci sequence），又称黄金分割数列，
 * 因数学家莱昂纳多·斐波那契（Leonardoda Fibonacci）以兔子繁殖为例子而引入，故又称为“兔子数列”，
 * 指的是这样一个数列：0、1、1、2、3、5、8、13、21、34、……
 * 在数学上，斐波那契数列以如下被以递推的方法定义：F(0)=0，F(1)=1, F(n)=F(n - 1)+F(n - 2)（n ≥ 2，n ∈ N*）
 *
 * @author stone
 * @date 2021/04/10
 */
public class Fibonacci {

    public static void main(String[] args) {
        System.out.println(loop(9));
        System.out.println(recursive(9));
    }

    /**
     * 循环
     */
    public static int loop(int n) {
        int first = 0;
        int second = 1;
        if (n < 0) {
            throw new IllegalArgumentException("n必须大于等于0");
        } else if (n == 0) {
            return first;
        } else if (n == 1) {
            return second;
        } else {
            int current = 0;
            for (int i = 2; i <= n; i++) {
                current = first + second;
                first = second;
                second = current;
            }
            return current;
        }
    }

    /**
     * 递归
     */
    public static int recursive(int n) {
        int first = 0;
        int second = 1;
        if (n < 0) {
            throw new IllegalArgumentException("n必须大于等于0");
        } else if (n == 0) {
            return first;
        } else if (n == 1) {
            return second;
        } else {
            return recursive(n - 1) + recursive(n - 2);
        }
    }

}

