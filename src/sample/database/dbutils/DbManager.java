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

    private static final String JDBC_DRIVER_HD = "jdbc:sqlite:database.db";
    //private static final String USER = "";
    //private static final String PASSWORD = "";

    private static ConnectionSource connectionSource;

    public static void initDatabase() {
        createConnectionSource();
        //dropTable();
        createTable();
        closeConnectionSource();
    }

    private static void createConnectionSource() {
        try {
            connectionSource = new JdbcConnectionSource(JDBC_DRIVER_HD);
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
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
