package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.database.modelFx.LoginModel;
import sample.database.modelFx.SetBudgetModel;
import sample.database.modelFx.SettingsModel;
import sample.utils.DialogUtils;
import sample.utils.FxmlUtils;

import java.sql.SQLException;

public class SetBudgetController {

    @FXML
    private Label usernameLabel;
    @FXML
    private TextField setMonthlyIncomeTextField;
    @FXML
    private TextField setAccountBalanceTextField;
    @FXML
    private Button applySetBudgetButton;
    @FXML
    private Button skipSetBudgetButton;

    private SetBudgetModel setBudgetModel;
    private SettingsModel settingsModel;
    private LoginModel loginModel;

    public static final String BORDER_PANE_MAIN_FXML = "/sample/fxml/BorderPaneMain.fxml";

    @FXML
    public void initialize() {
        this.setBudgetModel = new SetBudgetModel();

        this.settingsModel = new SettingsModel();
        try {
            settingsModel.init();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        this.loginModel = new LoginModel();

        setUsernameLabel();

        bindings();
    }

    public void setUsernameLabel() {
        try {
            this.usernameLabel.setText(this.loginModel.getLoggedUserFromDataBase());
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    private void bindings() {
        this.applySetBudgetButton.disableProperty().bind(this.setMonthlyIncomeTextField.textProperty().isEmpty()
                .or(this.setAccountBalanceTextField.textProperty().isEmpty()));
        try {
            if (!this.setBudgetModel.checkIfBudgetIsSet()) {
                this.skipSetBudgetButton.setDisable(true);
            }
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    @FXML
    public void onActionSetBudget(ActionEvent actionEvent) {
        if ((this.setMonthlyIncomeTextField.getText().matches("[\\d]+") ||
                this.setMonthlyIncomeTextField.getText().matches("[\\d]*[.][\\d]*")) &&
                (this.setAccountBalanceTextField.getText().matches("[\\d]+") ||
                this.setAccountBalanceTextField.getText().matches("[\\d]*[.][\\d]*"))) {
            try {
                double accountBalance = Double.parseDouble(setAccountBalanceTextField.getText());
                double monthlyIncome = Double.parseDouble(setMonthlyIncomeTextField.getText());
                this.setBudgetModel.saveBudgetInDataBase(accountBalance, monthlyIncome);
                FxmlUtils.newSceneFxmlLoader(BORDER_PANE_MAIN_FXML);
                FxmlUtils.closeStage(actionEvent);
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        } else {
            DialogUtils.amountError();
        }
    }

    @FXML
    public void onActionSkipSetBudgetButton(ActionEvent actionEvent) {
        FxmlUtils.newSceneFxmlLoader(BORDER_PANE_MAIN_FXML);
        FxmlUtils.closeStage(actionEvent);
    }
}
