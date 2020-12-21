package sample.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "logged_user")
public class LoggedUser implements BaseModel {

    public LoggedUser() {

    }

    @DatabaseField(canBeNull = false, id = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String username;

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
}
