import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Account {
    private String userRole;
    private String acHolderName;
    private String panNumber;
    private String acNumber;
    private double acBalance;
    private String acPin;
    private String acStatus; //  verifying|active|suspending|closed
    private LocalDate acOpeningDate;
    private ArrayList<Transaction> transactions;
    Scanner scanner = new Scanner(System.in);


    public Account(String acHolderName, String acNumber,String panNumber, String acPin){
        this.acHolderName=acHolderName;
        this.acNumber=acNumber;
        this.panNumber=panNumber;
        this.acPin=acPin;
        this.userRole="customer"; //giving role user by default
        this.acBalance=0.0; //New accounts start with the 0 balance
        this.acStatus="verifying"; //by default giving status as verifying, which can be updated later by bankAdmin
        this.acOpeningDate=LocalDate.now();
        this.transactions = new ArrayList<>();


    }


    public String getAcHolderName(){
        return acHolderName;
    }

    public void setAcHolderName(String acHolderName){
        this.acHolderName=acHolderName;
    }

    public String getAcNumber(){
        return acNumber;
    }

    public void setAcNumber(String acNumber){
        this.acNumber=acNumber;
    }

    public double getAcBalance(){
        return acBalance;
    }

    public void setAcBalance(double acBalance){
        this.acBalance=acBalance;
    }

    public String getAcPin(){
        return acPin;
    }

    public void setAcPin(String acPin){
        this.acPin=acPin;
    }

    public String getAcStatus() {
        return acStatus;
    }

    public LocalDate getAcOpeningDate() {
        return acOpeningDate;
    }

    public void setAcOpeningDate(LocalDate acOpeningDate) {
        this.acOpeningDate = acOpeningDate;
    }

    public void setAcStatus(String acStatus) {
        this.acStatus = acStatus;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber, String userRole) {
        this.panNumber = panNumber;
        if(!userRole.equalsIgnoreCase("manager")) {
            this.acStatus = "verifying";
        }
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void Deposit(){
        System.out.println("Enter the Deposit Amount: ");
        double amount=0;
        boolean validInput = false;

        while(!validInput) {
            try {
                amount = scanner.nextDouble();
                scanner.nextLine();
                if(amount<=0){
                    System.out.println("Please Enter Non-zero and Non-Negative amount: ");
                }else {
                    validInput=true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please Enter the valid Amount: ");
                scanner.nextLine();
            }
        }
        acBalance += amount;
        transactions.add(new Transaction("Deposit", amount));
    }

    public void Withdraw(){
        System.out.println("Enter the Amount to Withdraw: ");
        double amount=0;
        boolean validInput = false;

        while(!validInput) {
            try {
                amount = scanner.nextDouble();
                scanner.nextLine();
                if(amount<=0){
                    System.out.println("Please Enter Non-zero and Non-Negative amount: ");
                }else if(amount>acBalance){
                    System.out.println("Not Enough Balance, Current Account balance: "+acBalance);
                    return;
                }else {
                    validInput=true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please Enter the valid Amount: ");
                scanner.nextLine();
            }
        }
        acBalance -= amount;
        transactions.add(new Transaction("Withdraw", amount));
    }

    public void sortByDate(){
        Collections.sort(transactions, (transaction1, transaction2)-> transaction1.getTransDate().compareTo(transaction2.getTransDate()));
    }

    public void sortByDateAndAmount(){
        Collections.sort(transactions, Comparator.comparing(Transaction::getTransDate).thenComparing(Transaction::getAmount));
    }

    public List<Transaction> filterByTransType(String transType){
        return transactions.stream()
                .filter(transaction -> transaction.getTransType().equalsIgnoreCase(transType))
                .toList();
    }

    public List<Transaction> filterByFromAndToDate(LocalDate startDate, LocalDate endDate){
        return transactions.stream()
                .filter(transaction -> !transaction.getTransDate().isBefore(startDate) && !transaction.getTransDate().isAfter(endDate))
                .toList();
    }

    public void displayFilteredList(List<Transaction> transactions){
        int i = 0;
        if(transactions != null) {
            for (Transaction transaction : transactions) {
                i++;
                System.out.println(i + " | " + transaction.toString());
            }
        }else{
            System.out.println("Filtered List is empty");
        }
    }

    public void viewTransaction(){
        if(transactions == null){
            System.out.println("No Transactions are present in Records for "+ acNumber);
        }else{
            int i=0;
            for(Transaction transaction:transactions){
                i++;
                System.out.println(i+" | "+transaction.toString());
            }
        }
    }

    public String toString(boolean isPinVisible){
        StringBuilder sb = new StringBuilder();
                  sb.append("Account holder name: ").append(acHolderName)
                    .append(" | Account Number: ").append(acNumber)
                    .append(" | Account panNumber: ").append(panNumber);
            if(isPinVisible){
                sb.append(" | Account Pin: ").append(acPin);
            }
                  sb.append(" | User role: ").append(userRole)
                    .append(" | Account Balance: ").append(acBalance)
                    .append(" | Account Status: ").append(acStatus)
                    .append(" | Account Opening Date: ").append(acOpeningDate)
                    .append(" | Transactions{");
                    for(Transaction transaction:transactions){

                               sb.append(transaction.toString()).append(",");
                    }
                    sb.append("}");

            return sb.toString();
    }

    public static Account fromString(String str){
        String[] parts = str.split(" \\| ");
        String acHolderName = parts[0].replace("Account holder name: ", "");
        String acNumber = parts[1].replace("Account Number: ", "");
        String panNumber = parts[2].replace("Account panNumber: ", "");
        String acPin = parts[3].replace("Account Pin: ", "");

        Account account = new Account(acHolderName, acNumber, panNumber, acPin);
        account.setUserRole(parts[4].replace("User role: ", "").trim());
        account.setAcBalance(Double.parseDouble(parts[5].replace("Account Balance: ", "").trim()));
        account.setAcStatus(parts[6].replace("Account Status: ", "").trim());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        account.setAcOpeningDate(LocalDate.parse(parts[7].replace("Account Opening Date: ", "").trim(), formatter));

        String transactionsPart = parts[8].replace("Transactions{","").replace("}","").trim();
        if(!transactionsPart.isEmpty()) {
            ArrayList<Transaction> transactions = new ArrayList<>();
            transactions.add(Transaction.fromString(transactionsPart));
            account.setTransactions(transactions);
        }
        return account;
    }

}
