package easy;

import java.util.HashMap;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _70_Solution_10 {

    // fibonacci 数列

    // 解法一：带缓存的递归
    // 执行耗时:1 ms,击败了13.72% 的Java用户
    // 内存消耗:32.8 MB,击败了77.76% 的Java用户
    public int climbStairs1(int n) {
        HashMap<Integer, Integer> cache = new HashMap<>();
        return climb_stairs(n, cache);
    }

    public int climb_stairs(int n, HashMap<Integer, Integer> cache) {
        if (n < 1) { return 0; }
        if (n < 3) { return n; }
        if (!cache.containsKey(n)) {
            cache.put(n, climb_stairs(n - 1, cache) + climb_stairs(n - 2, cache));
        }
        return cache.get(n);
    }

    // 解法儿：循环平推
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:33.2 MB,击败了10.95% 的Java用户
    public int climbStairs2(int n) {
        if (n < 1) { return 0; }
        if (n < 3) { return n; }
        int f1 = 1, f2 = 2, f3 = 3;
        for (int i = 3; i <= n; i++) {
            f3 = f1 + f2;
            f1 = f2;
            f2 = f3;
        }
        return f3;
    }

}
