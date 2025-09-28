int : money;%Budget
int : nr_item;%verifie quantitie of object
int : max_gain = 9999;
int : max_number = 50;
int : max_price = 2147483646;
%Range
set of int : GAIN = 1..max_gain;
set of int : ITEM = 1..nr_item;
set of int : NUMBER = 0..max_number;
set of int : PRICE = 1..max_price;
%Array of data
array[ITEM] of GAIN : item_gain;
array[ITEM] of string :item_name;%output
array[ITEM] of PRICE : item_priceD;
array[ITEM] of NUMBER: item_max;
array[ITEM] of NUMBER : item_numberD;
%Variable decision
array[ITEM] of var NUMBER: item_number;%decision value
array[ITEM] of var int: item_res;%debug
var int : maxiTacos;%optimization value

function var int:somme_number(int : i) = sum(k in item_numberD[i]..(item_numberD[i]+item_number[i]-1))(item_priceD[i]+k*item_priceD[i] + k*(k +1)*item_priceD[i]);
constraint  forall(i in ITEM)((item_number[i]+item_numberD[i])<= item_max[i]);
constraint  forall(i in ITEM)(item_number[i]>=0);
constraint  sum(i in ITEM )( somme_number(i)*bool2int(item_number[i]>0)) <= money ;
constraint maxiTacos = sum(i in ITEM)(item_number[i]*item_gain[i]);
constraint forall(i in ITEM)(item_res[i] == (somme_number(i)*bool2int(item_number[i]>0)));

solve maximize maxiTacos;
%output[show(fix(item_res[i]))++" : "++item_name[i]++" : " ++ show(item_number[i])++" \n"|i in ITEM where fix(item_number[i]) >0];
output[show(maxiTacos)++" : "++item_name[i]++" : " ++ show(item_number[i])++" \n"|i in ITEM where fix(item_number[i]) >0  ];