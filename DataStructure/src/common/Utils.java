package common;

/**
 * @author stone
 * @date 2021/04/12
 */
public class Utils {

    public static void reverseArray(Object[] objs, int start, int end) {
        for (int i = start; i < (start + end) / 2; i++) {
            Object temp = objs[i];
            objs[i] = objs[end - i];
            objs[end - i] = temp;
        }
    }

}
