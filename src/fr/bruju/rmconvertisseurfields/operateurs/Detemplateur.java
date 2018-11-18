package fr.bruju.rmconvertisseurfields.operateurs;

import fr.bruju.util.table.Contenu;

import java.util.function.Consumer;

public abstract class Detemplateur implements Consumer<Contenu> {
	private final String template;

	public Detemplateur(String template) {
		this.template = template + "<";
	}

	@Override
	public void accept(Contenu contenu) {
		String type = contenu.get("Type");

		if (type.startsWith(template) && type.endsWith(">")) {
			contenu.set("Type", type.substring(template.length(), type.length() - 1));
			actionDeTemplatage(contenu);
		}
	}

	protected abstract void actionDeTemplatage(Contenu contenu);


	public static class ExtracteurDeDisposition extends Detemplateur {
		private final String template;

		public ExtracteurDeDisposition(String template) {
			super(template);
			this.template = template;
		}

		protected final void actionDeTemplatage(Contenu contenu) {
			contenu.set("Disposition", template);
		}
	}

	public static class ForceurDeType extends Detemplateur {
		private final String typeForce;

		public ForceurDeType(String template, String typeForce) {
			super(template);
			this.typeForce = typeForce;
		}

		@Override
		protected void actionDeTemplatage(Contenu contenu) {
			contenu.set("Type", typeForce);
		}
	}

	public static class Dereferenceur extends Detemplateur {
		public Dereferenceur() {
			super("Ref");
		}

		@Override
		protected void actionDeTemplatage(Contenu contenu) {
			String type = contenu.get("Type");
			int position = type.indexOf(":");

			if (position == -1) {
				contenu.set("Type", "Int32");
			} else {
				contenu.set("Type", type.substring(position + 1));
			}
		}
	}
}
