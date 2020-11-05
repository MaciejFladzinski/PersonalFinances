package sample.database.modelFx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.database.dao.CreditDao;
import sample.database.models.Credit;
import sample.utils.converters.CreditConverter;
import sample.utils.exceptions.ApplicationException;

import java.util.List;

public class CreditModel {

    private ObservableList<CreditFx> creditFxObservableList = FXCollections.observableArrayList();

    private ObjectProperty<CreditFx> creditFxObjectProperty = new SimpleObjectProperty<>(new CreditFx());
    private ObjectProperty<CreditFx> creditFxObjectPropertyEdit = new SimpleObjectProperty<>(new CreditFx());

    public void init() throws ApplicationException {
        CreditDao creditDao = new CreditDao();

        List<Credit> credits = creditDao.queryForAll(Credit.class);
        this.creditFxObservableList.clear();
        credits.forEach(credit -> {
            CreditFx creditFx = CreditConverter.convertToCreditFX(credit);
            this.creditFxObservableList.add(creditFx);
        });
    }

    public void saveCreditInDataBase() throws ApplicationException {
        CreditDao creditDao = new CreditDao();
        Credit credit = CreditConverter.convertCredit(this.getCreditFxObjectProperty());
        creditDao.createOrUpdate(credit);
        init();
    }

    public void updateDescriptionInDataBase(String description) throws ApplicationException {
        CreditDao creditDao = new CreditDao();

        Credit tempCredit = creditDao.findById(Credit.class, getCreditFxObjectPropertyEdit().getIdProperty());
        tempCredit.setDescription(description);
        creditDao.createOrUpdate(tempCredit);
        init();
    }

    public void deleteCreditFromDataBase() throws ApplicationException {
        CreditDao creditDao = new CreditDao();
        creditDao.deleteById(Credit.class, this.getCreditFxObjectPropertyEdit().getIdProperty());
        init();
    }

    public ObservableList<CreditFx> getCreditFxObservableList() {
        return creditFxObservableList;
    }

    public void setCreditFxObservableList(ObservableList<CreditFx> creditFxObservableList) {
        this.creditFxObservableList = creditFxObservableList;
    }

    public CreditFx getCreditFxObjectProperty() {
        return creditFxObjectProperty.get();
    }

    public ObjectProperty<CreditFx> creditFxObjectPropertyProperty() {
        return creditFxObjectProperty;
    }

    public void setCreditFxObjectProperty(CreditFx creditFxObjectProperty) {
        this.creditFxObjectProperty.set(creditFxObjectProperty);
    }

    public CreditFx getCreditFxObjectPropertyEdit() {
        return creditFxObjectPropertyEdit.get();
    }

    public ObjectProperty<CreditFx> creditFxObjectPropertyEditProperty() {
        return creditFxObjectPropertyEdit;
    }

    public void setCreditFxObjectPropertyEdit(CreditFx creditFxObjectPropertyEdit) {
        this.creditFxObjectPropertyEdit.set(creditFxObjectPropertyEdit);
    }
}
