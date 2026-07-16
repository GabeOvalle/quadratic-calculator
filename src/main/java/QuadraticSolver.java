/**
 * Utility class for solving quadratic equations of the form
 * ax² + bx + c = 0.
 *
 * <p>Provides methods for obtaining exact solutions and decimal
 * approximations of the roots.</p>
 *
 * @author Gabriel Ovalle
 */
public class QuadraticSolver {

    private QuadraticSolver() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    /**
     * Represents the two roots of a quadratic equation.
     *
     * @param root1 the first root of the equation
     * @param root2 the second root of the equation
     */
    public record QuadraticRoots(String root1, String root2) {}

    /**
     * Represents the decimal representations of the two roots of a quadratic equation.
     *
     * @param root1 the first root of the equation
     * @param root2 the second root of the equation
     */
    public record DecimalRepresentations(Double root1, Double root2) {}

    /**
     * Computes the roots of a quadratic equation of the form
     * ax² + bx + c = 0 using the quadratic formula.
     *
     * <p>The returned roots are formatted as strings. Rational roots are
     * returned in simplified fractional form when possible. Irrational roots
     * are returned in simplified radical form. Complex roots are returned
     * using the imaginary unit {@code i}.</p>
     *
     * <p>Examples:</p>
     * <ul>
     *     <li>x² - 5x + 6 = 0 → 2, 3</li>
     *     <li>x² - 4x + 2 = 0 → 2 + √2, 2 - √2</li>
     *     <li>x² + 2x + 5 = 0 → -1 + 2i, -1 - 2i</li>
     * </ul>
     *
     * @param a the coefficient of x²; must not be zero
     * @param b the coefficient of x
     * @param c the constant term
     * @return a {@code QuadraticRoots} object containing the two roots of the
     *         equation as formatted strings
     * @throws ArithmeticException if {@code a} is zero, since the equation is
     *         not quadratic
     */
    public static QuadraticRoots getSolutions(Fraction a, Fraction b, Fraction c) throws ArithmeticException {

        if(a.doubleValue() == 0.0) {
            throw new ArithmeticException();
        }

        QuadraticRoots roots;

        Fraction discriminant = b.multiplyBy(b).subtract(Fraction.getFraction(4.0).multiplyBy(a).multiplyBy(c));

        if(discriminant.doubleValue() < 0.0) {
            if(isPerfectSquare(discriminant.abs()) && (Math.sqrt(discriminant.abs().doubleValue())) / (2*a.doubleValue()) == 1.0) {
                String root1 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " + i";
                String root2 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " - i";
                roots = new QuadraticRoots(root1, root2);
            } else if(isPerfectSquare(discriminant.abs())) {
                String root1 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " + i"
                        + formatAnswer((Math.sqrt(discriminant.abs().doubleValue())) / (2*a.doubleValue()));
                String root2 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " - i"
                        + formatAnswer((Math.sqrt(discriminant.abs().doubleValue())) / (2*a.doubleValue()));
                roots = new QuadraticRoots(root1, root2);
            } else {
                String root1 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " + i"
                        + simplifyRadicalFraction(discriminant.abs(), Fraction.getFraction(2, 1).multiplyBy(a));
                String root2 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " - i"
                        + simplifyRadicalFraction(discriminant.abs(), Fraction.getFraction(2, 1).multiplyBy(a));
                roots = new QuadraticRoots(root1, root2);
            }
        } else {
            if(isPerfectSquare(discriminant)) {
                Fraction root1 = b.negate().add(Fraction.getFraction((int)Math.sqrt(discriminant.getNumerator()),
                                (int)Math.sqrt(discriminant.getDenominator())))
                                .divideBy(Fraction.getFraction(2).multiplyBy(a));
                Fraction root2 = b.negate().subtract(Fraction.getFraction((int)Math.sqrt(discriminant.getNumerator()),
                                (int)Math.sqrt(discriminant.getDenominator())))
                        .divideBy(Fraction.getFraction(2).multiplyBy(a));
                roots = new QuadraticRoots(root1.toString(), root2.toString());
            } else {
                String root1 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " + "
                        + simplifyRadicalFraction(discriminant, Fraction.getFraction(2, 1).multiplyBy(a));
                String root2 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " - "
                        + simplifyRadicalFraction(discriminant, Fraction.getFraction(2, 1).multiplyBy(a));
                roots = new QuadraticRoots(root1, root2);
            }
        }

        return roots;
    }

