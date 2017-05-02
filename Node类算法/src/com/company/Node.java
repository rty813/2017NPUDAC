package com.company;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zhang on 2017/4/30.
 */

class Node{
    private int[] data;
    private ArrayList<Integer> point;

    Node(){
        data = new int[Main.N];
        point = new ArrayList<Integer>();
    }


    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public ArrayList<Integer> getPoint() {
        return point;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    public void addPoint(int t){
        this.point.add(t);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        return (this.hashCode() == obj.hashCode());
    }
}
