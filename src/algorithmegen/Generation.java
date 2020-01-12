/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Christophe Nasica
 */
public class Generation {
    private final List<Individu> generation;
    
    public Generation(int nbIndividus) {
        generation = new ArrayList(nbIndividus);
        for (int i = 0; i < nbIndividus; i++) {
            double r = 0.05 + (0.5 - 0.05) * Math.random();
            double q = 0.05 + (0.5 - 0.05) * Math.random();
            int k = (int) (100 + ((2000 - 100) + 1) * Math.random());
            int b0 = (int) (100 + ((2000 - 100) + 1) * Math.random());
            generation.add(new Individu(r, q, k, b0));
        }
    }
    
    public Generation(List<Individu> individus) {
        generation = new ArrayList();
        generation.addAll(individus);
    }
    
    public List<Individu> generation() {
        return generation;
    }
    
    public int size() {
        return generation.size();
    }
    
    public double getBestFitness() {
        double fitness = Integer.MAX_VALUE;
        for (Individu individu : generation) {
            if (individu.getFitness() < fitness) {
                fitness = individu.getFitness();
            }
        }
        return fitness;
    }
    
    public void determineProbabilite() {
        for (Individu individu : generation) {
            individu.setProbabilite((1/individu.getFitness()) / sommeInvFitness());
        }
    }
    
    public void recalculAllFitness() {
        for (Individu individu : generation) {
            individu.initFitness();
        }
    }
    
    private double sommeInvFitness() {
        double sommeInv = 0;
        for (Individu individu : generation) {
            sommeInv += 1/individu.getFitness();
        }
        return sommeInv;
    }
    
    public List<Individu> selection(int nbIndividus) {
        List<Individu> selection = new ArrayList(nbIndividus);
        List<double[]> plages = new ArrayList(nbIndividus);
        
        // premiere plage
        plages.add(new double[]{0, generation.get(0).getProbabilite()});
        for (int i = 1; i < generation.size(); i++) {
            double first = generation.get(i-1).getProbabilite();
            double second = first + generation.get(i).getProbabilite();
            plages.add(new double[]{first, second});
        }
        
        for (int j = 1; j < nbIndividus; j++) {
            double proba = j / (nbIndividus + 1); // proba qui doit se trouver dans notre plage
            int indicePlage = 0; // indice de la plage qui correspond a l indice d un individu de la generation
            for (double[] plage : plages) {
                if (proba > plage[0] && proba <= plage[1]) {
                    selection.add(generation.get(indicePlage));
                    break;
                }
                indicePlage++;
            }
        }
        
        return selection;
    }
    
    public void mutation() {
        List<Individu> toMutate = new ArrayList(size()/2); // indice des individus à muter
        int remaining = size() / 2;
        while (remaining > 0)
        {
            int random = (int) (Math.random() * size());
            toMutate.add(generation().remove(random));
            remaining --;
        }
        
        for (Individu individu : toMutate) {
            int nbVarToMutate = 1 + (int) (Math.random() * 4);
            List<String> variables = new ArrayList(Arrays.asList(new String[] {"r", "k", "q", "b0"}));
            for (int i = 0; i < nbVarToMutate; i++) {
                String varToMutate = variables.remove((int) (Math.random() * variables.size()));
                switch (varToMutate) {
                    case "r":
                        while (true)
                        {
                            double newR = GeneticUtils.loiNormale(individu.getR(), sigma(getAllR(toMutate)));
                            if (newR >= 0.05 && newR <= 0.5) {
                                individu.setR(newR);
                                break;
                            }
                        }
                        break;
                    case "k":
                        while (true)
                        {
                            double newK = GeneticUtils.loiNormale(individu.getK(), sigma(getAllK(toMutate)));
                            if (newK >= 0.05 && newK <= 0.5) {
                                individu.setK(newK);
                                break;
                            }
                        }
                        break;
                    case "q":
                        while (true)
                        {
                            double newQ = GeneticUtils.loiNormale(individu.getQ(), sigma(getAllQ(toMutate)));
                            if (newQ >= 100 && newQ <= 2000) {
                                individu.setQ(newQ);
                                break;
                            }
                        }
                        break;
                    case "b0":
                        while (true)
                        {
                            double newB0 = GeneticUtils.loiNormale(individu.getB0(), sigma(getAllB0(toMutate)));
                            if (newB0 >= 100 && newB0 <= 2000) {
                                individu.setB0(newB0);
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            individu.initCaptures();
        }
        generation.addAll(toMutate);
    }
    
    private double sigma(double[] values) {
        return maxAll(values) - sumAll(values) / values.length;
    }
    
    private double[] getAllR(List<Individu> individus) {
        double[] r = new double[individus.size()];
        for (int i = 0; i < individus.size(); i++) {
            r[i] = individus.get(i).getR();
        }
        return r;
    }
    
    private double[] getAllK(List<Individu> individus) {
        double[] r = new double[individus.size()];
        for (int i = 0; i < individus.size(); i++) {
            r[i] = individus.get(i).getK();
        }
        return r;
    }
    
    private double[] getAllQ(List<Individu> individus) {
        double[] r = new double[individus.size()];
        for (int i = 0; i < individus.size(); i++) {
            r[i] = individus.get(i).getQ();
        }
        return r;
    }
    
    private double[] getAllB0(List<Individu> individus) {
        double[] r = new double[individus.size()];
        for (int i = 0; i < individus.size(); i++) {
            r[i] = individus.get(i).getB0();
        }
        return r;
    }
    
    private double sumAll(double[] values) {
        double sum = 0;
        for (int i = 0 ; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }
    
    private double maxAll(double[] values) {
        double max = 0;
        for (int i = 0 ; i < values.length; i++) {
            if (values[i] > max)
                max = values[i];
        }
        return max;
    }
}