package sample.controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import sample.utils.DialogUtils;

public class SettingsController {

    @FXML
    public void setCaspian() {
        Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
    }

    @FXML
    public void setModena() {
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
    }

    @FXML
    public void openHelp() { DialogUtils.dialogHelpApplication(); }

    @FXML
    public void openAbout() {
        DialogUtils.dialogAboutApplication();
    }
}
