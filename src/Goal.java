import java.util.HashMap;

/**
 * User: Johannes Krampf <johkra@gmail.com>
 * Date: 07.02.11
 */
public class Goal {
    private Rule rule;
    private Goal parent;
    private HashMap<String, String> env;
    private int inx;

    public Goal(Rule rule, Goal parent) {
        this.rule = rule;
        this.parent = parent;
        this.env = new HashMap<String, String>();
        this.inx = 0;
    }

    public Goal(Rule rule, Goal parent, HashMap<String, String> env) {
        this.rule = rule;
        this.parent = parent;
        this.env = env;
        this.inx = 0;
    }

    private Goal() {
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Goal getParent() {
        return parent;
    }

    public void setParent(Goal parent) {
        this.parent = parent;
    }

    public HashMap<String, String> getEnv() {
        return env;
    }

    public int getInx() {
        return inx;
    }

    public void setInx(int inx) {
        this.inx = inx;
    }

    public Goal clone() {
        Goal clone = new Goal();
        clone.rule = rule.clone();
        clone.parent = parent;
        clone.env = new HashMap<String, String>(env);
        clone.inx = inx;
        return clone;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "rule=" + rule +
                ", inx=" + inx +
                ", env=" + env +
                '}';
    }
}
