package sample.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "categories")
public class Category implements BaseModel{

    public Category() {

    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "category", canBeNull = false, unique = true)
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
