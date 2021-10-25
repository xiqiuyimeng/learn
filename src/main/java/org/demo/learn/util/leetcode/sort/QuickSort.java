package org.demo.learn.util.leetcode.sort;

import java.util.Arrays;

/**
 * @author luwt
 * @date 2021/5/6.
 * 快速排序
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] array = {1, 3, 2, 5, 6, 4};
        sort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));
    }

    // 快排
    public static void sort(int[] array, int start, int end) {
        if (start >= end) {
            return;
        }
        int left = start, right = end;
        boolean direction = true;
        int key = array[start];
        L:
        while (left < right) {
            if (direction) {
                for (int i = right; i > left; i --) {
                    if (array[i] < key) {
                        array[left ++] = array[i];
                        right = i;
                        direction = false;
                        continue L;
                    }
                }
                right = left;
            } else {
                for (int i = left; i < right; i ++) {
                    if (array[i] > key) {
                        array[right --] = array[i];
                        left = i;
                        direction = true;
                        continue L;
                    }
                }
                left = right;
            }
        }
        array[left] = key;
        sort(array, start, left - 1);
        sort(array, left + 1, end);
    }
}
