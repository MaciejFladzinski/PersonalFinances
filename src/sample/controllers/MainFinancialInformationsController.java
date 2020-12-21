package sample.controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.database.dbutils.DbManager;
import sample.database.modelFx.*;
import sample.database.models.MonthlyExpenses;
import sample.database.models.SetBudget;
import sample.utils.DialogUtils;
import sample.utils.FxmlUtils;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MainFinancialInformationsController {

    public static final String LOGIN_FXML = "/sample/fxml/Login.fxml";

    @FXML
    private Label usernameInfoLabel;
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
    private Button logoutButton;
    @FXML
    private Button quitButton;

    private SetBudgetModel setBudgetModel;
    private MonthlyExpensesModel monthlyExpensesModel;
    private PlanningExpensesModel planningExpensesModel;
    private AddMonthlyIncomeModel addMonthlyIncomeModel;
    private LoginModel loginModel;

    @FXML
    public void initialize() {
        this.setBudgetModel = new SetBudgetModel();
        this.monthlyExpensesModel = new MonthlyExpensesModel();
        this.planningExpensesModel = new PlanningExpensesModel();
        this.addMonthlyIncomeModel = new AddMonthlyIncomeModel();
        this.loginModel = new LoginModel();

        bindings();
        initAll();
    }

    private void bindings() {
        this.editAccountBalanceButton.disableProperty().bind(editFinancialInfoTextField.textProperty().isEmpty());
        this.editMonthlyIncomeButton.disableProperty().bind(editFinancialInfoTextField.textProperty().isEmpty());
    }

    private void initAll() {
        initUsernameInfo();
        addMonthlyIncomeModel.addMonthlyIncome();
        try {
            initBudgetInfo();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        initMonthlyExpenses();
        initAverageOfMonthlyExpenses();
        initSumOfPlanningExpenses();
    }

    public void initUsernameInfo() {
        try {
            this.usernameInfoLabel.setText(this.loginModel.getLoggedUserFromDataBase());
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    private void initBudgetInfo() throws SQLException {
        Dao<SetBudget, Integer> setBudgetDao = DaoManager.createDao(DbManager.getConnectionSource(), SetBudget.class);
        QueryBuilder<SetBudget, Integer> setBudgetQueryBuilder = setBudgetDao.queryBuilder();
        setBudgetQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<SetBudget> prepareSetBudget = setBudgetQueryBuilder.prepare();
        List<SetBudget> setBudgets = setBudgetDao.query(prepareSetBudget);
        setBudgets.forEach(s -> {
            this.accountBalanceLabel.setText(String.valueOf(s.getAccountBalance()));
            this.monthlyIncomeLabel.setText(String.valueOf(s.getMonthlyIncome()));
        });
        DbManager.closeConnectionSource();
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

    @FXML
    public void onActionRefresh() {
        initAll();
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
        try {
            initBudgetInfo();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
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
        try {
            initBudgetInfo();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    private void clearEditBudget() {
        confirmCheckBox.setSelected(false);
        editFinancialInfoTextField.clear();
    }

    @FXML
    public void onActionLogout(ActionEvent actionEvent) {
        FxmlUtils.newSceneFxmlLoader(LOGIN_FXML);
        FxmlUtils.closeStage(actionEvent);
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
