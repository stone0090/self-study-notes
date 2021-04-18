package _2_2_liner_list;

/**
 * 第2章 线性表
 * 2.2 线性表的链式表示
 * 2.3.8 本节试题精选-综合应用题
 *
 * @author stone
 * @date 2021/04/15
 */
public class LinkListTest {

    public static void main(String[] args) {

        // 1. 设计一个递归算法，删除单链表L中所值为x的节点
        recursiveDeleteElem();

        // 2. 设计一个算法，从一个单链表中，删除所有值为x的节点，值为x的节点不唯一
        // 解法一：递归删除，同上
        // 解法二：循环删除
        loopDeleteElem();

        // 3. 编写算法实现从尾到头反向输出每个节点的值
        reversePrint();

        // 4. 编写删除单链表中最小值的高效算法（假设最小值是唯一的）
        deleteMin();

        // 5. 单链表就地倒置
        reverseSingleLinkList();

        // 6. 使链表元素递增有序
        sortSingleLinkList();

        // 7. 删除一个无序单链表中2个给定值之间的所有元素
        deleteRangeElem();

        // 8. 找出两个单链表的相同的元素
        findSameElem();

        // 17. 判断带头节点的循环双列表是否对称
        checkSymmetrical();

        // 18. 将1个循环单链表h1接到另一个循环单链表h2之后
        mergeCircularSingleLinkList();

        // 20. 按查询频率排序双链表中的元素
        sortByFreqDoubleLinkList();

    }

    private static void sortByFreqDoubleLinkList() {

        System.out.println("20. 按查询频率排序双链表中的元素");
        DoubleLinkList<Integer> linkList = new DoubleLinkList<>();
        linkList.insertFirst(1);
        linkList.insertFirst(2);
        linkList.insertFirst(3);
        linkList.insertFirst(4);
        linkList.print();
        linkList.locate(2);
        linkList.print();
        linkList.locate(3);
        linkList.locate(3);
        linkList.print();
        System.out.println("");
    }

    private static void mergeCircularSingleLinkList() {
        System.out.println("18. 将1个循环单链表h1接到另一个循环单链表h2之后");
        CircularSingleLinkList<Integer> h1 = new CircularSingleLinkList<>();
        h1.insertFirst(1);
        h1.insertFirst(2);
        h1.insertFirst(3);
        h1.insertFirst(4);
        h1.print();
        CircularSingleLinkList<Integer> h2 = new CircularSingleLinkList<>();
        h2.insertFirst(6);
        h2.insertFirst(7);
        h2.insertFirst(8);
        h2.insertFirst(9);
        h2.print();
        CircularSingleLinkList<Integer> h1Last = h1.locateLast();
        CircularSingleLinkList<Integer> h2Last = h2.locateLast();
        h1Last.setNext(h2.getNext());
        h2Last.setNext(h1);
        h1.print();
        System.out.println("");
    }

    private static void checkSymmetrical() {
        System.out.println("17. 判断带头节点的循环双列表是否对称");
        CircularDoubleLinkList<Integer> linkList = new CircularDoubleLinkList<>();
        linkList.insertLast(1);
        linkList.insertLast(2);
        linkList.insertLast(3);
        linkList.insertLast(4);
        linkList.insertLast(5);
        linkList.print();
        boolean isSymmetrical = true;
        CircularDoubleLinkList<Integer> head = linkList;
        CircularDoubleLinkList<Integer> prior = linkList.getPrior();
        CircularDoubleLinkList<Integer> next = linkList.getNext();
        while (!next.equals(head) && !prior.equals(head)) {
            if (!next.getData().equals(prior.getData())) {
                isSymmetrical = false;
                break;
            }
            prior = prior.getPrior();
            next = next.getNext();
        }
        System.out.println("判断带头节点的循环双列表是否对称：" + isSymmetrical);
        System.out.println("");
    }

    private static void findSameElem() {
        System.out.println("8. 找出两个单链表的相同的元素");
        SingleLinkList<Integer> linkList = new SingleLinkList<>();
        linkList.setData(1);
        linkList.insertLast(new Integer[] {2, 3, 4, 5, 6});
        linkList.print();
        SingleLinkList<Integer> singleLinkList2 = new SingleLinkList<>();
        singleLinkList2.setData(4);
        singleLinkList2.insertLast(new Integer[] {5, 6, 7, 8, 9});
        singleLinkList2.print();
        SingleLinkList<Integer> current = linkList;
        System.out.print("相同的元素有：");
        while (current != null) {
            if (singleLinkList2.locate(current.getData()) != null) {
                System.out.print(current.getData() + " ");
            }
            current = current.getNext();
        }
        System.out.println("");
        System.out.println("");
    }

    private static void deleteRangeElem() {
        System.out.println("7. 删除一个无序单链表中2个给定值之间的所有元素");
        SingleLinkList<Integer> linkList = new SingleLinkList<>();
        linkList.insertLast(new Integer[] {4, 5, 7, 34, 23, 14, 22, 33, 34, 56, 31});
        linkList.print();
        Integer start = 7;
        Integer end = 40;
        SingleLinkList<Integer> current = linkList.getNext();
        SingleLinkList<Integer> prior = null;
        while (current != null) {
            if (current.getData() > start && current.getData() < end) {
                if (current.getNext() != null) {
                    current.setData(current.getNext().getData());
                    current.setNext(current.getNext().getNext());
                    continue;
                } else {
                    if (prior != null) {
                        prior.setNext(prior.getNext().getNext());
                    } else {
                        current.setData(null);
                    }
                }
            }
            prior = current;
            current = current.getNext();
        }
        linkList.print();
        System.out.println("");
    }

