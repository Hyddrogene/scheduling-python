#include <iostream>
#include <string>

// Structure point
struct point {
    int x;
    int y;
    std::string id;

    // Méthode pour afficher les informations du point
    void display() const {
        std::cout << "id: " << id;
        std::cout << ", x: " << x;
        std::cout << ", y: " << y << std::endl;
    }

    // Convertir un point en chaîne de caractères
    std::string ToString() const {
        return "id: " + id + ", x: " + std::to_string(x) + ", y: " + std::to_string(y);
    }
};

// Structure polygone
struct polygone {
    point x;
    point y;
    std::string id;

    // Constructeur
    polygone(point p1, point p2, std::string id_val) : x(p1), y(p2), id(id_val) {}

    // Convertir un polygone en chaîne de caractères
    std::string to_string() const {
        return "id: " + id + " {" + x.ToString() + "} {" + y.ToString() + "}";
    }
};

// Surcharge de l'opérateur << pour polygone
std::ostream& operator<<(std::ostream& os, const polygone& pol) {
    os << pol.to_string();
    return os;
}

// Fonction pour afficher un point
void printPoint(const point& p) {
    std::cout << "id: " << p.id;
    std::cout << ", x: " << p.x;
    std::cout << ", y: " << p.y << std::endl;
}

// Fonction pour additionner deux points
point plus(const point& p1, const point& p2) {
    return point{p1.x + p2.x, p1.y + p2.y, p1.id + p2.id};
}





// Fonction principale
int main() {
    int n = 10;

    // Affichage des nombres de 0 à n-1
    for (int i = 0; i < n; i++) {
        std::cout << " " << std::to_string(i);
    }
    std::cout << std::endl;

    // Création de points
    point p = {1, 2, "p1"};
    point p1 = {3, 6, "p2"};

    // Utilisation des méthodes et fonctions
    p.display();
    printPoint(p);
    printPoint(plus(p, p));

    // Création et affichage d'un polygone
    polygone pol(p, p1, "pol1");
    std::cout << pol << std::endl;

    return 0;
}
