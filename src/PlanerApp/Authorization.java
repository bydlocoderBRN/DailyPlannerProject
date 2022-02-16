package PlanerApp;

import java.util.ArrayList;
import java.util.Arrays;

public class Authorization {
    public static long p = 10^(-6);
    public static long v = 20;
    public static long t = 3*7*24*60;
    private static final String[] abcUpper = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private static final String[] abc = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    private static final String[] num = {"1","2","3","4","5","6","7","8","9","0"};
    private static final String[] symbols = {"!","@","$","%","^","&","*","?","+","-","_"};
    private static final String[] rusUpper = {"А","Б","В","Г","Д","Е","Ё","Ж","З","И","Й","К","Л","М","Н","О","П","Р","С","Т","У","Ф","Х","Ц","Ч","Ш","Щ","Ъ","Ы","Ь","Э","Ю","Я"};
    private static final String[] rus={"а","б","в","г","д","е","ё","ж","з","и","к","л","м","н","о","п","р","с","т","у","ф","х","ц","ч","ш","щ","ъ","ы","ь","э","ю","я"};
    private static ArrayList<String> currAlphabet = new ArrayList<String>();
    public static void plusAbcUpper(){
       currAlphabet.addAll(Arrays.asList(abcUpper));
    }
    public static void minusAbcUpper(){
        currAlphabet.removeAll(Arrays.asList(abcUpper));
    }
    public static void plusAbc(){
        currAlphabet.addAll(Arrays.asList(abc));
    }
    public static void minusAbc(){
        currAlphabet.removeAll(Arrays.asList(abc));
    }
    public static void plusRus(){
        currAlphabet.addAll(Arrays.asList(rus));
    }
    public static void minusRus(){
        currAlphabet.removeAll(Arrays.asList(rus));
    }
    public static void plusRusUpper(){
        currAlphabet.addAll(Arrays.asList(rusUpper));
    }
    public static void minusRusUpper(){
        currAlphabet.removeAll(Arrays.asList(rusUpper));
    }
    public static void plusSymbols(){
        currAlphabet.addAll(Arrays.asList(symbols));
    }
    public static void minusSymbols(){
        currAlphabet.removeAll(Arrays.asList(symbols));
    }
    public static void plusNumbers(){
        currAlphabet.addAll(Arrays.asList(num));
    }
    public static void minusNumbers(){
        currAlphabet.removeAll(Arrays.asList(num));
    }
    public static void customAlphabet(String alph){
        currAlphabet.addAll(Arrays.asList(alph.split(" ")));
    }
    public static String[] generatePassword(double p, double v, double t){
        int s =(int)Math.ceil((v*t)/p);
        int a = currAlphabet.size();
        int l = (int)(Math.log(s)/Math.log(a));
        String password = "";
        for (int i=0;i<l;i++){
            password+=currAlphabet.get((int)(Math.random()*(currAlphabet.size()-1)));
        }
        currAlphabet.clear();
        return new String[]{password, String.valueOf(s), String.valueOf(a), String.valueOf(l)};
    }
}
