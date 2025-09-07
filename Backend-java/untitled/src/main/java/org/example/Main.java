package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Solution sol = new Solution();
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a string: ");
        String s = sc.nextLine();

        int result = sol.minOperations(s);
        System.out.println("Minimum operations: " + result);
    }
}

class Solution {
    public int minOperations(String s) {
        char[] arr = s.toCharArray();
        int n = arr.length;
        int[] r = new int[26];
        Arrays.sort(arr);
        for (int i = 0; i < n; i++) {
            r[arr[i] - 'a']++;
        }
        int ans = 0, cidx = (int) (arr[0] - 'a') + 1;
        int ccval = 1;
        for (int i = 0; i < n; i++) {
            char c = arr[i];
            if (c == 'a') continue;
            char nchar = '.';
            for (int j = cidx; j < 26; j++) {
                if (r[j] > 0) {
                    nchar = (char) (j + 'a');
                    cidx = j + 1;
                    r[j] += r[(int) c - 'a'];
                    ccval = ((j ) - ((int) c - 'a'));
                    System.out.println(ccval + " " + j + " " + c);
                    break;
                }
            }
            ans += ccval;
            System.out.println(arr[i]);
        }
        return ans;
    }
}
