import org.junit.jupiter.api.*;

public class TestSuite {

    @Nested
    @DisplayName("getSolutions Tests")
    class getSolutionsTests {

        @Test
        @DisplayName("Correctly solves equations with real solutions")
        void shouldCalculateCorrectSolutions_whenEquationHasRealSolutions() {
            QuadraticSolver.QuadraticRoots answers1 = QuadraticSolver.getSolutions(1.0, -10.0, 25.0);
            Assertions.assertEquals("5", answers1.root1());
            Assertions.assertEquals("5", answers1.root2());

            QuadraticSolver.QuadraticRoots answers2 = QuadraticSolver.getSolutions(6.0, -13.0, 6.0);
            Assertions.assertEquals("3/2", answers2.root1());
            Assertions.assertEquals("2/3", answers2.root2());
        }

        @Test
        @DisplayName("Correctly solves equations with complex solutions")
        void shouldCalculateCorrectSolutions_whenEquationHasComplexSolutions() {
            QuadraticSolver.QuadraticRoots answers = QuadraticSolver.getSolutions(1.0, 4.0, 5.0);
            Assertions.assertEquals("-2 + i", answers.root1());
            Assertions.assertEquals("-2 - i", answers.root2());
        }

        @Test
        @DisplayName("Throws Exception when coefficient a equals zero")
        void shouldThrowException_whenAEqualsZero() {
            Assertions.assertThrows(ArithmeticException.class, () -> QuadraticSolver.getSolutions(0.0, 1.0, 1.0));
        }
    }
}
