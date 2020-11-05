package sample.database.modelFx;

import javafx.beans.property.*;

import java.time.LocalDate;

public class CreditFx {

    private IntegerProperty idProperty = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> addedDateProperty = new SimpleObjectProperty<>(LocalDate.now());
    private SimpleDoubleProperty amountProperty = new SimpleDoubleProperty();
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
