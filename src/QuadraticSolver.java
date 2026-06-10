public class QuadraticSolver {

    public record QuadraticRoots(String root1, String root2) {}

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
