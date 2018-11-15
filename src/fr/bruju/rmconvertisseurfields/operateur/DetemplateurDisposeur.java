package fr.bruju.rmconvertisseurfields.operateur;

public class DetemplateurDisposeur implements Detemplateur.M {
	public String nomDisposition;

	public DetemplateurDisposeur(String nomDisposition) {
		this.nomDisposition = nomDisposition;
	}

	@Override
	public void m(Detemplateur.Doublon doublon) {
		doublon.disposition = nomDisposition;
	}
}
