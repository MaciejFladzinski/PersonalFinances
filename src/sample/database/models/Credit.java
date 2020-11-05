package sample.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "credits")
public class Credit implements BaseModel{

    public Credit() {

    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(format = "yyyy.MM.dd")
    private Date date;

    @DatabaseField(canBeNull = false)
    private double amount;

    @DatabaseField()
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
