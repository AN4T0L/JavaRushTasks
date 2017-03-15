package com.javarush.task.task31.task3111;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;



public class SearchFileVisitor extends SimpleFileVisitor<Path> {

    private List<Path> foundFiles = new ArrayList<>();
    private String partOfContent;
    private String partOfName;
    private int maxSize;
    private int minSize;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String s = new String(Files.readAllBytes(file));
        long fileSize = Files.size(file);

        if((minSize == 0 || fileSize>=minSize) && (maxSize == 0 || fileSize<=maxSize)){
            if(partOfName==null || file.getFileName().toString().contains(partOfName)){
                if(partOfContent ==null)
                    foundFiles.add(file);
                else if(s.contains(partOfContent))
                    foundFiles.add(file);
            }
        }


        return super.visitFile(file, attrs);
    }

    public void setPartOfContent(String partOfContent) {
        this.partOfContent = partOfContent;
    }

    public void setPartOfName(String partOfName) {
        this.partOfName = partOfName;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public List<Path> getFoundFiles() {
        return foundFiles;
    }
}
