package sample.database.modelFx;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import sample.database.dbutils.DbManager;
import sample.database.models.SetBudget;
import sample.utils.DialogUtils;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SetBudgetModel {

    private LoginModel loginModel = new LoginModel();

    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public void init() {

    }

    public void saveBudgetInDataBase(double accountBalance, double monthlyIncome) throws SQLException {
        Dao<SetBudget, Integer> setBudgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        QueryBuilder<SetBudget, Integer> usernameQueryBuilder = setBudgetDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<SetBudget> prepareUsername = usernameQueryBuilder.prepare();
        List<SetBudget> username = setBudgetDao.query(prepareUsername);
        if (username.isEmpty()) {
            SetBudget setBudget = new SetBudget();
            setBudget.setUsername(this.loginModel.getLoggedUserFromDataBase());
            setBudget.setAccountBalance(Double.parseDouble(this.decimalFormat.format(accountBalance)));
            setBudget.setMonthlyIncome(Double.parseDouble(this.decimalFormat.format(monthlyIncome)));
            setBudgetDao.create(setBudget);
            DbManager.closeConnectionSource();
        }
        username.forEach(user -> {
            user.setAccountBalance(accountBalance);
            user.setMonthlyIncome(monthlyIncome);
            try {
                setBudgetDao.update(user);
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
            DbManager.closeConnectionSource();
        });
    }

    public void updateAccountBalanceInDataBase(double accountBalance) throws SQLException {
        Dao<SetBudget, Integer> setBudgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        QueryBuilder<SetBudget, Integer> usernameQueryBuilder = setBudgetDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<SetBudget> prepareUsername = usernameQueryBuilder.prepare();
        List<SetBudget> username = setBudgetDao.query(prepareUsername);
        username.forEach(user -> {
            user.setAccountBalance(Double.parseDouble(this.decimalFormat.format(accountBalance)));
            try {
                setBudgetDao.update(user);
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
            DbManager.closeConnectionSource();
        });
    }

    public void updateMonthlyIncomeInDataBase(double monthlyIncome) throws SQLException {
        Dao<SetBudget, Integer> setBudgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        QueryBuilder<SetBudget, Integer> usernameQueryBuilder = setBudgetDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<SetBudget> prepareUsername = usernameQueryBuilder.prepare();
        List<SetBudget> username = setBudgetDao.query(prepareUsername);
        username.forEach(user -> {
            user.setMonthlyIncome(Double.parseDouble(this.decimalFormat.format(monthlyIncome)));
            try {
                setBudgetDao.update(user);
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
            DbManager.closeConnectionSource();
        });
    }

    public void substractAccountBalanceInDataBase(double debit) throws SQLException {
        Dao<SetBudget, Integer> setBudgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        QueryBuilder<SetBudget, Integer> usernameQueryBuilder = setBudgetDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<SetBudget> prepareUsername = usernameQueryBuilder.prepare();
        List<SetBudget> username = setBudgetDao.query(prepareUsername);
        username.forEach(user -> {
            double oldAccountBalance = user.getAccountBalance();
            user.setAccountBalance(Double.parseDouble(this.decimalFormat.format(oldAccountBalance - debit)));
            try {
                setBudgetDao.update(user);
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
            DbManager.closeConnectionSource();
        });
    }

    public void addAccountBalanceInDataBase(double credit) throws SQLException {
        Dao<SetBudget, Integer> setBudgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        QueryBuilder<SetBudget, Integer> usernameQueryBuilder = setBudgetDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<SetBudget> prepareUsername = usernameQueryBuilder.prepare();
        List<SetBudget> username = setBudgetDao.query(prepareUsername);
        username.forEach(user -> {
            double oldAccountBalance = user.getAccountBalance();
            user.setAccountBalance(Double.parseDouble(this.decimalFormat.format(oldAccountBalance + credit)));
            try {
                setBudgetDao.update(user);
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
            DbManager.closeConnectionSource();
        });
    }

    public void addMonthlyIncomeToAccountBalanceInDataBase() throws SQLException {
        Dao<SetBudget, Integer> setBudgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        QueryBuilder<SetBudget, Integer> usernameQueryBuilder = setBudgetDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<SetBudget> prepareUsername = usernameQueryBuilder.prepare();
        List<SetBudget> username = setBudgetDao.query(prepareUsername);
        username.forEach(user -> {
            double oldAccountBalance = user.getAccountBalance();
            try {
                user.setAccountBalance(Double.parseDouble(this.decimalFormat.format(oldAccountBalance + getMonthlyIncomeFromDataBase())));
                setBudgetDao.update(user);
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
            DbManager.closeConnectionSource();
        });
    }

    public boolean checkIfBudgetIsSet() throws SQLException {
        Dao<SetBudget, Integer> setBudgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        QueryBuilder<SetBudget, Integer> usernameQueryBuilder = setBudgetDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<SetBudget> prepareUsername = usernameQueryBuilder.prepare();
        List<SetBudget> username = setBudgetDao.query(prepareUsername);
        if (username.isEmpty()) {
            DbManager.closeConnectionSource();
            return false;
        }
        DbManager.closeConnectionSource();
        return true;
    }

    public double getAccountBalanceFromDataBase() throws SQLException {
        Dao<SetBudget, Integer> setBudgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        QueryBuilder<SetBudget, Integer> usernameQueryBuilder = setBudgetDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<SetBudget> prepareUsername = usernameQueryBuilder.prepare();
        List<SetBudget> username = setBudgetDao.query(prepareUsername);
        AtomicReference<Double> accountBalance = new AtomicReference<>((double) 0);
        username.forEach(user -> {
            accountBalance.set(user.getAccountBalance());
            DbManager.closeConnectionSource();
        });
        return accountBalance.get();
    }

    public double getMonthlyIncomeFromDataBase() throws SQLException {
        Dao<SetBudget, Integer> setBudgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        QueryBuilder<SetBudget, Integer> usernameQueryBuilder = setBudgetDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<SetBudget> prepareUsername = usernameQueryBuilder.prepare();
        List<SetBudget> username = setBudgetDao.query(prepareUsername);
        AtomicReference<Double> actualMonthlyIncome = new AtomicReference<>((double) 0);
        username.forEach(user -> {
            actualMonthlyIncome.set(user.getMonthlyIncome());
            DbManager.closeConnectionSource();
        });
        return actualMonthlyIncome.get();
    }

}