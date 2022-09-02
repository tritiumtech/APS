import algo.Arrangement;
import algo.Environment;
import exceptions.ApsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestArrangement {
    public static void main(String args[]) throws ApsException {
        Environment env = TestEnvironment.testEnv();
        //testCrossOver(env);
        //testMutation(env);
        Arrangement a = Arrangement.generateInstance(env);
        Arrangement b = a.clone();
        System.out.println(a.equals(b));
        HashMap<Arrangement,Double> testMap = new HashMap<>();
        testMap.put(a,2.2);
        testMap.put(b,2.3);
        System.out.println(testMap.size());
        List<Arrangement> arrangements = new ArrayList<>();
        arrangements.add(a);
        System.out.println(arrangements.contains(b));
    }

    public static void testMutation(Environment env) {

        Arrangement a = null, b = null;
        try {
            a = Arrangement.generateInstance(env);
            for(int i = 0; i < 10; i++) {
                b = a.mutate();
                if(b!=null)
                    System.out.println(b);
            }

        } catch (ApsException e) {
            e.printStackTrace();
        }
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
            System.out.println(a.cost(env));
        } catch (ApsException e) {
            e.printStackTrace();
        }

    }
}
