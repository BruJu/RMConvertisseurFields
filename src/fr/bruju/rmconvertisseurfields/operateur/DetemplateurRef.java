package fr.bruju.rmconvertisseurfields.operateur;

public class DetemplateurRef implements Detemplateur.M {
	@Override
	public void m(Detemplateur.Doublon doublon) {
		int position = doublon.type.indexOf(":");

		if (position != -1) {
			doublon.type.substring(position + 1);
		} else {
			doublon.type = "Int32";
		}
	}
}
