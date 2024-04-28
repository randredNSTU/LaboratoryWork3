public class Main {

    // Метод для вычисления значения y на основе заданного графика
    public static double calculateY(double x) {
        // Проверяем специальные случаи для x = -9 и x = 9
        if (x == -9) {
            return 0; // Значение y в точке x = -9 равно 0
        } else if (x == 9) {
            return 3; // Значение y в точке x = 9 равно 3
        }

        if (x <= -9 || x >= 9) {
            return Double.NaN; // Неопределенное значение для x вне заданного интервала
        } else if (x < -6) {
            // Дуга от (-9,0) до (-6,-3)
            return -Math.sqrt(9 - Math.pow(x + 9, 2)); // умножение на -1 для инверсии y
        } else if (x < -3) {
            // Прямая линия от (-6,-3) до (-3,0)
            return x + 3;
        } else if (x <= 0) {
            // Дуга от (-3,0) до (0,3)
            return Math.sqrt(9 - Math.pow(x, 2));
        } else if (x <= 3) {
            // Прямая линия от (0,3) до (3,0)
            return -1 * (3.0 / 3.0) * x + 3; // уравнение прямой y = kx + b, k = -3/3, b = 3
        } else {
            // Прямая линия от (3,0) до (9,3)
            return (3.0 / 6.0) * (x - 3); // уравнение прямой y = kx + b, k = 3/6, b = 0
        }
    }

    public static void main(String[] args) {
        // Задаем интервал и шаг
        double startX = -9.0;
        double endX = 9.0;
        double step = 1;

        // Выводим заголовок таблицы
        System.out.println("X\t\tY");

        // Выводим значения функции для заданного интервала с заданным шагом
        for (double x = startX; x <= endX; x += step) {
            double y = calculateY(x);
            System.out.println(x + "\t\t" + y);
        }
    }
}
