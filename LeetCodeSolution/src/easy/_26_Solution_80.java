package easy;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _26_Solution_80 {

    // 解法一，两次循环（使用了额外的空间，不满足题意）
    // 执行耗时:101 ms,击败了5.06% 的Java用户
    // 内存消耗:40.4 MB,击败了94.58% 的Java用户
    private static int removeDuplicates1(int[] nums) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (!arrayList.contains(nums[i])) {
                arrayList.add(nums[i]);
            }
        }
        int j = 0;
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            nums[j++] = (int)iterator.next();
        }
        return j;
    }

    // 解法二，单次循环（使用了额外的空间，不满足题意）
    // 执行耗时:1 ms,击败了100.00% 的Java用户
    // 内存消耗:40 MB,击败了95.07% 的Java用户
    public static int removeDuplicates2(int[] nums) {
        if (nums.length == 0) {return 0;}
        int index = 1;
        int previous = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (previous != nums[i]) {
                previous = nums[i];
                nums[index++] = previous;
            }
        }
        return index;
    }

    // 解法三，快慢指针，一次循环
    // 执行耗时:1 ms,击败了100.00% 的Java用户
    // 内存消耗:40 MB,击败了95.02% 的Java用户
    private static int removeDuplicates3(int[] nums) {
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[i] != nums[j]) {
                nums[++i] = nums[j];
            }
        }
        return i + 1;
    }

    public static void main(String[] args) {
        int[] nums = new int[] {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        // int count = removeDuplicates1(nums);
        int count = removeDuplicates2(nums);
        // int count = removeDuplicates3(nums);
        for (int i = 0; i < count; i++) {
            System.out.println(nums[i]);
        }
    }

}
