package medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _15_Solution_00 {

    // 解题一：暴力破解，三层循环
    public static List<List<Integer>> threeSum1(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        List<String> unique = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            for (int j = i + 1; j < nums.length - 1; j++) {
                for (int k = j + 1; k < nums.length; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        int min = Math.min(Math.min(nums[i], nums[j]), nums[k]);
                        int max = Math.max(Math.max(nums[i], nums[j]), nums[k]);
                        String temp = String.valueOf(min) + (0 - min - max) + max;
                        if (!unique.contains(temp)) {
                            unique.add(temp);
                            result.add(Arrays.asList(min, 0 - min - max, max));
                        }
                    }
                }
            }
        }
        return result;
    }

    // 解题二：空间换时间，适用额外的HashMap。要点：1）对比大小，2）判重
    public static List<List<Integer>> threeSum2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        List<String> unique = new ArrayList<>();
        HashMap<Integer, Integer> hm = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                if (hm.containsKey(-(nums[i] + nums[j]))
                    && i != j
                    && j != hm.get(-(nums[i] + nums[j]))
                    && i != hm.get(-(nums[i] + nums[j]))) {
                    int min = Math.min(Math.min(nums[i], nums[j]), -(nums[i] + nums[j]));
                    int max = Math.max(Math.max(nums[i], nums[j]), -(nums[i] + nums[j]));
                    String temp = String.valueOf(min) + (0 - min - max) + max;
                    if (!unique.contains(temp)) {
                        unique.add(temp);
                        result.add(Arrays.asList(min, 0 - min - max, max));
                    }
                } else {
                    hm.put(nums[j], j);
                }
            }
        }
        return result;
    }

    // 解题三： 双指针，左右夹逼
    public static List<List<Integer>> threeSum3(int[] nums) {
        List<List<Integer>> result = new ArrayList();
        if (nums == null || nums.length == 0) { return result; }
        Arrays.sort(nums);
        for (int k = 0; k < nums.length - 2; k++) {
            if (nums[k] > 0) break;
            if (nums[k] == nums[k + 1]) continue;
            for (int i = k + 1, j = nums.length - 1; i < j;) {
                int sum = nums[k] + nums[i] + nums[j];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[k], nums[i], nums[j]));
                    while (i < j && nums[i++] == nums[i]) {}
                    while (i < j && nums[j--] == nums[j]) {}
                } else if (sum < 0) {
                    while (i < j && nums[i++] == nums[i]) {}
                } else if (sum > 0) {
                    while (i < j && nums[j--] == nums[j]) {}
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // int[] nums = {-1, 0, 1, 2, -1, -4};
        int[] nums = {1, 0, -1};
        List<List<Integer>> result = threeSum3(nums);
        result.forEach(x -> {
            x.forEach(y -> System.out.println(y.toString()));
            System.out.println("--------------------------");
        });
    }
}


