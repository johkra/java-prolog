import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Johannes Krampf <johkra@gmail.com>
 * Date: 06.02.11
 */
public class Rule {
    private Term head;
    private ArrayList<Term> goals;

    public Rule(String rule) throws ParseException {
        String[] flds = rule.split(":-");
        head = new Term(flds[0]);
        goals = new ArrayList<Term>();

        if (flds.length == 2) {
            flds = flds[1].replace("),", ");").split(";");
            for (String fld : flds) {
                goals.add(new Term(fld));
            }
        }
    }

    private Rule() {

    }

    public Term getHead() {
        return head;
    }

    public List<Term> getGoals() {
        return goals;
    }

    public void setGoals(ArrayList<Term> goals) {
        this.goals = goals;
    }

    public Rule clone() {
        Rule clone = new Rule();
        clone.head = head;
        clone.goals = new ArrayList<Term>(goals);
        return clone;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "head=" + head +
                ", goals=" + goals +
                '}';
    }
}
