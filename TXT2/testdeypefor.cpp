#include<iostream>

int main(){
    int max=10;
    int k = 0;
    for(int i=0;i<max;i){
        std::cout<<i<<" ";
        i=---i;
        //std::cout<<i<<" ";
        k++;
        if(k == 500){return 1;}
    }
        
    return 0;
}
