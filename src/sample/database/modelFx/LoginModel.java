package sample.database.modelFx;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import sample.database.dbutils.DbManager;
import sample.database.models.LoggedUser;
import sample.database.models.Users;
import sample.utils.DialogUtils;

import java.sql.SQLException;
import java.util.List;

public class LoginModel {

    public void init() {

    }

    public boolean checkLoginAndPassword(String username, String password) throws SQLException {
        Dao<Users, Integer> usersDao = DaoManager.createDao(DbManager.getConnectionSource(), Users.class);
        List<Users> users = usersDao.queryForAll();
        if (users.stream().filter(user -> user.getUsername().equals(username)).anyMatch(user -> user.getPassword().equals(password))) {
            return true;
        }
        DbManager.closeConnectionSource();
        return false;
    }

    public void addLoggedUserToDataBase(String username) throws SQLException {
        Dao<LoggedUser, Integer> loggedUserDao = DaoManager.createDao(DbManager.getConnectionSource(), LoggedUser.class);
        LoggedUser loggedUser = new LoggedUser();
        loggedUser.setId(1);
        loggedUser.setUsername(username);
        loggedUserDao.create(loggedUser);
        DbManager.closeConnectionSource();
    }

    public String getLoggedUserFromDataBase() throws SQLException {
        Dao<LoggedUser, Integer> loggedUserDao = DaoManager.createDao(DbManager.getConnectionSource(), LoggedUser.class);
        LoggedUser loggedUser = loggedUserDao.queryForId(1);
        String username = loggedUser.getUsername();
        DbManager.closeConnectionSource();
        return username;
    }

    public void removeLoggedUserFromDataBase() throws SQLException {
        Dao<LoggedUser, Integer> loggedUserDao = DaoManager.createDao(DbManager.getConnectionSource(), LoggedUser.class);
        loggedUserDao.deleteById(1);
        DbManager.closeConnectionSource();
    }

    public void updateFirstNameInDataBase(String firstName) throws SQLException {
        String username = getLoggedUserFromDataBase();
        Dao<Users, Integer> usersDao = DaoManager.createDao(DbManager.getConnectionSource(), Users.class);
        List<Users> users = usersDao.queryForAll();
        users.forEach(user -> {
            if (user.getUsername().equals(username)) {
                user.setFirstName(firstName);
                try {
                    usersDao.update(user);
                } catch (SQLException e) {
                    DialogUtils.errorDialog(e.getMessage());
                }
            }
        });
        DbManager.closeConnectionSource();
    }

    public void updateLastNameInDataBase(String lastName) throws SQLException {
        String username = getLoggedUserFromDataBase();
        Dao<Users, Integer> usersDao = DaoManager.createDao(DbManager.getConnectionSource(), Users.class);
        List<Users> users = usersDao.queryForAll();
        users.forEach(user -> {
            if (user.getUsername().equals(username)) {
                user.setLastName(lastName);
                try {
                    usersDao.update(user);
                } catch (SQLException e) {
                    DialogUtils.errorDialog(e.getMessage());
                }
            }
        });
        DbManager.closeConnectionSource();
    }

    public void updateUsernameInDataBase(String newUsername) throws SQLException {
        String username = getLoggedUserFromDataBase();
        Dao<Users, Integer> usersDao = DaoManager.createDao(DbManager.getConnectionSource(), Users.class);
        List<Users> users = usersDao.queryForAll();
        users.forEach(user -> {
            if (user.getUsername().equals(username)) {
                user.setUsername(newUsername);
                try {
                    usersDao.update(user);
                } catch (SQLException e) {
                    DialogUtils.errorDialog(e.getMessage());
                }
            }
        });
        DbManager.closeConnectionSource();
    }

    public void updatePasswordInDataBase(String password) throws SQLException {
        String username = getLoggedUserFromDataBase();
        Dao<Users, Integer> usersDao = DaoManager.createDao(DbManager.getConnectionSource(), Users.class);
        List<Users> users = usersDao.queryForAll();
        users.forEach(user -> {
            if (user.getUsername().equals(username)) {
                user.setPassword(password);
                try {
                    usersDao.update(user);
                } catch (SQLException e) {
                    DialogUtils.errorDialog(e.getMessage());
                }
            }
        });
        DbManager.closeConnectionSource();
    }

    public boolean checkPasswordFromDataBase(String password) throws SQLException {
        String username = getLoggedUserFromDataBase();
        Dao<Users, Integer> usersDao = DaoManager.createDao(DbManager.getConnectionSource(), Users.class);
        List<Users> users = usersDao.queryForAll();
        if (users.stream().filter(user -> user.getUsername().equals(username)).anyMatch(user -> user.getPassword().equals(password))) {
            return true;
        }
        DbManager.closeConnectionSource();
        return false;
    }
}
