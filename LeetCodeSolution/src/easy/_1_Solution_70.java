package easy;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _1_Solution_70 {

    // 解法一，嵌套循环
    // 执行耗时:15 ms,击败了57.03% 的Java用户
    // 内存消耗:37.2 MB,击败了91.44% 的Java用户
    private static int[] twoSum1(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if ((nums[i] + nums[j]) == target) {
                    return new int[] {i, j};
                }
            }
        }
        throw new IllegalArgumentException("没有结果");
    }

    // 解法二，空间换时间，使用额外hashMap
    // 执行耗时:3 ms,击败了97.55% 的Java用户
    // 内存消耗:37.1 MB,击败了92.20% 的Java用户
    private static int[] twoSum2(int[] nums, int target) {
        HashMap<Integer, Integer> hm = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (hm.containsKey(target - nums[i])) {
                return new int[] {hm.get(target - nums[i]), i};
            } else {
                hm.put(nums[i], i);
            }
        }
        throw new IllegalArgumentException("没有结果");
    }

    public static void main(String[] args) {
        int[] nums = new int[] {2, 7, 11, 15};
        int target = 9;
        System.out.println(Arrays.toString(twoSum1(nums, target)));
        System.out.println(Arrays.toString(twoSum2(nums, target)));
    }

}


