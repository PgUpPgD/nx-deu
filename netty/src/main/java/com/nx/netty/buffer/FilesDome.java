package com.nx.netty.buffer;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * 遍历目录文件
 */
public class FilesDome {

    private static Path path = Paths.get("E:\\maven\\maven_repository\\dom4j");
    private static AtomicInteger dirCount = new AtomicInteger();
    private static AtomicInteger fileCount = new AtomicInteger();

    /**
     Path 用来表示文件路径  Paths 是工具类，用来获取 Path 实例
     Files 操作文件
     */
    public static void main(String[] args) throws IOException {
        copyFile();
    }

    //遍历文件夹
    public static void walkFiles() throws IOException{
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            //进入一个文件夹之前
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException{
                System.out.println("===> " + dir);
                dirCount.incrementAndGet();
                return super.preVisitDirectory(dir, attrs);
            }

            //遍历到一个文件
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                System.out.println(file);
                fileCount.incrementAndGet();
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(dirCount);
        System.out.println(fileCount);
    }

    //统计jar的数目
    public static void jarCount() throws IOException{
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)throws IOException{
                if (file.toFile().getName().endsWith(".jar")){
                    fileCount.incrementAndGet();
                }
                return super.visitFile(file, attrs);
            }
        });
        System.out.println(fileCount);
    }

    //删除多级目录
    public static void deleteFile() throws IOException {
        path = Paths.get("D:\\nx");
        Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.delete(file);
                return super.visitFile(file, attrs);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {
                Files.delete(dir);
                return super.postVisitDirectory(dir, exc);
            }
        });
    }

    //拷贝多级目录
    public static void copyFile() throws IOException {
        long start = System.currentTimeMillis();
        String source = "D:\\test\\java_log";
        String target = "D:\\test\\java_log_1";
        Path path = Paths.get(source);
        Stream<Path> stream = Files.walk(path);
        stream.forEach(path1 -> {
            try {
                //替换前面的路径
                String targetName = path1.toString().replace(source, target);
                // is directory
                if (Files.isDirectory(path)){
                    Files.createDirectory(Paths.get(targetName));
                }
                // is regular file
                if (Files.isRegularFile(path)){
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
