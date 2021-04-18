package medium;

import java.util.Arrays;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _189_Solution_50 {

    // 解法一，嵌套循环
    // 执行耗时:107 ms,击败了20.67% 的Java用户
    // 内存消耗:37.8 MB,击败了93.81% 的Java用户
    public static void rotate1(int[] nums, int k) {
        k = k % nums.length;
        for (int i = 0; i < k; i++) {
            int previous_value = nums[nums.length - 1];
            for (int j = 0; j < nums.length; j++) {
                int temp = nums[j];
                nums[j] = previous_value;
                previous_value = temp;
            }
        }
    }

    // 解法二，使用额外数组，两次循环（使用了额外的空间，不满足题意）
    // 执行耗时:1 ms,击败了81.21% 的Java用户
    // 内存消耗:37.4 MB,击败了95.41% 的Java用户
    public static void rotate2(int[] nums, int k) {
        int temp_nums[] = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            temp_nums[(k + i) % nums.length] = nums[i];
        }
        for (int j = 0; j < nums.length; j++) {
            nums[j] = temp_nums[j];
        }
    }

    // 解法三，单次循环
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:37.9 MB,击败了86.62% 的Java用户
    public static void rotate3(int[] nums, int k) {
        for (int i = 0, j = 0; j < nums.length; i++) {
            int previous_index = i;
            int previous_value = nums[i];
            do {
                int next_index = (previous_index + k) % nums.length;
                int next_value = nums[next_index];
                nums[next_index] = previous_value;
                previous_index = next_index;
                previous_value = next_value;
                j++;
            } while (i != previous_index);
        }
    }

    // 解法四，数组倒置
    // 执行耗时:1 ms,击败了81.21% 的Java用户
    // 内存消耗:37.7 MB,击败了94.42% 的Java用户
    public static void rotate4(int[] nums, int k) {
        k = k % nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    public static void reverse(int nums[], int start, int end) {
        while (start < end) {
            nums[start] = nums[start] + nums[end];
            nums[end] = nums[start] - nums[end];
            nums[start] = nums[start] - nums[end];
            start++;
            end--;
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int k = 3;
        rotate4(nums, k);
        // reverse(nums, 5, 9);
        System.out.println(Arrays.toString(nums));
        // System.out.println((6-0)%7);

    }

}
