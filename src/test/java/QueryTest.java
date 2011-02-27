import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * User: Johannes Krampf <johkra@gmail.com>
 * Date: 27.02.11
 */
public class QueryTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testSimpleQuery() throws Exception {
        String commands = "boy(bill)\n" +
                "boy(bill)?";
        byte[] bytes = commands.getBytes("utf-8");
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        Prolog.procFile(input,"");
        assertEquals("Yes\n", outContent.toString());
    }

    @Test
    public void testVariableQuery() throws Exception {
        String commands = "boy(X)?";
        byte[] bytes = commands.getBytes("utf-8");
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        Prolog.procFile(input, "");
        assertEquals("{X=bill}\n", outContent.toString());
    }

    @Test
    public void testIndirectQuery() throws Exception {
        String commands = "mother(alice,bill)\n" +
                "child(J,K) :- mother(K,J)\n" +
                "son(X,Y) :- child(X,Y),boy(X)\n" +
                "son(X,alice)?";
        byte[] bytes = commands.getBytes("utf-8");
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        Prolog.procFile(input, "");
        assertEquals("{X=bill}\n", outContent.toString());
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(null);
    }
}
