package encryptdecrypt;

import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

class CaesarCipher {
    //shifting algorithm
    public String doShift(String text, int shift) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c >= 'A' && c <= 'Z')
                chars[i] = (char) ((c - 'A' + shift) % 26 + 'A');
            else if (c >= 'a' && c <= 'z')
                chars[i] = (char) ((c - 'a' + shift) % 26 + 'a');
        }
        return new String(chars);
    }

    public String decryptMessage(String message, int shift) {
        return doShift(message, 26 - shift);
    }
    public String encryptMessage(String message, int shift) {
        return doShift(message, shift);
    }
}

public class Main extends CaesarCipher {
    public static void main(String[] args) throws IOException {
        ArrayList<String> argsArrList = new ArrayList<>();
        Collections.addAll(argsArrList, args);
        boolean encMode;
        int algMode;
        int key;
        int outArgument;
        String dataToCrypt;
        char[] outputData;
        String inputFilePath;

        encMode = checkMode(argsArrList);
        algMode = checkAlg(argsArrList);
        key = checkKey(argsArrList);
        outArgument = checkOut(argsArrList);

        if ((argsArrList.contains("-data") && argsArrList.contains("-in"))
                || (argsArrList.contains("-data") && !argsArrList.contains("-in"))) {
            //making a char array to crypt it
            dataToCrypt = argsArrList.get(argsArrList.indexOf("-data") + 1).toString();
            outputData = dataToCrypt.toCharArray();
            //check cryptography then making the output data
            outputData = setOutputData(algMode, encMode, key, dataToCrypt, outputData);
            //checking if input is from standard input/file => writing to standard output/file
            writingOutputData(argsArrList, outputData, outArgument);
        } else if (!argsArrList.contains("-data") && argsArrList.contains("-in")) {
            //reading the input from file
            inputFilePath = argsArrList.get(argsArrList.indexOf("-in") + 1).toString();
            File inFile = new File(inputFilePath);
            Scanner scan = new Scanner(inFile);
            dataToCrypt = scan.nextLine();
            outputData = dataToCrypt.toCharArray();
            scan.close();
            outputData = setOutputData(algMode, encMode, key, dataToCrypt, outputData);
            writingOutputData(argsArrList, outputData, outArgument);
        }
    }

    private static int checkAlg(@NotNull ArrayList<String> argsArrList) {
        boolean shiftMode;
        boolean unicodeMode;
        if (argsArrList.get(argsArrList.indexOf("-alg") + 1).equals("shift")) {
            shiftMode = true;
            return 1;
        } else if (argsArrList.get(argsArrList.indexOf("-alg") + 1).equals("unicode")) {
            unicodeMode = true;
            return 2;
        } else {
            return 1;
        }
    }

    private static boolean checkMode(ArrayList<String> argsArrList) {
        boolean encMode;
        if (!argsArrList.contains("-mode")) {
            //encryption
            encMode = true;
        } else {
            if (argsArrList.get(argsArrList.indexOf("-mode") + 1).equals("enc")) {
                encMode = true;
            } else {
                //decryption
                encMode = false;
            }
        }
        return encMode;
    }

    private static int checkOut(ArrayList<String> argsArrList) {
        int outArgument;
        if (argsArrList.contains("-out")) {
            //write to file
            outArgument = 1;
        } else {
            //write to standard output
            outArgument = 0;
        }
        return outArgument;
    }

    private static int checkKey(ArrayList<String> argsArrList) {
        int key;
        if (argsArrList.contains("-key")) {
            key = Integer.parseInt(argsArrList.get(argsArrList.indexOf("-key") + 1).toString());
        } else {
            key = 0;
        }
        return key;
    }

    private static char[] setOutputData(int algMode, boolean encMode, int key, String dataToCrypt, char[] outputData) {
        CaesarCipher cipher = new CaesarCipher();
        switch (algMode){
            //shifting
            case 1:
                outputData = setOutputDataForShiftingCrypt(encMode, key, dataToCrypt, cipher);
                break;
            //unicode
            case 2:
                outputData = setOutputDataForUniCodeCrypt(encMode, key, dataToCrypt, outputData);
                break;
            default:
                System.out.println("Error");
                break;
        }
        return outputData;
    }

    private static char[] setOutputDataForShiftingCrypt(boolean encMode, int key, String dataToCrypt, CaesarCipher cipher) {
        char[] outputData;
        if (encMode) {
            outputData = cipher.encryptMessage(dataToCrypt, key).toCharArray();
        } else {
            outputData = cipher.decryptMessage(dataToCrypt, key).toCharArray();
        }
        return outputData;
    }

    private static char[] setOutputDataForUniCodeCrypt(boolean encMode, int key, String dataToCrypt, char[] outputData) {
        if (encMode) {
            for(int i=0; i<dataToCrypt.length(); i++) {
                int asciiC = (int) outputData[i];
                outputData[i] = (char) (asciiC + key);
            }
        }
        else {
            for(int i=0; i<dataToCrypt.length(); i++) {
                int asciiC = (int) outputData[i];
                outputData[i] = (char) (asciiC - key);
            }
        }
        return outputData;
    }

    private static void writingOutputData(ArrayList<String> argsArrList, char[] outputData, int outArgument) throws IOException {
        String outputFilePath;
        switch (outArgument) {
            //standard output
            case 0:
                String output = new String(outputData);
                System.out.println(output);
                break;
            //write to file
            case 1:
                outputFilePath = argsArrList.get(argsArrList.indexOf("-out")+1).toString();
                File file = new File(outputFilePath);
                FileWriter outFile = null;
                if (file.exists()) {
                    // System.out.println("Rewriting file...");
                } else {
                    //System.out.println("New file created...");
                    file.createNewFile();
                }
                outFile = new FileWriter(file);
                outFile.write(outputData);
                outFile.close();
                break;
        }
    }
}







