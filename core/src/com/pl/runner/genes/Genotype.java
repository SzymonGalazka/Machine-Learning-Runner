package com.pl.runner.genes;

import java.util.concurrent.ThreadLocalRandom;

public class Genotype {

    private double genotype[];
    private static final double rangeStart = -0.5;
    private static final double rangeEnd = 0.5;

    public Genotype() {
        if(GeneController.getTopGeneration()==0) createGenotype();
        else mutateGenotype();
    }

    private void mutateGenotype() {
        genotype = new double[4];
        double[] alphaGene = GeneController.mutate(GeneController.getAlphaGene());
        for(int i=0;i<genotype.length;i++){
            genotype[i] = alphaGene[i];
        }
    }

    private void createGenotype() {
        genotype = new double[4];
        for(int i = 0;i<genotype.length;i++) {
            genotype[i] = getRandomDouble();
        }
    }

    public static double getRandomDouble(){
        return ThreadLocalRandom.current().nextDouble(rangeStart, rangeEnd);
    }

    public void setGenotype(double[] genotype) {
        this.genotype = genotype;
    }

    public double[] getGenotype() {
        return genotype;
    }

}
