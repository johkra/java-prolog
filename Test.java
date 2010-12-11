import java.util.List;
import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		Atom a = new Atom("Tom");
		List<Term> head = new ArrayList<Term>();
		head.add(a);
		List<Term> head2 = new ArrayList<Term>();
		head2.add(a);
		Atom a2 = new Atom("Tom");
		List<Term> head3 = new ArrayList<Term>();
		head3.add(a2);
		Atom a3 = new Atom("Jerry");
		List<Term> head4 = new ArrayList<Term>();
		head4.add(a3);

		Fact cat = new Fact(head);

		Rules rules = new Rules();
		rules.addRule(cat);
		System.out.println("Is Tom a cat ?- " + rules.is(cat, head));
		System.out.println("Is Tom a cat ?- " + rules.is(cat, head2));
		System.out.println("Is Tom a cat ?- " + rules.is(cat, head3));
		System.out.println("Is Jerry a cat ?- " + rules.is(cat, head4));
	}
}
