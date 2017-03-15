package com.javarush.task.task31.task3102;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/* 
Находим все файлы
*/
public class Solution {
    public static void main(String[] args) {
        try{
        getFileTree("/test").forEach(System.out::println);

            }catch (IOException e){}
    }
    public static List<String> getFileTree(String root) throws IOException {

        List<Path> p = Files.walk(Paths.get(root)).filter(e-> Files.isRegularFile(e)).collect(Collectors.toList());
        List<String> s = new ArrayList<>();
        p.forEach(e -> s.add(e.toString()));
        return s;
    }
}
