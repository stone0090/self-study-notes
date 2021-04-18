package easy;

import java.util.Arrays;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _88_Solution_00 {

    // 原创解法：双指针，临时数组，空间换时间（代码复杂度较高，不容易记忆，容易出错）
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:36.3 MB,击败了29.79% 的Java用户
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        if (n == 0) { return; }
        if (m == 0) {
            System.arraycopy(nums2, 0, nums1, 0, n);
            return;
        }
        int[] temp_nums = new int[m + n];
        for (int i = 0, j = 0, k = 0; k < (m + n); k++) {
            if (j >= n || (i < m && nums1[i] <= nums2[j])) {
                temp_nums[k] = nums1[i++];
            } else {
                temp_nums[k] = nums2[j++];
            }
        }
        System.arraycopy(temp_nums, 0, nums1, 0, m + n);
    }

    // 解法一：合并数组再排序，时间：O((m+n)log(m+n))
    // 执行耗时:1 ms,击败了32.59% 的Java用户
    // 内存消耗:36.2 MB,击败了49.82% 的Java用户
    public void merge1(int[] nums1, int m, int[] nums2, int n) {
        System.arraycopy(nums2, 0, nums1, m, n);
        Arrays.sort(nums1);
    }

    // 解法二：双指针，从小到大，需要额外空间（比我代码更加优雅）
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:35.9 MB,击败了93.70% 的Java用户
    public void merge2(int[] nums1, int m, int[] nums2, int n) {
        int[] temp_nums = new int[m];
        System.arraycopy(nums1, 0, temp_nums, 0, m);
        int i = 0, j = 0, k = 0;
        while ((i < m) && (j < n)) {
            nums1[k++] = temp_nums[i] < nums2[j] ? temp_nums[i++] : nums2[j++];
        }
        if (i < m) { System.arraycopy(temp_nums, i, nums1, k, m - i); }
        if (j < n) { System.arraycopy(nums2, j, nums1, k, n - j); }
    }

    // 解法三：双指针，从大到小，不需要额外空间
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:36 MB,击败了87.88% 的Java用户
    public void merge3(int[] nums1, int m, int[] nums2, int n) {
        int i = m - 1, j = n - 1, k = m + n - 1;
        while (i >= 0 && j >= 0) {
            nums1[k--] = nums1[i] > nums2[j] ? nums1[i--] : nums2[j--];
        }
        System.arraycopy(nums2, 0, nums1, 0, j + 1);
    }

    public static void main(String[] args) {
        int[] nums1 = new int[] {4, 0, 0, 0, 0, 0};
        int m = 1;
        int[] nums2 = new int[] {1, 2, 3, 5, 6};
        int n = 5;
        new _88_Solution_00().merge3(nums1, m, nums2, n);
        System.out.println(Arrays.toString(nums1));
    }

}
