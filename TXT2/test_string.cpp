#include <iostream>

std::string fonction_msg(){
    std::string n= "le message est valide";
    if (true ){
        n= "n est modificier";
    }
    return n;
}

int main(){
    
    std::string msg="default";
    msg = fonction_msg();
    std::cout <<msg<<std::endl;
    return 0;
}
