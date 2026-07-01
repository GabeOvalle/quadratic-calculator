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
                "a TEXT NOT NULL, " +
                "b TEXT NOT NULL, " +
                "c TEXT NOT NULL, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ");";
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addEquation(String equation, Coefficients coefficients) {
        String sql = "INSERT INTO equation_history(equation, a, b, c) VALUES(?, ?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equation);
            pstmt.setString(2, coefficients.a.toString());
            pstmt.setString(3, coefficients.b.toString());
            pstmt.setString(4, coefficients.c.toString());

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

    public static Coefficients getCoefficients(String equation) {
        String sql = "SELECT a, b, c FROM equation_history WHERE equation = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equation);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Fraction a = Fraction.getFraction(rs.getString("a"));
                Fraction b = Fraction.getFraction(rs.getString("b"));
                Fraction c = Fraction.getFraction(rs.getString("c"));

                return new Coefficients(a, b, c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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

    public record Coefficients(Fraction a, Fraction b, Fraction c) {}
}
