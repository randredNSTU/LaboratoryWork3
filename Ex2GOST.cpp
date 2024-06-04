#include <iostream>
#include <vector>
#include <cmath>
#include <random>
#include <algorithm>

using namespace std;

// Функция решета Эратосфена для нахождения всех простых чисел до n
vector<int> primes(int n);

// Функция для генерации нового простого числа из старого по ГОСТ
int build_new_from_old(vector<int> prime, int bit);

// Функция для возведения числа a в степень b по модулю n
int power_mod(int a, int b, int n);

// Функция для генерации случайного целого числа в диапазоне [a, b]
int rn_int(int a, int b);

// Функция для генерации случайного дробного числа в диапазоне [a, b]
double rn_double(int a, int b);

// Функция для печати результата
void print_res(vector<int> res);

int main() {
    // Генерация всех простых чисел до 500
    vector<int> prime = primes(500);

    // Ввод требуемого размера числа в битах
    int bit;
    cin >> bit;

    // Вектор для хранения сгенерированных простых чисел
    vector<int> res;
    
    // Генерация 10 различных простых чисел заданной длины
    while (res.size() != 10) {
        // Генерация нового простого числа
        int p = build_new_from_old(prime, bit);

        // Проверка на уникальность сгенерированного числа
        if (find(res.begin(), res.end(), p) == res.end()) {
            res.push_back(p);
        }
    }

    // Печать результата
    print_res(res);
}

vector<int> primes(int n) {
    // Инициализация вектора, где is_prime[i] показывает, является ли число i простым
    vector<bool> is_prime(n + 1, true);
    // Вектор для хранения простых чисел
    vector<int> primes;

    // Реализация решета Эратосфена
    for (int p = 2; p * p <= n; ++p) {  // Для всех чисел от 2 до sqrt(n)
        if (is_prime[p]) {  // Если число p простое
            for (int i = p * p; i <= n; i += p)  // Удаление кратных чисел p
                is_prime[i] = false;
        }
    }

    // Сбор всех простых чисел в вектор
    for (int p = 2; p <= n; ++p) {
        if (is_prime[p])
            primes.push_back(p);
    }

    return primes;  // Возвращение вектора простых чисел
}

// Алгоритм перехода от меньшего простого числа к большему по ГОСТ
int build_new_from_old(vector<int> prime, int bit) {
    int q;  // Переменная для хранения выбранного простого числа
    int max_index = 0;  // Максимальный индекс в векторе простых чисел
    int p;  // Переменная для хранения нового простого числа

    // Нахождение максимального индекса, такого что prime[max_index] < 2^(bit / 2)
    for (; prime[max_index] < pow(2, bit / 2); max_index++);

    while (true) {
        // Выбор случайного простого числа q
        q = prime[rn_int(0, max_index)];
        // Проверка, что q находится в заданном диапазоне
        if (q > pow(2, (bit / 2) - 1) && q <= pow(2, bit / 2) - 1) break;
    }

    while (true) {
        // Генерация случайного числа от 0 до 1
        double zakaruchka = rn_double(0, 1);
        // Вычисление нового числа n на основе q и zakaruchka
        double n = (pow(2, bit - 1) / q) + (pow(2, bit - 1) * zakaruchka / q);

        // Если n нечетное, сделать его четным
        if ((int)n % 2 == 1) n++;

        // Перебор всех u, начиная с 0 с шагом 2
        for (int u = 0; true; u += 2) {
            // Вычисление кандидата в простые числа
            p = (n + u) * q + 1;
            // Если p превышает 2^bit, выйти из цикла
            if (p > pow(2, bit)) break;

            // Проверка условия теоремы Диемитко
            if (power_mod(2, p - 1, p) == 1 && power_mod(2, n + u, p) != 1) return p;
        }
    }
}

// Функция для возведения числа a в степень b по модулю n
int power_mod(int a, int b, int n) {
    int result = 1;  // Результат возведения в степень по модулю
    while (b > 0) {  // Пока степень b больше 0
        if (b % 2 == 1)  // Если b нечетное
            result = (result * a) % n;  // Умножить результат на a по модулю n
        a = (a * a) % n;  // Возвести a в квадрат по модулю n
        b /= 2;  // Разделить b на 2
    }
    return result;  // Возвращение результата
}

// Функция для генерации случайного целого числа в диапазоне [a, b]
int rn_int(int a, int b) {
    // Инициализация генератора случайных чисел
    static mt19937 mt_rand(random_device{}());
    return uniform_int_distribution<int>(a, b)(mt_rand);  // Возвращение случайного числа
}

// Функция для генерации случайного дробного числа в диапазоне [a, b]
double rn_double(int a, int b) {
    // Инициализация генератора случайных чисел
    static mt19937 mt_rand(random_device{}());
    return uniform_real_distribution<double>(a, b)(mt_rand);  // Возвращение случайного числа
}

// Функция для печати результата
void print_res(vector<int> res) {
    // Печать каждого элемента вектора
    for (int i = 0; i < res.size(); i++) {
        cout << i + 1 << "\t\t" << "|" << "\t\t" << res[i] << endl;
    }
}
