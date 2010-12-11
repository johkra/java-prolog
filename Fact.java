import java.util.List;
import java.util.ArrayList;

public class Fact {
	private List<Term> head;
	private List<Term> body;
	private String name;

	public List<Term> getHead() {
		return this.head;
	}

	public List<Term> getBody() {
		return this.body;
	}

	public String getName() {
		return this.name;
	}

	public Fact(String name, List<Term> head, List<Term> body) {
		this.name = name;
		this.head = head;
		this.body = body;
	}

	public Fact(String name, List<Term> head) {
		this.name = name;
		this.head = head;
		this.body = new ArrayList<Term>();
		this.body.add( new Atom("true") );
	}

	public boolean equals(Object o) {
		return (o instanceof Fact) &&
			((Fact)o).getName().equals(this.name);
	}
}
