package com.nx.collection.array;

public class SparseArray {

    public static void main(String[] args) {

        int[][] chessArr = new int[11][11];
        chessArr[1][2] = 1;
        chessArr[2][3] = 2;
        for (int[] row:chessArr){
            for (int item:row){
                System.out.printf("%d\t",item);
            }
            System.out.println();
        }

        //计算原来数组中有多少个非空值
        int sum = 0;
        for (int i = 0; i < chessArr.length; i++) {
            for (int j = 0; j < chessArr[i].length; j++) {
                if (chessArr[i][j] != 0){
                    sum++;
                }
            }
        }

        //构造稀疏数组
        int[][] sparseArr = new int[sum+1][3];
        sparseArr[0][0] = 11;
        sparseArr[0][1] = 11;
        sparseArr[0][2] = sum;
        //补充稀疏数组中其他的数据
        int rowNum=0;
        for (int i = 0; i < chessArr.length; i++) {
            for (int j = 0; j < chessArr[i].length; j++) {
                if (chessArr[i][j] != 0){
                    rowNum++;
                    sparseArr[rowNum][0] = i;
                    sparseArr[rowNum][1] = j;
                    sparseArr[rowNum][2] = chessArr[i][j];
                }
            }
        }

        //输出稀疏数组
        System.out.println("-------------------打印稀疏数组---------------------");
        for (int i=0;i<sparseArr.length;i++){
            System.out.printf("%d\t%d\t%d\n",sparseArr[i][0],sparseArr[i][1],sparseArr[i][2]);
        }

        //把稀疏数组恢复成二维数组
        int[][] chessArr1 = new int[sparseArr[0][0]][sparseArr[0][1]];
        for (int i = 1; i < sparseArr.length; i++) {
            //给二维数组赋值  该行         该列              值为
            chessArr1[sparseArr[i][0]][sparseArr[i][1]] = sparseArr[i][2];
        }

        System.out.println("--------------------恢复的二维数组-----------------------");
        for (int[] row:chessArr1){
            for (int item:row){
                System.out.printf("%d\t",item);
            }
            System.out.println();
        }

    }


}
