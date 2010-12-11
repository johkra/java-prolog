import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Test {
	public static List<Term> List(Term ... elements) {
		List<Term> list = new ArrayList<Term>();
		for(Term t: Arrays.asList(elements)) {
			list.add(t);
		}
		return list;
	}
	public static Atom atom(String name) {
		return new Atom(name);
	}
	public static Variable var(String name) {
		return new Variable(name);
	}

	public static void main(String[] args) {
		Fact cat = new Fact("cat", List(atom("Tom")) );
		Fact cat2 = new Fact("cat", List(atom("Tom")) );
		Fact mouse = new Fact("mouse", List(atom("Jerry")) );
		Fact mouse3 = new Fact("mouse", List(atom("Micky")) );

		Fact mouse2 = new Fact("mouse", List(var("X")) );
		Fact cat3 = new Fact("cat", List(var("Y")) );

		Rules rules = new Rules();
		rules.addRule(cat);
		rules.addRule(mouse);
		rules.addRule(mouse3);

		test(cat, List(atom("Tom")) );
		test(cat2, List(atom("Tom")) );
		test(cat, List(atom("Tom")) );
		test(cat2, List(atom("Tom")) );
		test(cat, List(atom("Tom")) );
		test(cat2, List(atom("Tom")) );
		test(cat, List(atom("Jerry")) );
		test(mouse, List(atom("Jerry")) );
		test(mouse, List(atom("Tom")) );

		rules.is(mouse2, List(var("X")) );
		rules.is(cat3, List(var("Y")) );
	}

	public static void test(Fact f, List<Term> head) {
		Rules rules = new Rules();
		System.out.println("Is " + head.get(0).getName() + " a " + f.getName() + " ?- " +
				rules.is(f, head));
	}
}
