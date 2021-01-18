package sample.utils.converters;

import sample.database.modelFx.CreditFx;
import sample.database.modelFx.LoginModel;
import sample.database.models.Credit;
import sample.utils.DialogUtils;
import sample.utils.Utils;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class CreditConverter {

    public static Credit convertToCredit(CreditFx creditFx) {
        Credit credit = new Credit();

        credit.setId(creditFx.getIdProperty());
        LoginModel loginModel = new LoginModel();
        try {
            credit.setUsername(loginModel.getLoggedUserFromDataBase());
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        credit.setDate(Utils.convertToDate(creditFx.getAddedDateProperty()));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        credit.setAmount(Double.parseDouble(decimalFormat.format(creditFx.getAmountProperty())));
        credit.setDescription(creditFx.getDescriptionProperty());

        return credit;
    }

    public static CreditFx convertToCreditFX(Credit credit) {
        CreditFx creditFx = new CreditFx();

        creditFx.setIdProperty(credit.getId());
        LoginModel loginModel = new LoginModel();
        try {
            creditFx.setUsernameProperty(loginModel.getLoggedUserFromDataBase());
        } catch (SQLException e) {
            DialogUtils.errorDialog(e.getMessage());
        }
        creditFx.setAddedDateProperty(Utils.convertToLocalDate(credit.getDate()));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        creditFx.setAmountProperty(Double.parseDouble(decimalFormat.format(credit.getAmount())));
        creditFx.setDescriptionProperty(credit.getDescription());

        return creditFx;
    }
}
