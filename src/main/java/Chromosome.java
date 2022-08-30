import java.util.List;

/**
 * @author      Amos Zhou
 */
public interface Chromosome {
    public List<Arrangement> crossover(Arrangement other);
    public List<Arrangement> mutate();
    public double cost();
}
