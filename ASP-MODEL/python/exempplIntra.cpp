#include <iostream>
#include <memory>
#include <cstdlib>
#include <immintrin.h> // Pour les intrinsics AVX, SSE

template <typename T>
struct AlignedDeleter {
    void operator()(T* ptr) const {
        std::free(ptr); // Libérer la mémoire allouée avec aligned_alloc
    }
};

template <typename T>
std::unique_ptr<T[], AlignedDeleter<T>> make_aligned_array(size_t size, size_t alignment) {
    // Aligner la mémoire à la limite spécifiée
    T* ptr = static_cast<T*>(std::aligned_alloc(alignment, size * sizeof(T)));
    if (!ptr) {
        throw std::bad_alloc();
    }
    return std::unique_ptr<T[], AlignedDeleter<T>>(ptr);
}

/*
template <typename T>
std::unique_ptr<T[], AlignedDeleter<T>> make_aligned_array(size_t size, size_t alignment) {
    T* ptr = nullptr;
    if (posix_memalign(reinterpret_cast<void**>(&ptr), alignment, size * sizeof(T)) != 0) {
        throw std::bad_alloc();
    }
    return std::unique_ptr<T[], AlignedDeleter<T>>(ptr);
}
*/


int main() {
    constexpr size_t size = 16;         // Taille du tableau
    constexpr size_t alignment = 32;   // Alignement (32 octets pour AVX)

    // Créer un tableau aligné
    auto aligned_array = make_aligned_array<float>(size, alignment);

    // Initialiser le tableau
    for (size_t i = 0; i < size; ++i) {
        aligned_array[i] = static_cast<float>(i) * 1.1f;
    }

    #ifdef __AVX__
    // Exemple d'utilisation avec intrinsics SIMD AVX
    __m256 vector = _mm256_load_ps(&aligned_array[0]); // Charger 8 floats alignés dans un registre AVX
    #else
    std::cerr << "AVX n'est pas activé ou pris en charge sur cette machine." << std::endl;
    #endif
    
    // Afficher les éléments du tableau
    for (size_t i = 0; i < size; ++i) {
        std::cout << aligned_array[i] << " ";
    }
    std::cout << std::endl;

    return 0;
}
 
