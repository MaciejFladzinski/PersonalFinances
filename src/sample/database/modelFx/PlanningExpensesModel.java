package sample.database.modelFx;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.database.dao.PlanningExpensesDao;
import sample.database.dbutils.DbManager;
import sample.database.models.PlanningExpenses;
import sample.utils.DialogUtils;
import sample.utils.FxmlUtils;
import sample.utils.converters.PlanningExpensesConverter;
import sample.utils.exceptions.ApplicationException;

import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class PlanningExpensesModel {

    private ObservableList<PlanningExpensesFx> planningExpensesFxObservableList = FXCollections.observableArrayList();

    private ObjectProperty<PlanningExpensesFx> planningExpensesFxObjectProperty = new SimpleObjectProperty<>(new PlanningExpensesFx());
    private ObjectProperty<PlanningExpensesFx> planningExpensesFxObjectPropertyEdit = new SimpleObjectProperty<>(new PlanningExpensesFx());

    private SetBudgetModel setBudgetModel;
    private MonthlyExpensesModel monthlyExpensesModel;

    public void init() throws ApplicationException {
        this.setBudgetModel = new SetBudgetModel();
        this.monthlyExpensesModel = new MonthlyExpensesModel();

        PlanningExpensesDao planningExpensesDao = new PlanningExpensesDao();

        List<PlanningExpenses> planningExpensesList = planningExpensesDao.queryForAll(PlanningExpenses.class);
        this.planningExpensesFxObservableList.clear();
        planningExpensesList.forEach(planningExpense -> {
            PlanningExpensesFx planningExpensesFx = PlanningExpensesConverter.convertToPlanningExpensesFx(planningExpense);
            planningExpensesFx.setPermissionProperty(calculateForPermission(planningExpensesFx.getAmountProperty()));
            planningExpensesFx.setCommentProperty(calculateForComment(planningExpensesFx.getAmountProperty()));
            this.planningExpensesFxObservableList.add(planningExpensesFx);
        });
    }

    public void savePlanningExpenseInDataBase() throws ApplicationException {
        PlanningExpensesDao planningExpensesDao = new PlanningExpensesDao();
        PlanningExpenses planningExpenses = PlanningExpensesConverter.convertToPlanningExpenses(this.getPlanningExpensesFxObjectProperty());
        planningExpenses.setPermission(calculateForPermission(planningExpenses.getAmount()));
        planningExpenses.setComment(calculateForComment(planningExpenses.getAmount()));
        planningExpensesDao.createOrUpdate(planningExpenses);
        init();
    }

    private String calculateForComment(double amount) {
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        double actualAccountBalance = 0;
        try {
            actualAccountBalance = this.setBudgetModel.getAccountBalanceFromDataBase();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        double actualAverageOfMonthlyExpenses = 0;
        try {
            actualAverageOfMonthlyExpenses = this.monthlyExpensesModel.getAverageOfMonthlyExpenses();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        double savings = actualAccountBalance - (actualAverageOfMonthlyExpenses + amount);
        if (savings >= 0) {
            return bundle.getString("comment.yes");
        }
        else {
            double missing = - savings;
            double actualMonthlyIncome = 0;
            try {
                actualMonthlyIncome = this.setBudgetModel.getMonthlyIncomeFromDataBase();
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }

            if (actualMonthlyIncome - actualAverageOfMonthlyExpenses <= 0) {
                return bundle.getString("comment.start.saving");
            }

            double timeNeeded = missing / (actualMonthlyIncome - actualAverageOfMonthlyExpenses);
            int monthsNeeded = (int) Math.ceil(timeNeeded);
            if (monthsNeeded == 1) {
                return bundle.getString("comment.planning.expenses") + monthsNeeded + bundle.getString("comment.month");
            }
            else {
                return bundle.getString("comment.planning.expenses") + monthsNeeded + bundle.getString("comment.months");
            }
        }
    }

    public String calculateForPermission(double amount) {
        ResourceBundle bundle = FxmlUtils.getResourceBundle();
        double actualAccountBalance = 0;
        try {
            actualAccountBalance = this.setBudgetModel.getAccountBalanceFromDataBase();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        double actualAverageOfMonthlyExpenses = 0;
        try {
            actualAverageOfMonthlyExpenses = this.monthlyExpensesModel.getAverageOfMonthlyExpenses();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        double savings = actualAccountBalance - (actualAverageOfMonthlyExpenses + amount);
        if (savings >= 0) {
            return bundle.getString("permission.yes");
        }
        else {
            return bundle.getString("permission.no");
        }
    }

    public void updateDescriptionInDataBase(String description) throws ApplicationException {
        PlanningExpensesDao planningExpensesDao = new PlanningExpensesDao();
        PlanningExpenses tempPlanningExpenses = planningExpensesDao.findById(PlanningExpenses.class, this.getPlanningExpensesFxObjectPropertyEdit().getIdProperty());
        tempPlanningExpenses.setDescription(description);
        planningExpensesDao.createOrUpdate(tempPlanningExpenses);
        init();
    }

    public void deletePlanningExpenseInDataBase() throws ApplicationException {
        PlanningExpensesDao planningExpensesDao = new PlanningExpensesDao();
        planningExpensesDao.deleteById(PlanningExpenses.class, this.getPlanningExpensesFxObjectPropertyEdit().getIdProperty());
        init();
    }

    public double getSumOfPlanningExpenses() throws SQLException {
        Dao<PlanningExpenses, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), PlanningExpenses.class);
        AtomicReference<Double> sum = new AtomicReference<>((double) 0);
        QueryBuilder<PlanningExpenses, Integer> expensesQueryBuilder = dao.queryBuilder();
        expensesQueryBuilder.selectColumns("amount");
        PreparedQuery<PlanningExpenses> prepareExpenses = expensesQueryBuilder.prepare();
        List<PlanningExpenses> expenses = dao.query(prepareExpenses);
        expenses.forEach(e -> {
            sum.updateAndGet(v -> new Double(v + e.getAmount()));
        });
        DbManager.closeConnectionSource();
        return sum.get();
    }

    public ObservableList<PlanningExpensesFx> getPlanningExpensesFxObservableList() {
        return planningExpensesFxObservableList;
    }

    public void setPlanningExpensesFxObservableList(ObservableList<PlanningExpensesFx> planningExpensesFxObservableList) {
        this.planningExpensesFxObservableList = planningExpensesFxObservableList;
    }

    public PlanningExpensesFx getPlanningExpensesFxObjectProperty() {
        return planningExpensesFxObjectProperty.get();
    }

    public ObjectProperty<PlanningExpensesFx> planningExpensesFxObjectPropertyProperty() {
        return planningExpensesFxObjectProperty;
    }

    public void setPlanningExpensesFxObjectProperty(PlanningExpensesFx planningExpensesFxObjectProperty) {
        this.planningExpensesFxObjectProperty.set(planningExpensesFxObjectProperty);
    }

    public PlanningExpensesFx getPlanningExpensesFxObjectPropertyEdit() {
        return planningExpensesFxObjectPropertyEdit.get();
    }

    public ObjectProperty<PlanningExpensesFx> planningExpensesFxObjectPropertyEditProperty() {
        return planningExpensesFxObjectPropertyEdit;
    }

    public void setPlanningExpensesFxObjectPropertyEdit(PlanningExpensesFx planningExpensesFxObjectPropertyEdit) {
        this.planningExpensesFxObjectPropertyEdit.set(planningExpensesFxObjectPropertyEdit);
    }
}
