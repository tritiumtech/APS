import java.util.List;

public class TestArrangement {
    public static void main(String args[]) {
        Environment env = TestClass.testEnv();
        testCrossOver(env);
    }

    public static void testCrossOver(Environment env) {
        Arrangement a = new Arrangement(5, env);
        Arrangement b = new Arrangement(5, env);
        a.chromosome[0] = 0;
        a.chromosome[1] = 0;
        a.chromosome[2] = 1;
        a.chromosome[3] = 1;
        a.chromosome[4] = 0;
        b.chromosome[0] = 1;
        b.chromosome[1] = 0;
        b.chromosome[2] = 0;
        b.chromosome[3] = 1;
        b.chromosome[4] = 1;
        System.out.println(a);
        System.out.println(b);
        List<Arrangement> newPair = a.crossover(b);
        System.out.println(newPair.get(0));
        System.out.println(newPair.get(1));
    }
}
