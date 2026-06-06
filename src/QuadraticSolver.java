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
                String root1 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " + i√"
                        + discriminant.abs() + "/" + 2*a.doubleValue();
                String root2 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " - i√"
                        + discriminant.abs() + "/" + 2*a.doubleValue();
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
                String root1 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " + √"
                        + discriminant + "/" + 2*a.doubleValue();
                String root2 = formatAnswer((b.negate().doubleValue()) / (2*a.doubleValue())) + " - √"
                        + discriminant + "/" + 2*a.doubleValue();
                roots = new QuadraticRoots(root1, root2);
            }
        }

        return roots;
    }

    private static String formatAnswer(double answer) {
        try {
            Fraction fraction = Fraction.getFraction(answer);
            return fraction.toString();
        } catch(ArithmeticException e) {
            return String.format("%.3f", answer);
        }
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
