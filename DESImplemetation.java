 /*
 * @tile             DES Alogrithm Implementation 
 * @author           Mahedi Hasan
 * @description      developed for project submission in ICT-6541: Applied Cryptography course
 * @date             11/02/2016
 */
package desimplementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class DESImplemetation {

    private static DESAlgorithm des;
    private static String textFromFile;

    public static void main(String[] args) {
        System.out.println("\n**** DES Algorithm Implementation ****");
        Scanner input;
        int digit;
        String key, plain_text, cipher_text;

        while (true) {
            print();
            input = new Scanner(System.in);
            
            try{digit = input.nextInt();}
            catch(Exception ex){ 
                digit = 0;
            }

            switch (digit) {
                case 1: {
                    if (readDataFromAFile("key.txt")) {
                        key = textFromFile;
                        
                        if (readDataFromAFile("input.txt")) {
                            plain_text = textFromFile;
                            des_encrypt(plain_text, key);
                        }
                    }
                    break;
                }
                case 2: {
                    if (readDataFromAFile("key.txt")) {
                        key = textFromFile;
                        if (readDataFromAFile("output.txt")) {
                            cipher_text = textFromFile;;
                            des_decrypt(cipher_text, key);
                        }
                    }
                    break;
                }

                default: {
                    if (digit == 3) {
                        break;
                    }
                    System.out.println("Please type correct digit...");
                }
            }

            if (digit == 3) {
                break;
            }
        }

    }

    
    
    //this method do required padding for des algorithm
    public static String doPadding(String input_text) {
        if (input_text.length() % 8 != 0) {

            int paddingLength = 8 - input_text.length() % 8;
            for (int i = 0; i < paddingLength; i++) {
                input_text = input_text.concat(" ");
            }
        } else {
            return input_text;
        }
        return input_text;
    }

    
    
    //this method perform Electronic Code Book mode in DES Implementatiion
    public static String[] doECB(String plain_text) {
        int start = 0, end = 8;
        int noOfBlock = plain_text.length() / 8;
        String temp;
        String[] text_array = new String[noOfBlock];

        for (int i = 0; i < noOfBlock; i++) {
            temp = plain_text.substring(start, end);
            text_array[i] = temp;
            start = end;
            end = end + 8;
        }
        return text_array;
    }

    
    
    //this method read data from input file
    public static boolean readDataFromAFile(String fileName) {

        StringBuffer bf = new StringBuffer();
        try {
            File file = new File(fileName);
            int fileLenght = (int)file.length();
            if(fileLenght == 0){
                System.out.println("no file or text is found in " + fileName);
                return false;
            }
                
            byte[] buffer = new byte[fileLenght];
            FileInputStream fis = new FileInputStream(fileName);   
                
            int nRead;
            while((nRead = fis.read(buffer)) != -1) {
                bf.append(new String(buffer, Charset.forName("utf-8")));
            } 

            fis.close();

        } catch (IOException ex) {
            System.out.println(fileName + " file is not found");
            return false;
        }

        textFromFile = new String(bf);
        return true;
    }

    
    
    //this method write data into output ifle
    public static void writeDataInAFile(String text, String fileName) {

        byte[] buffer = text.getBytes(Charset.forName("utf-8"));

        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            fos.write(buffer);
            fos.close();
        } catch (IOException ex) {
            System.out.println("File not found");
        }
    }

    
    
    //this method encrypt data using des algorithm
    public static void des_encrypt(String plain_text, String key) {
        des = new DESAlgorithm(key);

        String[] plain_text_array = doECB(doPadding(plain_text));
        String cipher_block;
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < plain_text_array.length; i++) {
            cipher_block = des.encrypt(plain_text_array[i]);
            sb.append(cipher_block);
        }

        String cipher_text = new String(sb);
        writeDataInAFile(cipher_text, "output.txt");
        //System.out.println("\nCipher text: " + cipher_text);
        System.out.println("This cipher text is written in output.txt file");
    }

    
    
    //this method decrypt data using des algorithm
    public static void des_decrypt(String cipher_text, String key) {
        des = new DESAlgorithm(key);

        String[] cipher_text_array = doECB(doPadding(cipher_text));
        String plain_block;
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < cipher_text_array.length; i++) {
            plain_block = des.decrypt(cipher_text_array[i]);
            sb.append(plain_block);
        }

        String gen_plain_text = new String(sb);
        writeDataInAFile(gen_plain_text, "gen_input.txt");
        System.out.println("This generatd plain text is written in gen_input.txt file");
    }

    
    
    //this method print into system
    public static void print() {
        System.out.println("\nPlease ");
        System.out.println("Enter 1 for encrypting text from input.txt file");
        System.out.println("Enter 2 for decrypting text from output.txt file");
        System.out.println("Enter 3 for exit\n");
    }

}