    private static void sortSingleLinkList() {
        System.out.println("6. 使链表元素递增有序");
        SingleLinkList<Integer> linkList = new SingleLinkList<>();
        linkList.insertLast(new Integer[] {4, 3, 4, 5, 3, 4, 2, 3});
        linkList.print();
        SingleLinkList<Integer> loop = linkList.getNext();
        while (loop != null) {
            SingleLinkList<Integer> current = linkList.getNext();
            while (current != null) {
                if (current.getNext() != null) {
                    if (current.getData() > current.getNext().getData()) {
                        current.setData(current.getData() + current.getNext().getData());
                        current.getNext().setData(current.getData() - current.getNext().getData());
                        current.setData(current.getData() - current.getNext().getData());
                    }
                }
                current = current.getNext();
            }
            loop = loop.getNext();
        }
        linkList.print();
        System.out.println("");
    }

    private static void reverseSingleLinkList() {
        System.out.println("5. 单链表就地倒置");
        SingleLinkList<Integer> linkList = new SingleLinkList<>();
        linkList.insertLast(new Integer[] {1, 2, 3, 4, 5});
        linkList.print();
        recursiveReverse(linkList, null);
        linkList.print();
        System.out.println("");
    }

    private static SingleLinkList<Integer> recursiveReverse(SingleLinkList<Integer> linkList,
                                                            SingleLinkList<Integer> head) {
        if (linkList == null) {
            return null;
        } else {
            if (head == null) {
                head = linkList;
            }
        }
        if (linkList.getNext() == null) {
            return linkList;
        }
        SingleLinkList<Integer> lastNode = recursiveReverse(linkList.getNext(), head);
        if (linkList.getNext() != null) {
            linkList.getNext().setNext(linkList);
            if (linkList.equals(head)) {
                linkList.getNext().setNext(null);
                linkList.setNext(lastNode);
            }
        }
        return lastNode;
    }

    private static void deleteMin() {
        System.out.println("4. 编写删除单链表中最小值的高效算法（假设最小值是唯一的）");
        SingleLinkList<Integer> linkList = new SingleLinkList<>();
        linkList.setData(10);
        linkList.insertLast(new Integer[] {4, 3, 4, 5, 3, 4, 2, 3});
        linkList.print();
        SingleLinkList<Integer> minNode = null;
        SingleLinkList<Integer> current = linkList;
        while (current != null) {
            if (minNode == null) {
                minNode = current;
            } else {
                if (minNode.getData() > current.getData()) {
                    minNode = current;
                }
            }
            current = current.getNext();
        }
        linkList.delete(minNode);
        linkList.print();
        System.out.println("");
    }

    private static void reversePrint() {
        System.out.println("3. 编写算法实现从尾到头反向输出每个节点的值");
        SingleLinkList<Integer> linkList = new SingleLinkList<>();
        linkList.insertLast(new Integer[] {1, 2, 3, 4, 5});
        linkList.print();
        System.out.print("反向输出：");
        recursiveReversePrint(linkList);
        System.out.println("");
        System.out.println("");
    }

    private static void recursiveReversePrint(SingleLinkList<Integer> linkList) {
        if (linkList == null) {
            return;
        }
        recursiveReversePrint(linkList.getNext());
        if (linkList.getData() != null) {
            System.out.print(linkList.getData() + " ");
        }
    }

    private static void loopDeleteElem() {
        System.out.println("2. 设计一个算法，从一个单链表中，删除所有值为x的节点，值为x的节点不唯一");
        SingleLinkList<Integer> linkList = new SingleLinkList<>();
        linkList.insertLast(new Integer[] {2, 2, 3, 2, 2, 4, 5, 6, 3, 3, 4, 2, 3, 2, 2, 3, 4, 5, 2, 2, 2});
        linkList.print();
        Integer elem = 2;
        SingleLinkList<Integer> current = linkList.getNext();
        SingleLinkList<Integer> prior = null;
        while (true) {
            if (current.getData().equals(elem)) {
                if (current.getNext() != null) {
                    current.setData(current.getNext().getData());
                    current.setNext(current.getNext().getNext());
                    continue;
                } else {
                    if (prior != null) {
                        prior.setNext(prior.getNext().getNext());
                    } else {
                        current = null;
                    }
                }
            }
            if (current == null || current.getNext() == null) {
                break;
            } else {
                prior = current;
                current = current.getNext();
            }
        }
        linkList.print();
        System.out.println("");
    }

    private static void recursiveDeleteElem() {
        System.out.println("1. 设计一个递归算法，删除单链表L中所值为x的节点");
        SingleLinkList<Integer> linkList = new SingleLinkList<>();
        linkList.insertLast(new Integer[] {2, 3, 2, 2, 3, 4, 5, 2, 5, 2});
        linkList.print();
        recursiveDeleteElem(linkList, 2);
        linkList.print();
        System.out.println("");
    }

    private static void recursiveDeleteElem(SingleLinkList<Integer> linkList, Integer elem) {
        if (linkList == null || elem == null) {
            return;
        }
        if (linkList.getNext() != null) {
            if (elem.equals(linkList.getNext().getData())) {
                linkList.setNext(linkList.getNext().getNext());
                recursiveDeleteElem(linkList, elem);
            } else {
                recursiveDeleteElem(linkList.getNext(), elem);
            }
        }
        if (elem.equals(linkList.getData())) {
            if (linkList.getNext() != null) {
                linkList.setData(linkList.getNext().getData());
                linkList.setNext(linkList.getNext().getNext());
            } else {
                linkList.setData(null);
            }
        }
    }

}
