package fr.bruju.rmconvertisseurfields.operateur;

import fr.bruju.rmconvertisseurfields.Contenu;

public class IntegrationSizeField implements Operateur {
	private int sizeField;
	private int type;

	@Override
	public void lireHeader(Contenu contenu) {
		sizeField = contenu.getPositionChamp("Size Field?");
		type = contenu.getPositionChamp("Type");

		contenu.retirerDonnee(sizeField);
	}

	@Override
	public void appliquerAContenu(Contenu contenu) {
		String contenuSizeField = contenu.getDonnee(sizeField);

		if (contenuSizeField.equals("t")) {
			contenu.remplacerDonnee(type, "SizeField");
		}

		contenu.retirerDonnee(sizeField);
	}
}
