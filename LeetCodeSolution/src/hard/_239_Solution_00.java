package hard;

/**
 * @author stone
 * @date 2020/02/01
 */
public class _239_Solution_00 {

    // 解题一：暴力破解，两层循环
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0 || k == 0) return nums;
        int[] result = new int[nums.length - k + 1];
        for (int i = 0; i <= nums.length - k; i++) {
            for (int j = i; j < i + k; j++) {
                result[i] = (j == i) ? nums[j] : Math.max(result[i], nums[j]);
            }
        }
        return result;
    }
}


