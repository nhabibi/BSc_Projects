
import java.util.StringTokenizer;

class Solution {
    
    public String reverseWords(String s) {
        
        StringTokenizer st = new StringTokenizer(s);
        int number_of_words = st.countTokens();
        String[] token_arr = new String[number_of_words];
        String reversed_s = "";
        
        for(int i=0; i<number_of_words; ++i)
            token_arr[i] = st.nextToken();
         
        reversed_s = token_arr[number_of_words-1];
        for(int i=number_of_words-2; i>=0; --i)
            reversed_s = reversed_s + " " + token_arr[i];
        
        return reversed_s;
    }
}
