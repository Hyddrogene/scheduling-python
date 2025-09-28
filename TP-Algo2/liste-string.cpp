#include <iostream>


const int Lmax = 100;

struct List_string {
    int longueur;
    std::string T[Lmax+1];
    
};//FinStruct


List_string creer(){
    List_string L;
    L.longueur = 0;
    return L;
};//FinFunction


int longueur(List_string &L){
    return L.longueur;
};//FinFunction

std::string acces_element(const List_string &L, int pos){
     if (pos < 0 ) {throw std::out_of_range("pos < 0");};
     if (pos >= L.longueur){throw std::out_of_range("pos (" + std::to_string(pos) + ") >= longueur (" + std::to_string(L.longueur) + ")");};
    return L.T[pos];
};//FinFunction

void inserer_position(List_string &L,int pos, std::string elt){
    for(int i = L.longueur; i >= pos ;--i){
        L.T[i+1] = L.T[i];
    }
    L.T[pos] = elt;
    L.longueur++;
    
};//FinFunction


void afficher(const List_string &L){
    std::cout << "longueur=" << L.longueur<< " : [";
    for(int  i=0 ; i < L.longueur ;i++){
        
        if(L.longueur-1 == i ){
            std::cout << L.T[i] <<"]";
        }
        else{
            std::cout << L.T[i] <<",";
        }
    }
    std::cout <<"\n";
};//FinFunction


int main() {
try {
        List_string L = creer();

        // Insertions : tête, fin, milieu
        inserer_position(L, 0, "a");   // [10]
        inserer_position(L, 1, "b");   // [10,20]
        inserer_position(L, 0, "c");    // [5,10,20]
        inserer_position(L, 2, "d");   // [5,10,15,20]

        afficher(L);                  // attendu : longueur=4 : [5, 10, 15, 20]

        // Accès valides
        std::cout << "L[0]=" << acces_element(L, 0) << "\n";
        std::cout << "L[3]=" << acces_element(L, 3) << "\n";
        
        
        

        // Tests d'erreur (bornes)
        /*try { inserer_position(L, 6, 99); }
        catch (const std::exception& e) {
            std::cout << "(OK) exception insertion: " << e.what() << "\n";
        }*/
        try { (void)acces_element(L, 42); }
        catch (const std::exception& e) {
            std::cout << "(OK) exception accès: " << e.what() << "\n";
        }

    } catch (const std::exception& e) {
        std::cerr << "Fatal: " << e.what() << "\n";
        return 1;
    }
    return 0;
};//FinFunction
 
