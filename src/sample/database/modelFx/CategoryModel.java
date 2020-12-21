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
import sample.database.dbutils.DbManager;
import sample.database.models.Category;
import sample.utils.converters.CategoryConverter;
import sample.utils.exceptions.ApplicationException;

import java.sql.SQLException;
import java.util.List;

public class CategoryModel {

    LoginModel loginModel = new LoginModel();

    private ObjectProperty<CategoryFx> categoryFxObjectProperty = new SimpleObjectProperty<>(); // holds the currently selected category in comboBox
    private ObservableList<CategoryFx> categoryFxObservableList = FXCollections.observableArrayList(); // connected with comboBox

    public void init() throws SQLException {
        Dao<Category, Integer> categoryDao = DaoManager.createDao(DbManager.getConnectionSource(), Category.class);
        QueryBuilder<Category, Integer> categoryQueryBuilder = categoryDao.queryBuilder();
        categoryQueryBuilder.where().eq("username", this.loginModel.getLoggedUserFromDataBase());
        PreparedQuery<Category> prepareCategories = categoryQueryBuilder.prepare();
        List<Category> categories = categoryDao.query(prepareCategories);
        initCategoryList(categories);
    }

    private void initCategoryList(List<Category> categories) {
        this.categoryFxObservableList.clear();
        categories.forEach(category -> {
            CategoryFx categoryFx = CategoryConverter.convertToCategoryFx(category);
            this.categoryFxObservableList.add(categoryFx);
        });
    }

    public void saveCategoryInDataBase(String name) throws ApplicationException, SQLException {
        CategoryDao categoryDao = new CategoryDao();

        Category category = new Category();
        category.setUsername(this.loginModel.getLoggedUserFromDataBase());
        category.setCategory(name);
        categoryDao.createOrUpdate(category);
        init();
    }

    public void updateCategoryInDataBase() throws ApplicationException, SQLException {
        CategoryDao categoryDao = new CategoryDao();

        Category tempCategory = categoryDao.findById(Category.class, getCategoryFxObjectProperty().getId());
        tempCategory.setCategory(getCategoryFxObjectProperty().getName());
        categoryDao.createOrUpdate(tempCategory);
        init();
    }

    public void deleteCategoryById() throws ApplicationException, SQLException {
        CategoryDao categoryDao = new CategoryDao();

        categoryDao.deleteById(Category.class, categoryFxObjectProperty.getValue().getId());
        init();
    }

    public CategoryFx getCategoryFxObjectProperty() {
        return categoryFxObjectProperty.get();
    }

    public ObjectProperty<CategoryFx> categoryFxObjectPropertyProperty() {
        return categoryFxObjectProperty;
    }

    public void setCategoryFxObjectProperty(CategoryFx categoryFxObjectProperty) {
        this.categoryFxObjectProperty.set(categoryFxObjectProperty);
    }

    public ObservableList<CategoryFx> getCategoryFxObservableList() {
        return categoryFxObservableList;
    }

    public void setCategoryFxObservableList(ObservableList<CategoryFx> categoryFxObservableList) {
        this.categoryFxObservableList = categoryFxObservableList;
    }
}
