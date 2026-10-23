/*
 * TestSemaphore.java                                      23/10/2026
 * IUT de Rodez aucun copyright
 */


public class TestSemaphore {

    private static final int NB_PRODUITS_PAR_PRODUCTEUR = 10;

    public static void main(String[] args) throws InterruptedException {

        System.out.println("===== scenario equilibre  =====");
        testerScenario(2, 3, 20);

        System.out.println("\n===== scenario desequilibre =====");
        testerScenario(5, 2, 50);
    }

    private static void testerScenario(
            int nbProducteurs,
            int nbConsommateurs,
            int totalProduits) throws InterruptedException {

        ProducteurConsommateur pc = new ProducteurConsommateur();

        Thread[] producteurs = new Thread[nbProducteurs];
        Thread[] consommateurs = new Thread[nbConsommateurs];

        // répartir exactement la consommation totale
        int base = totalProduits / nbConsommateurs;
        int reste = totalProduits % nbConsommateurs;

        for (int i = 0; i < nbConsommateurs; i++) {
            int quantite = base + (i < reste ? 1 : 0);
            consommateurs[i] = new Thread(new Consommateur(pc, quantite),"Consommateur-" + (i + 1));
            consommateurs[i].start();
        }

        for (int i = 0; i < nbProducteurs; i++) {
            producteurs[i] = new Thread(new Producteur(pc, NB_PRODUITS_PAR_PRODUCTEUR),"Producteur-" + (i + 1));
            producteurs[i].start();
        }

        // monitoring périodique
        boolean terminer;
        do {
            terminer = true;

            for (Thread t : producteurs) {
                if (t.isAlive()) {
                    terminer = false;
                }
            }
            for (Thread t : consommateurs) {
                if (t.isAlive()) {
                    terminer = false;
                }
            }

            System.out.println("\n--- État du buffer ---");
            System.out.println("Produits fabriqués : " + pc.getNbProduitsFabriques());
            System.out.println("Produits consommés : " + pc.getNbProduitsConsommes());
            System.out.println("Contenu : " + java.util.Arrays.toString(pc.getBuffer()));

            if (!terminer) {
                Thread.sleep(1000);
            }

        } while (!terminer);

        // attendre la fin de tous les threads et arreter
        for (Thread t : producteurs) {
            t.join();
        }
        for (Thread t : consommateurs) {
            t.join();
        }

        int fabriques = pc.getNbProduitsFabriques();
        int consommes = pc.getNbProduitsConsommes();

        System.out.println("\n--- Bilan ---");
        System.out.println("Fabriqués : " + fabriques);
        System.out.println("Consommés : " + consommes);
        System.out.println("Buffer : "+ java.util.Arrays.toString(pc.getBuffer()));

        if (fabriques == totalProduits && consommes == totalProduits) {
            System.out.println("TEST RÉUSSI : tous les produits "+ "ont été consommés.");
        } else {
            System.out.println("TEST ÉCHOUÉ : incohérence détectée.");
        }
    }
}
