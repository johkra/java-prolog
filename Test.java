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

		Fact cat = new Fact("cat", head);
		Fact cat2 = new Fact("cat", head3);
		Fact mouse = new Fact("mouse", head4);

		Rules rules = new Rules();
		rules.addRule(cat);
		rules.addRule(mouse);

		test(cat, head);
		test(cat2, head);
		test(cat, head2);
		test(cat2, head2);
		test(cat, head3);
		test(cat2, head3);
		test(cat, head4);
		test(mouse, head4);
		test(mouse, head);
	}

	public static void test(Fact f, List<Term> head) {
		Rules rules = new Rules();
		System.out.println("Is " + head.get(0).getName() + " a " + f.getName() + " ?- " +
				rules.is(f, head));
	}
}
