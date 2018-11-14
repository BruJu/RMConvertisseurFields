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

		table.retirerChamp("PersistIfDefault");
		table.retirerChamp("Is2k3");
		table.ajouterLigne();


		table.transformerContenu(Main::transformerSizeField);

		table.insererChampApres("Type", "Disposition");
		table.modifierChamp("Default Value", Main::retirerDefautRm2k);

		table.transformerContenu(contenu -> disposer(contenu, "Vector", "Vector"));
		table.transformerContenu(contenu -> disposer(contenu, "Array", "Array"));

		table.transformerContenu(Main::enleverRef);
		table.transformerContenu(Main::enleverEnum);
		table.transformerContenu(contenu -> remplacer(contenu, "ItemAnimation:Ref<Actor>", "Int32"));



		table.ecrire(CHEMIN_DESTINATION);
	}

	private static void remplacer(Contenu contenu, String s, String s2) {
		if (contenu.getDonnees().get(3).equals(s)) {
			contenu.getDonnees().set(3, s2);
		}
	}

	private static void enleverEnum(Contenu contenu) {
		String nomType = contenu.getDonnees().get(3);

		if (!nomType.startsWith("Enum<") || !nomType.endsWith(">")) {
			return;
		}

		contenu.getDonnees().set(3, "Int32");
	}

	private static void enleverRef(Contenu contenu) {
		String nomType = contenu.getDonnees().get(3);

		if (!nomType.startsWith("Ref<") || !nomType.endsWith(">")) {
			return;
		}
		nomType = nomType.substring(4, nomType.length() - 1);

		if (nomType.contains(":")) {
			nomType = nomType.split(":")[1];
		} else {
			nomType = "Int32";
		}

		contenu.getDonnees().set(3, nomType);
	}

	private static void disposer(Contenu contenu, String typeCpp, String disposition) {
		String nomType = contenu.getDonnees().get(3);

		if (!nomType.startsWith(typeCpp + "<") || !nomType.endsWith(">")) {
			return;
		}

		nomType = nomType.substring(typeCpp.length() + 1, nomType.length() - 1);

		contenu.getDonnees().set(3, nomType);
		contenu.getDonnees().set(4, disposition);
	}

	private static void transformerSizeField(Contenu contenu) {
		boolean estSizeField = contenu.getDonnees().remove(3).equals("t");

		if (estSizeField) {
			contenu.getDonnees().set(3, "SizeField");
		}
	}


	private static String retirerDefautRm2k(String defaut) {
		if (defaut.contains("|")) {
			defaut = defaut.split("\\|")[1];
		}

		return defaut;
	}


	private static Contenu creerContenu(String ligne) {
		String[] champs = ligne.split(",");

		List<String> donnees = new ArrayList<>();

		for (String champ : champs) {
			donnees.add(champ);
		}

		return new Contenu(donnees);
	}
}
