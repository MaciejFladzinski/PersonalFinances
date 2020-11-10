package sample.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "settings")
public class Settings implements BaseModel {

    public Settings() {
    }

    @DatabaseField(canBeNull = false, id = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private boolean modena;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isModena() {
        return modena;
    }

    public void setModena(boolean modena) {
        this.modena = modena;
    }
}