    /**
     * Computes decimal representations of the real roots of a quadratic equation
     * of the form ax² + bx + c = 0 using the quadratic formula.
     *
     * <p>The returned roots are in decimal form. Unlike
     * {@link #getSolutions(Fraction, Fraction, Fraction)}, this method does not
     * preserve exact fractional, radical, or complex forms.</p>
     *
     * <p>If the equation has no real roots (that is, the discriminant is
     * negative), both returned doubles are null.</p>
     *
     * @param a the coefficient of x²; must not be zero
     * @param b the coefficient of x
     * @param c the constant term
     * @return a {@code DecimalRepresentations} object containing decimal
     *         representations of the real roots, or null doubles if the
     *         equation has no real roots.
     * @throws ArithmeticException if {@code a} is zero, since the equation is
     *         not quadratic
     */
    public static DecimalRepresentations getDecimalRepresentations(Fraction a, Fraction b, Fraction c) throws ArithmeticException {

        if(a.doubleValue() == 0.0) {
            throw new ArithmeticException();
        }

        DecimalRepresentations roots;

        Fraction discriminant = b.multiplyBy(b).subtract(Fraction.getFraction(4.0).multiplyBy(a).multiplyBy(c));

        if(discriminant.doubleValue() < 0.0) {
            roots = new DecimalRepresentations(null, null);
        } else {
            double root1 = (b.negate().doubleValue() + Math.sqrt(discriminant.doubleValue())) / (2 * a.doubleValue());
            double root2 = (b.negate().doubleValue() - Math.sqrt(discriminant.doubleValue())) / (2 * a.doubleValue());
            roots = new DecimalRepresentations(root1, root2);
        }

        return roots;
    }

