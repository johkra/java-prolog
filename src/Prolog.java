import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * User: Johannes Krampf <johkra@gmail.com>
 * Date: 07.02.11
 *
 * This is a translation of prolog1.py written by Chris Meyers under a copyleft license.
 *
 * See http://web.archive.org/web/20071014055005/ibiblio.org/obp/py4fun/prolog/prolog1.html for the code and explanations.
 */
public class Prolog {
    private static ArrayList<Rule> rules = new ArrayList<Rule>();
    private static Boolean trace = false;
//      sent      = None

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

    private static void procFile(InputStream file, String prompt) {
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
                line = line.replaceAll("#.*", "").replace(" ", "");
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
                        search(new Term(line));
                    } else {
                        rules.add(new Rule(line));
                    }
                } catch (ParseException e) {
                    System.err.println("err: " + e.getMessage());
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

    private static Boolean unify(Term srcTerm, HashMap<String, String> srcEnv, Term destTerm, HashMap<String, String> destEnv) {
        if (srcTerm.getArgs().length != destTerm.getArgs().length) {
            return false;
        }
        if (!srcTerm.getPred().equals(destTerm.getPred())) {
            return false;
        }
        for (int i = 0; i < srcTerm.getArgs().length; i++) {
            String srcArg = srcTerm.getArgs()[i];
            String destArg = destTerm.getArgs()[i];
            String srcVal = srcArg;
            if ((srcArg.length() == 1) && (srcArg.charAt(0) >= 'A') && (srcArg.charAt(0) <= 'Z')) {
                srcVal = (String) srcEnv.get(srcArg);
            }
            if (srcVal != null) {
                if ((destArg.length() == 1) && (destArg.charAt(0) >= 'A') && (destArg.charAt(0) <= 'Z')) {
                    String destVal = null;
                    destVal = (String) destEnv.get(destArg);
                    if (destVal == null) {
                        destEnv.put(destArg, srcVal);
                    } else if (!destVal.equals(srcVal)) {
                        return false;
                    }
                } else if (!destArg.equals(srcVal)) {
                    return false;
                }
            }
        }
        return true;
    }

//        if srcVal :    # constant or defined Variable in source
//            if destArg <= 'Z' :  # Variable in destination
//                destVal = destEnv.get(destArg)
//                if not destVal : destEnv[destArg] = srcVal  # Unify !
//                elif destVal != srcVal : return 0           # Won't unify
//            elif     destArg != srcVal : return 0           # Won't unify
//    return 1

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
                unify(c.getRule().getHead(), c.getEnv(), parent.getRule().getGoals().get(parent.getInx()), parent.getEnv());
                parent.setInx(parent.getInx() + 1);
                queue.add(parent);
                continue;
            }
            term = c.getRule().getGoals().get(c.getInx());
            for (Rule rule : rules) {
                if (!rule.getHead().getPred().equals(term.getPred())) {
                    continue;
                }
                if (rule.getHead().getArgs().length != term.getArgs().length) {
                    continue;
                }
                Goal child = new Goal(rule, c);
                if (unify(term, c.getEnv(), rule.getHead(), child.getEnv())) {
                    queue.add(child);
                }
            }
        }
    }
}
