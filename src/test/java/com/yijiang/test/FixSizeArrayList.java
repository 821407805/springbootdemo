package com.yijiang.test;

import java.util.ArrayList;

/**
 * @ClassName: FixSizeLinkedList
 * @Description: 固定长度的List
 * @Author Jason
 * @Date 2020/9/9 15:18
 */
public class FixSizeArrayList<E> extends ArrayList<E> {
    int capacity;
    public FixSizeArrayList(int capacity) {
        super();
        this.capacity = capacity;
    }
    @Override
    public boolean add(E e) {
        if( size() > capacity ){
            return false;
        }
        return super.add(e);
    }

}
