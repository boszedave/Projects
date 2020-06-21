package banking;

import java.security.SecureRandom;

public class BankCard {
    private String cardNumber;
    private String cardPin;
    private int balance = 0;

    public BankCard() {
        final String BIN = "400000";

        SecureRandom secRand = new SecureRandom();
        cardNumber = BIN + String.format("%09d", secRand.nextInt(1000000000));
        cardNumber = cardNumber + checkSum(cardNumber);
        cardPin = String.format("%04d", secRand.nextInt(10000));
    }

    public static String checkSum(String cardNumber) {
        int[] number = new int[cardNumber.length()];
        int sum = 0;
        for(int i = 0; i < cardNumber.length() ; i++){
            number[i] = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            if (i % 2 == 0) {
                if (number[i] * 2 > 9) {
                    number[i] = number[i] * 2 - 9;
                } else {
                    number[i] = number[i] * 2;
                }
            }
            sum = sum + number[i];
        }
        return String.valueOf((10 - (sum % 10) == 10) ? 0 : (10 - (sum % 10)));
    }

    public static boolean validateCardNumber(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardPin() {
        return cardPin;
    }

    public void setCardPin(String cardPin) {
        this.cardPin = cardPin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
