package sample.controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import sample.database.dao.CategoryDao;
import sample.database.dbutils.DbManager;
import sample.database.modelFx.CategoryFx;
import sample.database.modelFx.CategoryModel;
import sample.database.modelFx.CreditModel;
import sample.database.modelFx.LoginModel;
import sample.database.models.Category;
import sample.database.models.Credit;
import sample.database.models.Debit;
import sample.utils.DialogUtils;
import sample.utils.FxmlUtils;
import sample.utils.Utils;
import sample.utils.exceptions.ApplicationException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StatisticsController {

    // input
    @FXML
    private ComboBox showLastComboBox;
    @FXML
    private ComboBox forComboBox;
    @FXML
    private CheckBox categoryCheckBox;
    @FXML
    private ComboBox<CategoryFx> categoryComboBox;
    @FXML
    private Button showButton;

    // line chart
    @FXML
    private LineChart<String, Double> statisticsLineChart;
    @FXML
    private CategoryAxis timeAxis; // X
    @FXML
    private NumberAxis amountAxis; // Y

    // models
    private CategoryModel categoryModel;
    private CreditModel creditModel;
    private LoginModel loginModel;

    static ResourceBundle bundle = FxmlUtils.getResourceBundle();

    private DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    ObservableList<XYChart.Data<String, Double>> data = FXCollections.observableArrayList();
    SortedList<XYChart.Data<String, Double>> sortedData = new SortedList<>(data, (data1, data2) ->
            data1.getXValue().compareTo(data2.getXValue()));

    @FXML
    public void initialize() {
        this.categoryModel = new CategoryModel();
        try {
            categoryModel.init();
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }

        this.creditModel = new CreditModel();
        this.loginModel = new LoginModel();

        bindings();
    }

    private void bindings() {
        this.categoryComboBox.setItems(this.categoryModel.getCategoryFxObservableList());
        ObservableList<String> showLastList = FXCollections.observableArrayList(bundle.getString("statistics.month"), bundle.getString("statistics.2months"), bundle.getString("statistics.3months"), bundle.getString("statistics.6months"), bundle.getString("statistics.year"), bundle.getString("statistics.from.beginning"));
        this.showLastComboBox.setItems(showLastList);
        ObservableList<String> forList = FXCollections.observableArrayList(bundle.getString("statistics.debits"), bundle.getString("statistics.credits"));
        this.forComboBox.setItems(forList);
    }

    @FXML
    public void onActionShowLineChart() {
        statisticsLineChart.getData().clear();
        data.clear();
        statisticsLineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
        statisticsLineChart.setAnimated(false);

        XYChart.Series<String, Double> series = new XYChart.Series<>(sortedData);
        statisticsLineChart.getData().add(series);

        if (this.forComboBox.getValue() == bundle.getString("statistics.debits")) {
            try {
                series.setName(showLastDebitsFromDataBase());
            } catch (SQLException | ApplicationException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        } else if (this.forComboBox.getValue() == bundle.getString("statistics.credits")) {
            categoryCheckBox.setSelected(false);
            try {
                series.setName(showLastCreditsFromDataBase());
            } catch (SQLException | ApplicationException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        }
    }

    private String showLastDebitsFromDataBase() throws SQLException, ApplicationException {
        initLabelInLineChart();
        if (this.showLastComboBox.getValue() == bundle.getString("statistics.month")) {
            getDebitXMonthsFromDataBase(1);
            if (categoryCheckBox.isSelected() && !categoryComboBox.getValue().getName().isEmpty()) {
                return bundle.getString("statistics.debits.month") + " " +
                        bundle.getString("statistics.for.category") + " " + categoryComboBox.getValue().getName();
            }
            else {
                return bundle.getString("statistics.debits.month");
            }
        } else if (this.showLastComboBox.getValue() == bundle.getString("statistics.2months")) {
            getDebitXMonthsFromDataBase(2);
            if (categoryCheckBox.isSelected() && !categoryComboBox.getValue().getName().isEmpty()) {
                return bundle.getString("statistics.debits.2months") + " " +
                        bundle.getString("statistics.for.category") + " " + categoryComboBox.getValue().getName();
            }
            else {
                return bundle.getString("statistics.debits.2months");
            }
        } else if (this.showLastComboBox.getValue() == bundle.getString("statistics.3months")) {
            getDebitXMonthsFromDataBase(3);
            if (categoryCheckBox.isSelected() && !categoryComboBox.getValue().getName().isEmpty()) {
                return bundle.getString("statistics.debits.3months") + " " +
                        bundle.getString("statistics.for.category") + " " + categoryComboBox.getValue().getName();
            }
            else {
                return bundle.getString("statistics.debits.3months");
            }
        } else if (this.showLastComboBox.getValue() == bundle.getString("statistics.6months")) {
            getDebitXMonthsFromDataBase(6);
            if (categoryCheckBox.isSelected() && !categoryComboBox.getValue().getName().isEmpty()) {
                return bundle.getString("statistics.debits.6months") + " " +
                        bundle.getString("statistics.for.category") + " " + categoryComboBox.getValue().getName();
            }
            else {
                return bundle.getString("statistics.debits.6months");
            }
        } else if (this.showLastComboBox.getValue() == bundle.getString("statistics.year")) {
            getDebitXMonthsFromDataBase(12);
            if (categoryCheckBox.isSelected() && !categoryComboBox.getValue().getName().isEmpty()) {
                return bundle.getString("statistics.debits.year") + " " +
                        bundle.getString("statistics.for.category") + " " + categoryComboBox.getValue().getName();
            }
            else {
                return bundle.getString("statistics.debits.year");
            }
        } else if (this.showLastComboBox.getValue() == bundle.getString("statistics.from.beginning")) {
            Dao<Debit, Integer> debitDao = DaoManager.createDao(DbManager.getConnectionSource(), Debit.class);

            if (categoryCheckBox.isSelected() && !categoryComboBox.getValue().getName().isEmpty()) {
                CategoryDao categoryDao = new CategoryDao();
                List<Category> categories = categoryDao.queryForAll(Category.class);

                AtomicInteger categoryId = new AtomicInteger();

                categories.forEach(c -> {
                    try {
                        if (c.getCategory().equals(categoryComboBox.getValue().getName()) &&
                                c.getUsername().equals(this.loginModel.getLoggedUserFromDataBase())) {
                            categoryId.set(c.getId());
                        }
                    } catch (SQLException e) {
                        DialogUtils.errorDialog(e.getMessage());
                    }
                });

                Category choosedCategory = categoryDao.findById(Category.class, categoryId.get());
                String categoryName = choosedCategory.getCategory();

                QueryBuilder<Debit, Integer> debitQueryBuilder = debitDao.queryBuilder();
                debitQueryBuilder.where().eq("category_id", categoryId);
                PreparedQuery<Debit> debitPreparedQuery = debitQueryBuilder.prepare();
                List<Debit> categoryDebits = debitDao.query(debitPreparedQuery);

                categoryDebits.forEach(categoryDebit -> {
                    String formattedDate = formatter.format(Utils.convertToLocalDate(categoryDebit.getDate()));
                    addData(data, formattedDate, categoryDebit.getAmount());
                });
                return bundle.getString("statistics.debits.from.beginning") + " " +
                        bundle.getString("statistics.for.category") + " " + categoryName;
            } else {
                QueryBuilder<Debit, Integer> debitQueryBuilder = debitDao.queryBuilder();
                debitQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
                PreparedQuery<Debit> prepareDebits = debitQueryBuilder.prepare();
                List<Debit> debits = debitDao.query(prepareDebits);
                debits.forEach(d -> {
                    String formattedDate = formatter.format(Utils.convertToLocalDate(d.getDate()));
                    addData(data, formattedDate, d.getAmount());
                });
                return bundle.getString("statistics.debits.from.beginning");
            }
        }
        return "";
    }

    private void initLabelInLineChart() {
        amountAxis.setLabel(bundle.getString("statistics.amount"));
        timeAxis.setLabel(bundle.getString("statistics.date"));
        statisticsLineChart.setTitle(bundle.getString("statistics.finances"));
    }

    private void getDebitXMonthsFromDataBase(int last) throws SQLException, ApplicationException {
        Date now = Utils.convertToDate(LocalDate.now());
        Date from = Utils.convertToDate(LocalDate.now().minusMonths(last));

        Dao<Debit, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), Debit.class);
        QueryBuilder<Debit, Integer> debitQueryBuilder = dao.queryBuilder();
        debitQueryBuilder.where().between("date", from, now);
        PreparedQuery<Debit> debitPreparedQuery = debitQueryBuilder.prepare();
        List<Debit> debits = dao.query(debitPreparedQuery);

        if (categoryCheckBox.isSelected() && !categoryComboBox.getValue().getName().isEmpty()) {
            CategoryDao categoryDao = new CategoryDao();
            List<Category> categories = categoryDao.queryForAll(Category.class);

            AtomicInteger categoryId = new AtomicInteger();

            categories.forEach(c -> {
                try {
                    if (c.getCategory().equals(categoryComboBox.getValue().getName()) &&
                            c.getUsername().equals(this.loginModel.getLoggedUserFromDataBase())) {
                        categoryId.set(c.getId());
                    }
                } catch (SQLException e) {
                    DialogUtils.errorDialog(e.getMessage());
                }
            });

            debits.forEach(debit -> {
                    if (debit.getCategory().getId() == categoryId.get()) {
                        String formattedDate = formatter.format(Utils.convertToLocalDate(debit.getDate()));
                        addData(data, formattedDate, debit.getAmount());
                    }
            });
        } else {
            debits.forEach(debit -> {
                try {
                    if (debit.getUsername().equals(this.loginModel.getLoggedUserFromDataBase())) {
                        String formattedDate = formatter.format(Utils.convertToLocalDate(debit.getDate()));
                        addData(data, formattedDate, debit.getAmount());
                    }
                } catch (SQLException e) {
                    DialogUtils.errorDialog(e.getMessage());
                }
            });
            DbManager.closeConnectionSource();
        }
    }

    private String showLastCreditsFromDataBase() throws SQLException, ApplicationException {
        initLabelInLineChart();
        if (this.showLastComboBox.getValue() == bundle.getString("statistics.month")) {
            getCreditXMonthsFromDataBase(1);
            return bundle.getString("statistics.credits.month");
        } else if (this.showLastComboBox.getValue() == bundle.getString("statistics.2months")) {
            getCreditXMonthsFromDataBase(2);
            return bundle.getString("statistics.credits.2months");
        } else if (this.showLastComboBox.getValue() == bundle.getString("statistics.3months")) {
            getCreditXMonthsFromDataBase(3);
            return bundle.getString("statistics.credits.3months");
        } else if (this.showLastComboBox.getValue() == bundle.getString("statistics.6months")) {
            getCreditXMonthsFromDataBase(6);
            return bundle.getString("statistics.credits.6months");
        } else if (this.showLastComboBox.getValue() == bundle.getString("statistics.year")) {
            getCreditXMonthsFromDataBase(12);
            return bundle.getString("statistics.credits.year");
        } else if (this.showLastComboBox.getValue() == bundle.getString("statistics.from.beginning")) {
            Dao<Credit, Integer> creditDao = DaoManager.createDao(DbManager.getConnectionSource(), Credit.class);
            QueryBuilder<Credit, Integer> creditQueryBuilder = creditDao.queryBuilder();
            creditQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
            PreparedQuery<Credit> prepareCredits = creditQueryBuilder.prepare();
            List<Credit> credits = creditDao.query(prepareCredits);
            credits.forEach(c -> {
                String formattedDate = formatter.format(Utils.convertToLocalDate(c.getDate()));
                addData(data, formattedDate, c.getAmount());
            });
            return bundle.getString("statistics.credits.from.beginning");
        }
        return "";
    }

    private void getCreditXMonthsFromDataBase(int last) throws SQLException {
        Date now = Utils.convertToDate(LocalDate.now());
        Date from = Utils.convertToDate(LocalDate.now().minusMonths(last));

        Dao<Credit, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), Credit.class);
        QueryBuilder<Credit, Integer> creditQueryBuilder = dao.queryBuilder();
        creditQueryBuilder.where().between("date", from, now);
        PreparedQuery<Credit> creditPreparedQuery = creditQueryBuilder.prepare();
        List<Credit> credits = dao.query(creditPreparedQuery);
        credits.forEach(c -> {
            try {
                if (c.getUsername().equals(this.loginModel.getLoggedUserFromDataBase())) {
                    String formattedDate = formatter.format(Utils.convertToLocalDate(c.getDate()));
                    addData(data, formattedDate, c.getAmount());
                }
            } catch (SQLException e) {
                DialogUtils.errorDialog(e.getMessage());
            }
        });
        DbManager.closeConnectionSource();
    }

    private void addData(ObservableList<XYChart.Data<String, Double>> data, String formattedDate, double value) {
        XYChart.Data<String, Double> dataAtDate = data.stream()
                .filter(d -> d.getXValue().equals(formattedDate))
                .findAny()
                .orElseGet(() -> {
                    XYChart.Data<String, Double> newData = new XYChart.Data<String, Double>(formattedDate, 0.0);
                    data.add(newData);
                    return newData;
                });
        dataAtDate.setYValue(dataAtDate.getYValue() + value);
    }
}
