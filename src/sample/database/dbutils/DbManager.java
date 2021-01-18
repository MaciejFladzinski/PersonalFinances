package sample.database.dbutils;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import sample.database.models.*;

import java.sql.SQLException;

public class DbManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbManager.class);

    //private static final String JDBC_DRIVER_HD = "jdbc:sqlite:database.db";

    private static final String URL = "jdbc:mysql://77.79.251.52/pawelskie_personal?autoReconnect=true";
    private static final String USER = "pawelskie_pers";
    private static final String PASSWORD = "Jbe231fdA!";

    private static ConnectionSource connectionSource;

    public static void initDatabase() {
        createConnectionSource();
        //dropTable();
        createTable();
        closeConnectionSource();
    }

    private static void createConnectionSource() {
        try {
            connectionSource = new JdbcConnectionSource(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public static ConnectionSource getConnectionSource() {
        if (connectionSource == null) {
            createConnectionSource();
        }
        return connectionSource;
    }

    public static void closeConnectionSource() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }

    private static void createTable() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
            TableUtils.createTableIfNotExists(connectionSource, Debit.class);
            TableUtils.createTableIfNotExists(connectionSource, Credit.class);
            TableUtils.createTableIfNotExists(connectionSource, PlanningExpenses.class);
            TableUtils.createTableIfNotExists(connectionSource, SetBudget.class);
            TableUtils.createTableIfNotExists(connectionSource, MonthlyExpenses.class);
            TableUtils.createTableIfNotExists(connectionSource, AddMonthlyIncome.class);
            TableUtils.createTableIfNotExists(connectionSource, Settings.class);
            TableUtils.createTableIfNotExists(connectionSource, Users.class);
            TableUtils.createTableIfNotExists(connectionSource, LoggedUser.class);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    private static void dropTable() {
        try {
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, Debit.class, true);
            TableUtils.dropTable(connectionSource, Credit.class, true);
            TableUtils.dropTable(connectionSource, PlanningExpenses.class, true);
            TableUtils.dropTable(connectionSource, SetBudget.class, true);
            TableUtils.dropTable(connectionSource, MonthlyExpenses.class, true);
            TableUtils.dropTable(connectionSource, AddMonthlyIncome.class, true);
            TableUtils.dropTable(connectionSource, Settings.class, true);
            TableUtils.dropTable(connectionSource, Users.class, true);
            TableUtils.dropTable(connectionSource, LoggedUser.class, true);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
