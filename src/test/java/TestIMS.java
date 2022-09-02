import algo.Environment;
import algo.IMS;
import exceptions.ApsException;

public class TestIMS {
    public static void main(String args[]) throws ApsException {
        IMS algo = new IMS(TestEnvironment.testEnv());
        algo.evolve();
    }
}
