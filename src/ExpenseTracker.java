import common.Expense;
import common.Income;
import common.Store;
import common.StoreType;
import java.util.ArrayList;
import java.util.Scanner;
import service.StorageService;
import storage.ExpenseStorage;
import storage.IncomeStorage;
import storage.StoreStorage;
import tools.DateUtils;

public class ExpenseTracker {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ArrayList<Expense> expenses = ExpenseStorage.loadExpenses();
            ArrayList<Income> incomes = IncomeStorage.loadIncomes();
            StorageService storageService = StorageService.getInstance();
            double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
            double totalIncomes = incomes.stream().mapToDouble(Income::getAmount).sum();
            Boolean expensesChanged = false;
            Boolean incomesChanged = false;

            while (true) {
                System.out.println("\nExpense Tracker Menu:");
                System.out.println("1. Add Expense");
                System.out.println("2. Add Income");
                System.out.println("3. View Expenses");
                System.out.println("4. View Incomes");
                System.out.println("5. View Stores");
                System.out.println("6. View Store Types");
                System.out.println("7. Profit/Loss Summary");
                System.out.println("8. View Expenses Between Dates");
                System.out.println("9. View Incomes Between Dates");
                System.out.println("10. Exit");
                System.out.println("11. Clear Database");
                System.out.print("Enter your choice: ");
                int choice = -1;
                try {
                    choice = scanner.nextInt();
                } catch (Exception e) {
                    choice = -1; // Invalid input will be handled in the default case
                }

                switch (choice) {
                    case 1 -> {
                        expensesChanged = true;
                        System.out.print("Enter date (DD.MM.YYYY): ");
                        String date = scanner.next();  
                        scanner.nextLine();

                        System.out.print("Enter market name: ");
                        String marketName = scanner.nextLine();

                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.print("Enter description: ");
                        String desc = scanner.nextLine();

                        Expense newExpense = new Expense(date, desc, amount, marketName);
                        expenses.add(newExpense);

                        storageService.submitTask(() -> {
                            ExpenseStorage.saveExpense(newExpense);
                        });
                    }
                    case 2 -> {
                        incomesChanged = true;
                        System.out.print("Enter date (DD.MM.YYYY): ");
                        String date = scanner.next();  
                        scanner.nextLine();

                        System.out.print("Enter income source: ");
                        String source = scanner.nextLine();

                        System.out.print("Enter amount: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();

                        System.out.print("Enter description: ");
                        String desc = scanner.nextLine();

                        Income newIncome = new Income(date, desc, amount, source);
                        
                        incomes.add(newIncome);

                        storageService.submitTask(() -> {
                            IncomeStorage.saveIncome(newIncome);
                        });
                    }
                    case 3 -> {
                        System.out.println("\nExpenses: \n---------------");

                        // Refresh expenses list from database to reflect any new additions
                        if(expensesChanged) expenses = ExpenseStorage.loadExpenses();
                        else expensesChanged = false;

                        for (Expense e : expenses) {
                            System.out.println(e);
                        }
                    }
                    case 4 -> {
                        System.out.println("\nIncomes: \n---------------");

                        // Refresh incomes list from database to reflect any new additions
                        if(incomesChanged) incomes = IncomeStorage.loadIncomes();
                        else incomesChanged = false;

                        for (Income i : incomes) {
                            System.out.println(i);
                        }
                    }
                    case 5 -> {
                        System.out.println("\nStores: \n---------------");
                        ArrayList<Store> stores = StoreStorage.loadStores();
                        for (Store s : stores){
                            System.out.println(s);
                        }
                    }
                    case 6 -> {
                        System.out.println("\nStore Types: \n---------------");
                        ArrayList<StoreType> storeTypes = storage.StoreTypeStorage.loadStoreTypes();
                        for (StoreType st : storeTypes) {
                            System.out.println(st);
                        }
                    }
                    case 7 -> {
                        if(expensesChanged) {
                            expenses = ExpenseStorage.loadExpenses();
                            totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
                        }
                        if(incomesChanged) {
                            incomes = IncomeStorage.loadIncomes();
                            totalIncomes = incomes.stream().mapToDouble(Income::getAmount).sum();
                        }
                        
                        double profitLoss = totalIncomes - totalExpenses;

                        System.out.println("\nProfit/Loss Summary: \n---------------");
                        System.out.printf("Total Expenses: %.2f\n", totalExpenses);
                        System.out.printf("Total Incomes: %.2f\n", totalIncomes);
                        System.out.printf("Net Profit/Loss: %.2f\n", profitLoss);
                    }
                    case 8 -> {
                        System.out.print("From date (DD.MM.YYYY): ");
                        String fromInput = scanner.next();
                        System.out.print("To date (DD.MM.YYYY): ");
                        String toInput = scanner.next();

                        String from = DateUtils.toDatabaseFormat(fromInput);
                        String to = DateUtils.toDatabaseFormat(toInput);

                        if (from == null || to == null) {
                            System.out.println("Invalid date format!");
                            break;
                        }

                        ArrayList<Expense> filteredExpenses = ExpenseStorage.loadExpensesBetween(from, to);

                        System.out.println("\nExpenses in period " + fromInput + " to " + toInput + ":");
                        for (Expense e : filteredExpenses) System.out.println(e);
                    }
                    case 9 -> {
                        System.out.print("From date (DD.MM.YYYY): ");
                        String fromInput = scanner.next();
                        System.out.print("To date (DD.MM.YYYY): ");
                        String toInput = scanner.next();

                        String from = DateUtils.toDatabaseFormat(fromInput);
                        String to = DateUtils.toDatabaseFormat(toInput);

                        if (from == null || to == null) {
                            System.out.println("Invalid date format!");
                            break;
                        }

                        ArrayList<Income> filteredIncomes = IncomeStorage.loadIncomesBetween(from, to);

                        System.out.println("\nIncomes in period " + fromInput + " to " + toInput + ":");
                        for (Income i : filteredIncomes) System.out.println(i);
                    }
                    case 10 -> {
                        System.out.println("Goodbye!");
                        storageService.shutdown();
                        return;
                    }
                    case 11 -> {
                        ExpenseStorage.clearDatabase();
                        expenses.clear();
                        System.out.println("Database cleared.");
                    }
                    default -> System.out.println("Invalid choice. Please select from the menu.");
                }
            }
        }
        catch (Exception e) {
            System.out.println("Scanner error: " + e.getMessage());
        }
    }
}