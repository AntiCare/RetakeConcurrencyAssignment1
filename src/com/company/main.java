package com.company;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

//2020/11/07
public class main {

    public static void main(String[] args) {
//        new main().assignment1_1();
//        new main().assignment1_2();
//        new main().assignment1_3();
          new main().assignment1_4(300000);
//        new main().assignment1_5(12);
    }

    /**Assignment1_1*/
    public void assignment1_1() {
        int[] nwArray = getElements(400000);
        Instant start = Instant.now();
        insertionSort(nwArray);
//        for (int i : nwArray) {
//            System.out.println(i);
//        }
        Instant finish = Instant.now();
        System.out.println(Duration.between(start, finish).toMillis()+" ms");
    }
    /**Assignment1_2*/
    public void assignment1_2() {
        int[] nwArray = getElements(400000);
        Instant start = Instant.now();
        SplitArray splitArray = new SplitArray(nwArray);
        insertionSort(splitArray.firstList);
        insertionSort(splitArray.secondList);
        MergeResult mergeResult = new MergeResult(splitArray.firstList,splitArray.secondList);
        mergeResult.getResultList();
        Instant finish = Instant.now();
        System.out.println(Duration.between(start, finish).toMillis()+" ms");

    }
    /**Assignment1_3*/
    public void assignment1_3() {
        int[] nwArray = getElements(400000);
        Instant start = Instant.now();
        SplitArray splitArray= new SplitArray(nwArray);
        Runnable runnable1 = () -> {
            insertionSort(splitArray.getFirstList());
        };
        Thread t1 = new Thread(runnable1,"firstList");
        Runnable runnable2 = () -> {
            insertionSort(splitArray.getSecondList());
        };
        Thread t2 = new Thread(runnable2,"secondList");
        //start thread t1, t2
        t1.start();
        t2.start();
        //join t1, t2
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //t1 , t2 finish. Start merge.
        MergeResult mergeResult = new MergeResult(splitArray.getFirstList(),splitArray.getSecondList());
        mergeResult.getResultList();

        Instant finish = Instant.now();
        System.out.println(Duration.between(start, finish).toMillis()+" ms");
    }

    /**Assignment1_4*/
    public void assignment1_4(int threshold) {
        int[] nwArray = getElements(400000);
        Instant start = Instant.now();
        recursive(nwArray,threshold);
//        for (int i : recursive(nwArray,threshold)) {
//            System.out.println(i);
//        }
        Instant finish = Instant.now();
        System.out.println(Duration.between(start, finish).toMillis()+" ms");
    }

    /**Assignment1_5*/
    public void assignment1_5(int threshold) {
        int[] nwArray = getElements(10);
        Instant start = Instant.now();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
          forkJoinPool.invoke(new RecursiveTasks(nwArray, threshold));
//        for (int i = 0; i < result.length ; i++) {
//            System.out.println(result[i]);
//        }
        Instant finish = Instant.now();
        System.out.println(Duration.between(start, finish).toMillis()+" ms");
    }

    /**
     * For Assignment1_4 recursive.
     * Split array if the array.length is larger than the threshold
     * sorts the each array in their respective thread -> merges all the results
     * @param array
     * @param threshold
     * @return
     */
    private int[] recursive(int[] array, int threshold ) {
        if (array.length > threshold) {
            //split array
            SplitArray splitArray = new SplitArray(array);

           //sort them in threads using recursive
            Tks m1 = new Tks(splitArray.getFirstList(),threshold);
            Tks m2 = new Tks(splitArray.getSecondList(),threshold);

            m1.start();
            m2.start();
            //wait on threads
            try{
                m1.join();
                m2.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            //merge array && return the merged arrays
            return new MergeResult(m1.getSortedArray(),m2.getSortedArray()).getResultList();
        }
            insertionSort(array);
            return array;

    }

    /**
     * For Assignment1_4, sort sub-task in thread using recursive.
     */
    public class Tks extends Thread{
        private volatile int[] sortedArray;
        private int[] arryToSort;
        int th;

        public Tks(int[] arryToSort,int threshold) {
            this.arryToSort = arryToSort;
            this.th = threshold;
        }
        @Override
        public void run() {
            sortedArray = recursive(arryToSort,th);
        }
        public int[] getSortedArray() {
            return sortedArray;
        }
    }

    /**
     * For Assignment1_5 forkJoinPool RecursiveTasks.
     * logic same as Assignment1_4.
     */
     static class RecursiveTasks extends RecursiveTask<int[]> {
        int[] nwArray;
        int threshold;

        public RecursiveTasks(int[] nwArray, int threshold) {
            this.nwArray=nwArray;
            this.threshold=threshold;
        }
        @Override
        protected int[] compute() {

            if (nwArray.length > threshold) {
                //split array
                SplitArray splitArray = new SplitArray(nwArray);
                //subtasks
                RecursiveTasks recursiveTasks1 = new RecursiveTasks(splitArray.getFirstList() , threshold);
                RecursiveTasks recursiveTasks2 = new RecursiveTasks(splitArray.getSecondList() , threshold);
                //Run subtasks
                invokeAll(recursiveTasks1, recursiveTasks2);
                //or use fork().
//                recursiveTasks1.fork();
//                recursiveTasks2.fork();
                //insertion sort subtask.
               insertionSort(recursiveTasks1.join()) ;
               insertionSort(recursiveTasks2.join()) ;
                //merge array
                MergeResult mergeResult = new MergeResult(recursiveTasks1.join(),recursiveTasks2.join());
                // return the merged arrays
                return mergeResult.getResultList();
            }
                insertionSort(nwArray);
                return nwArray;

        }
    }

    /**
     * @param length Create specified number of arrays.
     */
    private int[] getElements(int length){
        int[] nwArray = new int[length];
        for(int i = 0;i < length ; i++){
            nwArray[i] = (int)(Math.random() * Integer.MAX_VALUE);
        }
        return  nwArray;
    }


    public static int[] insertionSort(int[] arr)  {
        int n = arr.length;
        for(int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            //shift until you find the position to place the element 'key'
            while(j >= 0 && arr[j] > key) {
                arr[j+1] = arr[j];
                j--;
            }
            //place element 'key' in the correct position in the sorted part of the array
            arr[j+1] = key;
        }return arr;
    }


}






