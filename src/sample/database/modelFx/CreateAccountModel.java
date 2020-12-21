package sample.database.modelFx;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import sample.database.dbutils.DbManager;
import sample.database.models.Users;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CreateAccountModel {

    public void init() {

    }

    public void createUserInDataBase(String firstName, String lastName, String username, String password) throws SQLException {
        Dao<Users, Integer> usersDao = DaoManager.createDao(DbManager.getConnectionSource(), Users.class);
        Users user = new Users();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        usersDao.create(user);
        DbManager.closeConnectionSource();
    }

    public boolean checkIfExistUserInDataBase(String username) throws SQLException {
        Dao<Users, Integer> usersDao = DaoManager.createDao(DbManager.getConnectionSource(), Users.class);
        List<Users> users = usersDao.queryForAll();
        AtomicBoolean usernameExist = new AtomicBoolean(false);
        users.forEach(user -> {
            if (user.getUsername().equals(username)) {
                usernameExist.set(true);
            }
        });
        DbManager.closeConnectionSource();
        return usernameExist.get();
    }
}
