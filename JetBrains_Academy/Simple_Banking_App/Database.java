package banking;

import java.sql.*;

public class Database {
    private String url;

    public Database(String fileName) {
        this.url = "jdbc:sqlite:" + fileName;
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	number TEXT NOT NULL,\n"
                + "	pin TEXT,\n"
                + " balance INTEGER DEFAULT 0"
                + ");";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertIntoDB(String cardNumber, String cardPin, int balance) {
        String sql = "INSERT INTO card(number, pin, balance) VALUES(?,?, ?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, cardPin);
            pstmt.setInt(3, balance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addIncome(BankCard selectedCard, int incomeInput) {
        String sql = "UPDATE card SET balance = balance + ?" + "WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, incomeInput);
            pstmt.setString(2, selectedCard.getCardNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getBalance(BankCard selectedCard) {
        int balance = 0;
        String sql = "SELECT balance FROM card WHERE number =?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, selectedCard.getCardNumber());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                balance = rs.getInt("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

    public boolean checkIfAccountExists(String cardNumber, String cardPin) {
        String sql = "SELECT * FROM card WHERE number = ? AND pin = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cardNumber);
            pstmt.setString(2, cardPin);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public BankCard selectCard(String cardNumber, String cardPin) {
        BankCard selectedCard  = new BankCard();
        String resultCardNumber = "";
        String resultCardPin = "";

        String sql = "SELECT number, pin, balance FROM card WHERE number=" + cardNumber
                + " AND pin=" + cardPin;

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultCardNumber = rs.getString("number");
                resultCardPin = rs.getString("pin");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        selectedCard.setCardNumber(resultCardNumber);
        selectedCard.setCardPin(resultCardPin);
        return selectedCard;
    }

    public boolean checkIfTransferAccountExists(String cardNumber) {
        String sql = "SELECT * FROM card WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public BankCard selectReceiverCard(String cardNumber) {
        BankCard receiverCard  = new BankCard();
        String resultCardNumber = "";
        int resultBalance = 0;

        String sql = "SELECT number, balance FROM card WHERE number=" + cardNumber;

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                resultCardNumber = rs.getString("number");
                resultBalance = rs.getInt("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        receiverCard.setCardNumber(resultCardNumber);
        receiverCard.setBalance(resultBalance);
        return receiverCard;
    }

    public void subtractMoney(BankCard selectedCard, int incomeInput) {
        String sql = "UPDATE card SET balance = balance - ? WHERE number =?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, incomeInput);
            pstmt.setString(2, selectedCard.getCardNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAccount(BankCard selectedCard) {
        String sql = "DELETE FROM card WHERE number = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, selectedCard.getCardNumber());
            pstmt.executeUpdate();
            System.out.println("The account has been closed!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}
