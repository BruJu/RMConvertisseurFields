package fr.bruju.rmconvertisseurfields;

import fr.bruju.rmconvertisseurfields.operateurs.Detemplateur;
import fr.bruju.rmconvertisseurfields.operateurs.NumeroteurDEntrees;
import fr.bruju.util.table.Enregistrement;
import fr.bruju.util.table.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class Main {
	public static final String CHEMIN_SOURCE = "A:\\fields.csv";
	public static final String CHEMIN_DESTINATION = "A:\\javalcfreaderfields.csv";

	public static void main(String[] args) {
		Table table = new Table();
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
		table.insererChamp(0, "Ligne", new NumeroteurDEntrees());
		table.forEach(new TransformeurVersSizeField());
		table.retirerChamp("Size Field?");
		table.transformerUnChamp("Default Value", Main::retirerDefautRm2k);
		table.insererChampApres("Type", "Disposition", c -> "");
		table.forEach(new Detemplateur.ExtracteurDeDisposition("Vector"));
		table.forEach(new Detemplateur.ExtracteurDeDisposition("Array"));
		table.forEach(new Detemplateur.ForceurDeType("Enum", "Int32"));
		table.forEach(new Detemplateur.Dereferenceur());
		table.forEach(Main::transformerDeuxPoints);
		table.forEach(new Detemplateur.Dereferenceur());
		table.transformerUnChamp("Type", chaine -> chaine.equals("ItemAnimation:Ref<Actor>") ? "Int32" : chaine);
		corrigerTable(table);
	}

	private static void transformerDeuxPoints(Enregistrement enregistrement) {
		String type = enregistrement.get("Type");
		int position = type.indexOf(":");

		if (position != -1) {
			enregistrement.set("Type", type.substring(position + 1));
		}
	}


	private static void ecrireTable(Table table, String cheminDestination) {
		File f = new File(cheminDestination);
		String s = Serialisation.serialiserTable(table);

		try {
			f.createNewFile();
			FileWriter ff = new FileWriter(f);
			ff.write(s);
			ff.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	private static Object retirerDefautRm2k(Object defaut) {
		String defautStr = (String) defaut;

		if (defautStr.contains("|")) {
			defautStr = defautStr.split("\\|")[1];
		}

		return defautStr;
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