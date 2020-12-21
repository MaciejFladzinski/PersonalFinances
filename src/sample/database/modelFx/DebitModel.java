package sample.database.modelFx;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.database.dao.CategoryDao;
import sample.database.dao.DebitDao;
import sample.database.dbutils.DbManager;
import sample.database.models.Category;
import sample.database.models.Debit;
import sample.utils.converters.CategoryConverter;
import sample.utils.converters.DebitConverter;
import sample.utils.exceptions.ApplicationException;

import java.sql.SQLException;
import java.util.List;

public class DebitModel {

    LoginModel loginModel = new LoginModel();

    private ObservableList<DebitFx> debitFxObservableList = FXCollections.observableArrayList();
    private ObservableList<CategoryFx> categoryFxObservableList = FXCollections.observableArrayList();
    
    private ObjectProperty<DebitFx> debitFxObjectProperty = new SimpleObjectProperty<>(new DebitFx());
    private ObjectProperty<DebitFx> debitFxObjectPropertyEdit = new SimpleObjectProperty<>(new DebitFx());

    public void init() throws SQLException {
        initCategoryList();

        Dao<Debit, Integer> debitDao = DaoManager.createDao(DbManager.getConnectionSource(), Debit.class);
        QueryBuilder<Debit, Integer> usernameQueryBuilder = debitDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<Debit> prepareDebits = usernameQueryBuilder.prepare();
        List<Debit> debits = debitDao.query(prepareDebits);
        this.debitFxObservableList.clear();
        debits.forEach(debit -> {
            DebitFx debitFx = DebitConverter.convertToDebitFX(debit);
            this.debitFxObservableList.add(debitFx);
        });
    }

    private void initCategoryList() throws SQLException {
        Dao<Category, Integer> categoryDao = DaoManager.createDao(DbManager.getConnectionSource(), Category.class);
        QueryBuilder<Category, Integer> usernameQueryBuilder = categoryDao.queryBuilder();
        usernameQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<Category> prepareCategories = usernameQueryBuilder.prepare();
        List<Category> categories = categoryDao.query(prepareCategories);
        this.categoryFxObservableList.clear();
        categories.forEach(category -> {
            CategoryFx categoryFx = CategoryConverter.convertToCategoryFx(category);
            this.categoryFxObservableList.add(categoryFx);
        });
    }

    public void saveDebitInDataBase() throws ApplicationException, SQLException {
        Debit debit = DebitConverter.convertToDebit(this.getDebitFxObjectProperty());

        CategoryDao categoryDao = new CategoryDao();
        Category category = categoryDao.findById(Category.class, this.getDebitFxObjectProperty().getCategoryFxProperty().getId());
        debit.setCategory(category);

        DebitDao debitDao = new DebitDao();
        debitDao.createOrUpdate(debit);
        init();
    }

    public void updateDescriptionInDataBase(String description) throws ApplicationException, SQLException {
        DebitDao debitDao = new DebitDao();

        Debit tempDebit = debitDao.findById(Debit.class, getDebitFxObjectPropertyEdit().getIdProperty());
        tempDebit.setDescription(description);
        debitDao.createOrUpdate(tempDebit);
        init();
    }

    public void deleteDebitFromDataBase() throws ApplicationException, SQLException {
        DebitDao debitDao = new DebitDao();
        debitDao.deleteById(Debit.class, this.getDebitFxObjectPropertyEdit().getIdProperty());
        init();
    }

    public ObservableList<DebitFx> getDebitFxObservableList() {
        return debitFxObservableList;
    }

    public void setDebitFxObservableList(ObservableList<DebitFx> debitFxObservableList) {
        this.debitFxObservableList = debitFxObservableList;
    }

    public DebitFx getDebitFxObjectProperty() {
        return debitFxObjectProperty.get();
    }

    public ObjectProperty<DebitFx> debitFxObjectPropertyProperty() {
        return debitFxObjectProperty;
    }

    public void setDebitFxObjectProperty(DebitFx debitFxObjectProperty) {
        this.debitFxObjectProperty.set(debitFxObjectProperty);
    }

    public DebitFx getDebitFxObjectPropertyEdit() {
        return debitFxObjectPropertyEdit.get();
    }

    public ObjectProperty<DebitFx> debitFxObjectPropertyEditProperty() {
        return debitFxObjectPropertyEdit;
    }

    public void setDebitFxObjectPropertyEdit(DebitFx debitFxObjectPropertyEdit) {
        this.debitFxObjectPropertyEdit.set(debitFxObjectPropertyEdit);
    }

    public ObservableList<CategoryFx> getCategoryFxObservableList() {
        return categoryFxObservableList;
    }

    public void setCategoryFxObservableList(ObservableList<CategoryFx> categoryFxObservableList) {
        this.categoryFxObservableList = categoryFxObservableList;
    }
}
