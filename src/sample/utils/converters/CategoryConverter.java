package sample.utils.converters;

import sample.database.modelFx.CategoryFx;
import sample.database.modelFx.LoginModel;
import sample.database.models.Category;
import sample.utils.DialogUtils;

import java.sql.SQLException;

public class CategoryConverter {

    public static CategoryFx convertToCategoryFx(Category category){
        CategoryFx categoryFx = new CategoryFx();

        categoryFx.setId(category.getId());
        LoginModel loginModel = new LoginModel();
        try {
            categoryFx.setUsername(loginModel.getLoggedUserFromDataBase());
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        categoryFx.setName(category.getCategory());

        return categoryFx;
    }

    public static Category convertToCategory(CategoryFx categoryFx){
        Category category = new Category();

        category.setId(categoryFx.getId());
        LoginModel loginModel = new LoginModel();
        try {
            category.setUsername(loginModel.getLoggedUserFromDataBase());
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        category.setCategory(categoryFx.getName());

        return category;
    }
}
