#include <iostream>
#include <chrono>
#include <memory>
#include <immintrin.h> // Pour les intrinsics SIMD AVX

void add_classic(const float* a, const float* b, float* result, size_t size) {
    for (size_t i = 0; i < size; ++i) {
        result[i] = a[i] + b[i];
    }
}

void add_avx(const float* a, const float* b, float* result, size_t size) {
    for (size_t i = 0; i < size; i += 8) {
        __m256 av = _mm256_load_ps(&a[i]);
        __m256 bv = _mm256_load_ps(&b[i]);
        __m256 sum = _mm256_add_ps(av, bv);
        _mm256_store_ps(&result[i], sum);
    }
}


template <typename T>
std::unique_ptr<T[]> make_aligned_unique(size_t size, size_t alignment) {
    // Aligner la mémoire à la limite spécifiée
    T* ptr = static_cast<T*>(std::aligned_alloc(alignment, size * sizeof(T)));
    if (!ptr) {
        throw std::bad_alloc();
    }
    return std::unique_ptr<T[]>(ptr);
}



constexpr size_t size = 1024 * 1024; // Taille des tableaux (1 million d'éléments)
constexpr size_t alignment = 32;

int main() {
   // alignas(32) float a[size], b[size], result[size];
    
    
    // Créer des tableaux alignés
    auto a = make_aligned_unique<float>(size, alignment);
    auto b = make_aligned_unique<float>(size, alignment);
    auto result = make_aligned_unique<float>(size, alignment);
    
    
    /*   // Aligner les tableaux dynamiques
    if (posix_memalign(reinterpret_cast<void**>(&a), alignment, size * sizeof(float)) != 0 ||
        posix_memalign(reinterpret_cast<void**>(&b), alignment, size * sizeof(float)) != 0 ||
        posix_memalign(reinterpret_cast<void**>(&result), alignment, size * sizeof(float)) != 0) {
        std::cerr << "Erreur d'allocation mémoire alignée" << std::endl;
        return 1;
    }
    */
    

    // Initialisation des tableaux
    for (size_t i = 0; i < size; ++i) {
        a[i] = static_cast<float>(i);
        b[i] = static_cast<float>(2 * i);
    }

    // Mesurer le temps pour la version classique
    auto start_classic = std::chrono::high_resolution_clock::now();
    add_classic(a.get(), b.get(), result.get(), size);
    auto end_classic = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> time_classic = end_classic - start_classic;

    // Mesurer le temps pour la version AVX
   auto start_avx = std::chrono::high_resolution_clock::now();
    add_avx(a.get(), b.get(), result.get(), size);
    auto end_avx = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> time_avx = end_avx - start_avx;

    // Afficher les résultats
    std::cout << "Temps pris (classique) : " << time_classic.count() << " secondes\n";
    std::cout << "Temps pris (AVX) : " << time_avx.count() << " secondes\n";

    // Vérifier les résultats
    bool correct = true;
    for (size_t i = 0; i < size; ++i) {
        if (result[i] != a[i] + b[i]) {
            correct = false;
            break;
        }
    }
    std::cout << "Résultat correct : " << (correct ? "Oui" : "Non") << std::endl;

    return 0;
}
