import java.util.ArrayList;
import java.util.List;

public class Main {

    // Структура для хранения коэффициентов линейной аппроксимации
    static class ApproximationResult {
        double a; // Коэффициент a
        double b; // Коэффициент b

        public ApproximationResult(double a, double b) {
            this.a = a;
            this.b = b;
        }
    }

    // Структура для хранения результатов корреляции
    static class CorrelationResult {
        double correlation; // Коэффициент корреляции
        double t_value; // Значение t-критерия

        public CorrelationResult(double correlation, double t_value) {
            this.correlation = correlation;
            this.t_value = t_value;
        }
    }

    // Функция для линейной аппроксимации данных
    static ApproximationResult aproks(List<Double> x, List<Double> y) {
        // Суммирование значений x и y
        double x_sum = x.stream().mapToDouble(Double::doubleValue).sum();
        double y_sum = y.stream().mapToDouble(Double::doubleValue).sum();

        // Суммирование квадратов значений x и произведений значений x и y
        double x2_sum = x.stream().mapToDouble(a -> a * a).sum();
        double xy_sum = 0.0;
        for (int i = 0; i < x.size(); i++) {
            xy_sum += x.get(i) * y.get(i);
        }

        // Количество измерений
        int n = x.size();

        // Вычисление коэффициентов линейной аппроксимации
        double a = (n * xy_sum - x_sum * y_sum) / (n * x2_sum - x_sum * x_sum);
        double b = (y_sum - a * x_sum) / n;

        return new ApproximationResult(a, b);
    }

    // Функция для вычисления среднего значения
    static double mean(List<Double> vec) {
        return vec.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    }

    // Функция для вычисления коэффициентов корреляции
    static CorrelationResult korrel(List<Double> x, List<Double> y) {
        // Вычисление средних значений
        double x_mean = mean(x);
        double y_mean = mean(y);

        // Инициализация переменных для вычисления сумм
        double sum_xy = 0.0;
        double sum_x2 = 0.0;
        double sum_y2 = 0.0;
        int n = x.size();

        // Вычисление сумм для формулы коэффициента корреляции
        for (int i = 0; i < n; ++i) {
            sum_xy += (x.get(i) - x_mean) * (y.get(i) - y_mean);
            sum_x2 += (x.get(i) - x_mean) * (x.get(i) - x_mean);
            sum_y2 += (y.get(i) - y_mean) * (y.get(i) - y_mean);
        }

        // Вычисление коэффициента корреляции и t-критерия
        double r = sum_xy / (Math.sqrt(sum_x2) * Math.sqrt(sum_y2));
        double t = r * Math.sqrt(n - 2) / Math.sqrt(1 - r * r);

        return new CorrelationResult(r, t);
    }

    // Функция моделирования процесса остывания кофе
    static List<Double> coffee(double CoffeeTemperature, double RoomTemperature, double CoolingFactor, int Time) {
        // Вектор для хранения температур кофе в разные моменты времени
        List<Double> temperatures = new ArrayList<>();

        // Моделирование процесса остывания
        for (int t = 0; t <= Time; t++) {
            double temperature = RoomTemperature + (CoffeeTemperature - RoomTemperature) * Math.exp(-CoolingFactor * t);
            temperatures.add(temperature);
        }

        return temperatures;
    }

    // Функция для печати результатов линейной аппроксимации
    static void printApproximationResult(ApproximationResult result) {
        System.out.println("Approximation Result:");
        System.out.println("a: " + result.a + ", b = " + result.b);
    }

    // Функция для печати результатов корреляции
    static void printCorrelationResult(CorrelationResult result) {
        System.out.println("Correlation Result:");
        System.out.println("Correlation: " + result.correlation);
        System.out.println("T-value: " + result.t_value);
    }

    // Функция для печати данных о температуре кофе в конкретный момент времени
    static void printCoffee(List<Double> temperatures, List<Double> times) {
        for (int i = 0; i < temperatures.size(); i++) {
            System.out.println("Time - " + times.get(i) + ":  " + temperatures.get(i) + " C");
        }
    }

    public static void main(String[] args) {
        // Параметры моделирования процесса остывания кофе
        double CoffeeTemperature = 90; // Начальная температура кофе
        double RoomTemperature = 26; // Температура комнаты
        double CoolingFactor = 0.01; // Коэффициент остывания
        int Time = 60; // Временной предел в минутах

        // Моделирование процесса остывания кофе
        List<Double> temperatures = coffee(CoffeeTemperature, RoomTemperature, CoolingFactor, Time);

        // Создание временных меток для измерений
        List<Double> times = new ArrayList<>();
        for (int i = 0; i <= Time; i++) {
            times.add((double) i);
        }

        // Вычисление линейной аппроксимации и корреляции
        ApproximationResult approx_result = aproks(times, temperatures);
        CorrelationResult corr_result = korrel(times, temperatures);

        // Печать результатов
        printApproximationResult(approx_result);
        printCorrelationResult(corr_result);
        printCoffee(temperatures, times);
    }
}
