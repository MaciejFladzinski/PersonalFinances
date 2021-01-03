package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.database.modelFx.CreateAccountModel;
import sample.utils.DialogUtils;
import sample.utils.FxmlUtils;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateAccountController {

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button createNewAccountButton;
    @FXML
    private Button cancelButton;

    private CreateAccountModel createAccountModel;

    public static final String LOGIN_FXML = "/sample/fxml/Login.fxml";

    static ResourceBundle bundle = FxmlUtils.getResourceBundle();

    @FXML
    public void initialize() {
        this.createAccountModel = new CreateAccountModel();
        bindings();
    }

    public void bindings() {
        this.createNewAccountButton.disableProperty().bind(this.firstNameTextField.textProperty().isEmpty()
                .or(this.lastNameTextField.textProperty().isEmpty())
                .or(this.usernameTextField.textProperty().isEmpty())
                .or(this.passwordField.textProperty().isEmpty())
                .or(this.confirmPasswordField.textProperty().isEmpty()));
    }

    public void onActionCreateNewAccount(ActionEvent actionEvent) {
        if (this.passwordField.getText().equals(this.confirmPasswordField.getText()) &&
                this.firstNameTextField.getText().matches("[A-ZĄĆŃŚŹŻ][a-ząćńśźż]+") &&
                this.lastNameTextField.getText().matches("[A-ZĄĆŃŚŹŻ][a-ząćńśźż]+")) {
            try {
                if (!this.createAccountModel.checkIfExistUserInDataBase(this.usernameTextField.getText())) {
                    try {
                        createAccountModel.createUserInDataBase(this.firstNameTextField.getText(),
                                this.lastNameTextField.getText(),
                                this.usernameTextField.getText(),
                                this.passwordField.getText());
                        DialogUtils.userCreatedDialog();
                        FxmlUtils.newSceneFxmlLoader(LOGIN_FXML);
                        FxmlUtils.closeStage(actionEvent);
                    } catch (SQLException e) {
                        DialogUtils.errorDialog(e.getMessage());
                    }
                } else {
                    this.errorLabel.setText(bundle.getString("create.account.error.username"));
                    this.usernameTextField.clear();
                }
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        } else {
            this.errorLabel.setText(bundle.getString("create.account.error.password"));
            this.firstNameTextField.clear();
            this.lastNameTextField.clear();
            this.passwordField.clear();
            this.confirmPasswordField.clear();
        }
    }

    public void onActionCancel(ActionEvent actionEvent) {
        FxmlUtils.newSceneFxmlLoader(LOGIN_FXML);
        FxmlUtils.closeStage(actionEvent);
    }
}
