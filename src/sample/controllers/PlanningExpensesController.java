package sample.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import sample.database.modelFx.MonthlyExpensesModel;
import sample.database.modelFx.PlanningExpensesFx;
import sample.database.modelFx.PlanningExpensesModel;
import sample.database.modelFx.SetBudgetModel;
import sample.utils.DialogUtils;
import sample.utils.exceptions.ApplicationException;

import java.sql.SQLException;
import java.time.LocalDate;

public class PlanningExpensesController {

    // input
    @FXML
    private TextField amountTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private Button addPlanningExpenseButton;

    // tableView
    @FXML
    private TableView<PlanningExpensesFx> planningExpenseTableView;
    @FXML
    private TableColumn<PlanningExpensesFx, LocalDate> dateColumn;
    @FXML
    private TableColumn<PlanningExpensesFx, Number> amountColumn;
    @FXML
    private TableColumn<PlanningExpensesFx, String> descriptionColumn;
    @FXML
    private TableColumn<PlanningExpensesFx, String> permissionColumn;
    @FXML
    private TableColumn<PlanningExpensesFx, String> commentColumn;

    // delete planning expense
    @FXML
    private MenuItem deleteMenuItem;

    // models
    private PlanningExpensesModel planningExpensesModel;
    private SetBudgetModel setBudgetModel;
    private MonthlyExpensesModel monthlyExpensesModel;

    @FXML
    public void initialize() {
        this.planningExpensesModel = new PlanningExpensesModel();
        try {
            planningExpensesModel.init();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        this.setBudgetModel = new SetBudgetModel();
        this.monthlyExpensesModel = new MonthlyExpensesModel();

        bindings();
        bindingsTableView();
    }

    private void bindings() {
        StringConverter doubleToString = new NumberStringConverter();
        this.amountTextField.textProperty().bindBidirectional(this.planningExpensesModel.getPlanningExpensesFxObjectProperty().amountPropertyProperty(), doubleToString);
        this.planningExpensesModel.getPlanningExpensesFxObjectProperty().descriptionPropertyProperty().bind(this.descriptionTextField.textProperty());

        this.addPlanningExpenseButton.disableProperty().bind(this.amountTextField.textProperty().isEmpty().or(this.descriptionTextField.textProperty().isEmpty()));
        this.deleteMenuItem.disableProperty().bind(this.planningExpenseTableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void bindingsTableView() {
        this.planningExpenseTableView.setItems(this.planningExpensesModel.getPlanningExpensesFxObservableList());
        this.dateColumn.setCellValueFactory(cellData -> cellData.getValue().addedDatePropertyProperty());
        this.amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountPropertyProperty());
        this.descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionPropertyProperty());
        this.permissionColumn.setCellValueFactory(cellData -> cellData.getValue().permissionPropertyProperty());
        this.commentColumn.setCellValueFactory(cellData -> cellData.getValue().commentPropertyProperty());

        this.descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        this.planningExpenseTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.planningExpensesModel.setPlanningExpensesFxObjectPropertyEdit(newValue);
        });
    }

    @FXML
    public void onActionAddPlanningExpense() {
        if (this.amountTextField.getText().matches("[\\d]+") ||
                this.amountTextField.getText().matches("[\\d]*[.][\\d]*")) {
            try {
                this.planningExpensesModel.savePlanningExpenseInDataBase();
            } catch (ApplicationException | SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        } else {
            DialogUtils.amountError();
        }
        clearFields();
    }

    private void clearFields() {
        this.amountTextField.clear();
        this.descriptionTextField.clear();
    }

    @FXML
    public void onActionDeletePlanningExpense() {
        try {
            this.planningExpensesModel.deletePlanningExpenseInDataBase();
        } catch (ApplicationException | SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    @FXML
    public void onEditCommitDescription(TableColumn.CellEditEvent<PlanningExpensesFx, String> planningExpensesFxStringCellEditEvent) {
        this.planningExpensesModel.getPlanningExpensesFxObjectPropertyEdit().setDescriptionProperty(planningExpensesFxStringCellEditEvent.getNewValue());
        try {
            this.planningExpensesModel.updateDescriptionInDataBase(planningExpensesFxStringCellEditEvent.getNewValue());
        } catch (ApplicationException | SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }
}
