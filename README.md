# Quadratic Calculator

## 6/2/2026 Release
- User can input coefficients a, b, and c in a quadratic equation.
- Users can input integers and decimals.
- The two solutions are outputted to the user in decimal form.
- Complex solutions are outputted in the format a+/-ib, still using decimals.
- The user will get an error message if a non-number is entered.

## 6/12/2026 Release
- Users can now enter fractions as coefficients in addition to integers and decimals.
- Non-whole roots are displayed as fractions and irrational roots are displayed in exact form.
- Complex roots are displayed using fractions and radicals instead of decimals.
- Fraction roots and irrational roots will have exact or approximated decimal representations next to them.
- Answers with radicals in them are simplified.
- If a user doesn't put anything in one of the boxes, it will change to zero once "Calculate" is pressed.
- The user will get an error message if the A coefficient is zero.

## 7/15/2026 Release
- Added an interactive graph of the quadratic function.
- The graph automatically centers around the vertex and scales to fit the function.
- Users can zoom and pan the graph using the mouse.
- The graph displays the parabola's vertex and real x-intercepts.
- Added an equation history window that stores the 30 most recently solved equations.
- Equation history is stored in a local SQLite database and persists between application launches.
- Selecting an equation from the history reloads its coefficients, recalculates its solutions, and redraws its graph.
- Users can clear their equation history from within the application.
- Equations in the history are formatted using standard mathematical notation, omitting zero terms and simplifying coefficients of 1 and -1.
- Improved project structure by migrating the application to Gradle for dependency management.