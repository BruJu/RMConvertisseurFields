package fr.bruju.rmconvertisseurfields.operateurs;

import fr.bruju.util.table.Contenu;

import java.util.function.Function;

public class NumeroteurDEntrees implements Function<Contenu, Object> {
	private int idLigne = 1;

	@Override
	public String apply(Contenu contenu) {
		return Integer.toString(idLigne++);
	}
}
