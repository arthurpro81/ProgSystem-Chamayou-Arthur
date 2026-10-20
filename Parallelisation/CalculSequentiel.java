/*
 * CalculSequentiel.java                                 14/10/2026
 * IUT de Rodez aucun copyright
 */

import java.util.ArrayList;
import java.util.List;

public class CalculSequentiel {

    private static final int[] DONNEES =
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private static final int MULTIPLICATEUR = 1000;

    public static void main(String[] args) {
        System.out.println("=== Calcul Parallèle ===");

        long debut = System.nanoTime();
        long sommeTotal = 0;

        // liste des threads
        List<Calcule> threads = new ArrayList<>();

        // création et démarrage de tous les threads
        for (int i = 0; i < DONNEES.length; i++) {
            int valeur = DONNEES[i];

            Calcule th = new Calcule(
                    valeur, MULTIPLICATEUR, i);

            threads.add(th);
            th.start();
        }

        // attente de la fin de tous les threads
        for (Calcule th : threads) {
            try {
                th.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Attente interrompue.");
                return;
            }
        }

        // récupération des résultats
        for (Calcule th : threads) {
            sommeTotal += th.getResultat();
        }

        long duree = (System.nanoTime() - debut) / 1_000_000;

        System.out.println("Résultat total : " + sommeTotal);
        System.out.println("Durée : " + duree + " ms");
    }
}