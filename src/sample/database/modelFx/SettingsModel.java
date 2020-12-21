package sample.database.modelFx;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import javafx.application.Application;
import sample.database.dbutils.DbManager;
import sample.database.models.Settings;
import sample.utils.DialogUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SettingsModel {

    LoginModel loginModel = new LoginModel();

    public void init() throws SQLException {
        initStylesheetInDataBase();
    }

    public String initStylesheetInDataBase() throws SQLException {
        AtomicReference<String> stylesheet = new AtomicReference<>("modena");

        Dao<Settings, Integer> settingsDao = DaoManager.createDao(DbManager.getConnectionSource(), Settings.class);
        QueryBuilder<Settings, Integer> settingsQueryBuilder = settingsDao.queryBuilder();
        settingsQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<Settings> prepareSettings = settingsQueryBuilder.prepare();
        List<Settings> settings = settingsDao.query(prepareSettings);
        if (settings.isEmpty()) {
            Settings setting = new Settings();
            setting.setUsername(this.loginModel.getLoggedUserFromDataBase());
            setting.setModena(true);
            settingsDao.create(setting);
            DbManager.closeConnectionSource();
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        } else {
            settings.forEach(s -> {
                if (s.isModena()) {
                    Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
                } else {
                    Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
                    stylesheet.set("caspian");
                }
            });
        }
        return stylesheet.get();
    }

    public void setStylesheetCaspianInDataBase() throws SQLException {
        Dao<Settings, Integer> settingsDao = DaoManager.createDao(DbManager.getConnectionSource(), Settings.class);
        QueryBuilder<Settings, Integer> settingsQueryBuilder = settingsDao.queryBuilder();
        settingsQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<Settings> prepareSettings = settingsQueryBuilder.prepare();
        List<Settings> settings = settingsDao.query(prepareSettings);
        settings.forEach(s -> {
            s.setModena(false);
            try {
                settingsDao.update(s);
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        });
        DbManager.closeConnectionSource();
    }

    public void setStylesheetModenaInDataBase() throws SQLException {
        Dao<Settings, Integer> settingsDao = DaoManager.createDao(DbManager.getConnectionSource(), Settings.class);
        QueryBuilder<Settings, Integer> settingsQueryBuilder = settingsDao.queryBuilder();
        settingsQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<Settings> prepareSettings = settingsQueryBuilder.prepare();
        List<Settings> settings = settingsDao.query(prepareSettings);
        settings.forEach(s -> {
            s.setModena(true);
            try {
                settingsDao.update(s);
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        });
        DbManager.closeConnectionSource();
    }
}
