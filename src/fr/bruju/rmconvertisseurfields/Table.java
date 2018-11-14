package fr.bruju.rmconvertisseurfields;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class Table {
	private Contenu header;
	private List<Contenu> contenus;

	public Table() {
		this.contenus = new ArrayList<>();
	}
	

	public void ajouterContenu(Contenu contenu) {
		if (header == null && contenu.estUnHeader()) {
			header = contenu;
		} else {
			contenus.add(contenu);
		}
	}

	public void transformerContenu(Consumer<Contenu> modificateur) {
		modificateur.accept(header);
		contenus.forEach(modificateur::accept);
	}

	public void ecrire(String cheminDestination) {
		File f = new File(cheminDestination);
		String s = serialiser();

		try {
			f.createNewFile();
			FileWriter ff = new FileWriter(f);
			ff.write(s);
			ff.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String serialiser() {
		StringJoiner sj = new StringJoiner("\n");

		sj.add(header.serialiser());

		for (Contenu contenu : contenus) {
			sj.add(contenu.serialiser());
		}

		return sj.toString();
	}
	
	public int getNumeroChamp(String nomChamp) {
		return header.getPositionChamp(nomChamp);
	}
	
	
	public void modifierChamp(String nomChamp, UnaryOperator<String> fonctionDeRemplacement) {
		int idChamp = getNumeroChamp(nomChamp);
		
		for (Contenu contenu : contenus) {
			contenu.remplacerDonnee(idChamp, fonctionDeRemplacement.apply(contenu.getDonnee(idChamp)));
		}
	}


	public void retirerChamp(String nomChamp) {
		int idChamp = getNumeroChamp(nomChamp);

		header.retirerDonnee(idChamp);

		for (Contenu contenu : contenus) {
			contenu.retirerDonnee(idChamp);
		}
	}

	public void ajouterLigne() {
		header.inserer(0, "Ligne");

		for (Contenu contenu : contenus) {
			contenu.inserer(0, Integer.toString(contenu.getId()));
		}
	}

	public void insererChampApres(String colonneAvant, String nouvelleColonne) {
		int idChamp;

		if (colonneAvant == null) {
			idChamp = 0;
		} else {
			idChamp = getNumeroChamp(colonneAvant) + 1;
		}

		header.inserer(idChamp, nouvelleColonne);

		for (Contenu contenu : contenus) {
			contenu.inserer(idChamp, "");
		}
	}
}
