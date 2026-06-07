import org.junit.jupiter.api.*;

public class TestSuite {

    @Nested
    @DisplayName("getSolutions Tests")
    class getSolutionsTests {

        @Test
        @DisplayName("Correctly solves equations with real solutions")
        void shouldCalculateCorrectSolutions_whenEquationHasRealSolutions() {
            QuadraticSolver.QuadraticRoots answers1 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(-10, 1),
                    Fraction.getFraction(25, 1)
            );
            Assertions.assertEquals("5", answers1.root1());
            Assertions.assertEquals("5", answers1.root2());

            QuadraticSolver.QuadraticRoots answers2 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(6, 1),
                    Fraction.getFraction(-13,1),
                    Fraction.getFraction(6, 1)
            );
            Assertions.assertEquals("3/2", answers2.root1());
            Assertions.assertEquals("2/3", answers2.root2());

            QuadraticSolver.QuadraticRoots answers3 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(9, 4),
                    Fraction.getFraction(1, 2)
            );
            Assertions.assertEquals("-1/4", answers3.root1());
            Assertions.assertEquals("-2", answers3.root2());
        }

        @Test
        @DisplayName("Correctly solves equations with complex solutions")
        void shouldCalculateCorrectSolutions_whenEquationHasComplexSolutions() {
            QuadraticSolver.QuadraticRoots answers1 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(4, 1),
                    Fraction.getFraction(5, 1)
            );
            Assertions.assertEquals("-2 + i", answers1.root1());
            Assertions.assertEquals("-2 - i", answers1.root2());

            QuadraticSolver.QuadraticRoots answers2 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(7, 1)
            );
            Assertions.assertEquals("-1/2 + i√27/2.0", answers2.root1());
            Assertions.assertEquals("-1/2 - i√27/2.0", answers2.root2());
        }

        @Test
        @DisplayName("Correctly solves equations with irrational solutions")
        void shouldCalculateCorrectSolutions_whenEquationHasIrrationalSolutions() {
            QuadraticSolver.QuadraticRoots answers = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(6, 1),
                    Fraction.getFraction(7, 1)
            );
            Assertions.assertEquals("-3 + √8/2.0; ≈-1.5858", answers.root1());
            Assertions.assertEquals("-3 - √8/2.0; ≈-4.4142", answers.root2());
        }

        @Test
        @DisplayName("Throws Exception when coefficient a equals zero")
        void shouldThrowException_whenAEqualsZero() {
            Assertions.assertThrows(ArithmeticException.class, () -> QuadraticSolver.getSolutions(
                    Fraction.getFraction(0, 1),
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(1, 1)
            ));
        }
    }
}
