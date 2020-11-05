package sample.database.modelFx;

import javafx.beans.property.*;

import java.time.LocalDate;

public class PlanningExpensesFx {

    private IntegerProperty idProperty = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> addedDateProperty = new SimpleObjectProperty<>(LocalDate.now());
    private SimpleDoubleProperty amountProperty = new SimpleDoubleProperty();
    private SimpleStringProperty descriptionProperty = new SimpleStringProperty();
    private SimpleStringProperty permissionProperty = new SimpleStringProperty();
    private SimpleStringProperty commentProperty = new SimpleStringProperty();

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

    public String getPermissionProperty() {
        return permissionProperty.get();
    }

    public SimpleStringProperty permissionPropertyProperty() {
        return permissionProperty;
    }

    public void setPermissionProperty(String permissionProperty) {
        this.permissionProperty.set(permissionProperty);
    }

    public String getCommentProperty() {
        return commentProperty.get();
    }

    public SimpleStringProperty commentPropertyProperty() {
        return commentProperty;
    }

    public void setCommentProperty(String commentProperty) {
        this.commentProperty.set(commentProperty);
    }
}
