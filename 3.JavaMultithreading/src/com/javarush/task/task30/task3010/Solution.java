package com.javarush.task.task30.task3010;

/* 
Минимальное допустимое основание системы счисления
*/

public class Solution {
    public static void main(String[] args) {
            try {
            int maxRadixCount = 0;
            Character[] radix = new Character[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q',
                    'r', 's','t','u','v','w','x','y','z'};
            String s = args[0];
            if (s.length() > 255) throw new NumberFormatException();
            Character[] c = new Character[s.length()];
            for (int i = 0; i < s.length(); i++) {
                c[i] = s.charAt(i);
                if (c[i] < 48 || (c[i] > 57 && c[i] < 65) || (c[i] > 90 && c[i] < 97) || c[i]>122) {
                    System.out.println("incorrect");
                    throw new NumberFormatException();
                }
                c[i] = Character.toLowerCase(c[i]);
                if (c[i] > maxRadixCount) maxRadixCount = c[i];
            }
            for (int i = 0; i < radix.length; i++) {



                if((int)radix[i] == maxRadixCount){
                    if(radix[i]=='0') System.out.println("2");
                    else System.out.println(i+1);

                }
            }

        } catch (Exception e) {
        }
    }
}