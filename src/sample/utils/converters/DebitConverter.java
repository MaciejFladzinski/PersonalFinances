package sample.utils.converters;

import sample.database.modelFx.DebitFx;
import sample.database.modelFx.LoginModel;
import sample.database.models.Debit;
import sample.utils.DialogUtils;
import sample.utils.Utils;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class DebitConverter {

    public static Debit convertToDebit(DebitFx debitFx) {
        Debit debit = new Debit();

        debit.setId(debitFx.getIdProperty());
        LoginModel loginModel = new LoginModel();
        try {
            debit.setUsername(loginModel.getLoggedUserFromDataBase());
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        debit.setDate(Utils.convertToDate(debitFx.getAddedDateProperty()));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        debit.setAmount(Double.parseDouble(decimalFormat.format(debitFx.getAmountProperty())));
        debit.setCategory(CategoryConverter.convertToCategory(debitFx.getCategoryFxProperty()));
        debit.setDescription(debitFx.getDescriptionProperty());

        return debit;
    }

    public static DebitFx convertToDebitFX(Debit debit) {
        DebitFx debitFx = new DebitFx();

        debitFx.setIdProperty(debit.getId());
        LoginModel loginModel = new LoginModel();
        try {
            debitFx.setUsernameProperty(loginModel.getLoggedUserFromDataBase());
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        debitFx.setAddedDateProperty(Utils.convertToLocalDate(debit.getDate()));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        debitFx.setAmountProperty(Double.parseDouble(decimalFormat.format(debit.getAmount())));
        debitFx.setCategoryFxProperty(CategoryConverter.convertToCategoryFx(debit.getCategory()));
        debitFx.setDescriptionProperty(debit.getDescription());

        return debitFx;
    }
}
