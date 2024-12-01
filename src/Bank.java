import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bank {

    Scanner scanner = new Scanner(System.in);
    private ArrayList<Account> accounts;

    public Account getAccountsWithAcNumber(String acNo) {
        for(Account account:accounts){
            if(account.getAcNumber().equalsIgnoreCase(acNo)){
                return account;
            }
        }System.out.println("Account with account no "+acNo+" was not present");
        return null;
    }


    public Bank() {
        accounts = new ArrayList<>();
    }

    public String authUser(String acNumber, String pin){
        for(Account account:accounts) {
            if (acNumber.equalsIgnoreCase(account.getAcNumber()) && pin.equalsIgnoreCase(account.getAcPin())){
                if(account.getUserRole().equalsIgnoreCase("manager")){
                    return "manager";
                }else {
                    return "customer";
                }
            }
           }return "wrong";
        }

    public void addAccount(Account newAccount) {
        if (newAccount != null) {
            for (Account account : accounts) {
                if (newAccount.getPanNumber().equalsIgnoreCase(account.getPanNumber())) {
                    System.out.println("Account Exists");
                    return;
                }
            }
            int totalAccounts = accounts.size()+1;
            newAccount.setAcNumber("BANK0000"+totalAccounts);
            accounts.add(newAccount);
            saveAccounts();
        }
    }

    public void viewAccounts(){
        int i=0;
        for(Account account:accounts){
            i++;
            System.out.println(i +" | " +account.toString(true));
        }
        System.out.println(Constants.CONTINUE_PROMPT);
        scanner.nextLine();
    }

    public void deleteWithAcNumber(String acNo){
        for(Account account:accounts){
            if(account.getAcNumber().equalsIgnoreCase(acNo)){
                System.out.println(account.toString(false));
                System.out.println("Confirm Deletion of this account ? (Y | N)");
                String choice = scanner.nextLine();
                if(choice.equalsIgnoreCase("Y") || choice.contains("Y")) {
                    accounts.remove(account);
                    System.out.println("Successfully deleted Account");
                }else {
                    System.out.println("Cancelling deletion");
                }
                return;
            }
        }System.out.println("Account with account no "+acNo+" was not present");
    }

    public void searchWithAcNumber(String acNo){
        for(Account account:accounts){
            if(account.getAcNumber().equalsIgnoreCase(acNo)){
                System.out.println(account.toString(false));
                System.out.println(Constants.CONTINUE_PROMPT);
                scanner.nextLine();
                return;
            }
        }System.out.println("Account with account no "+acNo+" was not present");
    }

    public void updateUserName(String acNumber, String newName){
        for(Account account: accounts){
            if(account.getAcNumber().equalsIgnoreCase(acNumber)){
                account.setAcHolderName(newName);
                System.out.println("Name updated successfully");
                return;
            }
        }
    }

    public void updateStatus(String acNumber, int choice){
        for(Account account: accounts){
            if(account.getAcNumber().equalsIgnoreCase(acNumber)){
                switch (choice) {
                    case 1 -> account.setAcStatus("verifying");
                    case 2 -> account.setAcStatus("active");
                    case 3 -> account.setAcStatus("suspending");
                    case 4 -> account.setAcStatus("closed");
                    default -> System.out.println(Constants.INVALID_CHOICE);
                }return;
            }
        }System.out.println("Account number doesn't match with any existing account");
    }

    public void updatePin(String acNumber, String newPin, String oldPin){
        for(Account account: accounts){
            if(account.getAcNumber().equalsIgnoreCase(acNumber) && account.getAcPin().equals(oldPin)){
                account.setAcPin(newPin);
                System.out.println("Pin updated successfully");
                return;
            }else{
                System.out.println("Either no matching account found or wrongPin");
            }
        }
    }
    public void updatePanNumber(String acNumber, String newPan, String user){
        for(Account account: accounts){
            if(account.getAcNumber().equalsIgnoreCase(acNumber)){
                account.setPanNumber(newPan, user);
                System.out.println("Pan updated successfully");
                return;
            }
        }
    }


    public List<Account> filterWithBalance(double minAcBalance){
        return accounts.stream()
                .filter(account -> account.getAcBalance()<=minAcBalance)
                .toList();
    }

    public List<Account> filterWithStatus(String status){
        return accounts.stream()
                .filter(account -> account.getAcStatus().equalsIgnoreCase(status))
                .toList();
    }

    public List<Account> filterWithRole(String role){
        return accounts.stream()
                .filter(account -> account.getUserRole().equalsIgnoreCase(role))
                .toList();
    }


    public void displayFilteredAccounts(List<Account> accounts){
        if(accounts != null){
            int i=0;
            for(Account account:accounts) {
                i++;
                System.out.println(i+" | "+account.toString(false));
            }
        }else{
            System.out.println("No Records to Show for the Filter");
        }
    }

    public void saveAccounts(){
        String filename = "accounts";
        if(!accounts.isEmpty()){
            try(FileWriter writer = new FileWriter(filename+".txt")) {
                int count = 0;
                for (Account account : accounts) {
                    writer.write(account.toString(true)+"\n");
                    count++;
                }
//                System.out.println(count + " accounts saved to "+filename+" successfully" );
            }catch(Exception e){
                System.out.println("Exception occurred saving accounts to file");
            }
        }
    }

    public void viewTransactions(String acNumber){
        for(Account account:accounts){
            if(account.getAcNumber().equalsIgnoreCase(acNumber)){
                account.viewTransaction();
                return;
            }
        }
    }

    public void loadAccounts(){
        String fileName = "Accounts";
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName+".txt"))){
            String line;
            int count = 0;
            while ((line=reader.readLine())!=null){
                addAccount(Account.fromString(line));
                count++;
            }
            if(accounts.isEmpty()) {
                System.out.println("No accounts were loaded");
            }else{
                System.out.println(count+" accounts were loaded");
            }
        }catch(Exception e){
            System.out.println("Exception occurred while Loading Accounts");
            e.printStackTrace();
        }
    }
}