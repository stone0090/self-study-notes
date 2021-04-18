package easy;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _155_Solution_00 {

    // 我的解法：纯用数组实现最小栈
    // 执行耗时:6 ms,击败了97.70% 的Java用户
    // 内存消耗:40.2 MB,击败了61.21% 的Java用户
    public static class MinStack {

        int size;
        int min;
        int[] nums;

        /**
         * initialize your data structure here.
         */
        public MinStack() {
            min = Integer.MAX_VALUE;
            size = 0;
            nums = new int[2];
        }

        public void push(int x) {
            min = Math.min(min, x);
            if (size >= nums.length) {
                int[] temp = nums;
                nums = new int[size * 2];
                System.arraycopy(temp, 0, nums, 0, size);
            }
            nums[size++] = x;
        }

        public void pop() {
            if (size > 0) {
                recalMin(--size);
            }
        }

        public int top() {
            return nums[size - 1];
        }

        private void recalMin(int length) {
            min = Integer.MAX_VALUE;
            for (int i = 0; i < length; i++) {
                min = Math.min(min, nums[i]);
            }
        }

        public int getMin() {
            return min;
        }
    }

    public static void main(String[] args) {
        MinStack obj = new MinStack();
        obj.push(-2);
        obj.push(0);
        obj.push(-3);
        System.out.println(obj.getMin());
        obj.pop();
        System.out.println(obj.top());
        System.out.println(obj.getMin());
    }

}