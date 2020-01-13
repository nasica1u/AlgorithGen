/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmegen;

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
        int PAS_ENREGISTREMENT_PAST_FITNESS = 10;
        double FITNESS_MIN = 0.2;
        double FITNESS_STABLE = 0.05;
        
        int NB_SELECTION_PARENT = 40;
        int NB_SELECTION = 100;
        
        int iterations = 0;
        double fitness = generation.getBestFitness();
        double pastFitness = 0;
        while (iterations < MAX_ITERATIONS && fitness > FITNESS_MIN && (fitness - pastFitness) > FITNESS_STABLE)
        {
            System.out.println("Generation actuelle : "+iterations);
            System.out.println("Meilleur fitness actuel : "+fitness);
            
            // Selection des parents
            generation.determineProbabilite();
            List<Individu> selectionParent = generation.selection(NB_SELECTION_PARENT);
            
            // Recuperation enfants a partir des couples de parents
            List<Individu> enfants = GeneticUtils.getEnfants(GeneticUtils.getCouples(selectionParent));
            
            // Mutation de la population precedente
            generation.mutation();
            generation.recalculAllFitness(); // on recalcule les fitness apres les changements de mutation
            generation.determineProbabilite(); // recalcule de proba
            
            generation.generation().addAll(enfants); // ajout de tous les enfants avec la population ayant subit la mutation
            generation.recalculAllFitness();
            generation.determineProbabilite();
            
            // Selection de la nouvelle generation a la place de l ancienne
            GeneticUtils.reorderRandomly(generation.generation());
            generation.recalculAllFitness();
            generation.determineProbabilite();
            List<Individu> selection = generation.selection(NB_SELECTION);
            generation = new Generation(selection);
            generation.recalculAllFitness();
            
            fitness = generation.getBestFitness();
            
            iterations++;
        }
    }
    
}
