public class QuadraticSolver {

    public static String[] getSolutions(double a, double b, double c) throws ArithmeticException {

        if(a == 0) {
            throw new ArithmeticException();
        }

        String[] answers = new String[2];

        double discriminant = (b*b) - 4*a*c;

        if(discriminant < 0){
            double firstPart = (0 - b) / (2*a);
            double complexNumerator = Math.sqrt(0 - discriminant);
            String secondPart = "i" + String.format("%.3f", complexNumerator) + "/" + 2*a;
            answers[0] = firstPart + " + " + secondPart;
            answers[1] = firstPart + " - " + secondPart;
            return answers;
        } else {
            double ans1 = ((0 - b) + Math.sqrt(discriminant)) / (2*a);
            double ans2 = ((0 - b) - Math.sqrt(discriminant)) / (2*a);
            answers[0] = String.format("%.3f", ans1);
            answers[1] = String.format("%.3f", ans2);
            return answers;
        }

    }
}
