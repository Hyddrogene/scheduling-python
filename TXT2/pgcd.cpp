#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>

using u32 = unsigned int;

u32 min(const std::vector<u32> &k){
    u32 min =k[0];
    std::for_each(k.begin(),k.end(),[&min](u32 i){(min>i)?min = i:min;});
    return min;
}//FinFunction

u32 reduce_div(const std::vector<u32> &k, u32 n){
    u32 somme = 0;
    for(u32 i=0;i<k.size() && !somme;i++){somme+= k[i]%n;}
    return somme;
}//FinFunction

u32 pgcd(std::vector<u32> &k){
    u32 p=1;
    //std::sort(k.begin(),k.end(),[](u32 a,u32 b)->bool{return a<b;});
    for(u32 i=1;i<=static_cast<u32>(sqrt(min(k)))+1;i++){if(!reduce_div(k,i)) p=i;}
    return p;
}//FinFunction

int main(){
    std::vector<u32> t{48,60,12000,120,1200,38};
    std::cout<<"pgcd "<<pgcd(t)<<std::endl;
    return 0;
}//FinFunction
