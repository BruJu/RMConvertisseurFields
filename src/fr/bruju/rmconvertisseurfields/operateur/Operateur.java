package fr.bruju.rmconvertisseurfields.operateur;

import fr.bruju.rmconvertisseurfields.Contenu;

public interface Operateur {

	public void lireHeader(Contenu contenu);

	public void appliquerAContenu(Contenu contenu);
}
