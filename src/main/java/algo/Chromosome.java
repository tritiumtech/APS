package algo;
import algo.Arrangement;

import java.util.List;

/**
 * @author      Amos Zhou
 */
public interface Chromosome {
    public List<Arrangement> crossover(Arrangement other);
    public Arrangement mutate();
    public double cost(Environment env);
}

