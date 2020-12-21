package sample.database.modelFx;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import sample.database.dbutils.DbManager;
import sample.database.models.MonthlyExpenses;
import sample.utils.DialogUtils;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MonthlyExpensesModel {

    LoginModel loginModel = new LoginModel();

    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public void init() {
    }

    public void saveDebitToMonthlyExpensesInDataBase(int debitYear, int debitMonth, double amount) throws SQLException {
        Dao<MonthlyExpenses, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), MonthlyExpenses.class);
        AtomicBoolean done = new AtomicBoolean(false);
        QueryBuilder<MonthlyExpenses, Integer> monthlyExpensesQueryBuilder = dao.queryBuilder();
        monthlyExpensesQueryBuilder.where().eq("year", debitYear)
                .and().eq("month", debitMonth)
                .and().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<MonthlyExpenses> prepareMonthlyExpenses = monthlyExpensesQueryBuilder.prepare();
        List<MonthlyExpenses> monthlyExpenses = dao.query(prepareMonthlyExpenses);
        monthlyExpenses.forEach(monthlyExpense -> {
            try {
                monthlyExpense.setUsername(this.loginModel.getLoggedUserFromDataBase());
                monthlyExpense.setSumOfExpensesThisMonth(Double.parseDouble
                        (this.decimalFormat.format(monthlyExpense.getSumOfExpensesThisMonth() + amount)));
                dao.update(monthlyExpense);
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
            done.set(true);
        });

        if (!done.get()) {
            MonthlyExpenses newMonthlyExpenses = new MonthlyExpenses();
            newMonthlyExpenses.setUsername(this.loginModel.getLoggedUserFromDataBase());
            newMonthlyExpenses.setYear(debitYear);
            newMonthlyExpenses.setMonth(debitMonth);
            newMonthlyExpenses.setSumOfExpensesThisMonth(Double.parseDouble(this.decimalFormat.format(amount)));
            dao.create(newMonthlyExpenses);
        }
        DbManager.closeConnectionSource();
    }

    public void removeDebitFromMonthlyExpensesInDataBase(int debitYear, int debitMonth, double amount) throws SQLException {
        Dao<MonthlyExpenses, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), MonthlyExpenses.class);
        QueryBuilder<MonthlyExpenses, Integer> monthlyExpensesQueryBuilder = dao.queryBuilder();
        monthlyExpensesQueryBuilder.where().eq("year", debitYear)
                .and().eq("month", debitMonth)
                .and().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<MonthlyExpenses> prepareMonthlyExpenses = monthlyExpensesQueryBuilder.prepare();
        List<MonthlyExpenses> monthlyExpenses = dao.query(prepareMonthlyExpenses);
        monthlyExpenses.forEach(monthlyExpense -> {
            monthlyExpense.setSumOfExpensesThisMonth(Double.parseDouble(
                    this.decimalFormat.format(monthlyExpense.getSumOfExpensesThisMonth() - amount)));
            try {
                dao.update(monthlyExpense);
                if (monthlyExpense.getSumOfExpensesThisMonth() == 0) {
                    dao.deleteById(monthlyExpense.getId());
                }
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        });
        DbManager.closeConnectionSource();
    }

    public int getIdActualMonthlyExpensesFromDataBase(int actualYear, int actualMonth) throws SQLException {
        Dao<MonthlyExpenses, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), MonthlyExpenses.class);
        AtomicInteger id = new AtomicInteger();
        QueryBuilder<MonthlyExpenses, Integer> monthlyExpensesQueryBuilder = dao.queryBuilder();
        monthlyExpensesQueryBuilder.where().eq("year", actualYear)
                .and().eq("month", actualMonth)
                .and().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<MonthlyExpenses> prepareMonthlyExpenses = monthlyExpensesQueryBuilder.prepare();
        List<MonthlyExpenses> monthlyExpenses = dao.query(prepareMonthlyExpenses);
        monthlyExpenses.forEach(monthlyExpense -> {
                            id.set(monthlyExpense.getId());
        });
        DbManager.closeConnectionSource();
        return id.get();
    }

    public double getAverageOfMonthlyExpenses() throws SQLException {
        Dao<MonthlyExpenses, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), MonthlyExpenses.class);
        AtomicReference<Double> sum = new AtomicReference<>((double) 0);
        QueryBuilder<MonthlyExpenses, Integer> expensesQueryBuilder = dao.queryBuilder();
        expensesQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<MonthlyExpenses> prepareExpenses = expensesQueryBuilder.prepare();
        List<MonthlyExpenses> expenses = dao.query(prepareExpenses);
        expenses.forEach(e -> {
            sum.updateAndGet(v -> new Double(v + e.getSumOfExpensesThisMonth()));
        });

        double average = sum.get() / expenses.size();
        DbManager.closeConnectionSource();
        return Double.parseDouble(decimalFormat.format(average));
    }
}