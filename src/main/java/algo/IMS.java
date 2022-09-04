package algo;

import exceptions.ApsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Amos Zhou
 * Intelligent Manufacturing Scheduling algorithm
 */
public class IMS {
    public Environment env;
    public List<Arrangement> instances = new ArrayList<>();

    public IMS(Environment env) {
        this.env = env;
    }

    /**
     * 随机生成初代种群
     *
     * @param size 种群大小
     * @return
     */
    public void populate(int size) throws ApsException {
        for (int i = 0; i < size; i++) {
            Arrangement arrangement = Arrangement.generateInstance(env);
            if (!instances.contains(arrangement)) {
                arrangement.cost(env);
                instances.add(arrangement);
            }
        }
    }

    /**
     * 增殖
     */
    public void proliferate(int crossoverCounts, int mutationCounts) throws ApsException {
        double chances[] = calculateChances();
        // Cross Over
        for (int i = 0; i < crossoverCounts; i++) {
            int firstCandidate = rollTheWheel(chances);
            int secondCandidate = firstCandidate;
            while (secondCandidate == firstCandidate) {
                secondCandidate = rollTheWheel(chances);
            }
            Arrangement firstArrangement = instances.get(firstCandidate);
            Arrangement secondArrangement = instances.get(secondCandidate);

            List<Arrangement> newPair = firstArrangement.crossover(secondArrangement);
            while (newPair == null) {
//                System.out.println("Re-generating crossover pairs for ");
                firstCandidate = rollTheWheel(chances);
                secondCandidate = firstCandidate;
                while (secondCandidate == firstCandidate) {
                    secondCandidate = rollTheWheel(chances);
                }
                firstArrangement = instances.get(firstCandidate);
                secondArrangement = instances.get(secondCandidate);

//                System.out.println(firstArrangement);
//                System.out.println(secondArrangement);
                newPair = firstArrangement.crossover(secondArrangement);
            }
            if (newPair.size() > 1) {
                for (Arrangement arrangement : newPair)
                    instances.add(arrangement);
            }
        }
        // Mutate
        for (int i = 0; i < mutationCounts; i++) {
            int candidate = rollTheWheel(chances);
            Arrangement mutated = instances.get(candidate).mutate();
            while (mutated == null || instances.contains(mutated)) {
//                System.out.println("Re-generating mutated instances");
                candidate = rollTheWheel(chances);
                mutated = instances.get(candidate).mutate();
            }
            instances.add(mutated);
        }
    }

    /**
     * 计算轮盘赌中的生存几率
     *
     * @return
     */
    public double[] calculateChances() throws ApsException {
        if (instances.size() > 0) {
            double[] scores = new double[instances.size()];
            double[] chances = new double[instances.size()];
            Arrangement first = instances.get(0);
            double min = first.cost, max = first.cost;
            for (int i = 0; i < instances.size(); i++) {
                scores[i] = instances.get(i).cost;
                if (min > scores[i]) min = scores[i];
                if (scores[i] > max) max = scores[i];
            }
            // 线性转换
            double sum = 0;
            for (int i = 0; i < instances.size(); i++) {
                if (max == min) {
                    scores[i] = 1;
                } else {
                    scores[i] = (max - scores[i]) / (max - min);
                }
                sum += scores[i];
            }
            for (int i = 0; i < instances.size(); i++) {
                chances[i] = scores[i] / sum;
            }
            return chances;
        } else {
            throw new ApsException("APS005 方案集为空");
        }
    }

    /**
     * 优胜劣汰
     */
    public void eliminate(int toKeep) {
        Collections.sort(instances);
        instances = instances.subList(0, toKeep);
    }

    /**
     * 轮盘赌算法，chances数组中记录被抽中的概率
     *
     * @param chances
     * @return
     */
    public int rollTheWheel(double[] chances) {
        Random r = new Random();
        double pos = r.nextDouble();
        double accu = 0;
        for (int i = 0; i < instances.size(); i++) {
            if (pos >= accu && pos < accu + chances[i]) {
                return i;
            }
            accu += chances[i];
        }
        return -1;
    }

    public void evolve() throws ApsException {
        double lastScore = 0;
        int runCount = 0;
        for (int epoch = 0; epoch < 100; epoch++) {
            System.out.println("Epoch " + epoch);
            populate(1000);
            System.out.println("Initial population");
            proliferate(500, 500);
            System.out.println("Proliferation completed");
            eliminate(1000);
            double score = stats();
            if (lastScore == score) {
                runCount++;
            } else {
                runCount = 0;
                lastScore = score;
            }
            if (runCount > 10) {
                break;
            }
        }
        printInstances();
    }

    public void printInstances() {
        Collections.sort(instances);
        int i = 0;
        for (Arrangement arrangement : instances) {
            System.out.println(i + " " + arrangement.cost + " " + arrangement);
        }
    }

    public double stats() {
        Arrangement first = instances.get(0);
        double min = first.cost, max = first.cost;
        double sum = 0;
        for (Arrangement arrangement : instances) {
            sum += arrangement.cost;
            if (arrangement.cost < min) min = arrangement.cost;
            if (arrangement.cost > max) max = arrangement.cost;
        }
        double mean = sum / instances.size();
        System.out.println("Population: " + instances.size() + " Mean=" + sum / instances.size() + " min=" + min + " max=" + max);
        return mean;
    }
}
