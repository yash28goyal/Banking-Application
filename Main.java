import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int minBalanceToOpen = 1000;
    private static final int minBalanceRemainAfterWithdraw = 1000;

    public static void main(String[] args) {
        BankingApplication bankingApp = new BankingApplication();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nAvailable Choices:");
            System.out.println("1. Create a new Account");
            System.out.println("2. Display all accounts");
            System.out.println("3. Update an account");
            System.out.println("4. Delete an account");
            System.out.println("5. Deposit an amount into your account");
            System.out.println("6. Withdraw an amount from your account");
            System.out.println("7. Search for an account");
            System.out.println("8. Exit");

            choice = bankingApp.getValidatedIntegerInput(scanner, "Enter your choice: ");

            switch (choice) {
                case 1 -> bankingApp.createAccount();
                case 2 -> bankingApp.displayAllAccounts();
                case 3 -> {
                    int accNum = bankingApp.getValidatedIntegerInput(scanner, "Enter account number: ");
                    bankingApp.updateAccountBalance(accNum);
                }
                case 4 -> {
                    int accNum = bankingApp.getValidatedIntegerInput(scanner, "Enter account number to delete: ");
                    bankingApp.deleteAccount(accNum);
                }
                case 5 -> {
                    int accNum = bankingApp.getValidatedIntegerInput(scanner, "Enter account number to deposit: ");
                    bankingApp.depositAmount(accNum);
                }
                case 6 -> {
                    int accNum = bankingApp.getValidatedIntegerInput(scanner, "Enter account number to withdraw: ");
                    bankingApp.withdrawAmount(accNum);
                }
                case 7 -> {
                    int accNum = bankingApp.getValidatedIntegerInput(scanner, "Enter account number to search: ");
                    bankingApp.searchAccount(accNum);
                }
                case 8 -> System.out.println("Exiting application. Thank you!");
                default -> System.out.println("❌ Invalid Choice!");
            }
        } while (choice != 8);
    }

    private static class BankingApplication {
        private final List<Account> accounts = new ArrayList<>();

        private boolean isAccountNumberUnique(int accountNumber) {
            for (Account account : accounts) {
                if (account.number == accountNumber) return false;
            }
            return true;
        }

        private boolean isValidName(String name) {
            return name.matches("^[a-zA-Z ]+$");
        }

        private boolean isValidDate(String date) {
            return date.matches("^\\d{4}-\\d{2}-\\d{2}$");
        }

        private int getValidatedIntegerInput(Scanner scanner, String prompt) {
            int value;
            while (true) {
                System.out.print(prompt);
                if (scanner.hasNextInt()) {
                    value = scanner.nextInt();
                    scanner.nextLine(); // consume leftover newline
                    break;
                } else {
                    System.out.println("❌ Invalid input! Please enter a numeric value.");
                    scanner.next(); // discard invalid token
                }
            }
            return value;
        }

        void createAccount() {
            Scanner scanner = new Scanner(System.in);

            int choice;
            while (true) {
                System.out.println("Select account type\n1. Salary Account\n2. Savings Account\n3. Current Account");
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    if (choice >= 1 && choice <= 3) break;
                    else System.out.println("❌ Please select between 1 to 3.");
                } else {
                    System.out.println("❌ Invalid input. Enter a number.");
                    scanner.next();
                }
            }
            scanner.nextLine(); // consume newline

            String name;
            while (true) {
                System.out.print("Enter name: ");
                name = scanner.nextLine();
                if (!isValidName(name)) {
                    System.out.println("❌ Name should contain only alphabets and spaces. No digits/symbols allowed.");
                } else break;
            }

            int number;
            while (true) {
                number = getValidatedIntegerInput(scanner, "Enter account number (numeric only): ");
                if (!isAccountNumberUnique(number)) {
                    System.out.println("❌ Account Number already exists! Please use a different number.");
                } else break;
            }

            String creationDate;
            while (true) {
                System.out.print("Enter creation date (YYYY-MM-DD): ");
                creationDate = scanner.nextLine();
                if (!isValidDate(creationDate)) {
                    System.out.println("❌ Invalid date format. Please use YYYY-MM-DD format.");
                } else break;
            }

            int balance;
            while (true) {
                balance = getValidatedIntegerInput(scanner, "Enter account balance: ");
                if (balance < minBalanceToOpen) {
                    System.out.println("❌ Opening balance must be at least ₹" + minBalanceToOpen);
                } else break;
            }

            switch (choice) {
                case 1 -> accounts.add(new SalaryAccount(name, number, creationDate, balance));
                case 2 -> accounts.add(new SavingsAccount(name, number, creationDate, balance));
                case 3 -> accounts.add(new CurrentAccount(name, number, creationDate, balance));
            }

            System.out.println("✅ Account created successfully!");
        }

        void displayAllAccounts() {
            if (accounts.isEmpty()) {
                System.out.println("⚠️ No accounts found.");
                return;
            }
            for (Account account : accounts) account.display();
        }

        private boolean isAccountExist(int accountNumber) {
            for (Account account : accounts) {
                if (account.number == accountNumber) return true;
            }
            return false;
        }

        void updateAccountBalance(int accountNumber) {
            if (!isAccountExist(accountNumber)) {
                System.out.println("❌ Account not found!");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            int amount = getValidatedIntegerInput(scanner, "Enter amount to update (can be +ve or -ve): ");

            for (Account account : accounts) {
                if (account.number == accountNumber) {
                    account.updateBalance(amount);
                    System.out.println("✅ Balance updated successfully!");
                    return;
                }
            }
        }

        void deleteAccount(int accountNumber) {
            for (Account account : accounts) {
                if (account.number == accountNumber) {
                    accounts.remove(account);
                    System.out.println("✅ Account deleted successfully!");
                    return;
                }
            }
            System.out.println("❌ Account not found!");
        }

        void depositAmount(int accountNumber) {
            if (!isAccountExist(accountNumber)) {
                System.out.println("❌ Account not found!");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            int amount = getValidatedIntegerInput(scanner, "Enter amount to be deposited: ");
            if (amount <= 0) {
                System.out.println("❌ Deposit amount must be positive!");
                return;
            }

            for (Account account : accounts) {
                if (account.number == accountNumber) {
                    account.updateBalance(amount);
                    System.out.println("✅ Amount deposited successfully!");
                    return;
                }
            }
        }

        void withdrawAmount(int accountNumber) {
            if (!isAccountExist(accountNumber)) {
                System.out.println("❌ Account not found!");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            int amount;
            while (true) {
                amount = getValidatedIntegerInput(scanner, "Enter amount to be withdrawn: ");
                if (!isPossibleWithdraw(accountNumber, amount)) {
                    System.out.println("❌ Cannot withdraw. Minimum balance ₹" + minBalanceRemainAfterWithdraw + " must be maintained.");
                } else break;
            }

            for (Account account : accounts) {
                if (account.number == accountNumber) {
                    account.updateBalance(-amount);
                    System.out.println("✅ Amount withdrawn successfully!");
                    return;
                }
            }
        }

        private boolean isPossibleWithdraw(int accountNumber, int amount) {
            for (Account account : accounts) {
                if (account.number == accountNumber) {
                    return account.balance - amount >= minBalanceRemainAfterWithdraw;
                }
            }
            return false;
        }

        void searchAccount(int accountNumber) {
            for (Account account : accounts) {
                if (account.number == accountNumber) {
                    System.out.println("✅ Account found:");
                    account.display();
                    return;
                }
            }
            System.out.println("❌ Account not found!");
        }
    }

    private static abstract class Account {
        String name, creationDate;
        int number, balance;

        Account(String n, int num, String date, int bal) {
            name = n;
            number = num;
            creationDate = date;
            balance = bal;
        }

        abstract void display();
        abstract void updateBalance(int amount);
    }

    private static class SavingsAccount extends Account {
        SavingsAccount(String n, int num, String date, int bal) {
            super(n, num, date, bal);
        }

        void display() {
            System.out.println("Savings Account - Name: " + name + ", Account Number: " + number +
                    ", Creation Date: " + creationDate + ", Balance: ₹" + balance);
        }

        void updateBalance(int amount) {
            balance += amount;
        }
    }

    private static class SalaryAccount extends Account {
        SalaryAccount(String n, int num, String date, int bal) {
            super(n, num, date, bal);
        }

        void display() {
            System.out.println("Salary Account - Name: " + name + ", Account Number: " + number +
                    ", Creation Date: " + creationDate + ", Balance: ₹" + balance);
        }

        void updateBalance(int amount) {
            balance += amount;
        }
    }

    private static class CurrentAccount extends Account {
        CurrentAccount(String n, int num, String date, int bal) {
            super(n, num, date, bal);
        }

        void display() {
            System.out.println("Current Account - Name: " + name + ", Account Number: " + number +
                    ", Creation Date: " + creationDate + ", Balance: ₹" + balance);
        }

        void updateBalance(int amount) {
            balance += amount;
        }
    }
}
