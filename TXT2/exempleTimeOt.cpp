#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>

using namespace std;
using namespace std::chrono;

// 📌 Génère un tableau aléatoire de taille `taille`
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

// 📌 Recherche naïve (O(n))
bool rechercheNaive(const vector<int>& v, int x) {
    return find(v.begin(), v.end(), x) != v.end();
}

// 📌 Recherche binaire (tri + binary_search) (O(n log n) pour le tri, puis O(log n) par recherche)
bool rechercheBinaire(const vector<int>& v, int x) {
    return binary_search(v.begin(), v.end(), x);
}

// 📌 Mesure le temps d'exécution d'une fonction lambda
template <typename Func>
double mesurerTemps(Func func) {
    auto start = high_resolution_clock::now();
    func();
    auto stop = high_resolution_clock::now();
    return duration<double, milli>(stop - start).count();  // Temps en millisecondes
}

int main() {
    constexpr int N = 1'000'000;   // 📌 Taille du tableau
    constexpr int M = 1000;        // 📌 Nombre de recherches successives
    int minVal = 0, maxVal = 1'000'000; // 📌 Plage de valeurs possibles

    // 🎲 Générer un grand tableau de données aléatoires
    vector<int> data = genererDonnees(N, minVal, maxVal);
    vector<int> dataSorted = data;  // Copie pour la version triée

    // 🎲 Générer M valeurs à rechercher
    vector<int> valeursCherchees = genererDonnees(M, minVal, maxVal);

    // 🔥 Test de la recherche naïve (O(n) * M)
    double tempsNaif = mesurerTemps([&]() {
        for (int x : valeursCherchees) {
            rechercheNaive(data, x);
        }
    });

    // 🔥 Test de la recherche binaire (Tri O(n log n) + M recherches O(log n))
    double tempsTri = mesurerTemps([&]() {
        sort(dataSorted.begin(), dataSorted.end());  // 📌 Tri une seule fois O(n log n)
    });

    double tempsBinaire = mesurerTemps([&]() {
        for (int x : valeursCherchees) {
            rechercheBinaire(dataSorted, x);  // 📌 O(log n) par recherche
        }
    });

    // 🏆 Affichage des résultats
    cout << "📊 Résultats pour " << M << " recherches dans un tableau de " << N << " éléments :\n";
    cout << "🔹 Recherche naïve (O(n) par recherche)       : " << tempsNaif << " ms\n";
    cout << "🔹 Tri du tableau (O(n log n))               : " << tempsTri << " ms\n";
    cout << "🔹 Recherche binaire après tri (O(log n))    : " << tempsBinaire << " ms\n";
    cout << "⚡ Temps total pour la recherche binaire     : " << tempsTri + tempsBinaire << " ms\n";

    return 0;
}
