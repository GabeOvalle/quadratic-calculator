import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Provides utility methods for managing the user's equation history.
 *
 * <p>This class is responsible for creating and interacting with the
 * SQLite database that stores previously solved quadratic equations and
 * their coefficients. It supports adding equations, retrieving stored
 * equations and coefficients, and clearing the equation history.</p>
 *
 * @author Gabriel Ovalle
 */
public class UserHistoryUtil {

    private static final String URL = "jdbc:sqlite:equation_history.db";

    private UserHistoryUtil() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * Initializes the equation history database.
     *
     * <p>If the database or its table does not already exist, this method
     * creates the table used to store equations, their coefficients, and
     * the time at which they were solved.</p>
     */
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

    /**
     * Adds a quadratic equation and its coefficients to the equation history.
     *
     * @param equation the formatted quadratic equation to store
     * @param coefficients the coefficients associated with the equation
     */
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

    /**
     * Retrieves the most recent equations from the equation history.
     *
     * <p>The returned list contains up to thirty distinct equations ordered
     * from most recently solved to least recently solved.</p>
     *
     * @return an observable list containing the most recently stored
     *         equations
     */
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

    /**
     * Retrieves the coefficients associated with a stored quadratic equation.
     *
     * @param equation the equation whose coefficients are to be retrieved
     * @return a {@link Coefficients} object containing the coefficients of
     *         the specified equation, or {@code null} if the equation is not
     *         found
     */
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

    /**
     * Removes all equations from the equation history database.
     *
     * <p>This method also resets the table's auto-incrementing identifier so
     * that newly added equations begin again with the first identifier.</p>
     */
    public static void clearHistory() {
        try(Connection conn = DriverManager.getConnection(URL);
            Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM equation_history");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='equation_history'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Represents the coefficients of a quadratic equation.
     *
     * @param a the coefficient of the {@code x²} term
     * @param b the coefficient of the {@code x} term
     * @param c the constant term
     */
    public record Coefficients(Fraction a, Fraction b, Fraction c) {}
}
