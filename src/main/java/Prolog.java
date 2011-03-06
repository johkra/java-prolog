import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * User: Johannes Krampf <johkra@gmail.com>
 * Date: 07.02.11
 * <p/>
 * This is a translation of prolog1.py written by Chris Meyers under a copyleft license.
 * <p/>
 * See http://web.archive.org/web/20071014055005/ibiblio.org/obp/py4fun/prolog/prolog2.html for the code and explanations.
 */
public final class Prolog {
    private static ArrayList<Rule> rules = new ArrayList<Rule>();
    private static Boolean trace = false;
    private static String indent = "";

    private Prolog() {
    }

    public static void main(String[] args) {
        for (String file : args) {
            if (file.equals(".")) {
                System.exit(0);
            }
            try {
                procFile(new FileInputStream(file), null);
            } catch (FileNotFoundException e) {
                System.err.println("File '" + file + "' not found.");
            }
        }
        procFile(System.in, "? ");
    }

    public static void procFile(InputStream file, String prompt) {
        HashMap env = new HashMap<String, String>();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(file));
            if (prompt != null) {
                System.out.print(prompt);
                System.out.flush();
            }
            String line = in.readLine();
            while (line != null) {
                if (line.equals("")) {
                    break;
                }
                line = line.replaceAll("#.*", "").replace(" is ", "*is*").replace(" ", "");
                if (line.equals("")) {
                    continue;
                }

                char last = line.charAt(line.length() - 1);
                char punc = '.';
                if (last == '?' || last == '.') {
                    punc = last;
                    line = line.substring(0, line.length() - 1);
                }
                try {
                    if (line.equals("trace")) {
                        trace = !trace;
                    } else if (line.equals("dump")) {
                        for (Rule rule : rules) {
                            System.out.println(rule);
                        }
                    } else if (punc == '?') {
                        search(new Term(line, null));
                    } else {
                        rules.add(new Rule(line));
                    }
                } catch (ParseException e) {
                    System.err.println("err: " + e.getMessage());
                    e.printStackTrace();
                }

                if (prompt != null) {
                    System.out.print(prompt);
                    System.out.flush();
                }
                line = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static Boolean isVariable(Term term) {
        return ((term.getArgs().size() == 0) && (term.getPred().charAt(0) >= 'A') && (term.getPred().charAt(0) <= 'Z'));
    }

    private static Boolean isConstant(Term term) {
        return ((term.getArgs().size() == 0) && ((term.getPred().charAt(0) < 'A') || (term.getPred().charAt(0) > 'Z')));
    }

    private static Boolean unify(Term src, HashMap<String, Term> srcEnv, Term dest, HashMap<String, Term> destEnv) throws ParseException {
        if (trace) {
            System.out.println(indent + "Unify " + src + " " + srcEnv + " to " + dest + " " + destEnv);
        }
        indent += "  ";
        if (src.getPred().equals("_") || dest.getPred().equals("_")) {
            return sts(true, "Wildcard");
        }
        if (isVariable(src)) {
            Term srcVal = eval(src, srcEnv);
            if (srcVal == null) {
                return sts(true, "Src unset");
            } else {
                return sts(unify(srcVal, srcEnv, dest, destEnv), "Unify to Src Value");
            }
        }
        if (isVariable(dest)) {
            Term destVal = eval(dest, destEnv);
            if (destVal != null) {
                return sts(unify(src, srcEnv, destVal, destEnv), "Unify to Dest value");
            } else {
                destEnv.put(dest.getPred(), eval(src, srcEnv));
                return sts(true, "Dest updated");
            }
        } else if (!src.getPred().equals(dest.getPred())) {
            return sts(false, "Diff predicates");
        } else if (src.getArgs().size() != dest.getArgs().size()) {
            return sts(false, "Diff # args");
        }
        HashMap<String, Term> dde = new HashMap<String, Term>(destEnv);
        for (int i = 0; i < src.getArgs().size(); i++) {
            if (!unify(src.getArgs().get(i), srcEnv, dest.getArgs().get(i), dde)) {
                return sts(false, "Arg doesn't unify");
            }
        }
        destEnv.putAll(dde);
        return sts(true, "All args unify");
    }

    private static Boolean sts(Boolean ok, String why) {
        indent = indent.substring(2);
        if (trace) {
            System.out.println(indent + (ok ? "Yes" : "No") + " " + why);
        }
        return ok;
    }

    private static void search(Term term) throws ParseException {
        Goal goal = new Goal(new Rule("all(done):-x(y)"), null);
        ArrayList<Term> list = new ArrayList<Term>();
        list.add(term);
        goal.getRule().setGoals(list);
        ArrayList<Goal> queue = new ArrayList<Goal>();
        queue.add(goal);
        while (queue.size() > 0) {
            Goal c = queue.remove(0);
            if (trace) {
                System.out.println("deque: " + c);
            }
            if (c.getInx() >= c.getRule().getGoals().size()) {
                if (c.getParent() == null) {
                    if (c.getEnv().size() > 0) {
                        System.out.println(c.getEnv());
                    } else {
                        System.out.println("Yes");
                    }
                    continue;
                }
                Goal parent = c.getParent().clone();
                Term head = c.getRule().getHead();
                Term currentGoal = parent.getRule().getGoals().get(parent.getInx());
                unify(head, c.getEnv(), currentGoal, parent.getEnv());
                parent.setInx(parent.getInx() + 1);
                queue.add(parent);
                if (trace) {
                    System.out.println("Requeuing " + parent);
                }
                continue;
            }
            Term currentGoal = c.getRule().getGoals().get(c.getInx());
            String currentPred = currentGoal.getPred();
            String[] operators = {"*is*", "cut", "fail", "<", "=="};
            if (Arrays.asList(operators).contains(currentPred)) {
                if (currentPred.equals("*is*")) {
                    Term ques = eval(currentGoal.getArgs().get(0), c.getEnv());
                    Term ans = eval(currentGoal.getArgs().get(1), c.getEnv());
                    if (ques == null) {
                        c.getEnv().put(currentGoal.getArgs().get(0).getPred(), ans);
                    } else if (!ques.getPred().equals(ans.getPred())) {
                        continue;
                    }
                } else if (currentPred.equals("cut")) {
                    queue = new ArrayList<Goal>();
                } else if (currentPred.equals("fail")) {
                    continue;
                } else if (eval(term, c.getEnv()) == null) {
                    continue;
                }
                c.setInx(c.getInx() + 1);
                queue.add(c);
                continue;
            }
            for (Rule rule : rules) {
                Term head = rule.getHead();
                if (!head.getPred().equals(currentGoal.getPred())) {
                    continue;
                }
                if (head.getArgs().size() != currentGoal.getArgs().size()) {
                    continue;
                }
                Goal child = new Goal(rule, c);
                if (unify(currentGoal, c.getEnv(), head, child.getEnv())) {
                    queue.add(child);
                }
            }
        }
    }

    private static Term eval(Term term, HashMap<String, Term> env) throws ParseException {
        if (Util.getOperators().contains(term.getPred())) {
            Integer a = Integer.getInteger(eval(term.getArgs().get(0), env).getPred());
            Integer b = Integer.getInteger(eval(term.getArgs().get(1), env).getPred());
            if (term.getPred().equals("+")) {
                return new Term(new Integer(a + b).toString(), null);
            }
            if (term.getPred().equals("-")) {
                return new Term(new Integer(a - b).toString(), null);
            }
            if (term.getPred().equals("*")) {
                return new Term(new Integer(a * b).toString(), null);
            }
        }
        // TODO: How to set types for lt, eq, original uses booleans
        if (isConstant(term)) {
            return term;
        }
        if (isVariable(term)) {
            Term ans = env.get(term.getPred());
            if (ans == null) {
                return null;
            }
            return eval(ans, env);
        }
        ArrayList<Term> args = new ArrayList<Term>();
        for (Term arg : term.getArgs()) {
            Term a = eval(arg, env);
            if (a == null) {
                return null;
            }
            args.add(a);
        }
        return new Term(term.getPred(), args);
    }
}
