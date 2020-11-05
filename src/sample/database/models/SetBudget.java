package sample.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "set budget")
public class SetBudget implements BaseModel{

    public SetBudget() {
    }

    @DatabaseField(canBeNull = false, id = true)
    private int id;

    @DatabaseField(columnName = "account balance", canBeNull = false)
    private double accountBalance;

    @DatabaseField(columnName = "monthly income", canBeNull = false)
    private double monthlyIncome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
}
