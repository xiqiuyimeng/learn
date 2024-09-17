package org.demo.learn.util.bloomFilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

/**
 * 布隆过滤器，实现快速判断元素不在集合中，布隆过滤器可以确定一定不在，而无法确定一定在
 * @author luwt-a
 * @date 2024/9/17
 */
public class BloomFilterUtil {

    public static void main(String[] args) {
        // 创建布隆过滤器，int 数据类型，存放1万个元素，误判率默认为 0.03
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 10000);
        // 向布隆过滤器添加元素
        for (int i = 0; i < 10000; i++) {
            bloomFilter.put(i);
        }

        long start = System.currentTimeMillis();
        // 判断存在的元素
        System.out.println(bloomFilter.mightContain(1));

        int errorCunt = 0, rightCount = 0;
        // 判断1万个不存在的元素
        for (int i = 10000; i < 20000; i++) {
            boolean mightContain = bloomFilter.mightContain(i);
            if (mightContain) {
                // 误判的元素
                errorCunt += 1;
            } else {
                // 判断元素不存在的
                rightCount += 1;
            }
        }

        System.out.println("判断耗时：" + (System.currentTimeMillis() - start) + "ms");
        System.out.println("误判个数：" + errorCunt + "， 判断正确个数：" + rightCount);
    }

}
