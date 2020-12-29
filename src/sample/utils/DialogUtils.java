package sample.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;
import java.util.ResourceBundle;

public class DialogUtils {

    static ResourceBundle bundle = FxmlUtils.getResourceBundle();

    public static void dialogAboutApplication() {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle(bundle.getString("about.title"));
        informationAlert.setHeaderText(bundle.getString("about.header"));
        informationAlert.setContentText(bundle.getString("about.content"));
        informationAlert.showAndWait();
    }

    public static void dialogHelpApplication() {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle(bundle.getString("help.title"));
        informationAlert.setHeaderText(bundle.getString("help.header"));
        informationAlert.setContentText(bundle.getString("help.content"));
        informationAlert.showAndWait();
    }

    public static Optional<ButtonType> confirmationDialog() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(bundle.getString("exit.title"));
        confirmationAlert.setHeaderText(bundle.getString("exit.header"));
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        return result;
    }

    public static void errorDialog(String error) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(bundle.getString("error.title"));
        errorAlert.setHeaderText(bundle.getString("error.header"));

        TextArea textArea = new TextArea(error);
        errorAlert.getDialogPane().setContent(textArea);

        errorAlert.showAndWait();
    }

    public static void userCreatedDialog() {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle(bundle.getString("create.account.title"));
        informationAlert.setHeaderText(bundle.getString("create.account.header"));
        informationAlert.setContentText(bundle.getString("create.account.content"));
        informationAlert.showAndWait();
    }

    public static void amountError() {
        Alert informationAlert = new Alert(Alert.AlertType.ERROR);
        informationAlert.setTitle(bundle.getString("error.title"));
        informationAlert.setHeaderText(bundle.getString("error.header"));
        informationAlert.setContentText(bundle.getString("add.credit.error.content"));
        informationAlert.showAndWait();
    }

    public static void changeFirstNameDialog() {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle(bundle.getString("edit.account"));
        informationAlert.setHeaderText(bundle.getString("change.first.name.header"));
        informationAlert.setContentText(bundle.getString("change.content"));
        informationAlert.showAndWait();
    }

    public static void changeLastNameDialog() {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle(bundle.getString("edit.account"));
        informationAlert.setHeaderText(bundle.getString("change.last.name.header"));
        informationAlert.setContentText(bundle.getString("change.content"));
        informationAlert.showAndWait();
    }

    public static void changeUsernameDialog() {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle(bundle.getString("edit.account"));
        informationAlert.setHeaderText(bundle.getString("change.username.header"));
        informationAlert.setContentText(bundle.getString("change.content"));
        informationAlert.showAndWait();
    }

    public static void changePasswordDialog() {
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle(bundle.getString("edit.account"));
        informationAlert.setHeaderText(bundle.getString("change.password.header"));
        informationAlert.setContentText(bundle.getString("change.content"));
        informationAlert.showAndWait();
    }

    public static String editDialog(String value) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(bundle.getString("edit.title"));
        dialog.setHeaderText(bundle.getString("edit.header"));
        dialog.setContentText(bundle.getString("edit.content"));

        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()) {
            return result.get();
        }
        else
        {
            return null;
        }
    }
}
