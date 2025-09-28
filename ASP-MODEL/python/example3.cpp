#include <iostream>
#include <string>

// Classe Point
class Point {
private:
    int x;
    int y;
    std::string id;

public:
    // Constructeur
    Point(int x_val, int y_val, const std::string& id_val)
        : x(x_val), y(y_val), id(id_val) {}

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

    // Getter pour accéder aux coordonnées
    int getX() const { return x; }
    int getY() const { return y; }
    std::string getId() const { return id; }

    // Fonction statique pour additionner deux points
    static Point plus(const Point& p1, const Point& p2) {
        return Point(p1.getX() + p2.getX(), p1.getY() + p2.getY(), p1.getId() + p2.getId());
    }
};

// Classe Polygone
class Polygone {
private:
    Point x;
    Point y;
    std::string id;

public:
    // Constructeur
    Polygone(const Point& p1, const Point& p2, const std::string& id_val)
        : x(p1), y(p2), id(id_val) {}

    // Convertir un polygone en chaîne de caractères
    std::string toString() const {
        return "id: " + id + " {" + x.ToString() + "} {" + y.ToString() + "}";
    }

    // Méthode pour afficher le polygone
    void display() const {
        std::cout << toString() << std::endl;
    }
};

// Surcharge de l'opérateur << pour Polygone
std::ostream& operator<<(std::ostream& os, const Polygone& pol) {
    os << pol.toString();
    return os;
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
    Point p(1, 2, "p1");
    Point p1(3, 6, "p2");

    // Utilisation des méthodes et fonctions
    p.display();
    std::cout << Point::plus(p, p).ToString() << std::endl;

    // Création et affichage d'un polygone
    Polygone pol(p, p1, "pol1");
    std::cout << pol << std::endl;

    return 0;
}
