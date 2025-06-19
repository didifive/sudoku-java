package br.dev.zancanela.sudoku.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:sqlite:sudoku.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private DatabaseUtil () {
        throw new UnsupportedOperationException("Esta classe utilitária não pode ser instanciada.");
    }
}