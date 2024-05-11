import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String incomingString = in.nextLine();
        System.out.println(calc(incomingString));
    }
    public static String calc(String input){
        boolean signOk = Auditor.okiSign(input);
        boolean areArabic = Auditor.arabicNumeralsOk(input);
        boolean areRoman = Auditor.romanNumeralsOk();
        int a, b, c = 0;
        if (signOk){
            String signItself = Auditor.sign();
            if (areArabic){
                a = Auditor.quantityFirst;
                b = Auditor.quantitySecond;
                c = switch (signItself) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> a / b;
                    default -> c;
                };
            } else if (areRoman) {
                a = Auditor.fromRomanFirst;
                b = Auditor.fromRomanSecond;
                c = switch (signItself) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> a / b;
                    default -> c;
                };
                if (c > 0){
                    return Auditor.fromArabicToRoman(c);
                } else {
                    throw new IllegalArgumentException("Не существует римских цифр меньше нуля");
                }
            } else throw new IllegalArgumentException();
        }
        return String.valueOf(c);
    }
}
class Auditor{
    static boolean plus, pluses, divider, dividers, minus, minuses, star, stars, a, d, s, m, sign;
    static boolean arabic1Ok, arabic2Ok, arabicOk;
    static boolean roman1Ok, roman2Ok;
    static int fromRomanFirst, fromRomanSecond;
    static String[] members;
    static String[] romanOutgoingNumerals = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX", "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX", "XXX", "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX", "XL", "XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "XLIX", "L", "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX", "LX", "LXI", "LXII", "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX", "LXX", "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX", "LXXX", "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX", "XC", "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX", "C"};
    static String memberFirst, memberSecond;
    static int quantityFirst, quantitySecond;
    enum RomanIncoming{
        I, II, III, IV, V, VI, VII, VIII, IX, X
    }
    static boolean okiSign(String input) {
        plus = input.contains("+");                 // есть ли плюс
        pluses = input.contains("++");
        a = plus && !pluses;
        divider = input.contains("/");              // есть ли знак деления
        dividers = input.contains("//");
        d = divider && !dividers;
        minus = input.contains("-");                // есть ли минус
        minuses = input.contains("--");
        s = minus && !minuses;
        star = input.contains("*");                 // есть ли знак умножения
        stars = input.contains("**");
        m = star && !stars;
        sign = (a & !d & !s & !m) | (!a & d & !s & !m) | (!a & !d & s & !m) | (!a & !d & !s & m);
        int countA = 0, countD = 0, countS = 0, countM = 0;
        for (char element : input.toCharArray()){
            if (element == '+') countA++;
            if (element == '-') countD++;
            if (element == '*') countS++;
            if (element == '/') countM++;
        }
        if (sign & ((countA == 1) | (countD == 1) | (countS == 1) | (countM == 1))){
            return sign;
        } else {
            throw new IllegalArgumentException();
        }
    }
    static String sign(){
        String symb;
        if (a){
            symb = "+";
        } else if (d) {
            symb = "/";
        } else if (s) {
            symb = "-";
        } else {
            symb = "*";
        }
        return symb;
    }
    static String member1(String input){
        String sign = sign();
        if ((!sign.equals("*")) & (!sign.equals("+"))){
            members = input.split(sign);
            memberFirst = members[0];
            return memberFirst;
        } else if (!sign.equals("*")) {
            members = input.split("\\+");
            memberFirst = members[0];
            return memberFirst;
        } else {
            members = input.split("\\*");
            memberFirst = members[0];
            return memberFirst;
        }
    }
    static String member2(){
        memberSecond = members[1];
        return memberSecond;
    }
    static boolean arabicNumeralsOk(String input){
        membersToInt(input);
        arabic1Ok = 0 < quantityFirst & quantityFirst < 11;
        arabic2Ok = 0 < quantitySecond & quantitySecond < 11;
        return arabicOk = arabic1Ok & arabic2Ok;
    }
    static void membersToInt(String input) {
        try {
            String m1 = member1(input);
            String m2 = member2();
            quantityFirst = Integer.parseInt(m1.strip());
            quantitySecond = Integer.parseInt(m2.strip());
        } catch (NumberFormatException xz){
            quantityFirst = 0;
            quantitySecond = 0;
        }
    }
    static boolean romanNumeralsOk(){
        boolean[] romanFirst = new boolean[10];
        boolean[] romanSecond = new boolean[10];
        String[] romansContaind = new String[10];
        for (int i = 0; i < 10; i++){
            romansContaind[i] = RomanIncoming.values()[i].name();
            romanFirst[i] = Objects.equals(memberFirst.strip(), romansContaind[i]);
            romanSecond[i] = Objects.equals(memberSecond, romansContaind[i]);
        }
        roman1Ok = romanFirst[0] | romanFirst[1] | romanFirst[2] | romanFirst[3] | romanFirst[4] | romanFirst[5] | romanFirst[6] | romanFirst[7] | romanFirst[8] | romanFirst[9];
        roman2Ok = romanSecond[0] | romanSecond[1] | romanSecond[2] | romanSecond[3] | romanSecond[4] | romanSecond[5] | romanSecond[6] | romanSecond[7] | romanSecond[8] | romanSecond[9];
        for (int i = 0; i < 10; i++) {
            if (romanFirst[i]) {
                fromRomanFirst = i + 1;
            }
            if (romanSecond[i]) {
                fromRomanSecond = i + 1;
            }
        }
        return roman1Ok & roman2Ok;
    }
    static String fromArabicToRoman(int preRes){
        return romanOutgoingNumerals[preRes - 1];
    }
}