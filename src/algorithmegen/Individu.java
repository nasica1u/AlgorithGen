/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmegen;

import java.util.Comparator;
import java.util.UUID;

/**
 *
 * @author Christophe Nasica
 */
public class Individu {
    
    private static final double[] E = {0.0,0.2,0.1,0.7,0.5,0.0,1.0,0.3,0.9,0.9,0.9,0.0,0.3,0.5,1.0,0.9,0.8,1.2,0.0,0.7,0.8};
    
    private UUID uuid; // unique ID
    
    private double r; // taux de croissnace
    private double q; // capturabilite
    private double k; // biomasse a l equilibre
    private double b0; // b0 <= k
    
    private double[] captures;
    private double fitness;
    
    private double probabilite;
    
    public Individu(double r, double q, double k, double b0) {
        uuid = UUID.randomUUID();
        this.r = r <= 0.5 ? r : 0.5;
        this.q = q <= 0.5 ? q : 0.5;
        this.k = k <= 2000 ? k : 2000;
        this.b0 = b0 > k ? k : b0;
        
        captures = new double[E.length];
        initCaptures();
        initFitness();
    }
    
    private Individu() {}
    
    public static final Individu createAlpha(double r, double q, double k, double b0) {
        Individu individu = new Individu();
        individu.setR(r);
        individu.setQ(q);
        individu.setK(k);
        individu.setB0(b0);
        
        individu.captures = new double[E.length];
        individu.initCaptures();
        
        return individu;
    }
    
    public final void initCaptures() {
        if (b0 > k)
            b0 = k;
        double biomasse = b0;
        for (int i = 0; i < E.length; i++) {
            
            biomasse = biomasse + r * (1 - biomasse/k) * biomasse - q * E[i] * biomasse;
            if(biomasse < 0)
                biomasse = 0;
            if (biomasse > k)
                biomasse = k;
            captures[i] = q * E[i] * biomasse;
        }
    }
    
    public final void initFitness() {
        for (int i = 0; i < E.length; i++) {
            fitness += Math.sqrt(Math.pow(GeneticUtils.individuAlpha.captures[i] - captures[i], 2));
        }
    }

    public double getR() {
        return r;
    }

    public double getQ() {
        return q;
    }

    public double getK() {
        return k;
    }

    public double getB0() {
        return b0;
    }
    
    public double getFitness() {
        return fitness;
    }
    
    public double getProbabilite() {
        return probabilite;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public void setK(double k) {
        this.k = k;
    }

    public void setB0(double b0) {
        this.b0 = b0;
    }
    
    public void setProbabilite(double probabilite) {
        this.probabilite = probabilite;
    }

    @Override
    public String toString() {
        return "Individu{" + "uuid=" + uuid + ", r=" + r + ", q=" + q + ", k=" + k + ", b0=" + b0 + '}';
    }
    
    public static class ProbaComparator implements Comparator<Individu> {

        @Override
        public int compare(Individu p1, Individu p2) {
            return Double.compare(p2.getProbabilite(), p1.getProbabilite());
        }
        
    }
    
    public static class FitnessComparator implements Comparator<Individu> {

        @Override
        public int compare(Individu p1, Individu p2) {
            return Double.compare(p1.getFitness(), p2.getFitness());
        }
        
    }
}
