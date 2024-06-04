import java.util.*;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        // Генерация всех простых чисел до 500
        List<Integer> prime = primes(500);

        // Ввод требуемого размера числа в битах
        Scanner scanner = new Scanner(System.in);
        int bit = scanner.nextInt();

        // Вектор для хранения сгенерированных простых чисел
        List<Integer> res = new ArrayList<>();

        // Генерация 10 различных простых чисел заданной длины
        while (res.size() != 10) {
            // Генерация нового простого числа
            int p = buildNewFromOld(prime, bit);

            // Проверка на уникальность сгенерированного числа
            if (!res.contains(p)) {
                res.add(p);
            }
        }

        // Печать результата
        printRes(res);
    }

    // Функция решета Эратосфена для нахождения всех простых чисел до n
    public static List<Integer> primes(int n) {
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        List<Integer> primes = new ArrayList<>();

        for (int p = 2; p * p <= n; ++p) {
            if (isPrime[p]) {
                for (int i = p * p; i <= n; i += p) {
                    isPrime[i] = false;
                }
            }
        }

        for (int p = 2; p <= n; ++p) {
            if (isPrime[p]) {
                primes.add(p);
            }
        }

        return primes;
    }

    // Алгоритм перехода от меньшего простого числа к большему по ГОСТ
    public static int buildNewFromOld(List<Integer> prime, int bit) {
        int q;
        int maxIndex = 0;
        int p;

        // Нахождение максимального индекса, такого что prime[maxIndex] < 2^(bit / 2)
        while (prime.get(maxIndex) < Math.pow(2, bit / 2)) {
            maxIndex++;
        }

        Random random = new Random();

        while (true) {
            // Выбор случайного простого числа q
            q = prime.get(rnInt(0, maxIndex));
            if (q > Math.pow(2, (bit / 2) - 1) && q <= Math.pow(2, bit / 2) - 1) break;
        }

        while (true) {
            // Генерация случайного числа от 0 до 1
            double zakaruchka = rnDouble(0, 1);
            double n = (Math.pow(2, bit - 1) / q) + (Math.pow(2, bit - 1) * zakaruchka / q);

            // Если n нечетное, сделать его четным
            if ((int) n % 2 == 1) n++;

            // Перебор всех u, начиная с 0 с шагом 2
            for (int u = 0; true; u += 2) {
                // Вычисление кандидата в простые числа
                p = (int) ((n + u) * q + 1);
                if (p > Math.pow(2, bit)) break;

                // Проверка условия теоремы Диемитко
                if (powerMod(2, p - 1, p) == 1 && powerMod(2, (int) n + u, p) != 1) return p;
            }
        }
    }

    // Функция для возведения числа a в степень b по модулю n
    public static int powerMod(int a, int b, int n) {
        int result = 1;
        while (b > 0) {
            if (b % 2 == 1) result = (result * a) % n;
            a = (a * a) % n;
            b /= 2;
        }
        return result;
    }

    // Функция для генерации случайного целого числа в диапазоне [a, b]
    public static int rnInt(int a, int b) {
        Random random = new Random();
        return random.nextInt(b - a + 1) + a;
    }

    // Функция для генерации случайного дробного числа в диапазоне [a, b]
    public static double rnDouble(int a, int b) {
        Random random = new Random();
        return a + (b - a) * random.nextDouble();
    }

    // Функция для печати результата
    public static void printRes(List<Integer> res) {
        for (int i = 0; i < res.size(); i++) {
            System.out.println((i + 1) + "\t\t|\t\t" + res.get(i));
        }
    }
}
