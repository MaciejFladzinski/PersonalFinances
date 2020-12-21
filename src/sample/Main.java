package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.database.dbutils.DbManager;
import sample.utils.FxmlUtils;

import java.util.Locale;

public class Main extends Application {

    public static final String LOGIN_FXML = "/sample/fxml/Login.fxml";

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Locale.setDefault(new Locale("en"));

        Pane borderPane = FxmlUtils.fxmlLoader(LOGIN_FXML);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Personal Finances");
        primaryStage.setResizable(false);
        primaryStage.show();

        DbManager.initDatabase();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}