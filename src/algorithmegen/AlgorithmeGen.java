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
public class AlgorithmeGen {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Individu individuAlpha = GeneticUtils.setIndividuAlpha(Individu.createAlpha(0.278, 0.222, 1055, 800));
        System.out.println("Individu acceptable : ");
        System.out.println(individuAlpha);
        
        // Premiere generation
        final int N = 100; // population initiale
        System.out.println("Premiere Generation de " + N + " individus.");
        Generation generation = new Generation(N);
        
        int MAX_ITERATIONS = 1000;
        double FITNESS_MIN = 1;
        
        int NB_SELECTION_PARENT = 40;
        int NB_SELECTION = 100;
        
        int iterations = 0;
        double fitness = generation.getBestFitness();
        
        double firstFitness = fitness;
        double lastFitness = Integer.MAX_VALUE;
        while (iterations < MAX_ITERATIONS && fitness > FITNESS_MIN)
        {
            // Croisement
            generation.determineProbabilite();
            List<Individu> enfants = GeneticUtils.getEnfants(GeneticUtils.getCouples(generation.selectionGeneration(NB_SELECTION_PARENT)));
            
            // Mutation
            generation.mutation();
            generation.recalculAllFitness();
            generation.determineProbabilite();
            
            // Selection
            List<Individu> totalGen = new ArrayList();
            totalGen.addAll(generation.generation());
            totalGen.addAll(enfants);
            GeneticUtils.reorderRandomly(totalGen);
            generation.generation().clear();
            generation.generation().addAll(totalGen);
            generation.recalculAllFitness();
            generation.determineProbabilite();
            
            List<Individu> newGen = new ArrayList();
            newGen.addAll(generation.selectionGeneration(NB_SELECTION));
            
            generation = new Generation(newGen);
            
            fitness = generation.getBestFitness();
            System.out.println("Meilleur fitness actuel : "+fitness);
            lastFitness = fitness;
            
            iterations++;
        }
        System.out.println("START : "+firstFitness);
        System.out.println("END : "+lastFitness);
    }
    
}
