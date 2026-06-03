public class QuadraticSolver {

    public record QuadraticRoots(String root1, String root2) {}

    public static QuadraticRoots getSolutions(double a, double b, double c) throws ArithmeticException {

        if(a == 0) {
            throw new ArithmeticException();
        }

        double discriminant = (b*b) - 4*a*c;

        if(discriminant < 0){
            String firstPart = formatAnswer((0 - b) / (2*a));

            double complexNumerator = Math.sqrt(0 - discriminant);
            String secondPart = "";
            try {
                if(complexNumerator / (2*a) != 1.0) {
                    secondPart = Fraction.getFraction(complexNumerator / (2*a)).toString();
                }
            } catch (ArithmeticException e) {
                secondPart = "sqrt(" + (0-discriminant) + ")/" + (2*a);
            }
            return new QuadraticRoots(firstPart + " + i" + secondPart, firstPart + " - i" + secondPart);
        } else {
            double ans1 = ((0 - b) + Math.sqrt(discriminant)) / (2*a);
            double ans2 = ((0 - b) - Math.sqrt(discriminant)) / (2*a);
            return new QuadraticRoots(formatAnswer(ans1), formatAnswer(ans2));
        }

    }

    private static String formatAnswer(double answer) {
        try {
            Fraction fraction = Fraction.getFraction(answer);
            if(fraction.getDenominator() == 1) {
                return String.valueOf(fraction.getNumerator());
            }
            return fraction.toString();
        } catch(ArithmeticException e) {
            return String.format("%.3f", answer);
        }
    }
}
