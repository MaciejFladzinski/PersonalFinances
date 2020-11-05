package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import sample.database.modelFx.*;
import sample.utils.DialogUtils;
import sample.utils.exceptions.ApplicationException;

import java.sql.SQLException;
import java.time.LocalDate;

public class CreditsController {

    // input
    @FXML
    private DatePicker creditDatePicker;
    @FXML
    private TextField amountTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private Button addCreditButton;

    // tableView
    @FXML
    private TableView<CreditFx> creditsTableView;
    @FXML
    private TableColumn<CreditFx, LocalDate> dateCreditsColumn;
    @FXML
    private TableColumn<CreditFx, Number> amountCreditsColumn;
    @FXML
    private TableColumn<CreditFx, String> descriptionCreditsColumn;

    // delete credit
    @FXML
    private MenuItem deleteMenuItem;

    // models
    private CreditModel creditModel;
    private SetBudgetModel setBudgetModel;

    @FXML
    public void initialize() {
        this.creditModel = new CreditModel();
        try {
            creditModel.init();
        } catch (ApplicationException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        this.setBudgetModel = new SetBudgetModel();

        bindings();
        bindingsTableView();
    }

    private void bindings() {
        StringConverter doubleToString = new NumberStringConverter();
        this.creditModel.getCreditFxObjectProperty().addedDatePropertyProperty().bind(this.creditDatePicker.valueProperty());
        this.amountTextField.textProperty().bindBidirectional(this.creditModel.getCreditFxObjectProperty().amountPropertyProperty(), doubleToString);
        this.creditModel.getCreditFxObjectProperty().descriptionPropertyProperty().bind(this.descriptionTextField.textProperty());

        this.addCreditButton.disableProperty().bind(this.creditDatePicker.valueProperty().isNull()
                .or(this.amountTextField.textProperty().isEmpty())
                .or(this.descriptionTextField.textProperty().isEmpty()));

        this.deleteMenuItem.disableProperty().bind(this.creditsTableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void bindingsTableView() {
        this.creditsTableView.setItems(this.creditModel.getCreditFxObservableList());
        this.dateCreditsColumn.setCellValueFactory(cellData -> cellData.getValue().addedDatePropertyProperty());
        this.amountCreditsColumn.setCellValueFactory(cellData -> cellData.getValue().amountPropertyProperty());
        this.descriptionCreditsColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionPropertyProperty());

        this.descriptionCreditsColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        this.creditsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.creditModel.setCreditFxObjectPropertyEdit(newValue);
        });
    }

    @FXML
    public void onActionAddCredit() {
        try {
            this.creditModel.saveCreditInDataBase();
            this.setBudgetModel.addAccountBalanceInDataBase(this.creditModel.getCreditFxObjectProperty().getAmountProperty());
        } catch (ApplicationException | SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        clearFields();
    }

    private void clearFields() {
        this.amountTextField.clear();
        this.descriptionTextField.clear();
    }

    @FXML
    public void OnActionDeleteCredit() {
        try {
            this.setBudgetModel.substractAccountBalanceInDataBase(this.creditModel.getCreditFxObjectPropertyEdit().getAmountProperty());
            this.creditModel.deleteCreditFromDataBase();
        } catch (SQLException | ApplicationException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

    }

    @FXML
    public void onEditCommitDescription(TableColumn.CellEditEvent<CreditFx, String> creditFxStringCellEditEvent) {
        this.creditModel.getCreditFxObjectPropertyEdit().setDescriptionProperty(creditFxStringCellEditEvent.getNewValue());
        try {
            this.creditModel.updateDescriptionInDataBase(creditFxStringCellEditEvent.getNewValue());
        } catch (ApplicationException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }
}
