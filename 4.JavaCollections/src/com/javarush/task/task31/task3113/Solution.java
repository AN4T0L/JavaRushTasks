package com.javarush.task.task31.task3113;





import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//
//import java.nio.file.*;
//import java.nio.file.attribute.BasicFileAttributes;



import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;




/* 
Что внутри папки?
*/


public class Solution {
    static long Size;
    static int CountFiles = 0;
    static long FoldersInFolder = 0;
    static Path folder;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String AbsPath = reader.readLine();
        folder = Paths.get(AbsPath);
        reader.close();
        if (!Files.isDirectory(folder)) {
            AbsPath = folder.toAbsolutePath().toString();
            System.out.println(AbsPath + " - не папка");
            return;
        }
        MyFileVisitor myFileVisitor = new MyFileVisitor();
        Files.walkFileTree(folder, myFileVisitor);
        System.out.println("Всего папок - " + (FoldersInFolder-1));
        System.out.println("Всего файлов - " + CountFiles);
        System.out.println("Общий размер - " + Size);
    }

    static class MyFileVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            CountFiles++;
            Size += Files.size(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path file, BasicFileAttributes attrs) {
            FoldersInFolder++;
            return FileVisitResult.CONTINUE;
        }
    }
}
//public class Solution {
//
//    public static void main(String[] args) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        String path = br.readLine();
//        Path userPath = Paths.get(path);
//        if(!Files.isDirectory(userPath)){
//            System.out.println(userPath.toAbsolutePath() + " - не папка");
//            return;
//        }
//        long i = Files.walk(userPath,1).map(e-> e.toFile())
//                .filter(e -> e.isDirectory()).count();
//        MySimpleFileVisitor mysfv = new MySimpleFileVisitor();
//        Files.walkFileTree(userPath,mysfv);
//        System.out.println("Всего папок - "+(i-1));
//        System.out.println("Всего файлов - "+mysfv.getCounterFiles());
//        System.out.println("Общий размер - "+mysfv.getAllSize());
//
//
//
//    }
//    static class MySimpleFileVisitor extends SimpleFileVisitor{
//        private int counterFiles = 0;
//        private static long allSize = 0;
//        @Override
//        public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
//            ++counterFiles;
//            allSize+=attrs.size();
//            return super.visitFile(file, attrs);
//        }
//
//
//        public long getAllSize() {
//            return allSize;
//        }
//
//        public int getCounterFiles() {
//            return counterFiles;
//        }
//
//
//    }
//}
