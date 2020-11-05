package sample.controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.database.dbutils.DbManager;
import sample.database.modelFx.MonthlyExpensesModel;
import sample.database.modelFx.PlanningExpensesModel;
import sample.database.modelFx.SetBudgetModel;
import sample.database.models.MonthlyExpenses;
import sample.database.models.SetBudget;
import sample.utils.DialogUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class MainFinancialInformationsController {

    @FXML
    private Label accountBalanceLabel;
    @FXML
    private Label monthlyIncomeLabel;
    @FXML
    private Label sumOfExpensesThisMonthLabel;
    @FXML
    private Label averageMonthlyExpensesLabel;
    @FXML
    private Label sumOfPlannedExpensesLabel;
    @FXML
    private TextField editFinancialInfoTextField;
    @FXML
    private CheckBox confirmCheckBox;
    @FXML
    private Button editAccountBalanceButton;
    @FXML
    private Button editMonthlyIncomeButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button quitButton;

    private SetBudgetModel setBudgetModel;
    private MonthlyExpensesModel monthlyExpensesModel;
    private PlanningExpensesModel planningExpensesModel;

    @FXML
    public void initialize() {
        this.setBudgetModel = new SetBudgetModel();
        this.monthlyExpensesModel = new MonthlyExpensesModel();
        this.planningExpensesModel = new PlanningExpensesModel();

        initAll();
        bindings();
    }

    private void initBudgetInfo() {
        Dao<SetBudget, Integer> budgetDao = null;
        try {
            budgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
            this.accountBalanceLabel.setText(String.valueOf(budgetDao.queryForId(1).getAccountBalance()));
            this.monthlyIncomeLabel.setText(String.valueOf(budgetDao.queryForId(1).getMonthlyIncome()));
            DbManager.closeConnectionSource();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    private void initMonthlyExpenses() {
        Dao<MonthlyExpenses, Integer> monthlyExpensesDao = null;
        try {
            monthlyExpensesDao = DaoManager.createDao(DbManager.getConnectionSource(), MonthlyExpenses.class);
            int year = LocalDate.now().getYear();
            int month = LocalDate.now().getMonth().getValue();
            int value = this.monthlyExpensesModel.getIdActualMonthlyExpensesFromDataBase(year, month);
            if (monthlyExpensesDao.queryForId(value) == null) {
                this.sumOfExpensesThisMonthLabel.setText("0");
            } else {
                this.sumOfExpensesThisMonthLabel.setText(String.valueOf(monthlyExpensesDao.
                        queryForId(this.monthlyExpensesModel.getIdActualMonthlyExpensesFromDataBase(year, month))
                        .getSumOfExpensesThisMonth()));
            }
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        DbManager.closeConnectionSource();
    }

    private void initAverageOfMonthlyExpenses() {
        try {
            this.averageMonthlyExpensesLabel.setText(String.valueOf(this.monthlyExpensesModel.getAverageOfMonthlyExpenses()));
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    private void initSumOfPlanningExpenses() {
        try {
            this.sumOfPlannedExpensesLabel.setText(String.valueOf(this.planningExpensesModel.getSumOfPlanningExpenses()));
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    private void bindings() {
        this.editAccountBalanceButton.disableProperty().bind(editFinancialInfoTextField.textProperty().isEmpty());
        this.editMonthlyIncomeButton.disableProperty().bind(editFinancialInfoTextField.textProperty().isEmpty());
    }

    @FXML
    public void onActionRefresh() {
        initAll();
    }

    private void initAll() {
        initBudgetInfo();
        initMonthlyExpenses();
        initAverageOfMonthlyExpenses();
        initSumOfPlanningExpenses();
    }

    @FXML
    public void onActionUpdateAccountBalance() {
        if (confirmCheckBox.isSelected())
        {
            try {
                this.setBudgetModel.updateAccountBalanceInDataBase(Double.parseDouble(editFinancialInfoTextField.getText()));
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        }
        clearEditBudget();
        initBudgetInfo();
    }

    @FXML
    public void onActionUpdateMonthlyIncome() {
        if (confirmCheckBox.isSelected())
        {
            try {
                this.setBudgetModel.updateMonthlyIncomeInDataBase(Double.parseDouble(editFinancialInfoTextField.getText()));
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        }
        clearEditBudget();
        initBudgetInfo();
    }

    private void clearEditBudget() {
        confirmCheckBox.setSelected(false);
        editFinancialInfoTextField.clear();
    }

    @FXML
    public void quitApp() {
        Optional<ButtonType> result = DialogUtils.confirmationDialog();
        if (result.get()==ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }
}
