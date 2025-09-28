#include<iostream>


const int Lmax = 100;

struct listeMot {
    int longueur;
    std::string T[Lmax+1];
    
};//FinStruct

listeMot creer(){
    listeMot M;
    M.longueur = 0;
    return M;
};//FinFunction

void ajouteDebut(listeMot &M,std::string mot){
    for(int i = M.longueur ; i >= 0 ;i--){
        M.T[i+1] = M.T[i];
    }
    M.longueur++;
    M.T[0] = mot;
};//FinFunction



void afficher(const listeMot &M){
    std::cout << "longueur=" << M.longueur<< " : [";
    for(int  i=0 ; i < M.longueur ;i++){
        
        if(M.longueur-1 == i ){
            std::cout << M.T[i] <<"]";
        }
        else{
            std::cout << M.T[i] <<",";
        }
    }
    std::cout <<"\n";
};//FinFunction



int main(){
    listeMot mots = creer();
    ajouteDebut(mots, "a");
    ajouteDebut(mots, "b");
    ajouteDebut(mots, "c");
    ajouteDebut(mots, "d");
    
    afficher(mots);
    return 0;
};
