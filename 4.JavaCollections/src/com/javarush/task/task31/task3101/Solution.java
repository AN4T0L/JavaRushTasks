package com.javarush.task.task31.task3101;



import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/*
Проход по дереву файлов
*/
public class Solution {
    public static void main(String[] args) throws IOException{
        final String path = args[0];
        final String resultFileAbsolutePath = args[1];
        String dirResFile = Paths.get(resultFileAbsolutePath).getParent().toString();
        List<String> list = new LinkedList<>();

        Files.walk(Paths.get(path)).map(String::valueOf).filter(f -> f.endsWith(".txt")).sorted().forEach(file -> {

            try {
                Path o = Paths.get(file);
                if(Files.size(o)>50 && !o.getFileName().toString().equals(resultFileAbsolutePath)) {
                    Files.delete(o);
                }
                else if(Files.size(o)<50 && !o.getFileName().toString().equals(resultFileAbsolutePath))
                    list.add(o.getFileName().toString());
                else deleteFile(o.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        String resultFileAbsoluteNewPath = dirResFile+"/allFilesContent.txt";
        FileUtils.renameFile(Paths.get(resultFileAbsolutePath).toFile(),Paths.get(resultFileAbsoluteNewPath).toFile());

        Path p = Paths.get(resultFileAbsoluteNewPath);
        try (BufferedWriter writer = Files.newBufferedWriter(p)) {
            list.forEach(s -> {
                    try{writer.write(s+"\n");}
                    catch (IOException e){}
                });

        }
    }

    public static void deleteFile(File file) {
        if (!file.delete()) System.out.println("Can not delete file with name " + file.getName());
    }
}
