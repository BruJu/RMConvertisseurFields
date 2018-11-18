package fr.bruju.rmconvertisseurfields.operateurs;

import fr.bruju.util.table.Enregistrement;

import java.util.function.Function;

public class NumeroteurDEntrees implements Function<Enregistrement, Object> {
	private int idLigne = 1;

	@Override
	public String apply(Enregistrement enregistrement) {
		return Integer.toString(idLigne++);
	}
}
