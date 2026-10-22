/*
 * TestMutex.java                                      22/10/2026
 * IUT de Rodez aucun copyright
 */
 
public class TestMutex {
    
    public final static int NB_TESTS = 50;

    public final static int NB_THREADS = 9;

    public final static Thread[] threads = new Thread[NB_THREADS];

    public static void main(String[] args) {

        int valeurAttendue = NB_THREADS * 1111111;
        double nbReussite = 0;
		int valeurGlobale = 0;
		int maximum = 0;
		int minimum = Integer.MAX_VALUE;
		double pourcentageDeReussite = 0.0;

        for (int i = 0; i < NB_TESTS; i++) {
            CompteurDangereux.resetCompteur();
			
			for (int g = 0; g < NB_THREADS; g++) {
			threads[g] = new Thread(new CompteurDangereux.Incrementeur("" + g, 1111111));
		    }
			
            for (int r = 0; r < NB_THREADS; r++) {
                 threads[r].start();
            }
        
            for (int j = 0; j < NB_THREADS; j++) {
                try {
                   threads[j].join(); 
                } catch(InterruptedException e) {
                    System.err.print(e.getMessage());
                    System.out.print("Erreur dans l'attente de fin de tous les Threads");
                }
            } 
			
			valeurGlobale = CompteurDangereux.getCompteur();
			if (valeurGlobale == valeurAttendue) {
				nbReussite++;
			}
			if (valeurGlobale > maximum) {
				maximum = valeurGlobale;
			}
			
			if (valeurGlobale < minimum) {
				minimum = valeurGlobale;
			}
		}

		pourcentageDeReussite = (nbReussite * 100 / NB_TESTS);
		System.out.println("---AFFICHAGE ---");
		System.out.println("Nombre de réussite sur " + NB_TESTS + ":" + nbReussite);
		System.out.println("Pourcentage de réussite sur " + NB_TESTS + ":" + pourcentageDeReussite + "%");
		System.out.println("Valeur minimale obtenue sur " + NB_TESTS + ":" + minimum);
		System.out.println("Valeur maximale obtenue sur " + NB_TESTS + ":" + maximum);
	}
} 