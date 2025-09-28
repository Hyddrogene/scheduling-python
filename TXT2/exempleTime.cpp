#include <iostream>
#include <cmath>
#include <chrono>

using namespace std;
using namespace std::chrono;

// Fonction pour mesurer le temps d'exécution d'une opération
template <typename Func>
double measureExecutionTime(Func func, int iterations) {
    auto start = high_resolution_clock::now();
    for (int i = 0; i < iterations; ++i) {
        func();
    }
    auto end = high_resolution_clock::now();
    return duration<double, milli>(end - start).count();
}

int main() {
    constexpr int iterations = 1'000'000; // Nombre d'itérations pour la mesure
    double x = 25849811721;//3.14; // Valeur testée

    // Mesure du temps d'exécution pour pow(x, 2)
    double time_pow2 = measureExecutionTime([&]() {
        volatile double result = pow(x, 2);
    }, iterations);

    // Mesure du temps d'exécution pour x * x
    double time_mult2 = measureExecutionTime([&]() {
        volatile double result = x * x;
    }, iterations);

    // Mesure du temps d'exécution pour pow(x, 3)
    double time_pow3 = measureExecutionTime([&]() {
        volatile double result = pow(x, 3);
    }, iterations);

    // Mesure du temps d'exécution pour x * x * x
    double time_mult3 = measureExecutionTime([&]() {
        volatile double result = x * x * x;
    }, iterations);

    // Affichage des résultats
    cout << "Comparaison des temps d'exécution sur " << iterations << " itérations:" << endl;
    cout << "pow(x, 2)    : " << time_pow2 << " ms" << endl;
    cout << "x * x        : " << time_mult2 << " ms" << endl;
    cout << "pow(x, 3)    : " << time_pow3 << " ms" << endl;
    cout << "x * x * x    : " << time_mult3 << " ms" << endl;

    return 0;
}
