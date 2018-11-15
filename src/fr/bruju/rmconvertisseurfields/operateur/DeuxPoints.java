package fr.bruju.rmconvertisseurfields.operateur;

import fr.bruju.rmconvertisseurfields.Contenu;

public class DeuxPoints implements Operateur {
	private int type;

	@Override
	public void lireHeader(Contenu contenu) {
		type = contenu.getPositionChamp("Type");
	}

	@Override
	public void appliquerAContenu(Contenu contenu) {
		String contenuType = contenu.getDonnee(type);

		int positionDeuxPoints = contenuType.indexOf(":");

		if (positionDeuxPoints != -1) {
			contenuType = contenuType.substring(positionDeuxPoints + 1);
			contenu.remplacerDonnee(type, contenuType);
		}
	}
}
