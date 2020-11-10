package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import sample.utils.FxmlUtils;

public class MainController {
    @FXML
    private BorderPane borderPane;

    @FXML
    private TopMenuButtonsController topMenuButtonsController;

    @FXML
    public void initialize() {
        topMenuButtonsController.setMainController(this);
    }

    public void setCenter(String fxmlPath) {
        borderPane.setCenter(FxmlUtils.fxmlLoader(fxmlPath));
    }
}
