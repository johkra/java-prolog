public class Variable implements Term {
	private String name;

	public String getName() {
		return this.name;
	}

	public Variable(String name) {
		this.name = name;
	}

	public boolean equals(Object o) {
		return (o instanceof Variable) &&
			((Variable)o).getName().equals(this.name);
	}
}
