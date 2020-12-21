package sample.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.database.modelFx.LoginModel;
import sample.utils.DialogUtils;
import sample.utils.FxmlUtils;

import java.sql.SQLException;
import java.util.ResourceBundle;


public class LoginController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private Button cancelButton;

    private LoginModel loginModel;

    public static final String SET_BUDGET_FXML = "/sample/fxml/SetBudget.fxml";
    public static final String CREATE_ACCOUNT_FXML = "/sample/fxml/CreateAccount.fxml";

    static ResourceBundle bundle = FxmlUtils.getResourceBundle();

    @FXML
    public void initialize() {
        this.loginModel = new LoginModel();

        try {
            this.loginModel.removeLoggedUserFromDataBase();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        bindings();
    }

    public void bindings() {
        this.loginButton.disableProperty().bind(this.usernameTextField.textProperty().isEmpty()
                .or(this.passwordField.textProperty().isEmpty()));
    }

    @FXML
    public void onActionLogin(ActionEvent actionEvent) {
        try {
            if (this.loginModel.checkLoginAndPassword(this.usernameTextField.getText(), this.passwordField.getText())) {
                this.loginModel.addLoggedUserToDataBase(this.usernameTextField.getText());
                FxmlUtils.newSceneFxmlLoader(SET_BUDGET_FXML);
                FxmlUtils.closeStage(actionEvent);
            } else {
                this.errorLabel.setText(bundle.getString("login.error"));
                this.usernameTextField.clear();
                this.passwordField.clear();
            }
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    @FXML
    public void onActionCreateAccount(ActionEvent actionEvent) {
        FxmlUtils.newSceneFxmlLoader(CREATE_ACCOUNT_FXML);
        FxmlUtils.closeStage(actionEvent);
    }

    @FXML
    public void onActionCancel() {
        Platform.exit();
        System.exit(0);
    }
}
