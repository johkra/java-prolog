public class Atom implements Term {
	private String name;

	public String getName() {
		return this.name;
	}

	public Atom(String name) {
		this.name = name;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Atom)) {
			return false;
		}
		Atom a = (Atom) o;

		return a.getName().equals(this.name);
	}
}
