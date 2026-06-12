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

            QuadraticSolver.QuadraticRoots answers4 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(0, 1),
                    Fraction.getFraction(-9, 1)
            );
            Assertions.assertEquals("3", answers4.root1());
            Assertions.assertEquals("-3", answers4.root2());

            QuadraticSolver.QuadraticRoots answers5 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(2, 1),
                    Fraction.getFraction(0, 1)
            );
            Assertions.assertEquals("0", answers5.root1());
            Assertions.assertEquals("-2", answers5.root2());
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
            Assertions.assertEquals("-1/2 + i3√(3)/2", answers2.root1());
            Assertions.assertEquals("-1/2 - i3√(3)/2", answers2.root2());
        }

        @Test
        @DisplayName("Correctly solves equations with irrational solutions")
        void shouldCalculateCorrectSolutions_whenEquationHasIrrationalSolutions() {
            QuadraticSolver.QuadraticRoots answers1 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(6, 1),
                    Fraction.getFraction(7, 1)
            );
            Assertions.assertEquals("-3 + √(2)", answers1.root1());
            Assertions.assertEquals("-3 - √(2)", answers1.root2());

            QuadraticSolver.QuadraticRoots answers2 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(2, 18),
                    Fraction.getFraction(5, 2),
                    Fraction.getFraction(2, 1)
            );
            Assertions.assertEquals("-45/4 + 9√(193/36)/2", answers2.root1());
            Assertions.assertEquals("-45/4 - 9√(193/36)/2", answers2.root2());
        }

        @Test
        @DisplayName("Throws Exception when coefficient A equals zero")
        void shouldThrowException_whenAEqualsZero() {
            Assertions.assertThrows(ArithmeticException.class, () -> QuadraticSolver.getSolutions(
                    Fraction.getFraction(0, 1),
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(1, 1)
            ));
        }
    }

    @Nested
    @DisplayName("getDecimalApproximations Tests")
    class getDecimalApproximationsTests {
        @Test
        @DisplayName("Correctly approximates rational solutions")
        void shouldCalculateCorrectApproximations_whenEquationHasRationalSolutions() {
            QuadraticSolver.DecimalApproximations answers = QuadraticSolver.getDecimalApproximations(
                    Fraction.getFraction(6, 1),
                    Fraction.getFraction(-13,1),
                    Fraction.getFraction(6, 1)
            );
            Assertions.assertEquals(1.5, answers.root1(), 0.0001);
            Assertions.assertEquals(0.66666666, answers.root2(), 0.0001);
        }

        @Test
        @DisplayName("Correctly approximates irrational solutions")
        void shouldCalculateCorrectApproximations_whenEquationHasIrrationalSolutions() {
            QuadraticSolver.DecimalApproximations answers = QuadraticSolver.getDecimalApproximations(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(6, 1),
                    Fraction.getFraction(7, 1)
            );
            Assertions.assertEquals(-1.5858, answers.root1(),0.0001);
            Assertions.assertEquals(-4.4142, answers.root2(), 0.0001);
        }

        @Test
        @DisplayName("Returns empty strings if solutions are complex")
        void shouldReturnEmptyString_whenEquationHasComplexRoots() {
            QuadraticSolver.DecimalApproximations answers2 = QuadraticSolver.getDecimalApproximations(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(4, 1),
                    Fraction.getFraction(5, 1)
            );
            Assertions.assertNull(answers2.root1());
            Assertions.assertNull(answers2.root2());
        }
    }
}
