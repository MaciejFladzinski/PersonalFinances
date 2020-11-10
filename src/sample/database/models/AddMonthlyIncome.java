package sample.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "add monthly income")
public class AddMonthlyIncome implements BaseModel {

    public AddMonthlyIncome() {
    }

    @DatabaseField(canBeNull = false, generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private int year;

    @DatabaseField(canBeNull = false)
    private int month;

    @DatabaseField(canBeNull = false)
    private boolean add;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }
}
