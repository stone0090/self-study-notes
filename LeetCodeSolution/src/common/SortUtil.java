package common;

import java.util.Arrays;

/**
 * @author stone
 * @date 2020/02/01
 */
public class SortUtil {

    // 冒泡排序
    public static int[] bubbleSort(int[] nums) {
        preCheck(nums);
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length - 1 - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    swap(nums, j, j + 1);
                }
            }
        }
        return nums;
    }

    // 选择排序
    public static int[] selectionSort(int[] nums) {
        preCheck(nums);
        for (int i = 0; i < nums.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[minIndex]) { minIndex = j; }
            }
            swap(nums, i, minIndex);
        }
        return nums;
    }

    // 插入排序
    public static int[] insertionSort(int[] nums) {
        preCheck(nums);
        for (int i = 1; i < nums.length; i++) {
            int target = nums[i];
            for (int j = i - 1; j >= 0; j--) {
                if (target < nums[j]) {
                    swap(nums, j++, j);
                }
            }
        }
        return nums;
    }

    // 插入排序2
    public static int[] insertionSort2(int[] nums) {
        preCheck(nums);
        for (int i = 1; i < nums.length; i++) {
            int target = nums[i];
            int k = i - 1;
            while (k >= 0 && target < nums[k]) { k--; }
            for (int j = i; j > k + 1; j--) {
                nums[j] = nums[j - 1];
            }
            nums[k + 1] = target;
        }
        return nums;
    }

    // 快速排序
    public static int[] quickSort(int[] nums) {
        preCheck(nums);
        return quick_sort(nums, 0, nums.length - 1);
    }

    private static int[] quick_sort(int[] nums, int left, int right) {
        if (left >= right) { return nums; }
        int k = left, mid_value = nums[right];
        for (int i = left; i < right; i++) {
            if (nums[i] <= mid_value) {
                swap(nums, i, k++);
            }
        }
        swap(nums, right, k);
        quick_sort(nums, left, k - 1);
        quick_sort(nums, k + 1, right);
        return nums;
    }

    // 归并排序
    public static int[] mergeSort(int[] nums) {
        preCheck(nums);
        return merge_sort(nums, 0, nums.length - 1);
    }

    private static int[] merge_sort(int[] nums, int left, int right) {
        if (left >= right) { return new int[] {nums[left]}; }
        int mid = (right + left) / 2;
        return merge(merge_sort(nums, left, mid), merge_sort(nums, mid + 1, right));
    }

    private static int[] merge(int[] nums1, int[] nums2) {
        int[] result = new int[nums1.length + nums2.length];
        int i = 0, j = 0, k = 0;
        while (i < nums1.length && j < nums2.length) { result[k++] = (nums1[i] < nums2[j]) ? nums1[i++] : nums2[j++]; }
        if (i < nums1.length) { System.arraycopy(nums1, i, result, k, nums1.length - i); }
        if (j < nums2.length) { System.arraycopy(nums2, j, result, k, nums2.length - j); }
        return result;
    }

    // 希尔排序
    public static void shellSort(int[] nums) {

    }

    private static void preCheck(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("入参数组不能为空");
        }
    }

    private static void swap(int[] nums, int a, int b) {
        if (a == b) { return; }
        nums[a] = nums[a] + nums[b];
        nums[b] = nums[a] - nums[b];
        nums[a] = nums[a] - nums[b];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(bubbleSort(new int[] {2, 56, 4, 5, 6, 44, 557, 1, 423, 6})));
        System.out.println(Arrays.toString(selectionSort(new int[] {2, 56, 4, 5, 6, 44, 557, 1, 423, 6})));
        System.out.println(Arrays.toString(insertionSort(new int[] {2, 56, 4, 5, 6, 44, 557, 1, 423, 6})));
        System.out.println(Arrays.toString(insertionSort2(new int[] {2, 56, 4, 5, 6, 44, 557, 1, 423, 6})));
        System.out.println(Arrays.toString(mergeSort(new int[] {2, 56, 4, 5, 6, 44, 557, 1, 423, 6})));
        System.out.println(Arrays.toString(quickSort(new int[] {2, 56, 4, 5, 6, 44, 557, 1, 423, 6})));
    }

}
