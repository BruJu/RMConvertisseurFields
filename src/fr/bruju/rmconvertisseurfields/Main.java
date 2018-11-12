package fr.bruju.rmconvertisseurfields;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {
	public static final String CHEMIN_SOURCE = "A:\\fields.csv";
	public static final String CHEMIN_DESTINATION = "A:\\javalcfreaderfields.csv";


	public static void main(String[] args) {
		Table table = new Table();

		try (Stream<String> flux = Files.lines(Paths.get(CHEMIN_SOURCE))) {
			flux.forEach(ligne -> {
				Contenu contenu = creerContenu(ligne);

				if (contenu != null) {
					table.ajouterContenu(contenu);
				}
			});
		} catch (IOException e) {
			System.exit(1);
		}

		table.transformerContenu(Main::enleverPersistEtis2k3);
		table.transformerContenu(Main::ajouterUnChampAuDebut);
		table.transformerContenu(Main::transformerSizeField);
		table.transformerContenu(Main::creerDisposition);
		table.transformerContenu(Main::retirerDefautRm2k);



		table.ecrire(CHEMIN_DESTINATION);
	}

	private static void enleverPersistEtis2k3(Contenu contenu) {
		contenu.getDonnees().remove(6);
		contenu.getDonnees().remove(6);
	}

	private static void ajouterUnChampAuDebut(Contenu contenu) {
		contenu.getDonnees().add(0, Integer.toString(contenu.getId()));
	}

	private static void transformerSizeField(Contenu contenu) {
		boolean estSizeField = contenu.getDonnees().remove(3).equals("t");

		if (estSizeField) {
			contenu.getDonnees().set(3, "SizeField");
		}
	}


	private static void retirerDefautRm2k(Contenu contenu) {
		String defaut = contenu.getDonnees().get(6);

		if (defaut.contains("|")) {
			defaut = defaut.split("\\|")[1];
			contenu.getDonnees().set(6, defaut);
		}

	}

	private static void creerDisposition(Contenu contenu) {
		contenu.getDonnees().add(4, "");
	}

	private static Contenu creerContenu(String ligne) {
		/*
		if (ligne.startsWith("#")) {
			return null;
		}

*/
		String[] champs = ligne.split(",");

		List<String> donnees = new ArrayList<>();

		for (String champ : champs) {
			donnees.add(champ);
		}

		return new Contenu(donnees);
	}
}
