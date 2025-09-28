#include <iostream>

int main() {
    int *(*p)[3] = new int* [3];  // Déclaration et allocation

    // Initialisation des pointeurs d'entiers
    for (int i = 0; i < 3; i++) {
        (*p)[i] = new int;
        *(*p)[i] = i + 1;  // Assigner une valeur à chaque entier
    }

    // Accès aux valeurs
    for (int i = 0; i < 3; i++) {
        std::cout << *(*p)[i] << " ";  // Affiche les valeurs des entiers
    }

    // Libération de la mémoire
    for (int i = 0; i < 3; i++) {
        delete (*p)[i];  // Libère chaque entier
    }
    delete[] p;  // Libère le tableau de pointeurs

    return 0;
}
