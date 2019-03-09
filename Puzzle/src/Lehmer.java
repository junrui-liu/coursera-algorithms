/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jun
 */
public class Lehmer {
    
    public static int[] decode(int[] code)
    {
        int n = code.length;
        boolean[] chosen = new boolean[n];
        int[] plain = new int[n];
        for (int i = 0; i < n; i++)
        {
            int current = code[i];
            int count = 0;
            int j = 0;
            while (count < current)
            {
                if (!chosen[j])
                    count++;
                j++;
            }
            while (chosen[j])
                j++;
            plain[i] = j;
            chosen[j] = true;
        }
        return plain;
    }
    
    public static int[] encode(int[] plain)
    {
        int n = plain.length;
        int[] code = new int[n];
        boolean[] chosen = new boolean[n];
        for (int i = 0; i < n; i++) {
            int current = plain[i];
            int count = 0;
            for (int k = 0; k < current; k++)
                if (!chosen[k])
                    count++;
            code[i] = count;
            chosen[current] = true;
        }
        return code;
    }
}
