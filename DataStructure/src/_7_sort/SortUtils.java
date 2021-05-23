package _7_sort;

import java.util.Random;

/**
 * @author stone
 * @date 2021/05/18
 */
public class SortUtils {

    /**
     * 1、插入排序（数组）
     */
    public static void insertSort(int[] arr) {
        if (arr == null || arr.length == 0 || arr.length == 1) {
            return;
        }
        insertSort(arr, 1);
    }

    /**
     * 2、希尔排序（数组）
     */
    public static void shellSort(int[] arr) {
        if (arr == null || arr.length == 0 || arr.length == 1) {
            return;
        }
        for (int step = arr.length / 2; step >= 1; step /= 2) {
            insertSort(arr, step);
        }
    }

    private static void insertSort(int[] arr, int step) {
        for (int i = step; i < arr.length; i++) {
            for (int j = i; j >= step; j -= step) {
                if (arr[j] < arr[j - step]) {
                    swap(arr, j, j - step);
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 3、冒泡排序（数组）
     */
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length == 0 || arr.length == 1) {
            return;
        }
        for (int i = arr.length; i >= 1; i--) {
            boolean earlyReturn = true;
            for (int j = 1; j < i; j++) {
                if (arr[j - 1] > arr[j]) {
                    swap(arr, j - 1, j);
                    earlyReturn = false;
                }
            }
            if (earlyReturn) {
                break;
            }
        }
    }

    /**
     * 4、快速排序（数组）
     */
    public static void quickSort(int[] arr) {
        if (arr == null || arr.length == 0 || arr.length == 1) {
            return;
        }
        quickSort(arr, 0, arr.length - 1);
    }

    private static void quickSort(int[] arr, int start, int end) {
        if (end <= start) {
            return;
        }
        int mid = partition(arr, start, end);
        quickSort(arr, start, mid - 1);
        quickSort(arr, mid + 1, end);
    }

    private static int partition(int[] arr, int start, int end) {
        int midValue = arr[start];
        boolean rightToLeft = true;
        while (start < end) {
            if (rightToLeft && arr[end] > midValue) {
                end--;
            } else if (!rightToLeft && arr[start] < midValue) {
                start++;
            } else {
                swap(arr, end, start);
                rightToLeft = !rightToLeft;
            }
        }
        arr[start] = midValue;
        return start;
    }

    /**
     * 5、选择排序
     */
    public static void selectSort(int[] arr) {
        if (arr == null || arr.length == 0 || arr.length == 1) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            int min = i;
            for (int j = i; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            if (i != min) {
                swap(arr, i, min);
            }
        }
    }

    /**
     * 6、堆排序
     */
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length == 0 || arr.length == 1) {
            return;
        }
        for (int i = (int)Math.ceil(arr.length / 2) - 1; i >= 0; i--) {
            maxHeapify(arr, i, arr.length);
        }
        for (int i = arr.length - 1; i > 0; i--) {
            swap(arr, 0, i);
            maxHeapify(arr, 0, i);
        }
    }

    /**
     * 构建大根堆
     */
    private static void maxHeapify(int[] arr, int root, int len) {
        int left = root * 2 + 1;
        int right = root * 2 + 2;
        int largest = root;
        if (left < len && arr[largest] < arr[left]) {
            largest = left;
        }
        if (right < len && arr[largest] < arr[right]) {
            largest = right;
        }
        if (largest != root) {
            swap(arr, root, largest);
            maxHeapify(arr, largest, len);
        }
    }

    /**
     * 7、归并排序
     */
    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length == 0 || arr.length == 1) {
            return;
        }
        mergeSort(arr, 0, arr.length - 1);
    }

    public static void mergeSort(int[] arr, int start, int end) {
        if (end <= start) {
            return;
        } else if (end - start == 1) {
            if (arr[start] > arr[end]) {
                swap(arr, start, end);
            }
            return;
        }
        int mid = start + (end - start + 1) / 2 - 1;
        mergeSort(arr, start, mid);
        mergeSort(arr, mid + 1, end);
        merge(arr, start, end);
    }

    private static void merge(int[] arr, int start, int end) {
        int[] temp = new int[end - start + 1];
        for (int i = start; i <= end; i++) {
            temp[i - start] = arr[i];
        }
        int left = 0, right = temp.length - 1, mid = right / 2 + 1;
        while (left <= right / 2 && mid <= right) {
            arr[start++] = (temp[left] < temp[mid]) ? temp[left++] : temp[mid++];
        }
        while (left <= right / 2) { arr[start++] = temp[left++]; }
        while (mid <= right) { arr[start++] = temp[mid++]; }
    }

    private static void swap(int[] arr, int src, int tar) {
        if (arr == null || arr.length == 0 || arr.length == 1) {
            return;
        }
        arr[src] = arr[src] + arr[tar];
        arr[tar] = arr[src] - arr[tar];
        arr[src] = arr[src] - arr[tar];
    }

    private static int[] random(int max, int length) {
        if (length < 0 || max < length) {
            return new int[] {};
        }
        int[] nums = new int[max];
        for (int i = 0; i < max; i++) {
            nums[i] = i + 1;
        }
        Random random = new Random();
        int[] result = new int[length];
        for (int i = max - 1, j = 0; j < length; i--, j++) {
            int r = random.nextInt(i);
            result[j] = nums[r];
            swap(nums, r, i);
        }
        return result;
    }

    private static void printArray(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(arr[i]);
        }
        System.out.println(sb);
    }

    public static void reverseArray(Object[] objs, int start, int end) {
        for (int i = start; i < (start + end) / 2; i++) {
            Object temp = objs[i];
            objs[i] = objs[end - i];
            objs[end - i] = temp;
        }
    }

    public static void main(String[] args) {
        int[] arr;
        int max = 100, length = 10;

        System.out.println("1、插入排序");
        arr = random(max, length);
        System.out.print("排序前：");
        printArray(arr);
        System.out.print("排序后：");
        insertSort(arr);
        printArray(arr);
        System.out.println("------------");

        System.out.println("2、希尔排序");
        arr = random(max, length);
        System.out.print("排序前：");
        printArray(arr);
        System.out.print("排序后：");
        shellSort(arr);
        printArray(arr);
        System.out.println("------------");

        System.out.println("3、冒泡排序");
        arr = random(max, length);
        System.out.print("排序前：");
        printArray(arr);
        System.out.print("排序后：");
        bubbleSort(arr);
        printArray(arr);
        System.out.println("------------");

        System.out.println("4、快速排序");
        arr = random(max, length);
        System.out.print("排序前：");
        printArray(arr);
        System.out.print("排序后：");
        quickSort(arr);
        printArray(arr);
        System.out.println("------------");

        System.out.println("5、选择排序");
        arr = random(max, length);
        System.out.print("排序前：");
        printArray(arr);
        System.out.print("排序后：");
        selectSort(arr);
        printArray(arr);
        System.out.println("------------");

        System.out.println("6、堆排序");
        arr = random(max, length);
        System.out.print("排序前：");
        printArray(arr);
        System.out.print("排序后：");
        heapSort(arr);
        printArray(arr);
        System.out.println("------------");

        System.out.println("7、归并排序");
        arr = random(max, length);
        System.out.print("排序前：");
        printArray(arr);
        System.out.print("排序后：");
        mergeSort(arr);
        printArray(arr);
        System.out.println("------------");

    }

}
