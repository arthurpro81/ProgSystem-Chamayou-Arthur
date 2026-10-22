/*
 * CompteurSecurise.java                                 20/10/2026
 * IUT de Rodez aucun copyright
 */


public class CompteurSecurise {
    private static int compteurGlobal = 0;
	
    private static final Object verrou = new Object();
    
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
                 
                /* On utilise synchronized pour éviter la race condition */
                synchronized(verrou) {
                    compteurGlobal++;
                }
                
            }
            System.out.println(nom + " terminé. Compteur vu : " + compteurGlobal);
        }
    }

    // on protège aussi les lectures 
    public static int getCompteur() {
        synchronized(verrou) {
            return compteurGlobal;
        }
    }

    public static void resetCompteur() {
        synchronized(verrou) {
            compteurGlobal = 0;
        }
    }

}