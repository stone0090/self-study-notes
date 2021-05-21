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
        A:
        for (int i = step; i < arr.length; i++) {
            for (int j = i - step; j >= 0; j -= step) {
                if (arr[j + step] < arr[j]) {
                    swap(arr, j + step, j);
                } else {
                    // 因为前面数据已经排好序，只要j+1有一次比j大，后面的数据就不用再重复比较，直接开始下一轮A循环
                    continue A;
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
            for (int j = 1; j < i; j++) {
                if (arr[j - 1] > arr[j]) {
                    swap(arr, j - 1, j);
                }
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
            if (rightToLeft && arr[end] >= midValue) {
                end--;
            } else if (!rightToLeft && arr[start] <= midValue) {
                start++;
            }
            swap(arr, end, start);
            rightToLeft = !rightToLeft;
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
        selectSort(arr, 0, arr.length);
    }

    /**
     * 6、堆排序
     */
    public static void heapSort(int[] arr) {

    }

    private static void selectSort(int[] arr, int start, int end) {
        if (arr == null || arr.length == 0 || arr.length == 1) {
            return;
        }
        for (int i = start; i < end; i++) {
            int minIndex = i;
            int minValue = arr[i];
            for (int j = i; j < end; j++) {
                if (arr[j] < minValue) {
                    minIndex = j;
                    minValue = arr[j];
                }
            }
            if (i != minIndex) {
                swap(arr, i, minIndex);
            }
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
        for (int i = start, j = 0; i <= end; i++, j++) {
            temp[j] = arr[i];
        }
        int i = 0, j = temp.length / 2, k = start;
        while (i < temp.length / 2 && j < temp.length) {
            arr[k++] = temp[i] < temp[j] ? temp[i++] : temp[j++];
        }
        while (i < temp.length / 2) { arr[k++] = temp[i++]; }
        while (j < temp.length) { arr[k++] = temp[j++]; }
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
        if (length <= 0 || max <= 0 || max < length) {
            return new int[] {};
        }
        int[] nums = new int[max];
        for (int i = 1; i <= max; i++) {
            nums[i - 1] = i;
        }
        Random random = new Random();
        int[] result = new int[length];
        for (int i = max, j = 0; i >= max - length && j < length; i--, j++) {
            int index = random.nextInt(i);
            result[j] = nums[index];
            swap(nums, index, i - 1);
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
