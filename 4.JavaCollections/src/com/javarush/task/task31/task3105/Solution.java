package com.javarush.task.task31.task3105;



import java.io.*;


import java.nio.file.Files;

import java.nio.file.Paths;


import java.util.HashMap;

import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/* 
Добавление файла в архив
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        String file = args[0];
        String zipArch = args[1];
        Map<String,byte[]> map = new HashMap<>();
        byte[] temp;
        long entrySize = 0;
        ZipEntry entry;
        int counter = 0;
        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(zipArch))){
            while((entry = zis.getNextEntry()) != null) {
                entrySize = entry.getSize();
                temp = new byte[(int)entrySize];
                while (counter < entrySize) {
                    temp[counter] = (byte) zis.read();
                    counter++;
                }
                map.put(entry.getName(), temp);
                counter = 0;

                zis.closeEntry();
            }
        }
        String name = file.substring(file.lastIndexOf("/")+1);
        byte[] byteFile = Files.readAllBytes(Paths.get(file));
        boolean checker = false;
        try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipArch))){
            for (Map.Entry<String, byte[]> e : map.entrySet()) {
                if (Paths.get(e.getKey()).getFileName().toString().equals(name)) {
                    checker = true;
                    zos.closeEntry();
                    continue;
                }
                ZipEntry zentry = new ZipEntry(e.getKey());
                zos.putNextEntry(zentry);
                zos.write(e.getValue());
                zos.closeEntry();
            }
            if(!checker){
                Files.copy(Paths.get(file),zos);
            }
        }
    }


}
