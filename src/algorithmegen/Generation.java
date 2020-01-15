/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
            int k = (int) (100 + ((2000 - 100)) * Math.random());
            int b0 = (int) (100 + ((2000 - 100)) * Math.random());
            generation.add(new Individu(r, q, k, b0));
        }
    }
    
    public Generation(List<Individu> individus) {
        generation = new ArrayList(individus.size());
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
    
    public List<Individu> selectionGeneration(int nbIndividus) {
        if (nbIndividus >= generation.size())
            return generation;
        List<Individu> selection = new ArrayList(nbIndividus);
        List<Individu> tmp = new ArrayList();
        tmp.addAll(generation);
        Collections.sort(tmp, new Individu.ProbaComparator());
        
        for (int i = 0; i < nbIndividus; i++) {
            selection.add(tmp.get(i));
        }
        
        return selection;
    }
    
    public List<Individu> selectionParents(int nbIndividus) {
        List<Individu> selection = new ArrayList(nbIndividus);
        List<double[]> plages = new ArrayList(size());
        
        // premiere plage
        plages.add(new double[]{0, generation.get(0).getProbabilite()});
        for (int i = 0; i < generation.size() - 1; i++) {
            double first = plages.get(i)[1];
            double second = first + generation.get(i+1).getProbabilite();
            //System.out.println(i+" => "+first+":"+second);
            plages.add(new double[]{first, second});
        }
        
        for (int j = 0; j < generation.size(); j++) {
            double proba = j / ((double)nbIndividus + 1); // proba qui doit se trouver dans notre plage
            int indicePlage = 0; // indice de la plage qui correspond a l indice d un individu de la generation
            for (double[] plage : plages) {
                //if (selection.size() == nbIndividus)
                //    break;
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
        while (remaining > 0) // tant qu il reste des mutations
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
                double mu;
                double sigma;
                switch (varToMutate) {
                    case "r":
                        mu = individu.getR();
                        sigma = sigma(getAllR(toMutate));
                        while (true)
                        {
                            //double newR = GeneticUtils.loiNormale(individu.getR(), sigma(getAllR(toMutate)));
                            double newR = mu + Math.random() * sigma;
                            if (newR >= 0 && newR <= 0.5) {
                                individu.setR(newR);
                                break;
                            }
                        }
                        break;
                    case "k":
                        mu = individu.getK();
                        sigma = sigma(getAllK(toMutate));
                        while (true)
                        {
                            //double newK = GeneticUtils.loiNormale(individu.getK(), sigma(getAllK(toMutate)));
                            double newK = mu + Math.random() * sigma;
                            if (newK >= 100 && newK <= 2000) {
                                individu.setK(newK);
                                break;
                            }
                        }
                        break;
                    case "q":
                        mu = individu.getQ();
                        sigma = sigma(getAllQ(toMutate));
                        while (true)
                        {
                            //double newQ = GeneticUtils.loiNormale(individu.getQ(), sigma(getAllQ(toMutate)));
                            double newQ = mu + Math.random() * sigma;
                            if (newQ >= 0 && newQ <= 0.5) {
                                individu.setQ(newQ);
                                break;
                            }
                        }
                        break;
                    case "b0":
                        mu = individu.getB0();
                        sigma = sigma(getAllB0(toMutate));
                        while (true)
                        {
                            //double newB0 = GeneticUtils.loiNormale(individu.getB0(), sigma(getAllB0(toMutate)));
                            double newB0 = mu + Math.random() * sigma;
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
