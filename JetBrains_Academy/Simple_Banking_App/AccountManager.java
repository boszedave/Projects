package banking;

import java.util.Scanner;

public class AccountManager {
    private Scanner scanner = new Scanner(System.in);
    private Database db;

    public AccountManager(String fileName) {
        this.db = new Database(fileName);
    }

    public void startApplication() {
        int menuOption;
        do {
            System.out.println("1. Create account\n2. Log into account\n0. Exit\n");
            menuOption = scanner.nextInt();
            switch (menuOption) {
                case 1:
                    createCard();
                    break;
                case 2:
                    loginToAccount();
                    break;
                case 0:
                    menuOption = 0;
                    break;
                default:
                    System.out.println("Wrong input, try again!");
            }
            if (menuOption == 0) {
                System.out.println("Bye");
            }
        } while (menuOption != 0);
    }

    public void createCard() {
        BankCard bcard = new BankCard();
        db.insertIntoDB(bcard.getCardNumber(), bcard.getCardPin(), 0);

        System.out.println("\nYour card have been created");
        System.out.println("Your card number: " + bcard.getCardNumber());
        System.out.println("Your PIN: " + bcard.getCardPin());
    }

    public void loginToAccount() {
        String cardNumberInput;
        String cardPinInput;
        BankCard selectedCard;

        scanner.nextLine();
        System.out.println("Enter your card number:");
        cardNumberInput = scanner.nextLine();
        System.out.println("Enter your PIN:");
        cardPinInput = scanner.nextLine();

        selectedCard = db.selectCard(cardNumberInput, cardPinInput);

        if (!db.checkIfAccountExists(cardNumberInput, cardPinInput)) {
            System.out.println("Wrong card number AND/OR PIN! Try again!");
        } else {
            System.out.println("You have successfully logged in!");
            accountManagement(selectedCard);
        }
    }

    public void accountManagement(BankCard selectedCard) {
        int menuOption;
        do {
            System.out.println("1. Balance\n2. Add income\n3. Do transfer\n4. Close Account\n5. Log out\n0. Exit");
            menuOption = scanner.nextInt();
            switch (menuOption) {
                case 1:
                    //read balance from DB
                    viewAccountBalance(selectedCard);
                    break;
                case 2:
                    //deposit money
                    addIncome(selectedCard);
                    break;
                case 3:
                    //this line is needed for prevent scanner bugging...
                    scanner.nextLine();
                    //transfer money
                    transferMoney(selectedCard);
                    break;
                case 4:
                    //delete row from DB
                    deleteAccount(selectedCard);
                    menuOption = 0;
                    break;
                case 5:
                    System.out.println("\nYou have successfully logged out!\n");
                    menuOption = 0;
                    break;
                case 0:
                    menuOption = 0;
                    System.out.println("You have logged out!");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + menuOption);
            }
        } while ( menuOption != 0);
    }

    public void viewAccountBalance(BankCard selectedCard) {
        db.getBalance(selectedCard);
        System.out.println("\nBalance: " + db.getBalance(selectedCard));
    }

    public void addIncome(BankCard selectedCard) {
        System.out.println("Enter how much money you want to deposit: ");
        db.addIncome(selectedCard, scanner.nextInt());
        System.out.println("Income was added!");
    }

    public void transferMoney(BankCard selectedCard) {
        String receiverCardNumber;
        int transferAmount;
        BankCard receiverCard;

        System.out.println("Transfer");
        System.out.println("Enter card number:");

        receiverCardNumber = scanner.nextLine();

        if (receiverCardNumber.equals(selectedCard.getCardNumber())) {
            System.out.println("You can't transfer money to the same account!");
        }
        else if (!selectedCard.validateCardNumber(receiverCardNumber)) {
            System.out.println("Probably you made a mistake in card number. Please try again!");
        }
        else if (!db.checkIfTransferAccountExists(receiverCardNumber)) {
            System.out.println("Such a card does not exist.");
        }
        else {
            receiverCard = db.selectReceiverCard(receiverCardNumber);
            System.out.println("Enter how much money you want to transfer:");
            transferAmount = scanner.nextInt();
            if (transferAmount <= db.getBalance(selectedCard)) {
                db.addIncome(receiverCard, transferAmount);
                db.subtractMoney(selectedCard, transferAmount);
                System.out.println("Success!");
            }
            else {
                System.out.println("You don't have enough money!");
            }
        }
    }

    public void deleteAccount(BankCard selectedCard) {
        db.deleteAccount(selectedCard);
    }


}
