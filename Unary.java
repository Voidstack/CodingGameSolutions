import java.util.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String MESSAGE = in.nextLine();      

        System.out.println(binaryToUnary(binaryFormater(MESSAGE)));
    }

    public static String binaryToUnary(String resultBinary){
        StringBuilder resultUnary = new StringBuilder();
        char lastChar = resultBinary.charAt(0);
        int combo = 0;
        for (char currentChar : resultBinary.toCharArray()) {
            if(currentChar != lastChar) {
                resultUnary
                        .append(lastChar == '0' ? "00 " : "0 ")
                        .append("0".repeat(combo))
                        .append(" ");
                combo = 0;
            }
            combo+=1;
            lastChar = currentChar;
        }

        if (combo > 0) {
            resultUnary
                    .append(lastChar == '0' ? "00 " : "0 ")
                    .append("0".repeat(combo));
        }
        return resultUnary.toString();
    }

    public static String binaryFormater(String message){
        StringBuilder resultBinaire = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            String charInBinary = Integer.toBinaryString(message.charAt(i));
            charInBinary = "0000000".substring(charInBinary.length()) + charInBinary;
            resultBinaire.append(charInBinary);
        }
        return resultBinaire.toString();
    }
}
