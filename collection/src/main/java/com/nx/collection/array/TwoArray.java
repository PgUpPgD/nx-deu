package com.nx.collection.array;

public class TwoArray {
    /**
     *      第一季度：22，66，44
     *      第二季度：77，33，88
     *      第三季度：11，66，99
     *      第四季度：25，45，65
     *
     */
    public static void main(String[] args) {
        int[][] arr={{22,66,44},{77,33,88},{11,66,99},{25,45,65}};
        int sum = calSum(arr);
        System.out.println(sum);
    }

    private static int calSum(int[][] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[i].length; j++) {
                sum += arr[i][j];
            }
        }
        return sum;
    }
}
