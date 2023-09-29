import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // the number of temperatures to analyse
        int MinPos = 5527;
        int MinNeg = -5527;
        int result = 0;

        for (int i = 0; i < n; i++) {
            int t = in.nextInt(); // a temperature expressed as an integer ranging from -273 to 5526

            if(t<=0 && t>=MinNeg) MinNeg = t;
            if(t>=0 && t<=MinPos) MinPos = t;
        }

        result = MinNeg/-1==MinPos?MinPos:MinNeg/-1<MinPos?MinNeg:MinPos;
        result = n==0?0:result;

        System.out.println(result);
    }
}
