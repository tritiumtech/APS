import algo.Arrangement;
import algo.Environment;
import exceptions.ApsException;

import java.util.List;

public class TestArrangement {
    public static void main(String args[]) {
        Environment env = TestEnvironment.testEnv();
        testCrossOver(env);
    }

    public static void testCrossOver(Environment env) {

        Arrangement a = null, b = null;
        try {
            a = Arrangement.generateInstance(env);
            b = Arrangement.generateInstance(env);
            System.out.println(a);
            System.out.println(b);
            List<Arrangement> newPair = a.crossover(b);
            System.out.println(newPair.get(0));
            System.out.println(newPair.get(1));
            a.cost(env);
        } catch (ApsException e) {
            e.printStackTrace();
        }

    }
}
