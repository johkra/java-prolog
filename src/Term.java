import java.text.ParseException;
import java.util.Arrays;

/**
 * User: Johannes Krampf <johkra@gmail.com>
 * Date: 06.02.11
 */

public class Term {
    private String pred;
    private String[] args;

    public Term(String term) throws ParseException {
        term = term.replace(" ", "");
        if (term.charAt(term.length() - 1) != ')') {
            throw new ParseException("Syntax error in term: '" + term + "' missing closing bracket", -1);

        }

        String[] flds = term.substring(0, term.length() - 1).split("\\(");
        if (flds.length != 2) {
            throw new ParseException("Syntax error in term: " + term, -1);
        }
        pred = flds[0];
        args = flds[1].split(",");
    }

    private Term() {
    }


    public String getPred() {
        return pred;
    }

    public String[] getArgs() {
        return args;
    }

    public Term clone() {
        Term clone = new Term();
        clone.pred = pred;
        clone.args = args.clone();
        return clone;
    }

    @Override
    public String toString() {
        return "Term{" +
                "pred='" + pred + '\'' +
                ", args=" + (args == null ? null : Arrays.asList(args)) +
                '}';
    }
}
