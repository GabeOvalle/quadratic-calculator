import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            assertEquals("5", answers1.root1());
            assertEquals("5", answers1.root2());

            QuadraticSolver.QuadraticRoots answers2 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(6, 1),
                    Fraction.getFraction(-13,1),
                    Fraction.getFraction(6, 1)
            );
            assertEquals("3/2", answers2.root1());
            assertEquals("2/3", answers2.root2());

            QuadraticSolver.QuadraticRoots answers3 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(9, 4),
                    Fraction.getFraction(1, 2)
            );
            assertEquals("-1/4", answers3.root1());
            assertEquals("-2", answers3.root2());

            QuadraticSolver.QuadraticRoots answers4 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(0, 1),
                    Fraction.getFraction(-9, 1)
            );
            assertEquals("3", answers4.root1());
            assertEquals("-3", answers4.root2());

            QuadraticSolver.QuadraticRoots answers5 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(2, 1),
                    Fraction.getFraction(0, 1)
            );
            assertEquals("0", answers5.root1());
            assertEquals("-2", answers5.root2());
        }

        @Test
        @DisplayName("Correctly solves equations with complex solutions")
        void shouldCalculateCorrectSolutions_whenEquationHasComplexSolutions() {
            QuadraticSolver.QuadraticRoots answers1 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(4, 1),
                    Fraction.getFraction(5, 1)
            );
            assertEquals("-2 + i", answers1.root1());
            assertEquals("-2 - i", answers1.root2());

            QuadraticSolver.QuadraticRoots answers2 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(7, 1)
            );
            assertEquals("-1/2 + i3√(3)/2", answers2.root1());
            assertEquals("-1/2 - i3√(3)/2", answers2.root2());
        }

        @Test
        @DisplayName("Correctly solves equations with irrational solutions")
        void shouldCalculateCorrectSolutions_whenEquationHasIrrationalSolutions() {
            QuadraticSolver.QuadraticRoots answers1 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(6, 1),
                    Fraction.getFraction(7, 1)
            );
            assertEquals("-3 + √(2)", answers1.root1());
            assertEquals("-3 - √(2)", answers1.root2());

            QuadraticSolver.QuadraticRoots answers2 = QuadraticSolver.getSolutions(
                    Fraction.getFraction(2, 18),
                    Fraction.getFraction(5, 2),
                    Fraction.getFraction(2, 1)
            );
            assertEquals("-45/4 + 9√(193/36)/2", answers2.root1());
            assertEquals("-45/4 - 9√(193/36)/2", answers2.root2());
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
    @DisplayName("getDecimalRepresentations Tests")
    class getDecimalApproximationsTests {
        @Test
        @DisplayName("Correctly approximates rational solutions")
        void shouldCalculateCorrectApproximations_whenEquationHasRationalSolutions() {
            QuadraticSolver.DecimalRepresentations answers = QuadraticSolver.getDecimalRepresentations(
                    Fraction.getFraction(6, 1),
                    Fraction.getFraction(-13,1),
                    Fraction.getFraction(6, 1)
            );
            assertEquals(1.5, answers.root1(), 0.0001);
            assertEquals(0.66666666, answers.root2(), 0.0001);
        }

        @Test
        @DisplayName("Correctly approximates irrational solutions")
        void shouldCalculateCorrectApproximations_whenEquationHasIrrationalSolutions() {
            QuadraticSolver.DecimalRepresentations answers = QuadraticSolver.getDecimalRepresentations(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(6, 1),
                    Fraction.getFraction(7, 1)
            );
            assertEquals(-1.5858, answers.root1(),0.0001);
            assertEquals(-4.4142, answers.root2(), 0.0001);
        }

        @Test
        @DisplayName("Returns empty strings if solutions are complex")
        void shouldReturnEmptyString_whenEquationHasComplexRoots() {
            QuadraticSolver.DecimalRepresentations answers2 = QuadraticSolver.getDecimalRepresentations(
                    Fraction.getFraction(1, 1),
                    Fraction.getFraction(4, 1),
                    Fraction.getFraction(5, 1)
            );
            Assertions.assertNull(answers2.root1());
            Assertions.assertNull(answers2.root2());
        }
    }

    @Nested
    @DisplayName("formatEquation tests")
    class formatEquationTests {
        @Test
        @DisplayName("Correctly formats equation with all positive coefficients")
        void testFormatEquation_AllPositive() {
            String result = QuadraticSolver.formatEquation(
                    "1",
                    "2",
                    "3");

            assertEquals("x² + 2x + 3", result);
        }

        @Test
        @DisplayName("Correctly formats equation with negative B and C")
        void testFormatEquation_NegativeBAndC() {
            String result = QuadraticSolver.formatEquation(
                    "1",
                    "-2",
                    "-3");

            assertEquals("x² - 2x - 3", result);
        }

        @Test
        @DisplayName("Correctly formats equation with negative A")
        void testFormatEquation_NegativeA() {
            String result = QuadraticSolver.formatEquation(
                    "-1",
                    "4",
                    "5");

            assertEquals("-x² + 4x + 5", result);
        }

        @Test
        @DisplayName("Correctly formats equation when B is zero")
        void testFormatEquation_ZeroB() {
            String result = QuadraticSolver.formatEquation(
                    "2",
                    "0",
                    "5");

            assertEquals("2x² + 5", result);
        }

        @Test
        @DisplayName("Correctly formats equation when C is zero")
        void testFormatEquation_ZeroC() {
            String result = QuadraticSolver.formatEquation(
                    "3",
                    "-4",
                    "0");

            assertEquals("3x² - 4x", result);
        }

        @Test
        @DisplayName("Correctly formats equation with 1 and -1 coefficients")
        void testFormatEquation_OneAndNegativeOneCoefficients() {
            String result = QuadraticSolver.formatEquation(
                    "1",
                    "-1",
                    "1");

            assertEquals("x² - x + 1", result);
        }

        @Test
        @DisplayName("Correctly formats equation with fraction coefficients")
        void testFormatEquation_FractionCoefficients() {
            String result = QuadraticSolver.formatEquation(
                    "3/2",
                    "-1/4",
                    "5");

            assertEquals("3/2x² - 1/4x + 5", result);
        }

        @Test
        @DisplayName("Correctly formats equation with decimal coefficients")
        void testFormatEquation_DecimalCoefficients() {
            String result = QuadraticSolver.formatEquation(
                    "1/5",
                    "-0.25",
                    "0.5");

            assertEquals("1/5x² - 0.25x + 0.5", result);
        }
    }

    @Nested
    @DisplayName("factoredForm Tests")
    class factoredFormTests {
        @Test
        @DisplayName("Distinct integer roots")
        void testDistinctIntegerRoots() {
            assertEquals(
                    "(x - 3)(x - 2)",
                    QuadraticSolver.factoredForm(
                            Fraction.ONE,
                            Fraction.getFraction(-5),
                            Fraction.getFraction(6))
            );
        }

        @Test
        @DisplayName("Repeated integer roots")
        void testRepeatedRoot() {
            assertEquals(
                    "(x - 2)(x - 2)",
                    QuadraticSolver.factoredForm(
                            Fraction.ONE,
                            Fraction.getFraction(-4),
                            Fraction.getFraction(4))
            );
        }

        @Test
        @DisplayName("One zero root")
        void testOneRootZero() {
            assertEquals(
                    "x(x - 2)",
                    QuadraticSolver.factoredForm(
                            Fraction.ONE,
                            Fraction.getFraction(-2),
                            Fraction.ZERO)
            );
        }

        @Test
        @DisplayName("Two zero roots")
        void testBothRootsZero() {
            assertEquals(
                    "x²",
                    QuadraticSolver.factoredForm(
                            Fraction.ONE,
                            Fraction.ZERO,
                            Fraction.ZERO)
            );
        }

        @Test
        @DisplayName("Two fraction roots")
        void testFractionRoots() {
            assertEquals(
                    "(x - 1)(6x - 1)",
                    QuadraticSolver.factoredForm(
                            Fraction.getFraction(6),
                            Fraction.getFraction(-7),
                            Fraction.ONE)
            );
        }

        @Test
        @DisplayName("Negative leading coefficient")
        void testNegativeLeadingCoefficient() {
            assertEquals(
                    "-(x - 2)(x - 3)",
                    QuadraticSolver.factoredForm(
                            Fraction.getFraction(-1),
                            Fraction.getFraction(5),
                            Fraction.getFraction(-6))
            );
        }

        @Test
        @DisplayName("Fraction gcf and negative roots")
        void testFractionGCFAndNegativeRoots() {
            assertEquals(
                    "1/2(x + 1)(x + 4)",
                    QuadraticSolver.factoredForm(
                            Fraction.getFraction("1/2"),
                            Fraction.getFraction("5/2"),
                            Fraction.getFraction(2))
            );
        }

        @Test
        @DisplayName("Complex roots")
        void testComplexRoots() {
            assertEquals(
                    "",
                    QuadraticSolver.factoredForm(
                            Fraction.ONE,
                            Fraction.ZERO,
                            Fraction.ONE)
            );
        }

        @Test
        @DisplayName("Irrational roots")
        void testIrrationalRoots() {
            assertEquals(
                    "",
                    QuadraticSolver.factoredForm(
                            Fraction.ONE,
                            Fraction.ZERO,
                            Fraction.getFraction(-2))
            );
        }
    }

    @Nested
    @DisplayName("getVertex Tests")
    class getVertexTests {
        @Test
        @DisplayName("Vertex with integer coordinates")
        void testVertex_IntegerCoordinates() {
            QuadraticSolver.Vertex vertex = QuadraticSolver.getVertex(
                    Fraction.ONE,
                    Fraction.getFraction(-4),
                    Fraction.getFraction(3));

            assertEquals(Fraction.getFraction(2), vertex.x());
            assertEquals(Fraction.getFraction(-1), vertex.y());
        }

        @Test
        @DisplayName("Vertex with fraction coordinates")
        void testVertex_FractionCoordinates() {
            QuadraticSolver.Vertex vertex = QuadraticSolver.getVertex(
                    Fraction.getFraction(2),
                    Fraction.getFraction(3),
                    Fraction.getFraction(-5));

            assertEquals(Fraction.getFraction(-3, 4), vertex.x());
            assertEquals(Fraction.getFraction(-49, 8), vertex.y());
        }

        @Test
        @DisplayName("Negative A coefficient")
        void testVertex_NegativeLeadingCoefficient() {
            QuadraticSolver.Vertex vertex = QuadraticSolver.getVertex(
                    Fraction.getFraction(-1),
                    Fraction.getFraction(2),
                    Fraction.getFraction(3));

            assertEquals(Fraction.ONE, vertex.x());
            assertEquals(Fraction.getFraction(4), vertex.y());
        }

        @Test
        @DisplayName("Vertex on Y Axis")
        void testVertex_OnYAxis() {
            QuadraticSolver.Vertex vertex = QuadraticSolver.getVertex(
                    Fraction.ONE,
                    Fraction.ZERO,
                    Fraction.getFraction(-5));

            assertEquals(Fraction.ZERO, vertex.x());
            assertEquals(Fraction.getFraction(-5), vertex.y());
        }
    }

    @Nested
    @DisplayName("getAxisOfSymmetry Tests")
    class getAxisOfSymmetryTests {
        @Test
        @DisplayName("Integer axis of symmetry")
        void testAxisOfSymmetry_Integer() {
            Fraction axis = QuadraticSolver.getAxisOfSymmetry(
                    Fraction.ONE,
                    Fraction.getFraction(-4));

            assertEquals(Fraction.getFraction(2), axis);
        }

        @Test
        @DisplayName("Fraction axis of symmetry")
        void testAxisOfSymmetry_Fraction() {
            Fraction axis = QuadraticSolver.getAxisOfSymmetry(
                    Fraction.getFraction(2),
                    Fraction.getFraction(3));

            assertEquals(Fraction.getFraction(-3, 4), axis);
        }

        @Test
        @DisplayName("Negative A coefficient")
        void testAxisOfSymmetry_NegativeLeadingCoefficient() {
            Fraction axis = QuadraticSolver.getAxisOfSymmetry(
                    Fraction.getFraction(-1),
                    Fraction.getFraction(2));

            assertEquals(Fraction.ONE, axis);
        }

        @Test
        @DisplayName("Axis matches vertex x-value")
        void testAxisMatchesVertexX() {
            Fraction a = Fraction.getFraction(5);
            Fraction b = Fraction.getFraction(-10);
            Fraction c = Fraction.getFraction(8);

            Fraction axis = QuadraticSolver.getAxisOfSymmetry(a, b);
            QuadraticSolver.Vertex vertex = QuadraticSolver.getVertex(a, b, c);

            assertEquals(axis, vertex.x());
        }
    }
}
