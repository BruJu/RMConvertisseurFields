package fr.bruju.rmconvertisseurfields;

import fr.bruju.util.table.Enregistrement;

import java.util.function.Consumer;

public class TransformeurVersSizeField implements Consumer<Enregistrement> {
	@Override
	public void accept(Enregistrement enregistrement) {
		boolean estSizeField = enregistrement.get("Size Field?").equals("t");

		if (estSizeField) {
			enregistrement.set("Type", "SizeField");
		}
	}
}
