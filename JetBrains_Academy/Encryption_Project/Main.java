package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;


public class Main {
    public static void main(String[] args) throws IOException {

        //creating an arraylist from main args array
        ArrayList<String> argsArrList = new ArrayList<>();
        Collections.addAll(argsArrList, args);

        //variables
        boolean encMode = false;
        int key = -1;
        String dataToCrypt = "";
        char[] outputData = new char[0];
        int outArgument = -1;
        String outputFilePath = "";
        String inputFilePath = "";

        //checking "-mode"
        encMode = checkMode(argsArrList);
        //checking "-key"
        key = checkKey(argsArrList);
        //checking "-out"
        outArgument = checkOut(argsArrList);

        //check if there is "-data" and/or "-in"
        if ( (argsArrList.contains("-data") && argsArrList.contains("-in"))
                || (argsArrList.contains("-data") && !argsArrList.contains("-in")) ) {
            //creating a char array to crypt it
            dataToCrypt = argsArrList.get(argsArrList.indexOf("-data")+1).toString();
            outputData = dataToCrypt.toCharArray();
            //check cryptography then set the output data
            setOutputData(encMode, key, dataToCrypt, outputData);
            //checking if input is from standard input/file => writing to standard output/file
            writingOutputData(argsArrList, outputData, outArgument);
        }

        else if (!argsArrList.contains("-data") && argsArrList.contains("-in")) {
            //reading from file
            inputFilePath = argsArrList.get(argsArrList.indexOf("-in")+1).toString();
            File inFile = new File(inputFilePath);
            Scanner scan = new Scanner(inFile);
            //creating a char array to crypt it
            dataToCrypt = scan.nextLine();
            outputData = dataToCrypt.toCharArray();
            scan.close();
            //check cryptography then set the output data
            setOutputData(encMode, key, dataToCrypt, outputData);
            //checking if input is from standard input/file => writing to standard output/file
            writingOutputData(argsArrList, outputData, outArgument);
        }
        else {
            dataToCrypt = "";
        }
    }

    private static int checkOut(ArrayList<String> argsArrList) {
        int outArgument;
        if (argsArrList.contains("-out")) {
            outArgument = 1;    //write to file
        }
        else {
            outArgument = 0;   //write to standard output
        }
        return outArgument;
    }

    private static int checkKey(ArrayList<String> argsArrList) {
        int key;
        if (argsArrList.contains("-key")) {
            //converting string "key" to int
            key = Integer.parseInt(argsArrList.get(argsArrList.indexOf("-key")+1).toString());
        }
        else {
            key = 0;
        }
        return key;
    }

    private static boolean checkMode(ArrayList<String> argsArrList) {
        boolean encMode;
        if (!argsArrList.contains("-mode")) {
            encMode = true;     //encryption
        }
        else {
            if (argsArrList.get(argsArrList.indexOf("-mode")+1).equals("enc")) {
                encMode = true;     //encryption
            }
            else {
                encMode = false;    //decryption
            }
        }
        return encMode;
    }

    private static void setOutputData(boolean encMode, int key, String dataToCrypt, char[] outputData) {
        if (encMode) {      //encryption
            for(int i=0; i<dataToCrypt.length(); i++) {
                int asciiC = (int) outputData[i];
                outputData[i] = (char) (asciiC + key);
            }
        }
        else {              //decryption
            for(int i=0; i<dataToCrypt.length(); i++) {
                int asciiC = (int) outputData[i];
                outputData[i] = (char) (asciiC - key);
            }
        }
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
                //creating a new file, give it a path
                outputFilePath = argsArrList.get(argsArrList.indexOf("-out")+1).toString();
                File file = new File(outputFilePath);
                FileWriter outFile = null;
                //checking if file exists
                if (file.exists()) {
                    //System.out.println("Rewriting file...");
                } else {
                    //System.out.println("New file created...");
                    file.createNewFile();
                }
                outFile = new FileWriter(file);
                //writing to file
                try {
                    outFile.write(outputData);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error!");
                }
                //closing file
                try {
                    outFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error!");
                }
                break;
        }
    }
}




