#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>

using namespace std;
using namespace std::chrono;

// Génération d'un grand tableau aléatoire
vector<int> genererDonnees(int taille, int minVal = 0, int maxVal = 1000000) {
    vector<int> v(taille);
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<int> dis(minVal, maxVal);
    
    for (int& x : v) {
        x = dis(gen);
    }
    return v;
}

// -> Recherche naïve (O(n))
bool rechercheNaive(const vector<int>& v, int x) {
    return find(v.begin(), v.end(), x) != v.end();
}

// -> Recherche rapide (tri + binary_search) (O(n log n) + O(log n))
bool rechercheRapide(vector<int>& v, int x) {
    //sort(v.begin(), v.end());  // Tri en O(n log n)
    return binary_search(v.begin(), v.end(), x);  // Recherche en O(log n)
}

// Mesure du temps d'exécution d'une fonction
template <typename Func>
double mesurerTemps(Func func) {
    auto start = high_resolution_clock::now();
    func();
    auto stop = high_resolution_clock::now();
    return duration<double, milli>(stop - start).count();  // Temps en millisecondes
}

int main() {
    constexpr int N = 1'000'000;  // Taille du tableau
    int valeurCherchee = 500000;  // Valeur à rechercher

    // Générer un grand tableau de données aléatoires
    vector<int> data = genererDonnees(N);
    vector<int> dataSorted = data;  // Copie pour la version triée

    // 🔥 Test recherche naïve (O(n))
    double tempsNaif = mesurerTemps([&]() {
        rechercheNaive(data, valeurCherchee);
    });

    // 🔥 Test recherche binaire (O(n log n) + O(log n))
    double tempsBinaire = mesurerTemps([&]() {
        rechercheRapide(dataSorted, valeurCherchee);
    });

    // 🏆 Affichage des résultats
    cout << "Résultats sur un tableau de " << N << " éléments :\n";
    cout << "-> Recherche naïve      : " << tempsNaif << " ms (O(n))\n";
    cout << "-> Recherche binaire    : " << tempsBinaire << " ms (O(n log n) + O(log n))\n";

    return 0;
}
