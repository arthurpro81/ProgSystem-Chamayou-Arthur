/*
 * ProducteurConsommateur.java                                      22/10/2026
 * IUT de Rodez aucun copyright
 */

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class ProducteurConsommateur {

    private static final int TAILLE_BUFFER = 5;
    private final Integer[] buffer = new Integer[TAILLE_BUFFER];

    private final Semaphore placesLibres = new Semaphore(TAILLE_BUFFER);
    private final Semaphore elementsDisponibles = new Semaphore(0);
    private final Semaphore mutexBuffer = new Semaphore(1);

    private int debutLecture = 0;
    private int debutEcriture = 0;
    private int nbProduitsFabriques = 0;
    private int nbProduitsConsommes = 0;

    public void insererProduit(int produit) throws InterruptedException {

        placesLibres.acquire();
        try {
            mutexBuffer.acquire();
        } catch (InterruptedException e) {
            placesLibres.release();
            throw e;
        }

        try {
            buffer[debutEcriture] = produit;
            debutEcriture = (debutEcriture + 1) % TAILLE_BUFFER;
            nbProduitsFabriques++;

            System.out.println("Produit inséré : " + produit);
            System.out.println("Buffer : "
                    + Arrays.toString(buffer));
        } finally {
            mutexBuffer.release();
        }

        elementsDisponibles.release();
    }

    public Integer retirerProduit() throws InterruptedException {

        elementsDisponibles.acquire();
        try {
            mutexBuffer.acquire();
        } catch (InterruptedException e) {
            elementsDisponibles.release();
            throw e;
        }

        Integer valeur;

        try {
            valeur = buffer[debutLecture];
            buffer[debutLecture] = null;
            debutLecture = (debutLecture + 1) % TAILLE_BUFFER;
            nbProduitsConsommes++;

            System.out.println("Produit consommé : " + valeur);
            System.out.println("Buffer : "
                    + Arrays.toString(buffer));
        } finally {
            mutexBuffer.release();
        }

        placesLibres.release();
        return valeur;
    }

    public int getNbProduitsFabriques() {
        return nbProduitsFabriques;
    }

    public int getNbProduitsConsommes() {
        return nbProduitsConsommes;
    }

    public int getTeteLecture() {
        return debutLecture;
    }

    public int getTeteEcriture() {
        return debutEcriture;
    }

    public Integer[] getBuffer() throws InterruptedException {
        mutexBuffer.acquire();
        try {
            return Arrays.copyOf(buffer, buffer.length);
        } finally {
            mutexBuffer.release();
        }
    }
}

/*
 * Producteur.java                                      23/10/2026
 * IUT de Rodez aucun copyright
 */

class Producteur implements Runnable {

    private final ProducteurConsommateur pc;
    private final int nbProduits;

    public Producteur(ProducteurConsommateur pc, int nbProduits) {
        this.pc = pc;
        this.nbProduits = nbProduits;
    }

    @Override
    public void run() {
        for (int i = 0; i < nbProduits; i++) {
            int produit = genererProduit();

            try {
                // attente d'une place libre
                // accès au buffer
                // ajout de l'élément au buffer
                pc.insererProduit(produit);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private int genererProduit() {
        return (int) (Math.random() * 100);
    }
}

/*
 * Consommateur.java                                      23/10/2026
 * IUT de Rodez aucun copyright
 */

class Consommateur implements Runnable {

    private final ProducteurConsommateur pc;
    private final int nbProduits;

    public Consommateur(ProducteurConsommateur pc, int nbProduits) {
        this.pc = pc;
        this.nbProduits = nbProduits;
    }

    @Override
    public void run() {
        for (int i = 0; i < nbProduits; i++) {
            try {
                // attente d'une place libre
                // accès au buffer
                // retirer l'élément du buffer
                Integer produit = pc.retirerProduit();
                System.out.println("Consommateur : produit reçu = " + produit);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}