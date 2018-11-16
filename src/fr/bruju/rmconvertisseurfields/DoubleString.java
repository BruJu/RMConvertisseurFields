package fr.bruju.rmconvertisseurfields;

import java.util.Map;
import java.util.Objects;

public class DoubleString {
	public final String a;
	public final String b;

	public DoubleString(String a, String b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DoubleString that = (DoubleString) o;
		return Objects.equals(a, that.a) &&
				Objects.equals(b, that.b);
	}

	@Override
	public int hashCode() {
		return Objects.hash(a, b);
	}


	public static void ajouter(Map<DoubleString, DoubleString> map,
							   String acle, String bcle, String avaleur, String bvaleur) {
		map.put(new DoubleString(acle, bcle), new DoubleString(avaleur, bvaleur));
	}
}
