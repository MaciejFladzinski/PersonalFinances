package sample.database.modelFx;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import sample.database.dbutils.DbManager;
import sample.database.models.SetBudget;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class SetBudgetModel {

    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public void init() {

    }

    public void saveBudgetInDataBase(double accountBalance, double monthlyIncome) throws SQLException {
        Dao<SetBudget, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        GenericRawResults<String[]> rawResult = dao.queryRaw("SELECT * FROM 'set budget'");
        List<String[]> result = rawResult.getResults();
        if(result.isEmpty()) {
            SetBudget setBudget = new SetBudget();
            setBudget.setId(1);
            setBudget.setAccountBalance(Double.parseDouble(this.decimalFormat.format(accountBalance)));
            setBudget.setMonthlyIncome(Double.parseDouble(this.decimalFormat.format(monthlyIncome)));
            dao.create(setBudget);
            DbManager.closeConnectionSource();
        }
        else {
            SetBudget setBudget = dao.queryForId(1);
            setBudget.setAccountBalance(accountBalance);
            setBudget.setMonthlyIncome(monthlyIncome);
            dao.update(setBudget);
            DbManager.closeConnectionSource();
        }
    }

    public void updateAccountBalanceInDataBase(double accountBalance) throws SQLException {
        Dao<SetBudget, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);

        SetBudget setBudget = dao.queryForId(1);
        setBudget.setAccountBalance(Double.parseDouble(this.decimalFormat.format(accountBalance)));
        dao.update(setBudget);
        DbManager.closeConnectionSource();
    }

    public void updateMonthlyIncomeInDataBase(double monthlyIncome) throws SQLException {
        Dao<SetBudget, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);

        SetBudget setBudget = dao.queryForId(1);
        setBudget.setMonthlyIncome(Double.parseDouble(this.decimalFormat.format(monthlyIncome)));
        dao.update(setBudget);
        DbManager.closeConnectionSource();
    }

    public void substractAccountBalanceInDataBase(double debit) throws SQLException {
        Dao<SetBudget, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);

        SetBudget setBudget = dao.queryForId(1);
        double oldAccountBalance = setBudget.getAccountBalance();
        setBudget.setAccountBalance(Double.parseDouble(this.decimalFormat.format(oldAccountBalance - debit)));
        dao.update(setBudget);
        DbManager.closeConnectionSource();
    }

    public void addAccountBalanceInDataBase(double credit) throws SQLException {
        Dao<SetBudget, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);

        SetBudget setBudget = dao.queryForId(1);
        double oldAccountBalance = setBudget.getAccountBalance();
        setBudget.setAccountBalance(Double.parseDouble(this.decimalFormat.format(oldAccountBalance + credit)));
        dao.update(setBudget);
        DbManager.closeConnectionSource();
    }

    public void addMonthlyIncomeToAccountBalanceInDataBase() throws SQLException {
        Dao<SetBudget, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);

        SetBudget setBudget = dao.queryForId(1);
        double oldAccountBalance = setBudget.getAccountBalance();
        setBudget.setAccountBalance(Double.parseDouble(this.decimalFormat.format(oldAccountBalance + getMonthlyIncomeFromDataBase())));
        dao.update(setBudget);
        DbManager.closeConnectionSource();
    }

    public boolean checkIfBudgetIsSet() throws SQLException {
        Dao<SetBudget, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        SetBudget setBudget = dao.queryForId(1);
        if (setBudget == null)
        {
            DbManager.closeConnectionSource();
            return false;
        }
        DbManager.closeConnectionSource();
        return true;
    }

    public double getAccountBalanceFromDataBase() throws SQLException {
        Dao<SetBudget, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        SetBudget setBudget = dao.queryForId(1);
        double accountBalance = setBudget.getAccountBalance();
        DbManager.closeConnectionSource();
        return accountBalance;
    }

    public double getMonthlyIncomeFromDataBase() throws SQLException {
        Dao<SetBudget, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        SetBudget setBudget = dao.queryForId(1);
        double actualMonthlyIncome = setBudget.getMonthlyIncome();
        DbManager.closeConnectionSource();
        return actualMonthlyIncome;
    }

}