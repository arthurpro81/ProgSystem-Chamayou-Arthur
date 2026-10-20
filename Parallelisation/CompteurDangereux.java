/*
 * CompteurDangereux.java                                 19/10/2026
 * IUT de Rodez aucun copyright
 */


public class CompteurDangereux {
    private static int compteurGlobal = 0;  // Variable partagée MODIFIABLE

    static class Incrementeur implements Runnable {
        private final String nom;
        private final int nombreIncrements;

        public Incrementeur(String nom, int nombreIncrements) {
            this.nom = nom;
            this.nombreIncrements = nombreIncrements;
        }

        @Override
        public void run() {
            for (int i = 0; i < nombreIncrements; i++) {
                // TODO: Expliquer pourquoi cette ligne pose problème
				
				/*
				 * Plusieurs threads peuvent modifier la même variable en même temps.
				 * L'incrémentation n'est pas une opération atomique : plusieurs threads
				 * peuvent lire la même valeur et écraser leurs modifications respectives.
				 * Le résultat final de compteurGlobal peut donc être incorrect.
				 */
                compteurGlobal++;  // RACE CONDITION ICI !
            }
            System.out.println(nom + " terminé. Compteur vu : " + compteurGlobal);
        }
    }

    public static int getCompteur() {
        return compteurGlobal;
    }

    public static void resetCompteur() {
        compteurGlobal = 0;
    }
}