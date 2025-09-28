#include <iostream>

using u32 = unsigned int ;
int main(int argc, char *argv[]){
    u32 h = 1000;
    
    if(argc){
        h = atoi(argv[1]);
    }
    //===================
    for (u32 i =0;i<h;i++){
        std::cout<<"oui_";
    }
    std::cout<<std::endl;
    return 0;
}//FinFunction
