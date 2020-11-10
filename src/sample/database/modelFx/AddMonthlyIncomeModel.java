package sample.database.modelFx;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import sample.database.dbutils.DbManager;
import sample.database.models.AddMonthlyIncome;
import sample.utils.DialogUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddMonthlyIncomeModel {

    SetBudgetModel setBudgetModel = new SetBudgetModel();

    public void init() {

    }

    public void addMonthlyIncome() {
        int actualYear = LocalDate.now().getYear();
        int actualMonth = LocalDate.now().getMonth().getValue();
        AtomicBoolean exist = new AtomicBoolean(false);
        Dao<AddMonthlyIncome, Integer> addMonthlyIncomeDao = null;
        try {
            addMonthlyIncomeDao = DaoManager.createDao(DbManager.getConnectionSource(), AddMonthlyIncome.class);
            List<AddMonthlyIncome> addMonthlyIncomeList = addMonthlyIncomeDao.queryForAll();
            addMonthlyIncomeList.forEach(monthlyIncome -> {
                if (monthlyIncome.getYear() == actualYear && monthlyIncome.getMonth() == actualMonth && monthlyIncome.isAdd()) {
                    exist.set(true);
                }
            });

            if (!exist.get()) {
                AddMonthlyIncome addMonthlyIncome = new AddMonthlyIncome();
                addMonthlyIncome.setYear(actualYear);
                addMonthlyIncome.setMonth(actualMonth);
                addMonthlyIncome.setAdd(true);
                addMonthlyIncomeDao.create(addMonthlyIncome);
                DbManager.closeConnectionSource();
                try {
                    setBudgetModel.addMonthlyIncomeToAccountBalanceInDataBase();
                } catch (SQLException e) {
                    DialogUtils.errorDialog(e.getMessage());
                }
            }
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

}