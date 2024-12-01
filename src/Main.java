import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    private static final Bank BANK = new Bank();
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args){

        LOGGER.info("Loading accounts...");
        BANK.loadAccounts();
        BANK.viewAccounts();
        System.out.println(Constants.DIVIDER);
        boolean keepRunning = true;
        while(keepRunning) {
            userSelectionMenu();
            int choice = getUserChoice();
            keepRunning = handleMenuChoice(choice);
        }
    }

    private static void userSelectionMenu() {
        System.out.println("Main Menu Selection");
        System.out.println(Constants.DIVIDER);
        System.out.println("1. Bank Manager");
        System.out.println("2. Bank User");
        System.out.println("3. Create new Account");
        System.out.println("4. Exit");
        System.out.println("Please enter your choice: ");
    }

    private static int getUserChoice() {
        while(true) {
            try {
                int choice = SCANNER.nextInt();
                SCANNER.nextLine();
                return choice;
            } catch (InputMismatchException e) {
                System.out.println(Constants.INVALID_CHOICE);
                SCANNER.nextLine();
            }
        }
    }

    private static boolean handleMenuChoice(int choice) {
        switch (choice) {
            case 1 -> bankManagerMenu(false, "TEMP");
            case 2 -> bankUserMenu(false, "TEMP");
            case 3 -> createNewAccount("customer");
            case 4 -> {
                System.out.println("Exiting...");
                return false;
            }
            default -> System.out.println(Constants.INVALID_CHOICE);
        }return true;
    }

    private static void bankManagerMenu (boolean isLoggedIn, String acNumber) {
        if(!isLoggedIn){
            acNumber = login("manager");
        }
        if (isLoggedIn || acNumber!= null) {
            boolean keepRunning = true;
            while(keepRunning) {
                System.out.println("******* Bank Manager menu ********");
                System.out.println("1. View all accounts ");
                System.out.println("2. Filter accounts by Status");
                System.out.println("3. Search account with account number");
                System.out.println("4. Update account information");
                System.out.println("5. Remove account with Account number");
                System.out.println("6. Find inactive account");
                System.out.println("7. Create Manager account");
                System.out.println("8. Create Customer account");
                System.out.println("9. Save Accounts");
                System.out.println("10. Load Accounts");
                System.out.println("11. Log out");
                System.out.println("Please Enter your Choice: ");

                keepRunning = handleManagerChoice();
            }
        }
        }

    private static boolean handleManagerChoice(){
        int choice = SCANNER.nextInt();
        SCANNER.nextLine();

        switch (choice){
            case 1 -> BANK.viewAccounts();
            case 2 -> filterWithStatus();
            case 3 -> searchAcWithNumber();
            case 4 -> updateAcInfo(null);
            case 5 -> removeAcWithNumber();
//          case 6 -> findInactiveAc();
            case 7 -> createNewAccount("manager");
            case 8 -> createNewAccount("customer");
            case 9 -> BANK.saveAccounts();
            case 10 -> BANK.loadAccounts();
            case 11 -> { System.out.println("Logging out..."); return false; }
            default -> System.out.println(Constants.INVALID_CHOICE);
        }
        return true;
    }

    private static void filterWithStatus(){
        System.out.println("Please Enter Status you wanna filter with");
        BANK.displayFilteredAccounts(BANK.filterWithStatus(SCANNER.nextLine()));
        System.out.println(Constants.CONTINUE_PROMPT);
        SCANNER.nextLine();
    }

    private static void searchAcWithNumber(){
        System.out.println("Please Enter Account number");
        BANK.searchWithAcNumber(SCANNER.nextLine());
    }

    private static void updateAcInfo(String acNumber){
        if(acNumber == null) {
            System.out.println("Please Enter Account number");
            acNumber = SCANNER.nextLine();
        }
        BANK.searchWithAcNumber(acNumber);
        if(BANK.getAccountsWithAcNumber(acNumber).getUserRole().equalsIgnoreCase("manager") ){
            updateMenuManager(acNumber);
        }else if(BANK.getAccountsWithAcNumber(acNumber).getUserRole().equalsIgnoreCase("customer")){
            updateMenuCustomer(acNumber);
        }

    }

    private static void updateMenuManager(String acNumber){
        System.out.println("1. User Name");
        System.out.println("2. Pin");
        System.out.println("3. Status");
        System.out.println("4. Pan Number");
        System.out.println("5. Cancel update");
        System.out.println("Enter number of the Field you want to update");
        int count = 3;
        while(count>0){
            try{
                int choice = SCANNER.nextInt();
                SCANNER.nextLine();
                switch (choice){
                    case 1 -> updateUserName(acNumber);
                    case 2 -> updatePin(acNumber);
                    case 3 -> updateStatus(acNumber);
                    case 4 -> updatePanNumber(acNumber);
                    case 5 -> count=0;
                    default -> System.out.println(Constants.INVALID_CHOICE);
                }count=0;
            }catch (InputMismatchException e){
                System.out.println(Constants.INVALID_CHOICE);
                count--;
            }
        }
    }

    private static void updateMenuCustomer(String acNumber){
        System.out.println("1. User Name");
        System.out.println("2. Pin");
        System.out.println("3. Pan Number");
        System.out.println("4. Cancel update");
        System.out.println("Enter number of the Field you want to update");
        int count = 3;
        while(count>0){
            try{
                int choice = SCANNER.nextInt();
                SCANNER.nextLine();
                switch (choice){
                    case 1 -> updateUserName(acNumber);
                    case 2 -> updatePin(acNumber);
                    case 3 -> updatePanNumber(acNumber);
                    case 4 -> count=0;
                    default -> System.out.println(Constants.INVALID_CHOICE);
                }count=0;
            }catch (InputMismatchException e){
                System.out.println(Constants.INVALID_CHOICE);
                count--;
            }
        }
    }

    private static void updateUserName(String acNumber){
            System.out.println("Enter the new UserName");
            String newName = SCANNER.nextLine();
            BANK.updateUserName(acNumber,newName);
    }

    private static void updatePin(String acNumber){
        System.out.println("Enter the old Pin");
        String oldPin = SCANNER.nextLine();
        System.out.println("Enter the new Pin");
        String newPin = SCANNER.nextLine();
        System.out.println("Re-Enter the new Pin");
        if(newPin.equals(SCANNER.nextLine()))
        {
            BANK.updatePin(acNumber,newPin, oldPin);
        }
    }

    private static void updateStatus(String acNumber){
        System.out.println("1.verifying");
        System.out.println("2.active");
        System.out.println("3.suspending ");
        System.out.println("4.closed ");
        int choice = SCANNER.nextInt();
        SCANNER.nextLine();
        BANK.updateStatus(acNumber,choice);
    }

    private static void updatePanNumber(String acNumber){
        System.out.println("Enter the new PanNumber");
        String newPan = SCANNER.nextLine();
        BANK.updatePanNumber(acNumber,newPan, "manager");
    }

    private static void removeAcWithNumber(){
        System.out.println("Enter the Account Number");
        BANK.deleteWithAcNumber(SCANNER.nextLine());
    }
    private static void bankUserMenu (boolean isLoggedIn, String acNumber) {
        if(!isLoggedIn){
            acNumber = login("customer");
        }
        if (isLoggedIn || acNumber != null) {
            boolean keepRunning = true;
            while(keepRunning) {
                System.out.println("****** Customer menu ******");
                System.out.println("1. View Account info");
                System.out.println("2. Update info");
                System.out.println("3. View Transaction history");
                System.out.println("4. Apply for closing account");
                System.out.println("5. Deposit money to account");
                System.out.println("6. Withdraw money from account");
                System.out.println("7. Log out");
                keepRunning = handleCustomerChoice(acNumber);
            }
        }
    }

    private static boolean handleCustomerChoice(String acNumber){
        int choice = SCANNER.nextInt();
        SCANNER.nextLine();
        switch (choice){
            case 1 -> BANK.searchWithAcNumber(acNumber);
            case 2 -> updateAcInfo(acNumber);
            case 3 -> BANK.viewTransactions(acNumber);
            case 4 -> BANK.getAccountsWithAcNumber(acNumber).setAcStatus("suspending");
            case 5 -> BANK.getAccountsWithAcNumber(acNumber).Deposit();
            case 6 -> BANK.getAccountsWithAcNumber(acNumber).Withdraw();
            case 7 -> {
                System.out.println("Logging out...");
                return false;
            }
            default -> System.out.println(Constants.INVALID_CHOICE);
        }return true;
    }

    private static void createNewAccount(String user){
        System.out.println("******* Creating New "+ user + " **********");
        System.out.println("Please Enter UserName: ");
        String acHolderName = SCANNER.nextLine();
        System.out.println("Create a new pin for your account: ");
        String acPin = SCANNER.nextLine();
        System.out.println("Please Enter Pan number: ");
        String panNumber = SCANNER.nextLine();
        String acNumber = "TEMP0000";
        Account newAccount = new Account(acHolderName, acNumber, panNumber, acPin);
        if(user.equalsIgnoreCase("manager"))newAccount.setUserRole("manager");
        BANK.addAccount(newAccount);
        acNumber = newAccount.getAcNumber();
        System.out.println("Account Created Successfully");
        System.out.println("Please note down your Account Number: "+ acNumber +"\n"+Constants.CONTINUE_PROMPT);
        SCANNER.nextLine();
        if(user.equalsIgnoreCase("manager"))bankManagerMenu(true, acNumber);
        if(user.equalsIgnoreCase("customer"))bankUserMenu(true, acNumber);
    }

    private static String login (String userRole) {
        int noOfTries = 3;
        while(noOfTries != 0) {
            System.out.println("************* Login page *************");
            System.out.println("Enter your Account Number");
            String acNumber = SCANNER.nextLine();
            System.out.println("Enter your Pin");
            String pin = SCANNER.nextLine();
            System.out.println(Constants.DIVIDER);
            String existingRole = BANK.authUser(acNumber, pin);
            if (existingRole.equalsIgnoreCase(userRole)) {
                return acNumber;
            } else {
                if(noOfTries>0)System.out.println("Wrong credentials please try again");
                noOfTries--;
                System.out.println("Tries remaining: "+ noOfTries);
            }
        }return null;
    }
}
