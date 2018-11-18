package fr.bruju.rmconvertisseurfields;

import fr.bruju.rmconvertisseurfields.operateur.*;
import fr.bruju.util.table.Table_;

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
		Table_ table = new Table_(new ArrayList<>());
		remplirTable(table, CHEMIN_SOURCE);
		appliquerChaineDeTraitements(table);
		ecrireTable(table, CHEMIN_DESTINATION);
	}


	private static void remplirTable(Table_ table, String cheminSource) {
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

	private static void appliquerChaineDeTraitements(Table_ table) {
		//table.retirerChamp(table.getPosition("PersistIfDefault"));
		/*
		table.retirerChamp("Is2k3");
		table.ajouterLigne();
		table.appliquerOperateur(new IntegrationSizeField());
		table.insererChampApres("Type", "Disposition");
		table.modifierChamp("Default Value", Main::retirerDefautRm2k);

		table.appliquerOperateur(new Detemplateur("Vector", new DetemplateurDisposeur("Vector")));
		table.appliquerOperateur(new Detemplateur("Array", new DetemplateurDisposeur("Array")));

		table.appliquerOperateur(new Detemplateur("Enum", d -> { d.type = "Int32"; }));
		table.appliquerOperateur(new Detemplateur("Ref", new DetemplateurRef()));
		table.appliquerOperateur(new DeuxPoints());
		table.appliquerOperateur(new Detemplateur("Ref", new DetemplateurRef()));


		table.modifierChamp("Type", chaine -> chaine.equals("ItemAnimation:Ref<Actor>") ? "Int32" : chaine);

		corrigerTable(table);
		*/
	}

	private static void ecrireTable(Table_ table, String cheminDestination) {
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

	private static String serialiser(Table_ table) {
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


	// =================================================================================================================
	// =================================================================================================================
	// =================================================================================================================
	// =================================================================================================================
	// =================================================================================================================
	// =================================================================================================================
	// =================================================================================================================
	// =================================================================================================================









	public static void main2(String[] args) {
		Table table = new Table();
		remplirTable(table, CHEMIN_SOURCE);
		appliquerChaineDeTraitements(table);
		table.ecrire(CHEMIN_DESTINATION);
	}

	private static void remplirTable(Table table, String cheminSource) {
		try (Stream<String> flux = Files.lines(Paths.get(cheminSource))) {
			flux.forEach(ligne -> {
				Contenu contenu = creerContenu(ligne);

				if (contenu != null) {
					table.ajouterContenu(contenu);
				}
			});
		} catch (IOException e) {
			System.exit(1);
		}
	}

	private static void appliquerChaineDeTraitements(Table table) {
		table.retirerChamp("PersistIfDefault");
		table.retirerChamp("Is2k3");
		table.ajouterLigne();
		table.appliquerOperateur(new IntegrationSizeField());
		table.insererChampApres("Type", "Disposition");
		table.modifierChamp("Default Value", Main::retirerDefautRm2k);

		table.appliquerOperateur(new Detemplateur("Vector", new DetemplateurDisposeur("Vector")));
		table.appliquerOperateur(new Detemplateur("Array", new DetemplateurDisposeur("Array")));

		table.appliquerOperateur(new Detemplateur("Enum", d -> { d.type = "Int32"; }));
		table.appliquerOperateur(new Detemplateur("Ref", new DetemplateurRef()));
		table.appliquerOperateur(new DeuxPoints());
		table.appliquerOperateur(new Detemplateur("Ref", new DetemplateurRef()));


		table.modifierChamp("Type", chaine -> chaine.equals("ItemAnimation:Ref<Actor>") ? "Int32" : chaine);

		corrigerTable(table);
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

		table.appliquerOperateur(new Remplaceur(substitutions));
	}


	private static String retirerDefautRm2k(String defaut) {
		if (defaut.contains("|")) {
			defaut = defaut.split("\\|")[1];
		}

		return defaut;
	}


	private static Contenu creerContenu(String ligne) {
		String[] champs = ligne.split(",", -1);

		List<String> donnees = new ArrayList<>();

		for (String champ : champs) {
			donnees.add(champ);
		}

		return new Contenu(donnees);
	}
}
