/*
 * Calcule.java                                 14/10/2026
 * IUT de Rodez aucun copyright
 */

public class Calcule extends Thread {
    
	int multiplicateur;
	int valeur;
	int index;
	int resultat;
	
	private static final int MULTIPLICATEUR = 1000;
	
	public Calcule(int valeur,int multiplicateur,int index) {
		this.multiplicateur=multiplicateur;
		this.valeur=valeur;
		this.index=index;
		this.resultat=0;
	}
	
	@Override
	public void run() {
		for (int j = 0; j < MULTIPLICATEUR; j++) {
            resultat += valeur * valeur + valeur;
            // on pourrais aussi mettre un sleep
        }
	}
	
	public int getResultat() {
		return resultat;
	}
}