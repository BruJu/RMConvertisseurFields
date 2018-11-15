package fr.bruju.rmconvertisseurfields.operateur;

import fr.bruju.rmconvertisseurfields.Contenu;

public class Detemplateur implements Operateur {
	private int colonneType;
	private int colonneDisposition;
	private String typeTemplateur;
	private M m;

	public Detemplateur(String typeTemplateur, M m) {
		this.typeTemplateur = typeTemplateur;
		this.m = m;
	}

	@Override
	public void lireHeader(Contenu contenu) {
		colonneType = contenu.getPositionChamp("Type");
		colonneDisposition = contenu.getPositionChamp("Disposition");
	}

	@Override
	public void appliquerAContenu(Contenu contenu) {
		String type = contenu.getDonnee(colonneType);

		if (!type.startsWith(typeTemplateur + "<") || !type.endsWith(">")) {
			return;
		}

		Doublon doublon = new Doublon(type.substring(typeTemplateur.length() + 1, type.length() - 1),
				contenu.getDonnee(colonneDisposition));

		m.m(doublon);

		contenu.remplacerDonnee(colonneType, doublon.type);
		contenu.remplacerDonnee(colonneDisposition, doublon.disposition);
	}


	public static class Doublon {
		public String type;
		public String disposition;

		public Doublon(String type, String disposition) {
			this.type = type;
			this.disposition = disposition;
		}
	}

	public interface M {
		public void m(Doublon doublon);
	}
}
