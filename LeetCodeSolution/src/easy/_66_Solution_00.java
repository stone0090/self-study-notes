package easy;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _66_Solution_00 {

    // 我的思考：转成数字，再转成数组，然而int越界（强行犯傻）
    public static int[] plusOne(int[] digits) {
        int num = arrayToNum(digits);
        return numToArray(num + 1);
    }

    public static int arrayToNum(int[] digits) {
        int num = 0;
        for (int i = 0; i < digits.length; i++) {
            num += digits[i] * Math.pow(10, digits.length - i - 1);
        }
        return num;
    }

    public static int[] numToArray(int num) {
        Deque<Integer> deque = new ArrayDeque<>();
        while (num >= 1) {
            deque.push(num % 10);
            num = num / 10;
        }
        int count = 0;
        int[] nums = new int[deque.size()];
        while (deque.peek() != null) {
            nums[count++] = deque.pop();
        }
        return nums;
    }

    // 我的代码：3种情况，分别判断（恶心至极）
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:35.1 MB,击败了59.39% 的Java用户
    public static int[] plusOne1(int[] digits) {
        int nine_count = 0;
        for (int i = 0; i < digits.length; i++) {
            if (digits[i] == 9) {nine_count++;}
        }
        int[] result;
        if (nine_count == digits.length) {
            result = new int[nine_count + 1];
            result[0] = 1;
            for (int i = 1; i < nine_count + 1; i++) {
                result[i] = 0;
            }
        } else if (nine_count == 0) {
            digits[digits.length - 1] = digits[digits.length - 1] + 1;
            result = digits;
        } else {
            digits[digits.length - 1] = digits[digits.length - 1] + 1;
            boolean next_add = digits[digits.length - 1] == 10;
            if (next_add) {
                digits[digits.length - 1] = 0;
            }
            for (int i = digits.length - 2; i >= 0; i--) {
                if (next_add) {
                    digits[i] = digits[i] + 1;
                }
                next_add = digits[i] == 10;
                if (next_add) {
                    digits[i] = 0;
                }
            }
            result = digits;
        }
        return result;
    }

    // 官方题解：优雅的代码
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:35.1 MB,击败了53.73% 的Java用户
    public static int[] plusOne2(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            digits[i]++;
            digits[i] = digits[i] % 10;
            if (digits[i] != 0) { return digits; }
        }
        int[] nums = new int[digits.length + 1];
        nums[0] = 1;
        return nums;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {9};
        System.out.println(Arrays.toString(plusOne2(nums)));
    }

}
