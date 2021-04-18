package easy;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _21_Solution_50 {

    public class ListNode {
        public int val;
        public ListNode next;

        ListNode(int x) { val = x; }
    }

    //  解法一：递归
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:37.6 MB,击败了60.67% 的Java用户
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        } else if (l2 == null) {
            return l1;
        } else if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }
    }

    //  解法二：逐个比较，嵌套循环
    public ListNode mergeTwoLists2(ListNode l1, ListNode l2) {
        ListNode result = new ListNode(0);
        ListNode temp = result;
        while(l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                temp.next = l1;
                l1 = l1.next;
            } else {
                temp.next = l2;
                l2 = l2.next;
            }
            temp = temp.next;
        }
        temp.next = (l1 == null) ? l2 : l1;
        return result.next;
    }

    public static void main(String[] args) {

    }

}


