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

public class DebitsController {

    // input
    @FXML
    private DatePicker debitDatePicker;
    @FXML
    private TextField amountTextField;
    @FXML
    private ComboBox<CategoryFx> categoryComboBox;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private Button addDebitButton;

    // tableView
    @FXML
    private TableView<DebitFx> debitsTableView;
    @FXML
    private TableColumn<DebitFx, LocalDate> dateDebitsColumn;
    @FXML
    private TableColumn<DebitFx, Number> amountDebitsColumn;
    @FXML
    private TableColumn<DebitFx, CategoryFx> categoryDebitsColumn;
    @FXML
    private TableColumn<DebitFx, String> descriptionDebitsColumn;

    // add category
    @FXML
    private TextField addCategoryTextField;
    @FXML
    private Button addCategoryButton;

    // edit or remove category
    @FXML
    private Button editCategoryButton;
    @FXML
    private ComboBox<CategoryFx> removeCategoryComboBox;
    @FXML
    private Button removeCategoryButton;

    // delete debit
    @FXML
    private MenuItem deleteMenuItem;

    // models
    private DebitModel debitModel;
    private CategoryModel categoryModel;
    private SetBudgetModel setBudgetModel;
    private MonthlyExpensesModel monthlyExpensesModel;

    @FXML
    public void initialize() {
        this.debitModel = new DebitModel();
        try {
            this.debitModel.init();
        } catch (ApplicationException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        this.categoryModel = new CategoryModel();
        try {
            categoryModel.init();
        } catch (ApplicationException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        this.setBudgetModel = new SetBudgetModel();
        this.monthlyExpensesModel = new MonthlyExpensesModel();

        bindings();
        bindingsTableView();
    }

    private void bindings() {
        this.categoryComboBox.setItems(this.categoryModel.getCategoryFxObservableList());
        this.removeCategoryComboBox.setItems(this.categoryModel.getCategoryFxObservableList());

        StringConverter doubleToString = new NumberStringConverter();
        this.debitModel.getDebitFxObjectProperty().addedDatePropertyProperty().bind(this.debitDatePicker.valueProperty());
        this.amountTextField.textProperty().bindBidirectional(this.debitModel.getDebitFxObjectProperty().amountPropertyProperty(), doubleToString);
        this.debitModel.getDebitFxObjectProperty().categoryFxPropertyProperty().bind(this.categoryComboBox.valueProperty());
        this.debitModel.getDebitFxObjectProperty().descriptionPropertyProperty().bind(this.descriptionTextField.textProperty());

        this.addCategoryButton.disableProperty().bind(this.addCategoryTextField.textProperty().isEmpty());
        this.removeCategoryButton.disableProperty().bind(this.categoryModel.categoryFxObjectPropertyProperty().isNull());
        this.editCategoryButton.disableProperty().bind(this.categoryModel.categoryFxObjectPropertyProperty().isNull());
        this.addDebitButton.disableProperty().bind(this.amountTextField.textProperty().isEmpty()
                .or(this.categoryComboBox.valueProperty().isNull())
                .or(this.debitDatePicker.valueProperty().isNull()));
        this.deleteMenuItem.disableProperty().bind(this.debitsTableView.getSelectionModel().selectedItemProperty().isNull());
    }

    private void bindingsTableView() {
        this.debitsTableView.setItems(this.debitModel.getDebitFxObservableList());
        this.dateDebitsColumn.setCellValueFactory(cellData -> cellData.getValue().addedDatePropertyProperty());
        this.amountDebitsColumn.setCellValueFactory(cellData -> cellData.getValue().amountPropertyProperty());
        this.categoryDebitsColumn.setCellValueFactory(cellData -> cellData.getValue().categoryFxPropertyProperty());
        this.descriptionDebitsColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionPropertyProperty());

        this.descriptionDebitsColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        this.debitsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.debitModel.setDebitFxObjectPropertyEdit(newValue);
        });
    }

    // ----------------------------------- Category ----------------------------------------
    @FXML
    public void onActionAddDebitCategory() {
        try {
            categoryModel.saveCategoryInDataBase(addCategoryTextField.getText());

        } catch (ApplicationException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        addCategoryTextField.clear();
    }

    @FXML
    public void onActionEditDebitCategory() {
        String newEditCategory = DialogUtils.editDialog(this.categoryModel.getCategoryFxObjectProperty().getName());
        if(newEditCategory!=null) {
            this.categoryModel.getCategoryFxObjectProperty().setName(newEditCategory);
            try {
                this.categoryModel.updateCategoryInDataBase();
            } catch (ApplicationException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        }
    }

    @FXML
    public void onActionRemoveCategoryComboBox() {
        this.categoryModel.setCategoryFxObjectProperty(this.removeCategoryComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void onActionRemoveDebitCategory() {
        try {
            this.categoryModel.deleteCategoryById();
        } catch (ApplicationException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    // ----------------------------------- Debit ----------------------------------------
    @FXML
    public void onActionAddDebit() {
        try {
            this.debitModel.saveDebitInDataBase();
            this.setBudgetModel.substractAccountBalanceInDataBase(this.debitModel.getDebitFxObjectProperty().getAmountProperty());
            this.monthlyExpensesModel.saveDebitToMonthlyExpensesInDataBase(
                    this.debitModel.getDebitFxObjectProperty().getAddedDateProperty().getYear(),
                    this.debitModel.getDebitFxObjectProperty().getAddedDateProperty().getMonth().getValue(),
                    this.debitModel.getDebitFxObjectProperty().getAmountProperty());
        } catch (ApplicationException | SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        clearFields();
    }

    private void clearFields() {
        this.amountTextField.clear();
        this.categoryComboBox.getSelectionModel().clearSelection();
        this.descriptionTextField.clear();
    }

    @FXML
    public void OnActionDeleteDebit() {
        try {
            this.setBudgetModel.addAccountBalanceInDataBase(this.debitModel.getDebitFxObjectPropertyEdit().getAmountProperty());
            this.monthlyExpensesModel.removeDebitFromMonthlyExpensesInDataBase(
                    this.debitModel.getDebitFxObjectPropertyEdit().getAddedDateProperty().getYear(),
                    this.debitModel.getDebitFxObjectPropertyEdit().getAddedDateProperty().getMonth().getValue(),
                    this.debitModel.getDebitFxObjectPropertyEdit().getAmountProperty());
            this.debitModel.deleteDebitFromDataBase();
        } catch (ApplicationException | SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }

    @FXML
    public void onEditCommitDescription(TableColumn.CellEditEvent<DebitFx, String> debitFxStringCellEditEvent) {
        this.debitModel.getDebitFxObjectPropertyEdit().setDescriptionProperty(debitFxStringCellEditEvent.getNewValue());
        try {
            this.debitModel.updateDescriptionInDataBase(debitFxStringCellEditEvent.getNewValue());
        } catch (ApplicationException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
    }
}
