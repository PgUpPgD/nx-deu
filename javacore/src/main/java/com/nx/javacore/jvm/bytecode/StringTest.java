package com.nx.javacore.jvm.bytecode;


public class StringTest {

    public static void m1(){
        String str = "";
        for (int i=0;i<10;i++){
            str = str+"nx,";
        }
        System.out.println(str);

    }

    public static void m2(){
        StringBuffer str = new StringBuffer();
        for (int i=0;i<10;i++){
            str.append("nx,");
        }
        System.out.println(str);

    }

    public static String m3(){
        String str = "hello";
        try {
           return str; //0
        }finally {
          str = "nx"; //1
        }
    }

    public static void main(String[] args) {
        System.out.println(m3());
    }

}
