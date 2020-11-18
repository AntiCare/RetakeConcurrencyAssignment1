package com.company;

public class MergeResult {
    private int[] firstList;
    private int[] secondList;
    private int[] resultList;

    public MergeResult(int[] firstList, int[] secondList) {
        this.firstList= firstList;
        this.secondList= secondList;
        this.resultList= new int [firstList.length + secondList.length];
        //Two get pointers for firstList and secondListList.
        int p1 = 0;
        int p2 = 0;
        //Set pointer for resultList
        int p = 0;
        // Compare elements from firstList and secondList,
        // then add the smallest one into the resultList.
        while (p1 < firstList.length && p2 < secondList.length) {

            if (firstList[p1] < secondList[p2]) {
                resultList[p++] = firstList[p1++];
            } else {
                resultList[p++] = secondList[p2++];
            }
        }

        while (p1 < firstList.length) {
            resultList[p++] = firstList[p1++];
        }

        while (p2 < secondList.length) {
            resultList[p++] = secondList[p2++];
        }
    }


    public void printResultList() {
        System.out.println("\n"+"Result is: " );
        for (int i = 0; i < resultList.length ; i++) {
            System.out.println(resultList[i]);
        }
    }

    public void printFirstList() {
        System.out.println("FirstList is: " );
        for (int i = 0; i < firstList.length; i++) {
            System.out.println(firstList[i]);
        }
    }

    public void printSecondList() {
        System.out.println("\n"+"SecondList is: " );
        for (int i = 0; i < secondList.length; i++) {
            System.out.println(secondList[i]);
        }
    }


    public int[] getResultList() {
        return resultList;
    }

}



