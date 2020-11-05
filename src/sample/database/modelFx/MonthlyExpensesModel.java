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

    DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public void init() {
    }

    public void saveDebitToMonthlyExpensesInDataBase(int debitYear, int debitMonth, double amount) throws SQLException {
        Dao<MonthlyExpenses, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), MonthlyExpenses.class);
        AtomicBoolean done = new AtomicBoolean(false);
        QueryBuilder<MonthlyExpenses, Integer> yearQueryBuilder = dao.queryBuilder();
        yearQueryBuilder.where().eq("year", debitYear);
        PreparedQuery<MonthlyExpenses> prepareYear = yearQueryBuilder.prepare();
        List<MonthlyExpenses> dateYear = dao.query(prepareYear);
        dateYear.forEach(year -> {
            QueryBuilder<MonthlyExpenses, Integer> monthQueryBuilder = dao.queryBuilder();
            try {
                monthQueryBuilder.where().eq("month", debitMonth);
                PreparedQuery<MonthlyExpenses> prepareMonth = monthQueryBuilder.prepare();
                List<MonthlyExpenses> dateMonth = dao.query(prepareMonth);
                dateMonth.forEach(month -> {
                    MonthlyExpenses monthlyExpenses = null;
                    try {
                        monthlyExpenses = dao.queryForId(month.getId());
                    } catch (SQLException e) {
                        DialogUtils.errorDialog(e.getMessage());
                    }
                    monthlyExpenses.setSumOfExpensesThisMonth(Double.parseDouble(this.decimalFormat.format(monthlyExpenses.getSumOfExpensesThisMonth() + amount)));
                    try {
                        dao.update(monthlyExpenses);
                    } catch (SQLException e) {
                        DialogUtils.errorDialog(e.getMessage());
                    }
                    done.set(true);
                });
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        });
        if (!done.get()) {
            MonthlyExpenses newMonthlyExpenses = new MonthlyExpenses();
            newMonthlyExpenses.setYear(debitYear);
            newMonthlyExpenses.setMonth(debitMonth);
            newMonthlyExpenses.setSumOfExpensesThisMonth(Double.parseDouble(this.decimalFormat.format(amount)));
            dao.create(newMonthlyExpenses);
        }
        DbManager.closeConnectionSource();
    }

    public void removeDebitFromMonthlyExpensesInDataBase(int debitYear, int debitMonth, double amount) throws SQLException {
        Dao<MonthlyExpenses, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), MonthlyExpenses.class);
        QueryBuilder<MonthlyExpenses, Integer> yearQueryBuilder = dao.queryBuilder();
        yearQueryBuilder.where().eq("year", debitYear);
        PreparedQuery<MonthlyExpenses> prepareYear = yearQueryBuilder.prepare();
        List<MonthlyExpenses> dateYear = dao.query(prepareYear);
        dateYear.forEach(year -> {
            QueryBuilder<MonthlyExpenses, Integer> monthQueryBuilder = dao.queryBuilder();
            try {
                monthQueryBuilder.where().eq("month", debitMonth);
                PreparedQuery<MonthlyExpenses> prepareMonth = monthQueryBuilder.prepare();
                List<MonthlyExpenses> dateMonth = dao.query(prepareMonth);
                dateMonth.forEach(month -> {
                    MonthlyExpenses monthlyExpenses = null;
                    try {
                        monthlyExpenses = dao.queryForId(month.getId());
                    } catch (SQLException e) {
                        DialogUtils.errorDialog(e.getMessage());
                    }
                    monthlyExpenses.setSumOfExpensesThisMonth(Double.parseDouble(this.decimalFormat.format(monthlyExpenses.getSumOfExpensesThisMonth() - amount)));
                    try {
                        dao.update(monthlyExpenses);
                        if (monthlyExpenses.getSumOfExpensesThisMonth() == 0)
                        {
                            dao.deleteById(month.getId());
                        }
                    } catch (SQLException e) {
                        DialogUtils.errorDialog(e.getMessage());
                    }
                });
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        });
        DbManager.closeConnectionSource();
    }

    public int getIdActualMonthlyExpensesFromDataBase(int actualYear, int actualMonth) throws SQLException {
        Dao<MonthlyExpenses, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), MonthlyExpenses.class);

        AtomicInteger id = new AtomicInteger();

        QueryBuilder<MonthlyExpenses, Integer> yearQueryBuilder = dao.queryBuilder();
        yearQueryBuilder.where().eq("year", actualYear);
        PreparedQuery<MonthlyExpenses> prepareYear = yearQueryBuilder.prepare();
        List<MonthlyExpenses> dateYear = dao.query(prepareYear);
        dateYear.forEach(year -> {
            QueryBuilder<MonthlyExpenses, Integer> monthQueryBuilder = dao.queryBuilder();
            try {
                monthQueryBuilder.where().eq("month", actualMonth);
                PreparedQuery<MonthlyExpenses> prepareMonth = monthQueryBuilder.prepare();
                List<MonthlyExpenses> dateMonth = dao.query(prepareMonth);
                dateMonth.forEach(month -> {
                    id.set(month.getId());
                });
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        });
        DbManager.closeConnectionSource();
        return id.get();
    }

    public double getAverageOfMonthlyExpenses() throws SQLException {
        Dao<MonthlyExpenses, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), MonthlyExpenses.class);
        AtomicReference<Double> sum = new AtomicReference<>((double) 0);
        QueryBuilder<MonthlyExpenses, Integer> expensesQueryBuilder = dao.queryBuilder();
        expensesQueryBuilder.selectColumns("sum of expenses this month");
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