package medium;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _11_Solution_30 {

    // 解法一：暴力双循环
    // 执行耗时:333 ms,击败了10.85% 的Java用户
    // 内存消耗:40.5 MB,击败了5.04% 的Java用户
    public int maxArea1(int[] height) {
        int max = 0;
        for (int i = 0; i < height.length - 1; i++) {
            for (int j = i + 1; j < height.length; j++) {
                max = Math.max(max, (j - i) * Math.min(height[i], height[j]));
            }
        }
        return max;
    }

    // 解法二：双指针夹逼
    // 执行耗时:3 ms,击败了91.52% 的Java用户
    // 内存消耗:39.1 MB,击败了83.38% 的Java用户
    public static int maxArea2(int[] height) {
        int max = 0;
        for (int i = 0, j = height.length - 1; i < j;) {
            int minHeight = (height[i] < height[j]) ? height[i++] : height[j--];
            max = Math.max(max, (j - i + 1) * minHeight);
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(maxArea2(new int[] {1, 1}));
    }

}


