import java.util.List;
import java.util.ArrayList;

public class Fact {
	private List<Term> head;
	private List<Term> body;

	public List<Term> getHead() {
		return this.head;
	}

	public List<Term> getBody() {
		return this.body;
	}

	public Fact(List<Term> head, List<Term> body) {
		this.head = head;
		this.body = body;
	}

	public Fact(List<Term> head) {
		this.head = head;
		this.body = new ArrayList<Term>();
		this.body.add( new Atom("true") );
	}
}
