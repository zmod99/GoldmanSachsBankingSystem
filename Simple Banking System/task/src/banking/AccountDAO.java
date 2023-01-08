package banking;

import java.sql.*;
import java.util.List;

public class AccountDAO {

    private Connection conn;

    public AccountDAO(String fileName) {
        try {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS card (\n" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                    "number VARCHAR NOT NULL, \n" +
                    "pin VARCHAR NOT NULL, \n" +
                    "balance INTEGER DEFAULT 0);";
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            Statement statement = conn.createStatement();
            statement.execute(createTableSQL);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadAccountsFromDBtoList(List<Account> accountList) {
        String selector = "SELECT * FROM card";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selector);
            while (resultSet.next()) {
                Account account = new Account();
                account.setCardNumber(resultSet.getString("number"));
                account.setCardPin(resultSet.getString("pin"));
                account.setBalance(resultSet.getInt("balance"));
                accountList.add(account);
            }
            statement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addAccount(Account account) {
        try {
            Statement statement = conn.createStatement();
            String insertSQL = "INSERT INTO card(number,pin,balance) VALUES("
                    + account.getCardNumber()
                    + "," + account.getCardPin()
                    + "," + account.getBalance()
                    + ")";

            statement.execute(insertSQL);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBalance(Account account) {
        try {
            Statement statement = conn.createStatement();
            String updateSQL = "UPDATE card SET balance=" + account.getBalance() + " WHERE number=" + account.getCardNumber();
            statement.execute(updateSQL);
            statement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteAccount(Account account) {
        try {
            Statement statement = conn.createStatement();
            String deleteRowSQL = "DELETE FROM card WHERE number=" + account.getCardNumber();
            statement.execute(deleteRowSQL);
            statement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
