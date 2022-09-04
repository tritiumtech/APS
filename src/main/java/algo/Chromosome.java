package algo;

import java.util.List;

/**
 * @author      Amos Zhou
 */
public interface Chromosome {
    List<Arrangement> crossover(Arrangement other);
    Arrangement mutate();
    double cost(Environment env);
}

