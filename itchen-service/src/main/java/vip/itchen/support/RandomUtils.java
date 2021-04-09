package vip.itchen.support;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具类
 * @author chenkh
 * @date 2020-09-28
 */
public class RandomUtils {

    /**
     * 随机取list的下标
     * @param listSize list长度
     * @param randomSize 随机数量
     * @return 下标
     */
    public static Set<Integer> randomListIndex(int listSize, int randomSize) {
        Set<Integer> set = new HashSet<>(randomSize);
        while (set.size() < listSize && set.size() < randomSize) {
            set.add(ThreadLocalRandom.current().nextInt(listSize));
        }
        return set;
    }
}
