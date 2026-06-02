import org.junit.jupiter.api.*;

public class TestSuite {

    @Nested
    @DisplayName("getSolutions Tests")
    class getSolutionsTests {

        @Test
        @DisplayName("Correctly solves equations with real solutions")
        void shouldCalculateCorrectSolutions_whenEquationHasRealSolutions() {
            String[] answers1 = QuadraticSolver.getSolutions(1.0, -10.0, 25.0);
            Assertions.assertEquals("5.000", answers1[0]);
            Assertions.assertEquals("5.000", answers1[1]);

            String[] answers2 = QuadraticSolver.getSolutions(6.0, -13.0, 6.0);
            Assertions.assertEquals("1.500", answers2[0]);
            Assertions.assertEquals("0.667", answers2[1]);
        }

        @Test
        @DisplayName("Correctly solves equations with complex solutions")
        void shouldCalculateCorrectSolutions_whenEquationHasComplexSolutions() {
            String[] answers = QuadraticSolver.getSolutions(1.0, 4.0, 5.0);
            Assertions.assertEquals("-2.0 + i2.000/2.0", answers[0]);
            Assertions.assertEquals("-2.0 - i2.000/2.0", answers[1]);
        }

        @Test
        @DisplayName("Throws Exception when coefficient a equals zero")
        void shouldThrowException_whenAEqualsZero() {
            Assertions.assertThrows(ArithmeticException.class, () -> QuadraticSolver.getSolutions(0.0, 1.0, 1.0));
        }
    }
}
