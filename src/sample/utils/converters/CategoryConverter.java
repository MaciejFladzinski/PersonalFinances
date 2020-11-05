package sample.utils.converters;

import sample.database.modelFx.CategoryFx;
import sample.database.models.Category;

public class CategoryConverter {

    public static CategoryFx convertToCategoryFx(Category category){
        CategoryFx categoryFx = new CategoryFx();
        categoryFx.setId(category.getId());
        categoryFx.setName(category.getCategory());
        return categoryFx;
    }

    public static Category convertToCategory(CategoryFx categoryFx){
        Category category = new Category();
        category.setId(categoryFx.getId());
        category.setCategory(categoryFx.getName());
        return category;
    }
}
