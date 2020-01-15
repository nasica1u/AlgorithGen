/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmegen;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christophe Nasica
 */
public class GeneticUtils {
    
    public static Individu individuAlpha;
    
    public static Individu setIndividuAlpha(Individu alpha) {
        individuAlpha = alpha;
        return individuAlpha;
    }
    
    public static List<Individu[]> getCouples(List<Individu> individus) {
        if (individus != null) {
            List<Individu[]> couples = new ArrayList(individus.size()/2);
            Individu[] couple = null;
            for (int i = 0; i < individus.size(); i++ ) {
                if (i % 2 == 0) {
                    couple = new Individu[2];
                    couple[0] = individus.get(i);
                }
                else {
                    couple[1] = individus.get(i);
                    couples.add(couple);
                }
            }
            //System.out.println("couples : "+couples.size());
            return couples;
        }
        return null;
    }
    
    public static List<Individu> getEnfants(List<Individu[]> parents) {
        if (parents != null && !parents.isEmpty()) {
            List<Individu> enfants = new ArrayList(parents.size() * 2);
            
            for (Individu[] parent : parents) {
                Individu pere = parent[0];
                Individu mere = parent[1];
                
                double a1 = loiNormale();
                double a2 = loiNormale();
                double a3 = loiNormale();
                double a4 = loiNormale();
                
                Individu enfantN1 = new Individu(a1 * pere.getR() + ((1 - a1) * mere.getR()),
                                                    a2 * pere.getQ() + ((1 - a2) * mere.getQ()),
                                                    a3 * pere.getK() + ((1 - a3) * mere.getK()),
                                                    a4 * pere.getB0() + ((1 - a4) * mere.getB0()));
                
                Individu enfantN2 = new Individu(a1 * mere.getR() + ((1 - a1) * pere.getR()),
                                                    a2 * mere.getQ() + ((1 - a2) * pere.getQ()),
                                                    a3 * mere.getK() + ((1 - a3) * pere.getK()),
                                                    a4 * mere.getB0() + ((1 - a4) * pere.getB0()));
                
                enfants.add(enfantN1);
                enfants.add(enfantN2);
            }
            //System.out.println("enfants : "+enfants.size());
            return enfants;
        }
        return null;
    }
    
    public static double loiNormale(double mu, double sigma) {
        while (true)
        {
            double alea1 = Math.random();
            double alea2 = Math.random();
            double w = Math.pow(alea1, 2) + Math.pow(alea2, 2);
            if (w <= 1 && w >= 0) {
                return mu + alea1 * sigma * Math.sqrt((-2 * Math.log(w)) / w);
            }
        }
    }
    
    public static double loiNormale() {
        return loiNormale(0.5, 0.2);
    }
    
    public static void reorderRandomly(List<Individu> individus) {
        int targetSize = individus.size();
        int size = targetSize;
        List<Individu> reordered = new ArrayList(size);
        
        while (reordered.size() < targetSize) {
            int nextRandom = (int) (Math.random() * size);
            reordered.add(individus.get(nextRandom));
            
            individus.remove(nextRandom);
            size = individus.size();
        }
        
        individus.clear();
        individus.addAll(reordered);
    }
}
