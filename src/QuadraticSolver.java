public class QuadraticSolver {

    public record QuadraticRoots(String root1, String root2) {}

    public static QuadraticRoots getSolutions(double a, double b, double c) throws ArithmeticException {

        if(a == 0) {
            throw new ArithmeticException();
        }

        double discriminant = (b*b) - 4*a*c;

        if(discriminant < 0){
            double firstPart = (0 - b) / (2*a);
            double complexNumerator = Math.sqrt(0 - discriminant);
            String secondPart = "i" + String.format("%.3f", complexNumerator) + "/" + 2*a;
            return new QuadraticRoots(firstPart + " + " + secondPart, firstPart + " - " + secondPart);
        } else {
            double ans1 = ((0 - b) + Math.sqrt(discriminant)) / (2*a);
            double ans2 = ((0 - b) - Math.sqrt(discriminant)) / (2*a);
            return new QuadraticRoots(String.format("%.3f", ans1), String.format("%.3f", ans2));
        }

    }
}
