package fr.bruju.rmconvertisseurfields.operateur;

import fr.bruju.rmconvertisseurfields.Contenu;
import fr.bruju.rmconvertisseurfields.DoubleString;
import fr.bruju.rmconvertisseurfields.operateur.Operateur;

import java.util.Map;

public class Remplaceur implements Operateur {
	private final Map<DoubleString, DoubleString> substitutions;
	private int posStructure;
	private int posField;
	private int posType;
	private int posDisposition;

	public Remplaceur(Map<DoubleString, DoubleString> substitutions) {
		this.substitutions = substitutions;
	}

	@Override
	public void lireHeader(Contenu contenu) {
		posStructure = contenu.getPositionChamp("Structure");
		posField = contenu.getPositionChamp("Field");
		posType = contenu.getPositionChamp("Type");
		posDisposition = contenu.getPositionChamp("Disposition");
	}

	@Override
	public void appliquerAContenu(Contenu contenu) {
		String structure = contenu.getDonnee(posStructure);
		String field = contenu.getDonnee(posField);

		DoubleString cle = new DoubleString(structure, field);
		DoubleString substitution = substitutions.remove(cle);

		if (substitution != null) {
			contenu.remplacerDonnee(posType, substitution.a);
			contenu.remplacerDonnee(posDisposition, substitution.b);
		}
	}
}
