import java.util.*;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        int sizePrimes = 500;  // Размер для генерации простых чисел
        List<Integer> prime = primes(sizePrimes);  // Генерация простых чисел

        Scanner scanner = new Scanner(System.in);
        int bit = scanner.nextInt();  // Ввод размера числа в битах

        List<Integer> q;  // Вектор для хранения множителей
        int n;  // Сгенерированное число
        int k = 0;  // Счетчик отвергнутых чисел
        int probability;  // Вероятность простоты

        List<Integer> res = new ArrayList<>();  // Вектор для хранения результатов
        List<String> resVerTest = new ArrayList<>();  // Вектор для хранения результатов теста
        List<Integer> otvegnutie = new ArrayList<>();  // Вектор для хранения количества отвергнутых чисел

        // Генерация 10 различных простых чисел
        while (res.size() != 10) {
            // Построение числа и проведение теста
            Pair<Integer, List<Integer>> result = builderTest(prime, bit);
            n = result.getKey();
            q = result.getValue();
            probability = testMillera(n, 10, q);

            // Проверка на простоту
            if (probability == 1) {
                // Проверка на уникальность числа
                if (!res.contains(n)) {
                    res.add(n);  // Добавление числа в результаты

                    // Дополнительная проверка числа
                    probability = testMillera(n, 1, q);
                    if (probability == 1) {
                        resVerTest.add("+");  // Число прошло проверку
                    } else {
                        resVerTest.add("-");  // Число не прошло проверку
                    }

                    otvegnutie.add(k);  // Добавление количества отвергнутых чисел
                    k = 0;  // Сброс счетчика отвергнутых чисел
                }
            } else {
                k++;  // Увеличение счетчика отвергнутых чисел
            }
        }

        // Вывод результатов
        printResults(res, resVerTest, otvegnutie);
    }

    // Функция решета Эратосфена для нахождения всех простых чисел до n
    public static List<Integer> primes(int n) {
        boolean[] isPrime = new boolean[n + 1];  // Вектор для отметки простоты чисел
        Arrays.fill(isPrime, true);
        List<Integer> primes = new ArrayList<>();  // Вектор для хранения простых чисел

        // Реализация решета Эратосфена
        for (int p = 2; p * p <= n; ++p) {  // Для всех чисел от 2 до sqrt(n)
            if (isPrime[p]) {  // Если число p простое
                for (int i = p * p; i <= n; i += p) {  // Удаление кратных чисел p
                    isPrime[i] = false;
                }
            }
        }

        // Сбор всех простых чисел в вектор
        for (int p = 2; p <= n; ++p) {
            if (isPrime[p]) {
                primes.add(p);
            }
        }

        return primes;  // Возвращение вектора простых чисел
    }

    // Функция для построения числа и проведения теста
    public static Pair<Integer, List<Integer>> builderTest(List<Integer> prime, int bit) {
        int maxIndex = 0;  // Максимальный индекс в векторе простых чисел
        int maxPow = 1;  // Максимальная степень двойки

        // Нахождение максимального индекса, такого что prime[maxIndex] < 2^(bit - 1)
        while (maxIndex < prime.size() && prime.get(maxIndex) < Math.pow(2, bit - 1)) {
            maxIndex++;
        }

        // Нахождение максимальной степени двойки, такой что 2^maxPow < 2^(bit - 1)
        while (Math.pow(2, maxPow) < Math.pow(2, bit - 1)) {
            maxPow++;
        }

        int m = 1;  // Начальное значение m
        List<Integer> q = new ArrayList<>();  // Вектор для хранения множителей

        // Построение числа m
        Random random = new Random();
        while (true) {
            int num = rn(0, maxIndex);  // Случайный индекс простого числа
            int power = rn(1, maxPow);  // Случайная степень

            // Проверка на переполнение и добавление множителя
            if (Math.pow(prime.get(num), power) != 0) {
                if (m * Math.pow(prime.get(num), power) < Integer.MAX_VALUE) {
                    m *= Math.pow(prime.get(num), power);
                    q.add(prime.get(num));
                }
            }

            // Проверка на соответствие условиям
            if (m > Math.pow(2, bit - 2)) {
                if (m >= Math.pow(2, bit - 1)) {
                    m = 1;
                    q.clear();
                } else {
                    break;
                }
            }
        }

        int n = 2 * m + 1;  // Инициализация числа n

        return new Pair<>(n, q);  // Возвращение числа и множителей
    }

    // Тест Миллера для проверки числа на простоту
    public static int testMillera(int n, int t, List<Integer> q) {
        List<Integer> a = new ArrayList<>();  // Вектор для хранения случайных чисел

        // Генерация t случайных чисел
        Random random = new Random();
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
        boolean flag = true;
        int i = 0;
        for (int aj : a) {
            if (q.get(i) != 0 && powerMod(aj, (n - 1) / q.get(i), n) != 1) {
                flag = false;
                if (i < q.size() - 1) {
                    i++;
                }
            }
        }

        // Проверка результата
        if (flag) {
            return 0;
        }

        return 1;
    }

    // Функция для возведения числа a в степень b по модулю n
    public static int powerMod(int a, int b, int n) {
        long result = 1;  // Начальное значение результата
        long base = a;
        while (b > 0) {  // Пока степень больше 0
            if (b % 2 == 1) {  // Если степень нечетная
                result = (result * base) % n;  // Умножение результата на a по модулю n
            }
            base = (base * base) % n;  // Возведение a в квадрат по модулю n
            b /= 2;  // Деление степени на 2
        }
        return (int) result;  // Возвращение результата
    }

    // Функция для генерации случайного числа в диапазоне [a, b]
    public static int rn(int a, int b) {
        Random random = new Random();
        return random.nextInt(b - a + 1) + a;
    }

    // Функция для вывода результатов
    public static void printResults(List<Integer> res, List<String> resVerTest, List<Integer> otvegnutie) {
        System.out.println("Prime Numbers\tTest Results\tOccurrences");  // Заголовок таблицы
        System.out.println("----------------------------------------------");  // Разделитель

        // Вывод каждого результата
        for (int i = 0; i < res.size(); i++) {
            System.out.println(res.get(i) + "\t\t" + resVerTest.get(i) + "\t\t" + otvegnutie.get(i));
        }
    }
}

// Вспомогательный класс для работы с парами
class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
