package sample.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "monthly_expenses")
public class MonthlyExpenses implements BaseModel {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String username;

    @DatabaseField(canBeNull = false)
    private int year;

    @DatabaseField(canBeNull = false)
    private int month;

    @DatabaseField(columnName = "sum of expenses this month")
    private double sumOfExpensesThisMonth;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getSumOfExpensesThisMonth() {
        return sumOfExpensesThisMonth;
    }

    public void setSumOfExpensesThisMonth(double sumOfExpensesThisMonth) {
        this.sumOfExpensesThisMonth = sumOfExpensesThisMonth;
    }
}
