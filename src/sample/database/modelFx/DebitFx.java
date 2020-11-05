package sample.database.modelFx;

import javafx.beans.property.*;

import java.time.LocalDate;

public class DebitFx {

    private IntegerProperty idProperty = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> addedDateProperty = new SimpleObjectProperty<>(LocalDate.now());
    private SimpleDoubleProperty amountProperty = new SimpleDoubleProperty();
    private ObjectProperty<CategoryFx> categoryFxProperty = new SimpleObjectProperty<>();
    private SimpleStringProperty descriptionProperty = new SimpleStringProperty();

    public int getIdProperty() {
        return idProperty.get();
    }

    public IntegerProperty idPropertyProperty() {
        return idProperty;
    }

    public void setIdProperty(int idProperty) {
        this.idProperty.set(idProperty);
    }

    public LocalDate getAddedDateProperty() {
        return addedDateProperty.get();
    }

    public ObjectProperty<LocalDate> addedDatePropertyProperty() {
        return addedDateProperty;
    }

    public void setAddedDateProperty(LocalDate addedDateProperty) {
        this.addedDateProperty.set(addedDateProperty);
    }

    public double getAmountProperty() {
        return amountProperty.get();
    }

    public SimpleDoubleProperty amountPropertyProperty() {
        return amountProperty;
    }

    public void setAmountProperty(double amountProperty) {
        this.amountProperty.set(amountProperty);
    }

    public CategoryFx getCategoryFxProperty() {
        return categoryFxProperty.get();
    }

    public ObjectProperty<CategoryFx> categoryFxPropertyProperty() {
        return categoryFxProperty;
    }

    public void setCategoryFxProperty(CategoryFx categoryFxProperty) {
        this.categoryFxProperty.set(categoryFxProperty);
    }

    public String getDescriptionProperty() {
        return descriptionProperty.get();
    }

    public SimpleStringProperty descriptionPropertyProperty() {
        return descriptionProperty;
    }

    public void setDescriptionProperty(String descriptionProperty) {
        this.descriptionProperty.set(descriptionProperty);
    }
}
