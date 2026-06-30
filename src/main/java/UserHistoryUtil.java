import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserHistoryUtil {
    private static final String URL = "jdbc:sqlite:equation_history.db";

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS equation_history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "equation TEXT NOT NULL, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addEquation(String equation) {
        String sql = "INSERT INTO equation_history(equation) VALUES(?)";
        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equation);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> getPastEquations() {
        ObservableList<String> equations = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT equation FROM equation_history ORDER BY timestamp DESC LIMIT 30";
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                equations.add(rs.getString("equation"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return equations;
    }

    public static void clearHistory() {
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM equation_history");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='equation_history'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
