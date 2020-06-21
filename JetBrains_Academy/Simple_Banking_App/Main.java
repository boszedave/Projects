package banking;

import java.util.*;
import java.sql.*;

//TODO this project is CRYING for OOP, DO IT!

public class Main {
    public static void main(String[] args) {

        String fileName = "";

        if (args.length > 0) {
            if (args[0].equals("-fileName")) {
                fileName = args[1];
            }
        }
        else {
            fileName = "card.s3db";
        }
        Database db = new Database(fileName);
        db.createNewDatabase();
        db.createNewTable();
        AccountManager acc = new AccountManager(fileName);
        acc.startApplication();


        /*


        */

    }
}
