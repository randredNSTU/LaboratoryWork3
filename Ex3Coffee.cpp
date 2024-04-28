#include <iostream>
#include <vector>
#include <numeric>
#include <cmath>
#include <stdexcept>

using namespace std;

// Структура для хранения коэффициентов линейной аппроксимации
struct ApproximationResult {
    double a; // Коэффициент a
    double b; // Коэффициент b
};

// Структура для хранения результатов корреляции
struct CorrelationResult {
    double correlation; // Коэффициент корреляции
    double t_value; // Значение t-критерия
};

// Функция для линейной аппроксимации данных
ApproximationResult aproks(const vector<double>& x, const vector<double>& y);

// Функция для вычисления коэффициентов корреляции
CorrelationResult korrel(const vector<double>& x, const vector<double>& y);

// Функция моделирования процесса остывания кофе
vector<double> coffee(double CoffeeTemperature, double RoomTemperature, double CoolingFactor, int Time);

// Функция для печати данных о температуре кофе в конкретный момент времени
void printCoffee(vector<double> temperatures, vector<double>times);

// Функция для печати результатов линейной аппроксимации
void printApproximationResult(ApproximationResult result);

// Функция для печати результатов корреляции
void printCorrelationResult(CorrelationResult result);

int main() {
    // Параметры моделирования процесса остывания кофе
    double CoffeeTemperature = 90; // Начальная температура кофе
    double RoomTemperature = 26; // Температура комнаты
    double CoolingFactor = 0.01; // Коэффициент остывания
    int Time = 60; // Временной предел в минутах

    // Моделирование процесса остывания кофе
    vector<double> temperatures = coffee(CoffeeTemperature, RoomTemperature, CoolingFactor, Time);

    // Создание временных меток для измерений
    vector<double> times(Time + 1);
    iota(times.begin(), times.end(), 0);

    // Вычисление линейной аппроксимации и корреляции
    ApproximationResult approx_result = aproks(times, temperatures);
    CorrelationResult corr_result = korrel(times, temperatures);

    // Печать результатов
    printApproximationResult(approx_result);
    printCorrelationResult(corr_result);
    printCoffee(temperatures, times);

    return 0;
}


// Функция для линейной аппроксимации данных
ApproximationResult aproks(const vector<double>& x, const vector<double>& y) {
    // Суммирование значений x и y
    double x_sum = accumulate(x.begin(), x.end(), 0.0);
    double y_sum = accumulate(y.begin(), y.end(), 0.0);

    // Суммирование квадратов значений x и произведений значений x и y
    double x2_sum = inner_product(x.begin(), x.end(), x.begin(), 0.0);
    double xy_sum = inner_product(x.begin(), x.end(), y.begin(), 0.0);

    // Количество измерений
    size_t n = x.size();

    // Вычисление коэффициентов линейной аппроксимации
    double a = (n * xy_sum - x_sum * y_sum) / (n * x2_sum - x_sum * x_sum);
    double b = (y_sum - a * x_sum) / n;

    return { a, b };
}

// Функция для вычисления среднего значения
double mean(const vector<double>& vec) {
    return accumulate(vec.begin(), vec.end(), 0.0) / vec.size();
}

// Функция для вычисления коэффициентов корреляции
CorrelationResult korrel(const vector<double>& x, const vector<double>& y) {
    // Вычисление средних значений
    double x_mean = mean(x);
    double y_mean = mean(y);

    // Инициализация переменных для вычисления сумм
    double sum_xy = 0.0;
    double sum_x2 = 0.0;
    double sum_y2 = 0.0;
    size_t n = x.size();

    // Вычисление сумм для формулы коэффициента корреляции
    for (size_t i = 0; i < n; ++i) {
        sum_xy += (x[i] - x_mean) * (y[i] - y_mean);
        sum_x2 += (x[i] - x_mean) * (x[i] - x_mean);
        sum_y2 += (y[i] - y_mean) * (y[i] - y_mean);
    }

    // Вычисление коэффициента корреляции и t-критерия
    double r = sum_xy / (sqrt(sum_x2) * sqrt(sum_y2));
    double t = r * sqrt(n - 2) / sqrt(1 - r * r);

    return { r, t };
}

// Функция моделирования процесса остывания кофе
vector<double> coffee(double CoffeeTemperature, double RoomTemperature, double CoolingFactor, int Time) {
    // Вектор для хранения температур кофе в разные моменты времени
    vector<double> temperatures;

    // Моделирование процесса остывания
    for (int t = 0; t <= Time; t++) {
        double temperature = RoomTemperature + (CoffeeTemperature - RoomTemperature) * exp(-CoolingFactor * t);
        temperatures.push_back(temperature);
    }

    return temperatures;
}

// Функция для печати результатов линейной аппроксимации
void printApproximationResult(ApproximationResult result) {
    cout << "Approximation Result:" << endl;
    cout << "a: " << result.a << ", b = " << result.b << endl;
}

// Функция для печати результатов корреляции
void printCorrelationResult(CorrelationResult result) {
    cout << "Correlation Result:" << endl;
    cout << "Correlation: " << result.correlation << endl;
    cout << "T-value: " << result.t_value << endl;
}

// Функция для печати данных о температуре кофе в конкретный момент времени
void printCoffee(vector<double> temperatures, vector<double>times) {
    for (int i = 0; i < temperatures.size(); i++) {
        cout << "Time - " << times[i] << ":  " << temperatures[i] << " C" << endl;
    }
}
