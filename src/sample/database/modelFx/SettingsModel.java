package sample.database.modelFx;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import javafx.application.Application;
import sample.database.dbutils.DbManager;
import sample.database.models.Settings;

import java.sql.SQLException;
import java.util.List;

public class SettingsModel {

    public void init() throws SQLException {
        initStylesheetInDataBase();
    }

    public String initStylesheetInDataBase() throws SQLException {
        Dao<Settings, Integer> settingsDao = DaoManager.createDao(DbManager.getConnectionSource(), Settings.class);
        GenericRawResults<String[]> rawResult = settingsDao.queryRaw("SELECT * FROM 'settings'");
        List<String[]> result = rawResult.getResults();
        if(result.isEmpty()) {
            Settings settings = new Settings();
            settings.setId(1);
            settings.setModena(true);
            settingsDao.create(settings);
            DbManager.closeConnectionSource();
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            return "modena";
        }
        else if(settingsDao.queryForId(1).isModena()) {
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            return "modena";
        }
        else if(!settingsDao.queryForId(1).isModena()) {
            Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
            return "caspian";
        }
        return "";
    }

    public void setStylesheetCaspianInDataBase() throws SQLException {
        Dao<Settings, Integer> settingsDao = DaoManager.createDao(DbManager.getConnectionSource(), Settings.class);
        Settings settings = settingsDao.queryForId(1);
        settings.setModena(false);
        settingsDao.update(settings);
        DbManager.closeConnectionSource();
    }

    public void setStylesheetModenaInDataBase() throws SQLException {
        Dao<Settings, Integer> settingsDao = DaoManager.createDao(DbManager.getConnectionSource(), Settings.class);
        Settings settings = settingsDao.queryForId(1);
        settings.setModena(true);
        settingsDao.update(settings);
        DbManager.closeConnectionSource();
    }
}
