
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

    /**
     * Represents the two roots of a quadratic equation.
     *
     * @param root1 the first root of the equation
     * @param root2 the second root of the equation
     */
    public record QuadraticRoots(String root1, String root2) {}


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
     * Computes decimal approximations of the real roots of a quadratic equation
     * of the form ax² + bx + c = 0 using the quadratic formula.
     *
     * <p>The returned roots are formatted as decimal strings. Unlike
     * {@link #getSolutions(Fraction, Fraction, Fraction)}, this method does not
     * preserve exact fractional, radical, or complex forms.</p>
     *
     * <p>If the equation has no real roots (that is, the discriminant is
     * negative), both returned strings are empty.</p>
     *
     * <p>If the equation has a whole number root,
     * an empty string is returned in its place.</p>
     *
     * @param a the coefficient of x²; must not be zero
     * @param b the coefficient of x
     * @param c the constant term
     * @return a {@code QuadraticRoots} object containing decimal
     *         approximations of the real roots, or empty strings if the
     *         equation has no real roots or whole number roots.
     * @throws ArithmeticException if {@code a} is zero, since the equation is
     *         not quadratic
     */
    public static QuadraticRoots getDecimalApproximations(Fraction a, Fraction b, Fraction c) throws ArithmeticException {

        if(a.doubleValue() == 0.0) {
            throw new ArithmeticException();
        }

        QuadraticRoots roots;

        Fraction discriminant = b.multiplyBy(b).subtract(Fraction.getFraction(4.0).multiplyBy(a).multiplyBy(c));

        if(discriminant.doubleValue() < 0.0) {
            roots = new QuadraticRoots("", "");
        } else {
            double root1 = (b.negate().doubleValue() + Math.sqrt(discriminant.doubleValue())) / (2 * a.doubleValue());
            double root2 = (b.negate().doubleValue() - Math.sqrt(discriminant.doubleValue())) / (2 * a.doubleValue());

            roots = new QuadraticRoots(formatDecimalApproximation(root1), formatDecimalApproximation(root2));
        }

        return roots;
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

    private static String formatDecimalApproximation(double approx) {
        String formattedApprox = "";

        if(Math.floor(approx) != approx) {
            if(String.valueOf(approx).length() > String.format("%.4f", approx).length()) {
                formattedApprox = "; ≈" + String.format("%.4f", approx);
            } else {
                formattedApprox = "; " + approx;
            }
        }

        return formattedApprox;
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
