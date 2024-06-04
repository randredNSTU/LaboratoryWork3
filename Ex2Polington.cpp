#include <iostream>
#include <vector>
#include <tuple>
#include <cmath>
#include <random>
#include <limits>
#include <algorithm>

using namespace std;

// Функция для нахождения всех простых чисел до n (решето Эратосфена)
vector<int> primes(int n);

// Функция для построения числа и проведения теста
pair<int, vector<int>> builder_test(vector<int> prime, int bit);

// Тест Поклингтона для проверки числа на простоту
int test_poklin(int n, int t, vector<int> q);

// Функция для возведения числа a в степень b по модулю n
int power_mod(int a, int b, int n);

// Функция для генерации случайного числа в диапазоне [a, b]
int rn(int a, int b);

// Функция для вывода результатов
void print_results(const vector<int>& res, const vector<string>& res_ver_test, const vector<int>& otvegnutie);

int main() {
    int size_primes = 500;  // Размер для генерации простых чисел
    vector<int> prime = primes(size_primes);  // Генерация простых чисел

    int bit = 0;  // Размер числа в битах
    cin >> bit;  // Ввод размера числа

    vector<int> q;  // Вектор для хранения множителей
    int n;  // Сгенерированное число
    int k = 0;  // Счетчик отвергнутых чисел
    int probability;  // Вероятность простоты

    vector<int> res;  // Вектор для хранения результатов
    vector<string> res_ver_test;  // Вектор для хранения результатов теста
    vector<int> otvegnutie;  // Вектор для хранения количества отвергнутых чисел

    // Генерация 10 различных простых чисел
    while (res.size() != 10) {
        // Построение числа и проведение теста
        tie(n, q) = builder_test(prime, bit);
        probability = test_poklin(n, 10, q);

        // Проверка на простоту
        if (probability == 1) {
            // Проверка на уникальность числа
            if (find(res.begin(), res.end(), n) == res.end()) {
                res.push_back(n);  // Добавление числа в результаты

                // Дополнительная проверка числа
                probability = test_poklin(n, 1, q);
                if (probability == 1) {
                    res_ver_test.push_back("+");  // Число прошло проверку
                } else {
                    res_ver_test.push_back("-");  // Число не прошло проверку
                }

                otvegnutie.push_back(k);  // Добавление количества отвергнутых чисел
                k = 0;  // Сброс счетчика отвергнутых чисел
            }
        } else {
            k++;  // Увеличение счетчика отвергнутых чисел
        }
    }

    // Вывод результатов
    print_results(res, res_ver_test, otvegnutie);
}

// Функция решета Эратосфена для нахождения всех простых чисел до n
vector<int> primes(int n) {
    vector<bool> is_prime(n + 1, true);  // Вектор для отметки простоты чисел
    vector<int> primes;  // Вектор для хранения простых чисел

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

// Функция для построения числа и проведения теста
pair<int, vector<int>> builder_test(vector<int> prime, int bit) {
    int max_index = 0;  // Максимальный индекс в векторе простых чисел
    int max_pow = 1;  // Максимальная степень двойки

    // Нахождение максимального индекса, такого что prime[max_index] < 2^((bit / 2) + 1)
    for (; (prime[max_index] < pow(2, (bit / 2) + 1)) && (max_index < prime.size()); max_index++);
    // Нахождение максимальной степени двойки, такой что 2^max_pow < 2^((bit / 2) + 1)
    for (; pow(2, max_pow) < pow(2, (bit / 2) + 1); max_pow++);

    int f = 1;  // Начальное значение f
    vector<int> q;  // Вектор для хранения множителей

    // Построение числа f
    while (true) {
        int num = rn(0, max_index);  // Случайный индекс простого числа
        int power = rn(1, max_pow);  // Случайная степень

        // Проверка на переполнение и добавление множителя
        if (pow(prime[num], power)) {
            if (f * pow(prime[num], power) < INT32_MAX) {
                f *= pow(prime[num], power);
                q.push_back(prime[num]);
            }
        }

        // Проверка на соответствие условиям
        if (f > pow(2, (bit / 2))) {
            if (f >= pow(2, (bit / 2) + 1)) {
                f = 1;
                q.clear();
            } else {
                break;
            }
        }
    }

    int R;  // Случайное четное число R

    // Генерация случайного четного числа R
    do {
        R = rn(pow(2, (bit / 2) - 1) + 1, pow(2, bit / 2));
    } while (R % 2 != 0);

    int n = R * f + 1;  // Инициализация числа n
    return make_pair(n, q);  // Возвращение числа и множителей
}

// Тест Поклингтона для проверки числа на простоту
int test_poklin(int n, int t, vector<int> q) {
    vector<int> a;  // Вектор для хранения случайных чисел
    int aj;  // Случайное число

    // Генерация t случайных чисел
    while (a.size() != t) {
        aj = rn(2, n - 1);

        if (find(a.begin(), a.end(), aj) == a.end()) {
            a.push_back(aj);
        }
    }

    // Первоначальная проверка простоты
    for (int aj : a) {
        if (power_mod(aj, n - 1, n) != 1) {
            return 0;
            break;
        }
    }

    // Дополнительная проверка простоты
    bool flag = true;
    int i = 0;
    for (int aj : a) {
        if (q[i] != 0 && power_mod(aj, (n - 1) / q[i], n) == 1) {
            flag = false;
            return 0;
            break;
        }
    }

    // Проверка результата
    if (flag) {
        return 1;
    }

    return 1;
}

// Функция для возведения числа a в степень b по модулю n
int power_mod(int a, int b, int n) {
    long long result = 1;  // Начальное значение результата
    while (b > 0) {  // Пока степень больше 0
        if (b % 2 == 1)  // Если степень нечетная
            result = (result * a) % n;  // Умножение результата на a по модулю n
        a = (a * a) % n;  // Возведение a в квадрат по модулю n
        b /= 2;  // Деление степени на 2
    }
    return result;  // Возвращение результата
}

// Функция для генерации случайного числа в диапазоне [a, b]
int rn(int a, int b) {
    mt19937 mt_rand(random_device{}());  // Инициализация генератора случайных чисел
    return uniform_int_distribution<int>(a, b)(mt_rand);  // Возвращение случайного числа
}

// Функция для вывода результатов
void print_results(const vector<int>& res, const vector<string>& res_ver_test, const vector<int>& otvegnutie) {
    cout << "Prime Numbers\tTest Results\tOccurrences" << endl;  // Заголовок таблицы
    cout << "----------------------------------------------" << endl;  // Разделитель

    // Вывод каждого результата
    for (size_t i = 0; i < res.size(); ++i) {
        cout << res[i] << "\t\t" << res_ver_test[i] << "\t\t" << otvegnutie[i] << endl;
    }
}