    /**
     * Creates a properly formatted quadratic equation string from the given
     * coefficients.
     *
     * <p>The returned string is formatted in the form
     * {@code ax² + bx + c}, omitting any terms whose coefficients are zero.
     * Coefficients of {@code 1} and {@code -1} are simplified (for example,
     * {@code x²} instead of {@code 1x²} and {@code -x} instead of
     * {@code -1x}). If all coefficients are zero, the method returns
     * {@code "0"}.</p>
     *
     * @param a the coefficient of the {@code x²} term
     * @param b the coefficient of the {@code x} term
     * @param c the constant term
     * @return a formatted string representing the quadratic equation
     */
    public static String formatEquation(String a, String b, String c) {
        StringBuilder equation = new StringBuilder();

        if(a.contains(".")) {
            double aValue = Double.parseDouble(a);
            if (aValue != 0) {
                if (aValue == 1) {
                    equation.append("x²");
                } else if (aValue == -1) {
                    equation.append("-x²");
                } else {
                    equation.append(a).append("x²");
                }
            }
        } else {
            Fraction aValue = Fraction.getFraction(a);
            if (!aValue.equals(Fraction.ZERO)) {
                if (aValue.equals(Fraction.ONE)) {
                    equation.append("x²");
                } else if (aValue.equals(Fraction.ONE.negate())) {
                    equation.append("-x²");
                } else {
                    equation.append(a).append("x²");
                }
            }
        }

        if(b.contains(".")) {
            double bValue = Double.parseDouble(b);
            if (bValue != 0) {
                if (!equation.isEmpty()) {
                    equation.append(bValue > 0 ? " + " : " - ");
                } else if (bValue < 0) {
                    equation.append("-");
                }

                double absB = Math.abs(bValue);

                if (absB == 1) {
                    equation.append("x");
                } else {
                    equation.append(absB).append("x");
                }
            }
        } else {
            Fraction bValue = Fraction.getFraction(b);
            if (!bValue.equals(Fraction.ZERO)) {
                if (!equation.isEmpty()) {
                    equation.append(bValue.compareTo(Fraction.ZERO) > 0 ? " + " : " - ");
                } else if (bValue.compareTo(Fraction.ZERO) < 0) {
                    equation.append("-");
                }

                Fraction absB = bValue.abs();

                if (absB.equals(Fraction.ONE)) {
                    equation.append("x");
                } else {
                    equation.append(absB).append("x");
                }
            }
        }

        if(c.contains(".")) {
            double cValue = Double.parseDouble(c);
            if (cValue != 0) {
                if (!equation.isEmpty()) {
                    equation.append(cValue > 0 ? " + " : " - ");
                } else if (cValue < 0) {
                    equation.append("-");
                }

                equation.append(Math.abs(cValue));
            }
        } else {
            Fraction cValue = Fraction.getFraction(c);
            if (!cValue.equals(Fraction.ZERO)) {
                if (!equation.isEmpty()) {
                    equation.append(cValue.compareTo(Fraction.ZERO) > 0 ? " + " : " - ");
                } else if (cValue.compareTo(Fraction.ZERO) < 0) {
                    equation.append("-");
                }

                equation.append(cValue.abs());
            }
        }

        if (equation.isEmpty()) {
            return "0";
        }

        return equation.toString();
    }

    private static String formatAnswer(double answer) {
        if(answer == 0.0) {
            return "";
        }

        try {
            Fraction fraction = Fraction.getFraction(answer);
            return fraction.toString();
        } catch(ArithmeticException e) {
            return String.format("%.3f", answer);
        }
    }


    private static String simplifyRadicalFraction(Fraction radicand, Fraction denominator) {

        if(radicand.getDenominator() > 1) {
            String firstPart = denominator.getDenominator() == 1 ? "" : String.valueOf(denominator.getDenominator());
            String secondPart = denominator.getNumerator() == 1 ? "" : "/" + denominator.getNumerator();
            return firstPart + "√(" + radicand + ")" + secondPart;
        }

        int largestSquareFactor = 1;

        for (int i = 1; i * i <= radicand.getNumerator(); i++) {
            int square = i * i;

            if (radicand.getNumerator() % square == 0) {
                largestSquareFactor = square;
            }
        }

        int outside = (int) Math.sqrt(largestSquareFactor);
        int inside = radicand.getNumerator() / largestSquareFactor;

        Fraction coefficient =
                Fraction.getFraction(outside)
                        .divideBy(denominator)
                        .reduce();

        String firstPart = coefficient.getNumerator() == 1 ? "" : String.valueOf(coefficient.getNumerator());
        String secondPart = coefficient.getDenominator() == 1 ? "" : "/" + coefficient.getDenominator();

        String radical =
                (inside == 1)
                        ? ""
                        : "√(" + inside + ")";

        if (inside == 1) {
            return coefficient.toString();
        }

        if (coefficient.equals(Fraction.getFraction(1))) {
            return radical;
        }

        if (coefficient.equals(Fraction.getFraction(-1))) {
            return "-" + radical;
        }

        return firstPart + radical + secondPart;
    }

    private static boolean isPerfectSquare(Fraction fraction) {
        if(fraction.doubleValue() < 0.0) {
            return false;
        }

        int numerator = (int) Math.sqrt(fraction.getNumerator());
        int denominator = (int) Math.sqrt(fraction.getDenominator());

        return (numerator*numerator == fraction.getNumerator())
                && (denominator*denominator == fraction.getDenominator());
    }
}
