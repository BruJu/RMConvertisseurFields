package fr.bruju.rmconvertisseurfields;

import fr.bruju.util.table.Contenu;
import fr.bruju.util.table.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Main {
	public static final String CHEMIN_SOURCE = "A:\\fields.csv";
	public static final String CHEMIN_DESTINATION = "A:\\javalcfreaderfields.csv";

	public static void main(String[] args) {
		Table table = new Table(new ArrayList<>());
		remplirTable(table, CHEMIN_SOURCE);
		appliquerChaineDeTraitements(table);
		ecrireTable(table, CHEMIN_DESTINATION);
	}


	private static void remplirTable(Table table, String cheminSource) {
		try (Stream<String> flux = Files.lines(Paths.get(cheminSource))) {

			AtomicBoolean booleen = new AtomicBoolean();
			booleen.set(false);

			flux.forEach(ligne -> {
				String[] valeurs = ligne.split(",", -1);
				if (valeurs[0].startsWith("#")) {
					valeurs[0] = valeurs[0].substring(1);
				}

				List<String> valeursListees = new ArrayList<>();

				for (String valeur : valeurs) {
					valeursListees.add(valeur);
				}

				if (!booleen.getAndSet(true)) {
					table.ajouterChamps(valeursListees);
				} else {
					table.ajouterContenu(valeursListees);
				}
			});
		} catch (IOException e) {
			System.exit(1);
		}
	}

	private static void appliquerChaineDeTraitements(Table table) {
		table.retirerChamp("PersistIfDefault");
		table.retirerChamp("Is2k3");
		table.insererChamp(0, "Ligne", new Function<Contenu, Object>() {
			private int idLigne = 1;

			@Override
			public Object apply(Contenu contenu) {
				return Integer.toString(idLigne++);
			}
		});

		table.forEach(contenu -> {
			boolean estSizeField = contenu.get("Size Field?").equals("t");

			if (estSizeField) {
				contenu.set("Type", "SizeField");
			}
		});

		table.retirerChamp("Size Field?");

		table.transformerUnChamp("Default Value", Main::retirerDefautRm2k);

		table.insererChamp(table.getPosition("Type") + 1, "Disposition", c -> "");

		table.forEach(disposeur("Vector"));
		table.forEach(disposeur("Array"));
		table.forEach(chaineurParPrefixe("Enum", contenu -> contenu.set("Type", "Int32")));


		table.forEach(chaineurParPrefixe("Ref", Main::appliquerTransformationDeRef));
		table.forEach(Main::transformerDeuxPoints);
		table.forEach(chaineurParPrefixe("Ref", Main::appliquerTransformationDeRef));

		table.transformerUnChamp("Type", chaine -> chaine.equals("ItemAnimation:Ref<Actor>") ? "Int32" : chaine);

		corrigerTable(table);
	}

	private static void transformerDeuxPoints(Contenu contenu) {
		String type = contenu.get("Type");
		int position = type.indexOf(":");

		if (position != -1) {
			contenu.set("Type", type.substring(position + 1));
		}
	}

	private static void appliquerTransformationDeRef(Contenu contenu) {
		String type = contenu.get("Type");
		int position = type.indexOf(":");

		if (position == -1) {
			contenu.set("Type", "Int32");
		} else {
			contenu.set("Type", type.substring(position + 1));
		}
	}

	private static void ecrireTable(Table table, String cheminDestination) {
		File f = new File(cheminDestination);
		String s = serialiser(table);

		try {
			f.createNewFile();
			FileWriter ff = new FileWriter(f);
			ff.write(s);
			ff.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String serialiser(Table table) {
		StringJoiner sb = new StringJoiner("\n", "#", "");

		sb.add(serialiser(table.getColonnes()));

		table.forEach(contenu -> {
			StringJoiner sj = new StringJoiner(",");
			contenu.reconstruireObjet((champ, objet) -> {
				sj.add((String) objet);
			});

			sb.add(sj.toString());
		});

		return sb.toString();
	}

	private static String serialiser(List<String> colonnes) {
		StringJoiner sj = new StringJoiner(",");

		for (String colonne : colonnes) {
			sj.add(colonne);
		}

		return sj.toString();
	}


	private static Object retirerDefautRm2k(Object defaut) {
		String defautStr = (String) defaut;

		if (defautStr.contains("|")) {
			defautStr = defautStr.split("\\|")[1];
		}

		return defautStr;
	}


	private static Consumer<Contenu> chaineurParPrefixe(String prefixe, Consumer<Contenu> consumer) {
		return contenu -> {
			String type = contenu.get("Type");

			if (type.startsWith(prefixe + "<") && type.endsWith(">")) {
				contenu.set("Type", type.substring(prefixe.length() + 1, type.length() - 1));
				consumer.accept(contenu);
			}
		};
	}

	private static Consumer<Contenu> disposeur(String prefixe) {
		return chaineurParPrefixe(prefixe, contenu -> contenu.set("Disposition", prefixe));
	}

	private static void corrigerTable(Table table) {
		Map<DoubleString, DoubleString> substitutions = new HashMap<>();


		DoubleString.ajouter(substitutions, "Actor", "battle_commands", "UInt32", "Tuple_7");
		DoubleString.ajouter(substitutions, "Class", "battle_commands", "UInt32", "Tuple_7");
		DoubleString.ajouter(substitutions, "SaveActor", "battle_commands", "UInt32", "Tuple_7");
		DoubleString.ajouter(substitutions, "Database", "version", "Int32", "");
		DoubleString.ajouter(substitutions, "Database", "commoneventD2", "", "");
		DoubleString.ajouter(substitutions, "Database", "commoneventD3", "", "");
		DoubleString.ajouter(substitutions, "Database", "classD1", "", "");
		DoubleString.ajouter(substitutions, "MoveRoute", "move_commands", "MoveCommandSpecial", "");
		DoubleString.ajouter(substitutions, "SaveSystem", "variables", "Int32LittleEndian", "Vector");

		DoubleString.ajouter(substitutions, "EventCommand", "parameters", "Int32", "List");

		DoubleString.ajouter(substitutions, "Parameters", "maxhp", "Int16", "Tuple_99");
		DoubleString.ajouter(substitutions, "Parameters", "maxsp", "Int16", "Tuple_99");
		DoubleString.ajouter(substitutions, "Parameters", "attack", "Int16", "Tuple_99");
		DoubleString.ajouter(substitutions, "Parameters", "defense", "Int16", "Tuple_99");
		DoubleString.ajouter(substitutions, "Parameters", "spirit", "Int16", "Tuple_99");
		DoubleString.ajouter(substitutions, "Parameters", "agility", "Int16", "Tuple_99");

		DoubleString.ajouter(substitutions, "TreeMap", "tree_order", "Int32", "List");

		table.forEach(contenu -> {
			String type = contenu.get("Type");

			if (type.equals("SizeField")) {
				return;
			}

			String structure = contenu.get("Structure");
			String field = contenu.get("Field");

			DoubleString cle = new DoubleString(structure, field);
			DoubleString substitution = substitutions.remove(cle);

			if (substitution != null) {
				contenu.set("Type", substitution.a);
				contenu.set("Disposition", substitution.b);
			}
		});
	}
}