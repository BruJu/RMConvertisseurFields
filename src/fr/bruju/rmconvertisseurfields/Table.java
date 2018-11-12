package fr.bruju.rmconvertisseurfields;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class Table {
	private List<Contenu> contenus;

	public Table() {
		this.contenus = new ArrayList<>();
	}

	public void ajouterContenu(Contenu contenu) {
		contenus.add(contenu);
	}

	public void transformerContenu(Consumer<Contenu> modificateur) {
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

		for (Contenu contenu : contenus) {
			sj.add(contenu.serialiser());
		}

		return sj.toString();
	}
}
