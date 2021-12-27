package com.nx.collection.stack;

import java.util.Stack;

public class Solution {

    public boolean isValid(String s){
        Stack<Character> sta = new Stack<>();
        for (int i=0;i<s.length();i++){
            char c = s.charAt(i);
            if(c=='('||c=='['||c=='{'){
                sta.push(c);
            }else {
                if (sta.isEmpty()) return false;
                Character topChar = sta.pop();
                if(c==')'&&topChar!='('){
                    return false;
                }
                if(c==']'&&topChar!='['){
                    return false;
                }
                if(c=='}'&&topChar!='{'){
                    return false;
                }
            }
        }
        return sta.isEmpty();
    }
}
