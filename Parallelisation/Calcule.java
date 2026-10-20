/*
 * Calcule.java                                 14/10/2026
 * IUT de Rodez aucun copyright
 */

public class Calcule extends Thread {

    private int multiplicateur;
    private int valeur;
    private int index;
    private long resultat;

    public Calcule(int valeur, int multiplicateur, int index) {
        this.multiplicateur = multiplicateur;
        this.valeur = valeur;
        this.index = index;
        this.resultat = 0;
    }

    @Override
    public void run() {
        for (int j = 0; j < multiplicateur; j++) {
            resultat += valeur * valeur + valeur;
        }

        System.out.println("Thread " + index
                + " : traitement de " + valeur
                + " terminé : " + resultat);
    }

    public long getResultat() {
        return resultat;
    }
}