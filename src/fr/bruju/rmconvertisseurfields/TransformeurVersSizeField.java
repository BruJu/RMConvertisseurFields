package fr.bruju.rmconvertisseurfields;

import fr.bruju.util.table.Contenu;

import java.util.function.Consumer;

public class TransformeurVersSizeField implements Consumer<Contenu> {
	@Override
	public void accept(Contenu contenu) {
		boolean estSizeField = contenu.get("Size Field?").equals("t");

		if (estSizeField) {
			contenu.set("Type", "SizeField");
		}
	}
}
