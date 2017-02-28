package com.javarush.task.task30.task3008;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    public  static void writeMessage(String message){
        System.out.println(message);
    }
    public static String readString(){
        String s = "";
        boolean b = true;
        while(b) try {
            s = bf.readLine();
            b = false;
        } catch (IOException e) {
            writeMessage("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
        }
        return s;
    }
    public  static int readInt(){
        Integer x = null;
        boolean b = true;
        while(b) try{
            String s = readString();
            x = Integer.parseInt(s);
            b = false;
        }catch (NumberFormatException e){
            writeMessage("Произошла ошибка\n" +
                    "при попытке ввода числа. Попробуйте еще раз.");
        }
        return x;
    }
}
