package com.nx.javacore.jvm.cloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 加载本机指定位置上的class文件
 *  socket
 *  http
 */
public class SelfClassLoader extends ClassLoader {

    private String path;
    private String clName;


    public SelfClassLoader(String path, String clName) {
        this.path = path;
        this.clName = clName;
    }

    public SelfClassLoader(){}


    @Override
    public Class findClass(String name) {
        byte[] b = loadClassData(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) {
        name = path+name.replace(".","\\")+".class";
        System.out.println(name);
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(new File(name));
            out = new ByteArrayOutputStream();
            int i = 0;
            while ((i=in.read())!=-1){
                out.write(i);
            }
        }catch (Exception e){

        }finally {
            //关闭流
        }
        return out.toByteArray();


    }
}
