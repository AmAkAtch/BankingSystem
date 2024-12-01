import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    public String transType;
    public LocalDate transDate;
    public double amount;

    public Transaction(String transType,  double amount){
        this.transType=transType;
        this.transDate= LocalDate.now();
        this.amount=amount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public LocalDate getTransDate() {
        return transDate;
    }

    public void setTransDate(LocalDate transDate) {
        this.transDate = transDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "Transaction Type=" + transType +
                ", Transaction Date=" + transDate +
                ", Amount=" + amount +
                '}';
    }

    public static Transaction fromString(String str){
        String[] parts = str.replace("Transactions{","").replace("}","").split(", ");
        String transType = parts[0].replace("Transaction Type=","");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate transDate = LocalDate.parse(parts[1].replace("Transaction Date=",""), formatter);

        double amount = Double.parseDouble(parts[2].replace("Amount=",""));

        Transaction transaction = new Transaction(transType, amount);
        transaction.setTransDate(transDate);

        return transaction;
    }
}
