package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    static List<Account> accounts = new ArrayList<>();


    public static void main(String[] args) {

        AccountDAO accountDAO = new AccountDAO(args[1]);
        accountDAO.loadAccountsFromDBtoList(accounts);
        menu(accountDAO);

    }

    static void menu(AccountDAO accountDAO) {
        int choice = 1;
        while (choice != 0) {
            System.out.println("1. Create an account\n2. Log into account\n0. Exit");
            try {
                choice = Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Wrong input!");
                break;
            }

            switch (choice) {
                case 1:
                    Account account = new Account();
                    accounts.add(account);
                    account.createCard();
                    account.createPIN();
                    System.out.println("Your account has been created");
                    System.out.println("Your card number:\n" + account.getCardNumber());
                    System.out.println("Your card PIN:\n" + account.getCardPin());
                    accountDAO.addAccount(account);
                    break;
                case 2:
                    System.out.println("Enter card number:");
                    String cardNumber = scanner.nextLine();
                    System.out.println("Enter card PIN:");
                    String cardPin = scanner.nextLine();
                    Account acc = findAccount(cardNumber, cardPin);
                    if (acc != null) {
                        cardMenu(acc, accountDAO);
                    } else {
                        System.out.println("Wrong input!");
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Wrong input!");

            }
        }
    }

    static void cardMenu(Account account, AccountDAO accountDAO) {

        System.out.println("You have successfully logged in!");
        boolean isActive = true;
        while (isActive) {
            System.out.println("1. Balance\n2. Add income\n3. Do transfer\n4. Close account\n5.Log out \n0.Exit");
            String choice = scanner.next();
            scanner.nextLine();
            System.out.println();
            switch (Integer.parseInt(choice)) {
                case 1:
                    System.out.println(account.getBalance());
                    break;

                case 2:
                    System.out.println("Enter income:");
                    account.addIncome(scanner.nextDouble());
                    accountDAO.updateBalance(account);
                    System.out.println("Income was added!");
                    break;
                case 3:
                    boolean isFound = false;
                    System.out.println("Enter card number:");
                    String cardNumberReciever = scanner.nextLine();
                    if (cardNumberReciever.length() != 16) {
                        System.out.println("Wrong input!");
                        break;
                    }
                    if (Account.checkIfValid(cardNumberReciever)) {
                        for (Account receiver : accounts) {        //iterira po accountima u listi i provjerava card number
                            if (receiver.getCardNumber().equals(cardNumberReciever)) {
                                System.out.println("Enter the money amount that you wish to transfer:");
                                double transferAmount = scanner.nextDouble();   // accounts are receiver(receiver) and account(sender)
                                transferMoney(account, receiver, transferAmount);
                                accountDAO.updateBalance(account);
                                accountDAO.updateBalance(receiver);
                                isFound = true;
                            }
                        }
                        if (!isFound) {
                            System.out.println("This card does not exist in the system!"); //ovo ispise ako broj kartice podlijeze luhn alg. ali je nema u db/programu
                        }
                    } else {
                        System.out.println("You probably made a mistake in the card number. Please try again!"); // ako nije prosla luhn algoritam check, checkifvalid()
                    }
                    break;

                case 4:
                    accounts.remove(account);
                    accountDAO.deleteAccount(account);
                    System.out.println("The account has been closed!\n");
                    isActive = false;
                    break;
                case 5:
                    isActive = false;
                    System.out.println("You have successfully logged out!");
                    break;
                case 0:
                    System.out.println("Bye!");
                    System.exit(0);
            }
        }

    }

    static Account findAccount(String cardNumber, String cardPIN) {
        for (int i = 0; i < accounts.size(); i++) {
            if (cardNumber.equals(accounts.get(i).getCardNumber()) && cardPIN.equals(accounts.get(i).getCardPin())) {
                return accounts.get(i);
            }
        }
        return null;


    }

    static void transferMoney(Account sender, Account receiver, double transferAmount) {
        if (sender.getBalance() < transferAmount) {
            System.out.println("Not enough money!");
            return;
        }
        receiver.addIncome(transferAmount);
        sender.setBalance(sender.getBalance() - transferAmount);
        System.out.println("Success!");
    }



}