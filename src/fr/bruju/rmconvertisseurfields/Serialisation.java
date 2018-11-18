package fr.bruju.rmconvertisseurfields;

import fr.bruju.util.table.Table;

import java.util.List;
import java.util.StringJoiner;

public class Serialisation {
	public static String serialiserTable(Table table) {
		StringJoiner sb = new StringJoiner("\n", "#", "");

		sb.add(serialiserColonnes(table.getColonnes()));

		table.forEach(contenu -> {
			StringJoiner sj = new StringJoiner(",");
			contenu.reconstruireObjet((champ, objet) -> {
				sj.add((String) objet);
			});

			sb.add(sj.toString());
		});

		return sb.toString();
	}


	public static String serialiserColonnes(List<String> colonnes) {
		StringJoiner sj = new StringJoiner(",");

		for (String colonne : colonnes) {
			sj.add(colonne);
		}

		return sj.toString();
	}
}
