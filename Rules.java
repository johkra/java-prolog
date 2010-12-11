import java.util.List;
import java.util.ArrayList;

public class Rules {
	private static List<Fact> rules = new ArrayList<Fact>();

	public Rules() {
	}

	public void addRule(Fact f) {
		rules.add(f);
	}

	public boolean is(Fact f, List<Term> head) {
		boolean result = false;
		for(Fact rule: rules) {
			if ((rule.equals(f)) && (f.getHead().equals(head))) {
				Term first = f.getHead().get(0);
				if (first instanceof Variable) {
					System.out.println(first.getName() + " = " + rule.getHead().get(0).getName());
				}
				result = true;
			}
		}
		return result;
	}

}
