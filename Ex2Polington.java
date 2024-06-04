import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {
        int sizePrimes = 500; // Размер для генерации простых чисел
        List<Integer> prime = primes(sizePrimes); // Генерация простых чисел

        Scanner scanner = new Scanner(System.in);
        int bit = scanner.nextInt(); // Ввод размера числа

        List<Integer> q; // Вектор для хранения множителей
        int n; // Сгенерированное число
        int k = 0; // Счетчик отвергнутых чисел
        int probability; // Вероятность простоты

        List<Integer> res = new ArrayList<>(); // Вектор для хранения результатов
        List<String> resVerTest = new ArrayList<>(); // Вектор для хранения результатов теста
        List<Integer> otvegnutie = new ArrayList<>(); // Вектор для хранения количества отвергнутых чисел

        // Генерация 10 различных простых чисел
        while (res.size() != 10) {
            // Построение числа и проведение теста
            Tuple<Integer, List<Integer>> result = builderTest(prime, bit);
            n = result.first;
            q = result.second;
            probability = testPoklin(n, 10, q);

            // Проверка на простоту
            if (probability == 1) {
                // Проверка на уникальность числа
                if (!res.contains(n)) {
                    res.add(n); // Добавление числа в результаты

                    // Дополнительная проверка числа
                    probability = testPoklin(n, 1, q);
                    if (probability == 1) {
                        resVerTest.add("+"); // Число прошло проверку
                    } else {
                        resVerTest.add("-"); // Число не прошло проверку
                    }

                    otvegnutie.add(k); // Добавление количества отвергнутых чисел
                    k = 0; // Сброс счетчика отвергнутых чисел
                }
            } else {
                k++; // Увеличение счетчика отвергнутых чисел
            }
        }

        // Вывод результатов
        printResults(res, resVerTest, otvegnutie);
    }

    // Функция для нахождения всех простых чисел до n (решето Эратосфена)
    public static List<Integer> primes(int n) {
        boolean[] isPrime = new boolean[n + 1]; // Вектор для отметки простоты чисел
        Arrays.fill(isPrime, true);
        List<Integer> primes = new ArrayList<>(); // Вектор для хранения простых чисел

        // Реализация решета Эратосфена
        for (int p = 2; p * p <= n; p++) { // Для всех чисел от 2 до sqrt(n)
            if (isPrime[p]) { // Если число p простое
                for (int i = p * p; i <= n; i += p) // Удаление кратных чисел p
                    isPrime[i] = false;
            }
        }

        // Сбор всех простых чисел в вектор
        for (int p = 2; p <= n; p++) {
            if (isPrime[p])
                primes.add(p);
        }

        return primes; // Возвращение вектора простых чисел
    }

    // Функция для построения числа и проведения теста
    public static Tuple<Integer, List<Integer>> builderTest(List<Integer> prime, int bit) {
        int maxIndex = 0; // Максимальный индекс в векторе простых чисел
        int maxPow = 1; // Максимальная степень двойки

        // Нахождение максимального индекса, такого что prime[maxIndex] < 2^((bit / 2) + 1)
        while (prime.get(maxIndex) < Math.pow(2, (bit / 2) + 1) && maxIndex < prime.size()) {
            maxIndex++;
        }

        // Нахождение максимальной степени двойки, такой что 2^maxPow < 2^((bit / 2) + 1)
        while (Math.pow(2, maxPow) < Math.pow(2, (bit / 2) + 1)) {
            maxPow++;
        }

        int f = 1; // Начальное значение f
        List<Integer> q = new ArrayList<>(); // Вектор для хранения множителей

        // Построение числа f
        while (true) {
            int num = rn(0, maxIndex); // Случайный индекс простого числа
            int power = rn(1, maxPow); // Случайная степень

            // Проверка на переполнение и добавление множителя
            if (Math.pow(prime.get(num), power) != 0) {
                if (f * Math.pow(prime.get(num), power) < Integer.MAX_VALUE) {
                    f *= Math.pow(prime.get(num), power);
                    q.add(prime.get(num));
                }
            }

            // Проверка на соответствие условиям
            if (f > Math.pow(2, (bit / 2))) {
                if (f >= Math.pow(2, (bit / 2) + 1)) {
                    f = 1;
                    q.clear();
                } else {
                    break;
                }
            }
        }

        int R; // Случайное четное число R

        // Генерация случайного четного числа R
        do {
            R = rn((int) Math.pow(2, (bit / 2) - 1) + 1, (int) Math.pow(2, bit / 2));
        } while (R % 2 != 0);

        int n = R * f + 1; // Инициализация числа n
        return new Tuple<>(n, q); // Возвращение числа и множителей
    }

    // Тест Поклингтона для проверки числа на простоту
    public static int testPoklin(int n, int t, List<Integer> q) {
        List<Integer> a = new ArrayList<>(); // Вектор для хранения случайных чисел

        // Генерация t случайных чисел
        while (a.size() != t) {
            int aj = rn(2, n - 1);

            if (!a.contains(aj)) {
                a.add(aj);
            }
        }

        // Первоначальная проверка простоты
        for (int aj : a) {
            if (powerMod(aj, n - 1, n) != 1) {
                return 0;
            }
        }

        // Дополнительная проверка простоты
        for (int aj : a) {
            for (int qi : q) {
                if (qi != 0 && powerMod(aj, (n - 1) / qi, n) == 1) {
                    return 0;
                }
            }
        }

        return 1;
    }

    // Функция для возведения числа a в степень b по модулю n
    public static int powerMod(int a, int b, int n) {
        long result = 1; // Начальное значение результата
        long base = a;

        while (b > 0) { // Пока степень больше 0
            if (b % 2 == 1) { // Если степень нечетная
                result = (result * base) % n; // Умножение результата на a по модулю n
            }
            base = (base * base) % n; // Возведение a в квадрат по модулю n
            b /= 2; // Деление степени на 2
        }

        return (int) result; // Возвращение результата
    }

    // Функция для генерации случайного числа в диапазоне [a, b]
    public static int rn(int a, int b) {
        return ThreadLocalRandom.current().nextInt(a, b + 1); // Возвращение случайного числа
    }

    // Функция для вывода результатов
    public static void printResults(List<Integer> res, List<String> resVerTest, List<Integer> otvegnutie) {
        System.out.println("Prime Numbers\tTest Results\tOccurrences"); // Заголовок таблицы
        System.out.println("----------------------------------------------"); // Разделитель

        // Вывод каждого результата
        for (int i = 0; i < res.size(); i++) {
            System.out.println(res.get(i) + "\t\t" + resVerTest.get(i) + "\t\t" + otvegnutie.get(i));
        }
    }

    // Класс для хранения пары значений
    static class Tuple<X, Y> {
        public final X first;
        public final Y second;

        public Tuple(X first, Y second) {
            this.first = first;
            this.second = second;
        }
    }
}
