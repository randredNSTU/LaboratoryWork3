#include <iostream>
#include <vector>
#include <tuple>
#include <cmath>
#include <random>
#include <limits>
#include <algorithm>

using namespace std;

// Функция для получения таблицы простых чисел
vector<int> primes(int n);
pair<int, vector<int>> builder_test(vector<int> prime, int bit);
int test_poklin(int n, int t, vector<int> q);

int power_mod(int a, int b, int n);
int rn(int a, int b);

void print_results(const vector<int>& res, const vector<string>& res_ver_test, const vector<int>& otvegnutie);

int main() {
    int size_primes = 500; // Размер таблицы малых простых чисел
    vector<int> prime = primes(size_primes); // Строим таблицу малых простых чисел

    int bit = 0;
    cin >> bit; // Запрашиваем размер требуемого простого числа

    vector<int> q; // Вектор для хранения факторов F
    int n; // Переменная для хранения числа n
    int k = 0; // Счетчик для отклоненных чисел
    int probability; // Переменная для хранения результата теста

    vector<int> res; // Вектор для хранения найденных простых чисел
    vector<string> res_ver_test; // Вектор для хранения результатов дополнительного теста
    vector<int> otvegnutie; // Вектор для хранения количества отклоненных чисел

    while(res.size() != 10) { // Повторяем, пока не найдем 10 простых чисел
        tie(n, q) = builder_test(prime, bit); // Строим число n и получаем разложение F
        probability = test_poklin(n, 10, q); // Проверяем число n с параметром надежности 10

        if(probability == 1) { // Если n прошло тест Поклингтона
            if (find(res.begin(), res.end(), n) == res.end()) { // Если n еще не добавлено в результат
                res.push_back(n); // Добавляем n в результат

                probability = test_poklin(n, 1, q); // Проверяем число n с параметром надежности 1
                if(probability == 1) {
                    res_ver_test.push_back("+"); // Если n прошло тест, добавляем "+" в результат
                } else {
                    res_ver_test.push_back("-"); // Если n не прошло тест, добавляем "-" в результат
                }

                otvegnutie.push_back(k); // Добавляем количество отклоненных чисел
                k = 0; // Сбрасываем счетчик отклоненных чисел
            }
        } else {
            k++; // Увеличиваем счетчик отклоненных чисел
        }
    }

    print_results(res, res_ver_test, otvegnutie); // Печатаем результаты
}

vector<int> primes(int n) {
    vector<bool> is_prime(n + 1, true); // Инициализируем вектор для проверки простых чисел
    vector<int> primes; // Вектор для хранения простых чисел

    for (int p = 2; p * p <= n; ++p) {
        if (is_prime[p]) {
            for (int i = p * p; i <= n; i += p)
                is_prime[i] = false; // Помечаем все кратные p как составные
        }
    }

    for (int p = 2; p <= n; ++p) {
        if (is_prime[p])
            primes.push_back(p); // Добавляем простые числа в вектор
    }

    return primes; // Возвращаем таблицу простых чисел
}

pair<int, vector<int>> builder_test(vector<int> prime, int bit) {
    int max_index = 0;
    int max_pow = 1;
    // Находим максимальный индекс и степень для построения числа F
    for (; (prime[max_index] < pow(2, (bit / 2) + 1)) && (max_index < prime.size()); max_index++);
    for (; pow(2, max_pow) < pow(2, (bit / 2) + 1); max_pow++);

    int f = 1; // Начальное значение F
    vector<int> q; // Вектор для хранения факторов F

    while(true){
        int num = rn(0, max_index); // Случайный индекс для выбора простого числа
        int power = rn(1, max_pow); // Случайная степень для выбора простого числа
        
        if (pow(prime[num], power)) {
            if(f * pow(prime[num], power) < INT32_MAX) {
                f *= pow(prime[num], power); // Умножаем F на простое число в степени power
                q.push_back(prime[num]); // Добавляем простое число в вектор q
            }
        }

        if(f > pow(2, (bit / 2))){
            if(f >= pow(2, (bit / 2) + 1)){
                f = 1; // Если F слишком велико, сбрасываем его
                q.clear(); // Очищаем вектор q
            } else {
                break; // Если F подходит по размеру, выходим из цикла
            }
        }
    }

    int R; // Переменная для хранения значения R

    do {
        R = rn(pow(2, (bit / 2) - 1) + 1, pow(2, bit / 2)); // Генерируем случайное четное число R
    } while (R % 2 != 0);

    int n = R * f + 1; // Вычисляем значение n
    return make_pair(n, q); // Возвращаем значение n и вектор q
}


int test_poklin(int n, int t, vector<int> q) {
    vector<int> a; // Вектор для хранения случайных чисел a
    int aj;

    while (a.size() != t) {
        aj = rn(2, n - 1); // Генерируем случайное число a

        if (find(a.begin(), a.end(), aj) == a.end()) {
            a.push_back(aj); // Добавляем a в вектор, если оно еще не добавлено
        }
    }

    for (int aj : a) {
        if (power_mod(aj, n - 1, n) != 1) {
            return 0; // Если a^(n-1) mod n != 1, возвращаем 0 (n составное)
            break;
        }
    }

    bool flag = true;
    int i = 0;
    for (int aj : a) {
        if (q[i] != 0 && power_mod(aj, (n - 1) / q[i], n) == 1) {
            flag = false;
            return 0; // Если a^((n-1)/q) mod n == 1, возвращаем 0 (n составное)
            break;
        }
    }

    if (flag) {
        return 1; // Если все проверки пройдены, возвращаем 1 (n вероятно простое)
    }

    return 1;
}

int power_mod(int a, int b, int n) {
    long long result = 1;
    while (b > 0) {
        if (b % 2 == 1)
            result = (result * a) % n; // Если b нечетное, умножаем результат на a
        a = (a * a) % n; // Квадрат a по модулю n
        b /= 2; // Делим b на 2
    }
    return result; // Возвращаем результат
}

int rn(int a, int b) {
    mt19937 mt_rand(random_device{}()); // Инициализируем генератор случайных чисел
    return uniform_int_distribution<int>(a, b)(mt_rand); // Возвращаем случайное число в диапазоне [a, b]
}

void print_results(const vector<int>& res, const vector<string>& res_ver_test, const vector<int>& otvegnutie) {
    cout << "Prime Numbers\tTest Results\tOccurrences" << endl;
    cout << "----------------------------------------------" << endl;

    for (size_t i = 0; i < res.size(); ++i) {
        cout << res[i] << "\t\t" << res_ver_test[i] << "\t\t" << otvegnutie[i] << endl; // Печатаем результаты
    }
}
