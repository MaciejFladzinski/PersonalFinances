package sample.controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import sample.database.modelFx.SettingsModel;
import sample.utils.DialogUtils;

import java.sql.SQLException;

public class SettingsController {

    @FXML
    private ToggleButton caspianToggleButton;
    @FXML
    private ToggleButton modenaToggleButton;
    @FXML
    private ToggleGroup caspian_modena;

    private SettingsModel settingsModel;

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
}
