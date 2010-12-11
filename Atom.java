public class Atom implements Term {
	private String name;

	public String getName() {
		return this.name;
	}

	public Atom(String name) {
		this.name = name;
	}

	public boolean equals(Object o) {
		return (o instanceof Atom) && 
			((Atom)o).getName().equals(this.name);
	}
}
