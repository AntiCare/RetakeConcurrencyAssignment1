package com.company;

import java.util.ArrayList;

public class SplitArray {
    int[] firstList, secondList;
    public SplitArray(int[] input) {
        this.firstList = new int[input.length/2];
        this.secondList = new int[input.length-firstList.length];
        for (int i = 0; i <input.length ; i++) {
            if(i<firstList.length) {
                firstList[i]=input[i];
            }else {
                secondList[i - firstList.length] = input[i];
            }
        }

    }

    public int[] getFirstList() {
        return firstList;
    }     

    public int[] getSecondList() {
        return secondList;
    }

}
