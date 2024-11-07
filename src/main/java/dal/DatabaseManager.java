package dal;

import exeptions.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public enum DatabaseManager {
    INSTANCE;

    public Connection getConnection()
    {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/weatherdb",
                    "mctg",
                    "mctgproject2024");
        } catch (SQLException e) {
            throw new DataAccessException("Datenbankverbindungsaufbau nicht erfolgreich", e);
        }
    }

}
