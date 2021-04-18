package easy;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _283_Solution_40 {

    // 解法一：双循环，冒泡排序
    // 执行耗时:113 ms,击败了5.10% 的Java用户
    // 内存消耗:37.4 MB,击败了86.49% 的Java用户
    public void moveZeroes1(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length - 1; j++) {
                if (nums[j] == 0) {
                    swap(nums, j, j + 1);
                }
            }
        }
    }

    // 解法二：快慢指针
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:36.4 MB,击败了98.69% 的Java用户
    public void moveZeroes2(int[] nums) {
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != 0) {
                nums[i++] = nums[j];
            }
        }
        for (int k = i; k < nums.length; k++) {
            nums[k] = 0;
        }
    }

    // 解法三：快慢指针（更优解）
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:37.3 MB,击败了90.96% 的Java用户
    public void moveZeroes3(int[] nums) {
        for (int i = 0, j = 0; j < nums.length; j++) {
            if (nums[j] != 0) {
                swap(nums, i++, j);
            }
        }
    }

    private void swap(int[] nums, int src, int tar) {
        if (src == tar) return;
        nums[src] = nums[src] + nums[tar];
        nums[tar] = nums[src] - nums[tar];
        nums[src] = nums[src] - nums[tar];
    }

}
