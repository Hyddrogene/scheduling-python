#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cmath>

struct Point {
    int x;
    int y;
    std::string id;

    Point(int x_val, int y_val, const std::string& id_val)
        : x(x_val), y(y_val), id(id_val) {}

    std::string ToString() const {
        return "id: " + id + ", x: " + std::to_string(x) + ", y: " + std::to_string(y);
    }
};

struct Polygone {
    std::vector<Point> points;
    std::string id;

    Polygone(const std::vector<Point>& pts, const std::string& id_val)
        : points(pts), id(id_val) {}

    std::string ToString() const {
        std::string result = "Polygone " + id + ": ";
        for (const auto& p : points) {
            result += "{" + p.ToString() + "} ";
        }
        return result;
    }

    // Vérifie si deux polygones sont isomorphes
    bool isIsomorphic(const Polygone& other) const {
        if (points.size() != other.points.size()) {
            return false; // Pas le même nombre de points
        }

        // Calcule les distances entre chaque paire de points pour les deux polygones
        auto getDistances = [](const std::vector<Point>& pts) {
            std::vector<double> distances;
            for (size_t i = 0; i < pts.size(); ++i) {
                for (size_t j = i + 1; j < pts.size(); ++j) {
                    double dist = std::sqrt(std::pow(pts[i].x - pts[j].x, 2) +
                                            std::pow(pts[i].y - pts[j].y, 2));
                    distances.push_back(dist);
                }
            }
            std::sort(distances.begin(), distances.end());
            return distances;
        };

        std::vector<double> dist1 = getDistances(points);
        std::vector<double> dist2 = getDistances(other.points);

        return dist1 == dist2; // Isomorphisme si les distances correspondent
    }
};

struct Bibliotheque {
    std::vector<Polygone> polygones;

    void ajouterPolygone(const Polygone& pol) {
        polygones.push_back(pol);
    }

    void afficherPolygones() const {
        for (const auto& pol : polygones) {
            std::cout << pol.ToString() << std::endl;
        }
    }

    void verifierIsomorphisme(const Polygone& pol) const {
        for (const auto& existing : polygones) {
            if (existing.isIsomorphic(pol)) {
                std::cout << "Le polygone " << pol.id << " est isomorphe au polygone " << existing.id << "." << std::endl;
                return;
            }
        }
        std::cout << "Le polygone " << pol.id << " n'est isomorphe à aucun polygone existant." << std::endl;
    }
};

int main() {
    Bibliotheque biblio;

    // Création de polygones
    Polygone pol1({Point(0, 0, "A"), Point(1, 1, "B"), Point(1, 0, "C")}, "pol1");
    Polygone pol2({Point(0, 0, "A"), Point(2, 2, "B"), Point(2, 0, "C")}, "pol2");
    Polygone pol3({Point(0, 0, "A"), Point(1, 0, "B"), Point(0, 1, "C")}, "pol3");

    // Ajout de polygones à la bibliothèque
    biblio.ajouterPolygone(pol1);
    biblio.ajouterPolygone(pol2);

    // Affichage des polygones
    biblio.afficherPolygones();

    // Vérification de l'isomorphisme
    biblio.verifierIsomorphisme(pol3);

    return 0;
}
