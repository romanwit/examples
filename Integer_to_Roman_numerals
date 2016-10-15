public class Solution {
public string IntToRoman(int num) {

    int[] values = new int[13] {1000,900,500,400,100,90,50,40,10,9,5,4,1};
    string[] symbols = new string[13]{"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
    
    String s = "";

    int i = 0;
    while( num > 0){
        int k = num/values[i];
        for( int j = 0; j < k; j++){
            s+=symbols[i];
        }
       
        num %= values[i];
        i++;
    }
    
    return s;
    }
}
