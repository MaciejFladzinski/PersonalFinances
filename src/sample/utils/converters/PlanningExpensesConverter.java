package sample.utils.converters;

import sample.database.modelFx.PlanningExpensesFx;
import sample.database.models.PlanningExpenses;
import sample.utils.Utils;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class PlanningExpensesConverter {

    public static PlanningExpenses convertToPlanningExpenses (PlanningExpensesFx planningExpensesFx) {
        PlanningExpenses planningExpenses = new PlanningExpenses();
        planningExpenses.setId(planningExpensesFx.getIdProperty());
        planningExpenses.setDate(Utils.convertToDate(LocalDate.now()));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        planningExpenses.setAmount(Double.parseDouble(decimalFormat.format(planningExpensesFx.getAmountProperty())));
        planningExpenses.setDescription(planningExpensesFx.getDescriptionProperty());
        planningExpenses.setPermission(planningExpensesFx.getPermissionProperty());
        planningExpenses.setComment(planningExpensesFx.getCommentProperty());
        return planningExpenses;
    }

    public static PlanningExpensesFx convertToPlanningExpensesFx (PlanningExpenses planningExpenses) {
        PlanningExpensesFx planningExpensesFx = new PlanningExpensesFx();
        planningExpensesFx.setIdProperty(planningExpenses.getId());
        planningExpensesFx.setAddedDateProperty(Utils.convertToLocalDate(planningExpenses.getDate()));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        planningExpensesFx.setAmountProperty(Double.parseDouble(decimalFormat.format(planningExpenses.getAmount())));
        planningExpensesFx.setDescriptionProperty(planningExpenses.getDescription());
        planningExpensesFx.setPermissionProperty(planningExpenses.getPermission());
        planningExpensesFx.setCommentProperty(planningExpenses.getComment());
        return planningExpensesFx;
    }
}
