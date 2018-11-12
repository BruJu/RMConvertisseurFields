package fr.bruju.rmconvertisseurfields;

import java.util.List;
import java.util.StringJoiner;

public class Contenu {
	private final List<String> donnees;

	public Contenu(List<String> donnees) {
		this.donnees = donnees;
	}

	public List<String> getDonnees() {
		return donnees;
	}

	public String serialiser() {
		StringJoiner sj = new StringJoiner(",");
		for (String donnee : donnees) {
			sj.add(donnee);
		}
		return sj.toString();
	}
}
