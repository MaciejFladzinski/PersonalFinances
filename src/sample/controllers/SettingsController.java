package sample.controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.database.modelFx.LoginModel;
import sample.database.modelFx.SettingsModel;
import sample.utils.DialogUtils;
import sample.utils.FxmlUtils;

import java.sql.SQLException;
import java.util.ResourceBundle;

public class SettingsController {

    @FXML
    private ToggleButton caspianToggleButton;
    @FXML
    private ToggleButton modenaToggleButton;
    @FXML
    private ToggleGroup caspian_modena;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private Button changeFirstNameButton;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private Button changeLastNameButton;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmNewPasswordField;
    @FXML
    private Button newPasswordButton;
    @FXML
    private Label errorLabel;

    private SettingsModel settingsModel;
    private LoginModel loginModel;

    static ResourceBundle bundle = FxmlUtils.getResourceBundle();

    @FXML
    public void initialize() {
        this.settingsModel = new SettingsModel();
        try {
            String stylesheet = settingsModel.initStylesheetInDataBase();
            if (stylesheet.equals("modena")) {
                modenaToggleButton.setSelected(true);
            } else if (stylesheet.equals("caspian")) {
                caspianToggleButton.setSelected(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.loginModel = new LoginModel();

        bindings();
    }

    private void bindings() {
        this.changeFirstNameButton.disableProperty().bind(this.firstNameTextField.textProperty().isEmpty());
        this.changeLastNameButton.disableProperty().bind(this.lastNameTextField.textProperty().isEmpty());
        this.newPasswordButton.disableProperty().bind(this.passwordField.textProperty().isEmpty()
                .or(this.newPasswordField.textProperty().isEmpty())
                .or(this.confirmNewPasswordField.textProperty().isEmpty()));
    }

    @FXML
    public void setCaspian() {
        try {
            settingsModel.setStylesheetCaspianInDataBase();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    }

    @FXML
    public void setModena() {
        try {
            settingsModel.setStylesheetModenaInDataBase();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
    }

    @FXML
    public void openHelp() { DialogUtils.dialogHelpApplication(); }

    @FXML
    public void openAbout() {
        DialogUtils.dialogAboutApplication();
    }

    @FXML
    public void onActionChangeFirstName() {
        try {
            this.loginModel.updateFirstNameInDataBase(this.firstNameTextField.getText());
            DialogUtils.changeFirstNameDialog();
            this.firstNameTextField.clear();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    @FXML
    public void onActionChangeLastName() {
        try {
            this.loginModel.updateLastNameInDataBase(this.lastNameTextField.getText());
            DialogUtils.changeLastNameDialog();
            this.lastNameTextField.clear();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    @FXML
    public void onActionChangePassword() {
        try {
            if (this.loginModel.checkPasswordFromDataBase(this.passwordField.getText()) &&
            this.newPasswordField.getText().equals(this.confirmNewPasswordField.getText())) {
                this.loginModel.updatePasswordInDataBase(newPasswordField.getText());
                DialogUtils.changePasswordDialog();
                this.passwordField.clear();
                this.newPasswordField.clear();
                this.confirmNewPasswordField.clear();
            }
            else {
                this.errorLabel.setText(bundle.getString("create.account.error.password"));
            }
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }
}
