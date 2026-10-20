/*
 * CalculParallele.java                                 14/10/2026
 * IUT de Rodez aucun copyright
 */

public class CalculParallele {
    
    /** Numéros des Threads */
    private static final int[] DONNEES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int MULTIPLICATEUR = 1000;    

    public static void main(String[] args) {
        System.out.println("=== Calcul Parallele ===");

        Calcule[] threads = new Calcule[DONNEES.length];

        long debut = System.nanoTime();
        long sommeTotal = 0;

		// création et démarrage de tous les threads
        for (int i = 0; i < DONNEES.length; i++) {

            threads[i] = new Calcule(DONNEES[i], MULTIPLICATEUR, i);
            threads[i].start();
        }

        // attente de la fin de tous les threads
        for (int i = 0; i < DONNEES.length; i++) {
            try {
               threads[i].join();
            } catch(InterruptedException e) {
                System.err.print(e.getMessage());
                System.out.print("Erreur dans l'attente de fin de tous les Threads");
            }
            sommeTotal += threads[i].getResultat();
        }

        long duree = (System.nanoTime() - debut) / 1_000_000;
        System.out.println("Résultat total : " + sommeTotal);
        System.out.println("Durée : " + duree + " ms");
    }
}