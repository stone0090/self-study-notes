package easy;

import java.util.ArrayDeque;
import java.util.Deque;

import data.structure.ListNode;

/**
 * @author stone
 * @date 2020/01/08
 */
public class _206_Solution_00 {

    // 我的解题：使用额外的双端队列
    public static ListNode reverseList1(ListNode head) {
        Deque<Integer> deque = new ArrayDeque<>();
        while (head != null) {
            deque.push(head.val);
            head = head.next;
        }
        ListNode result = new ListNode(0);
        ListNode temp = result;
        while (deque.peek() != null) {
            temp.next = new ListNode(deque.pop());
            temp = temp.next;
        }
        return result;
    }

    // 官方解题一：双指针迭代
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:37.5 MB,击败了5.00% 的Java用户
    public static ListNode reverseList2(ListNode head) {
        ListNode previous = null, current = head;
        while (current != null) {
            ListNode temp = current.next;
            current.next = previous;
            previous = current;
            current = temp;
        }
        return previous;
    }

    // 官方解题二：递归
    // 执行耗时:0 ms,击败了100.00% 的Java用户
    // 内存消耗:37.2 MB,击败了22.53% 的Java用户
    public static ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) { return head; }
        ListNode current = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return current;
    }

    public static void main(String[] args) {
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;
        reverseList2(n1);
    }

}
