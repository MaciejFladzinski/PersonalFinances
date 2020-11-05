package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import sample.database.modelFx.SetBudgetModel;
import sample.utils.DialogUtils;
import sample.utils.FxmlUtils;

import java.sql.SQLException;

public class SetBudgetController {

    @FXML
    private TextField setMonthlyIncomeTextField;
    @FXML
    private TextField setAccountBalanceTextField;
    @FXML
    private Button applySetBudgetButton;
    @FXML
    private Button skipSetBudgetButton;

    private SetBudgetModel setBudgetModel;
    public static final String BORDER_PANE_MAIN_FXML = "/sample/fxml/BorderPaneMain.fxml";

    @FXML
    public void initialize() {
        this.setBudgetModel = new SetBudgetModel();
        bindings();
    }

    private void bindings() {
        this.applySetBudgetButton.disableProperty().bind(this.setMonthlyIncomeTextField.textProperty().isEmpty()
                .or(this.setAccountBalanceTextField.textProperty().isEmpty()));

        try {
            if (this.setBudgetModel.checkIfBudgetIsSet() == false) {
                this.skipSetBudgetButton.setDisable(true);
            }
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    @FXML
    public void onActionSetBudget(ActionEvent actionEvent) {
        double accountBalance = Double.parseDouble(setAccountBalanceTextField.getText());
        double monthlyIncome = Double.parseDouble(setMonthlyIncomeTextField.getText());
        try {
            this.setBudgetModel.saveBudgetInDataBase(accountBalance, monthlyIncome);
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        FxmlUtils.newSceneFxmlLoader(BORDER_PANE_MAIN_FXML);
        FxmlUtils.closeStage(actionEvent);
    }

    @FXML
    public void onActionSkipSetBudgetButton(ActionEvent actionEvent) {
        FxmlUtils.newSceneFxmlLoader(BORDER_PANE_MAIN_FXML);
        FxmlUtils.closeStage(actionEvent);
    }
}
