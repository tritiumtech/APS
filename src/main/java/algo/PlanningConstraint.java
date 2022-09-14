package algo;

public class PlanningConstraint {
    public Constraint name;
    public float weight;
    public ScoreStats stats;

    public PlanningConstraint(Constraint name, float weight) {
        this.name = name;
        this.weight = weight;
        stats = new ScoreStats();
    }
}
