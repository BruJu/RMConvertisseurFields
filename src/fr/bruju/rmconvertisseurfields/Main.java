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



		table.ecrire(CHEMIN_DESTINATION);
	}

	private static Contenu creerContenu(String ligne) {
		if (ligne.startsWith("#")) {
			return null;
		}

		String[] champs = ligne.split(",");

		List<String> donnees = new ArrayList<>();

		for (String champ : champs) {
			donnees.add(champ);
		}

		return new Contenu(donnees);
	}
}
