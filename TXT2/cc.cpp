#include <iostream>
#include <array>

const int MAX_N= 3;
using tab_ent = std::array<int, MAX_N>;

int saisie(tab_ent &t){
    int i=0;
    int enter;
    do{
        
        std::cout<<"Rentrez un nombre numero "<<i+1<<" supérieur ou égale 0 ?";
        std::cin>>enter;
        if(enter>=0){
            t[i]=enter;
            i++;
        }
    }
    while(enter>=0 and i<MAX_N);

    return i;
}//FinFunction


void diff(tab_ent &t,int size){
    int sp=0;
    int si=0;
    
    int c_sp=0;
    int c_si=0;
    
    if(size == 0){
        std::cout<<" Non calculable car vide"<<std::endl;
    }
    for(int i=0;i<size;i++){
        if(t[i]%2 == 0){
            c_sp++;
            sp+= t[i];
        }
        else{
            c_si++;
            si+=t[i];
        }
    }
    
    if(c_sp == 0 ){
        std::cout<<" PAs possible il y'a que des nombres impaires"<<std::endl;
        return;
    }
    if(c_si == 0 ){
        std::cout<<" PAs possible il y'a que des nombres paires"<<std::endl;
        return;
    }
    
    std::cout<<"diff = "<<sp-si<<std::endl;
    
    
    
}//FinFunction

int main(){
tab_ent t;
int size_de_t = saisie(t);
diff(t,size_de_t);
}//FinMain
