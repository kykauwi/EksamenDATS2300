package no.oslomet.cs.algdat.Eksamen;

import java.util.Comparator;

public class Main {
    public static void main (String[] args){
       /* Integer[] a = {4,7,2,9,5,10,8,1,3,6};
        EksamenSBinTre<Integer> tre = new EksamenSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) tre.leggInn(verdi);
        System.out.println(tre.antall()); // Utskrift: 10
*/

        int[] a = {4,7,2,9,4,10,8,7,4,6,1};
        EksamenSBinTre<Integer> tre = new EksamenSBinTre<>(Comparator.naturalOrder());
        for (int verdi : a) tre.leggInn(verdi);
        System.out.println(tre.fjernAlle(4)); // 3
        tre.fjernAlle(7); tre.fjern(8);
        System.out.println(tre.antall()); // 5
        System.out.println(tre.toString() + " ");
    }
}
