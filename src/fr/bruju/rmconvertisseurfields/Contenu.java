package fr.bruju.rmconvertisseurfields;

import java.util.List;
import java.util.StringJoiner;

public class Contenu {
	private static int compteur = 0;

	private final List<String> donnees;
	private final int id;
	private final boolean estUnCommentaire;
	
	public String getDonnee(int numero) {
		return donnees.get(numero);
	}
	
	public void remplacerDonnee(int numero, String valeur) {
		donnees.set(numero, valeur);
	}
	
	public void retirerDonnee(int numero) {
		donnees.remove(numero);
	}

	public Contenu(List<String> donnees) {
		if (donnees.get(0).startsWith("#")) {
			estUnCommentaire = true;
			donnees.set(0, donnees.get(0).substring(1));
		} else {
			estUnCommentaire = false;
		}

		this.donnees = donnees;
		id = compteur++;
	}

	public List<String> getDonnees() {
		return donnees;
	}

	public String serialiser() {
		StringJoiner sj;
		if (estUnCommentaire) {
			sj = new StringJoiner(",", "#", "");
		} else {
			sj = new StringJoiner(",");
		}

		for (String donnee : donnees) {
			sj.add(donnee);
		}
		return sj.toString();
	}

	public int getId() {
		return id;
	}

	public boolean estUnHeader() {
		return estUnCommentaire;
	}

	public int getPositionChamp(String nomChamp) {
		for (int i = 0 ; i != donnees.size() ; i++) {
			if (donnees.get(i).equals(nomChamp)) {
				return i;
			}
		}
		
		return -1;
	}

	public void inserer(int idCase, String contenu) {
		donnees.add(idCase, contenu);
	}

	public int nombreDeColonnes() {
		return donnees.size();
	}
}
