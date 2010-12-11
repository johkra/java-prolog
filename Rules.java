import java.util.List;
import java.util.ArrayList;

public class Rules {
	private static List<Fact> rules = new ArrayList<Fact>();

	public Rules() {
	}

	public void addRule(Fact f) {
		rules.add(f);
	}

	public Boolean is(Fact f, List<Term> head) {
		for(Fact rule: rules) {
			if ((rule.equals(f)) && (f.getHead().equals(head))) {
				return true;
			}
		}
		return false;
	}

}
