#include <iostream>
#include <string>

using namespace std;
using u32 = unsigned int ;

bool palindrome_t(std::string& mot,int i,int j,bool b){
    if(i>j){return b;}
    return  palindrome_t(mot,i++,j--,b && (mot.at(i) == mot.at(j)));
}

int fibo(int n){
    if (n == 0){
        return 0;
    }
    else if(n == 1) {
        return 1;
    }
    return fibo(n-1) + fibo(n-2);
}

int fibo_t(int n,int a,int b){
    if(n == 0){
        cout<<"passage 0"<<endl;
        return a;
    }
    if(n == 1){
        cout<<"passage 1"<<endl;
        return b;
    }
    else {
        return fibo_t(n-1,b,a+b);
    }

}

int fibo_r(int n){return fibo_t(n,0,1);}

int fibo_i(int n){
    if(n == 0){return 0;}
    int a = 1;
    int b = 1;
    int c = 0;
    for (int i =2 ;i<n;i++) {
        c = a+b;
        a = b;
        b = c;
    }
    return b;
}

int main()
{
    cout << "Hello World!" << endl;
    u32 c,n,k;
    c = n =k =14;
    cout<<c<<","<<n<<","<<k<<endl;
    int g = 28;
    cout<<fibo_r(g)<<endl;
    cout<<fibo(g)<<endl;
    cout<<fibo_i(g)<<endl;
    return 0;
}
