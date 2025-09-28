#include <iostream>
#include <vector>
#include <string>

#include <algorithm>// Pour std::sort
#include <numeric> // Pour std::accumulate

struct point{
    int x;
    int y;
    std::string id;
    
    void display(){
    std::cout<<":"<<id;
    std::cout<<",:"<<x;
    std::cout<<",:"<<y<<std::endl;}
    
    std::string ToString() const{return ":"+id+",:"+std::to_string(x)+ ",:"+std::to_string(y);}
};


struct polygone{
    point x;
    point y;
    std::string id;
    
    polygone(point p1, point p2, std::string id_val) : x(p1), y(p2), id(id_val) {}
    
    std::string to_string() const{return "id :"+id+" "+x.ToString()+" "+y.ToString();}
};


// Surcharge de l'opérateur << pour polygone
std::ostream& operator<<(std::ostream& os, const polygone& pol) {
    os << pol.to_string();
    return os;
}



void printPoint(const point &p){
    std::cout<<"id :"<<p.id;
    std::cout<<", x :"<<p.x;
    std::cout<<", y :"<<p.y<<std::endl;
    
}


point plus(const point &p1, const point &p2){
    point p = {p1.x+p2.x,p2.y+p1.y,p2.id+p1.id};
    return p;
}

int main(){
    int n = 10;
    for(int i = 0; i< n ;i++){
        std::cout<<" "+std::to_string(i); 
    }
    std::cout<<std::endl;
    
    point p = {1,2,"p1"};
    point p1 = {3,6,"p2"};
    //point p(1,2,"p1");
    p.display();
    printPoint(p);
    printPoint(plus(p,p));
    
    polygone pol(p,p1,"pol1");
    std::cout<<pol;
    
    //%%%%%%%%%%%%%%%%%%%
    
    std::vector<int> vec = {5, 3, 8, 6, 2};

    int sum = std::accumulate(vec.begin(), vec.end(), 0); // La somme initiale est 0
    std::cout << "Somme des éléments : " << sum << std::endl;
    
    std::sort(vec.begin(), vec.end()); // La somme initiale est 0
    //std::cout << "Somme des éléments : " << vec << std::endl;
    
    std::for_each(vec.begin(), vec.end(), [](int val) {std::cout << "Valeur : " << val << std::endl;});

    
}//FinMain
