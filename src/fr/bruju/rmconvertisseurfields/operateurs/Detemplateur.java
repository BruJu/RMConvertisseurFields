package fr.bruju.rmconvertisseurfields.operateurs;

import fr.bruju.util.table.Enregistrement;

import java.util.function.Consumer;

public abstract class Detemplateur implements Consumer<Enregistrement> {
	private final String template;

	public Detemplateur(String template) {
		this.template = template + "<";
	}

	@Override
	public void accept(Enregistrement enregistrement) {
		String type = enregistrement.get("Type");

		if (type.startsWith(template) && type.endsWith(">")) {
			enregistrement.set("Type", type.substring(template.length(), type.length() - 1));
			actionDeTemplatage(enregistrement);
		}
	}

	protected abstract void actionDeTemplatage(Enregistrement enregistrement);


	public static class ExtracteurDeDisposition extends Detemplateur {
		private final String template;

		public ExtracteurDeDisposition(String template) {
			super(template);
			this.template = template;
		}

		protected final void actionDeTemplatage(Enregistrement enregistrement) {
			enregistrement.set("Disposition", template);
		}
	}

	public static class ForceurDeType extends Detemplateur {
		private final String typeForce;

		public ForceurDeType(String template, String typeForce) {
			super(template);
			this.typeForce = typeForce;
		}

		@Override
		protected void actionDeTemplatage(Enregistrement enregistrement) {
			enregistrement.set("Type", typeForce);
		}
	}

	public static class Dereferenceur extends Detemplateur {
		public Dereferenceur() {
			super("Ref");
		}

		@Override
		protected void actionDeTemplatage(Enregistrement enregistrement) {
			String type = enregistrement.get("Type");
			int position = type.indexOf(":");

			if (position == -1) {
				enregistrement.set("Type", "Int32");
			} else {
				enregistrement.set("Type", type.substring(position + 1));
			}
		}
	}
}
