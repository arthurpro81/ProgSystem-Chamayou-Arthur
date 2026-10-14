/*
 * CalculSequentiel.java                                 14/10/2026
 * IUT de Rodez aucun copyright
 */

public class CalculSequentiel {
    private static final int[] DONNEES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

	private static final int MULTIPLICATEUR = 1000;
    public static void main(String[] args) {
        System.out.println("=== Calcul Séquentiel ===");

        long debut = System.nanoTime();
        long sommeTotal = 0;
		// liste de thread
		

        // TODO: Ce code sera remplacé par une version parallèle
        for (int i = 0; i < DONNEES.length; i++) {
            int valeur = DONNEES[i];
			
			Calcule th = new Calcule(valeur,MULTIPLICATEUR,i);
			// start
            th.start();
			try {
				th.join();
			} catch {
				// a completer
			}
			// join
            sommeTotal += th.getResultat();
            System.out.println("Traitement de " + th.valeur + " terminé : " + th.getResultat());
        }

        long duree = (System.nanoTime() - debut) / 1_000_000;
        System.out.println("Résultat total : " + sommeTotal);
        System.out.println("Durée : " + duree + " ms");
    }
}
