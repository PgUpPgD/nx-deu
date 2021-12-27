package com.nx.collection.hash;

/**
 * 输出小写字母组成的字符串中，第一个不重复的字符的下标
 */
public class Solution {
    public static int firstUniqChar(String s){
        int[] uniqCharArray = new int[26];
        for (int i = 0; i < s.length(); i++){ //使用hash表统计了各个字母的出现字数
            uniqCharArray[s.charAt(i) - 'a']++;
        }
        for(int i = 0; i < s.length(); i++){
            if(uniqCharArray[s.charAt(i) - 'a'] == 1){
                return i;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        String s = "abac";
        System.out.println(firstUniqChar(s));
    }

}
