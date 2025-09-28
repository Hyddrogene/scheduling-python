#include<iostream>

const int Lmax = 100;

struct occMot{
    std::string mot;
    int occ;
};//FinStruct


struct Cell {
    occMot val; 
    Cell* next; 
};//FinStruct

struct listeMots {
    int longueur;
    Cell* head; 
};//FinStruct




// Partie 2 (déclarés ici, non utilisés dans cette partie)
struct tabMots { std::string T[Lmax]; int n; };

struct occLettre { char lettre; int occ; };
struct tabLettres { occLettre T[26]; };




void initialise(listeMots& L) { 
    L.longueur = 0;
    L.head = nullptr; 
}//FinFunction


void ajouteDebut(std::string mot, listeMots & L) {
    occMot oc;
    oc.mot = mot;
    oc.occ = 1;
    Cell* cel = new Cell;
    cel->val = oc;
    cel->next = L.head;
    L.head = cel;
    L.longueur++;
}//FinFunction



void ajoute(const std::string& mot, listeMots& L) {
    Cell* previous = nullptr;
    Cell* current = L.head;

    // trouver la première position où current->mot >= mot :  O(n)
    while (current != nullptr && current->val.mot < mot) {
        previous = current;
        current = current->next;
    }

    // mot existant : on incrémente
    if (current != nullptr && current->val.mot == mot) {
        current->val.occ++;
        return;
    }

    // insérer une nouvelle Cell avant 'current'
    Cell* cel = new Cell{{mot, 1}, current};
    L.longueur++;
    if (previous != nullptr){
        previous->next = cel;// insertion après previous
    } 
    else {
        L.head = cel;// insertion au début
    }      
}



void ajoute_compact(const std::string& mot, listeMots& L) {
    Cell** p = &L.head;
    while (*p && (*p)->val.mot < mot) p = &(*p)->next;
    if (*p && (*p)->val.mot == mot) { (*p)->val.occ++; return; }
    *p = new Cell{{mot, 1}, *p};  // insère sans cas particulier
    L.longueur++;
}






//Question 4
void afficherOccMot(occMot &mot){
    std::cout <<"{mot=" <<mot.mot<<", occ="<<mot.occ<<"}";
}//FinFunction

void afficher(const listeMots &M){
    std::cout << "[";
    Cell* cel = M.head;
    while(cel != nullptr){
        afficherOccMot(cel->val);
        if (cel->next != nullptr){ std::cout <<",";};
        cel = cel->next;
    }
    std::cout<< "], longueur=" << M.longueur;
    std::cout <<"\n";
}//FinFunction


void affiche_compact(listeMots L) {
    for (Cell* c = L.head; c; c = c->next)
        std::cout <<"(mot="<< c->val.mot << " , occ=" << c->val.occ << ")\n";
}//FinFunction


//Question 5
int taille(const listeMots &L){
    return L.longueur;
}//FinFunction





//Question 6
int nombreMots(const listeMots &M){
    Cell* cel = M.head;
    int sum = 0;
    while(cel != nullptr){
        sum += cel->val.occ ;
        cel = cel->next;
    }
    return sum;
}//FinFunction


int nombreMots_compact(const listeMots& M) {
    int sum = 0;
    for (auto cel = M.head; cel; cel = cel->next) {
        sum += cel->val.occ;
    }
    return sum;
}//FinFunction






//Question 7

bool estValide(const listeMots &L) {
    if (L.head == nullptr){ return true;}
    
    Cell* cel = L.head;
    
    if (cel->val.occ < 1){ return false;}
    
    for (Cell* n = cel->next; n;  n = n->next) {
        if (n->val.occ < 1){ return false;}
        if (cel->val.mot > n->val.mot) {return false;} // strictement croissant
        cel = n;
    }
    return true;
}//FinFunction




//Question 8
int plusLong(const listeMots &L) {
    int max = 0;
    for (auto cel = L.head; cel; cel = cel->next) {
        if (max < cel->val.mot.size()){max = cel->val.mot.size();}
        //max = std::max<int>(max, (int)c->val.mot.size());
    }
    return max;
}//FinFunction



//Question 9
void afficheLongueur(const listeMots &L, int longueur) {
    for (auto cel = L.head; cel; cel = cel->next) {
        if (longueur == cel->val.mot.size()){std::cout << cel->val.mot<<" ,";}
    }
}//FinFunction





//Question 10 

void afficheTriLongueur(const listeMots& L) {
    
    int maxLen = plusLong(L);
    for (int target = 0; target <= maxLen; ++target) {
        for (Cell* c = L.head; c; c = c->next) {
            if (c->val.mot.size() == target) {
                afficherOccMot(c->val);
            }
        }
    }
    std::cout<<std::endl;
}//FinFunction



//Question 11



void addWordsFromLine(const std::string& line, listeMots& L) {
    std::string w;
    for (char ch : line + " ") {
        if (std::isspace(static_cast<unsigned char>(ch))) {
            if (!w.empty()) { ajoute(w, L); w.clear(); }
        } else {
            w.push_back(ch);
        }
    }
}

void saisit(listeMots& L) {
    std::cout << "Entrez des mots (ligne vide pour terminer):\n";
    std::string line;
    while (std::getline(std::cin, line) && !line.empty()) {
        addWordsFromLine(line, L);
    }
}






void remplit(tabMots& T, listeMots L) {
    T.n = 0;
    for (Cell* c = L.head; c && T.n < Lmax; c = c->next) {
        T.T[T.n++] = c->val.mot;       // un seul exemplaire de chaque mot
    }
    // Si L contient plus de Lmax mots, on tronque.
}



void affiche(const tabMots& T) {
    for (int i = 0; i < T.n; ++i) {
        std::cout << T.T[i] << "\n";
    }
}





int main(){
    listeMots L; initialise(L);
ajouteDebut("pomme", L); ajoute("banane", L); ajoute("pomme", L); ajoute("cactus", L);ajoute("cacfddsfsdfsdfdsfdsfdstus", L);
//     ajouteDebut("abricot", L); // ne maintient pas l'ordre (voulu par 2.)
afficher(L);
std::cout<< "Somme : "<< nombreMots(L)<<"\n";
     std::cout << "valide=" << estValide(L) << ", taille=" << taille(L)  << ", total=" << nombreMots(L) << ", maxLen=" << plusLong(L) << "\n";
     afficheLongueur(L,6);
     std::cout << "\nTri par longueur:\n"; afficheTriLongueur(L);
     saisit(L);
//     return 0;
     afficher(L);

};//FinStruct
