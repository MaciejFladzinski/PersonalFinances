package sample.controllers;

import javafx.fxml.FXML;

public class TopMenuButtonsController {

    public static final String DEBITS_FXML = "/sample/fxml/Debits.fxml";
    public static final String CREDITS_FXML = "/sample/fxml/Credits.fxml";
    public static final String PLANNING_EXPENSES_FXML = "/sample/fxml/PlanningExpenses.fxml";
    public static final String SETTINGS_FXML = "/sample/fxml/Settings.fxml";
    public static final String STATISTICS_FXML = "/sample/fxml/Statistics.fxml";

    private MainController mainController;

    @FXML
    public void openDebits() {
        mainController.setCenter(DEBITS_FXML);
    }

    @FXML
    public void openCredits() {
        mainController.setCenter(CREDITS_FXML);
    }

    @FXML
    public void openPlanningExpenses() {
        mainController.setCenter(PLANNING_EXPENSES_FXML);
    }

    @FXML
    public void openStatistics() {mainController.setCenter(STATISTICS_FXML);}

    @FXML
    public void openSettings() {
        mainController.setCenter(SETTINGS_FXML);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
