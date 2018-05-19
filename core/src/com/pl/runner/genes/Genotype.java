package com.pl.runner.genes;

import java.util.concurrent.ThreadLocalRandom;

public class Genotype {

    private double genotype[];
    private static final double rangeStart = -0.5;
    private static final double rangeEnd = 0.5;
    private static final int rangeStartInt = 0;
    private static final int rangeEndInt = 3;

    public Genotype() {
        createGenotype();

    }

    private void createGenotype() {
        genotype = new double[4];
        for(int i = 0;i<genotype.length;i++) {             //kazda czesc genu dostaje random
            genotype[i] = getRandomDouble();
        }
    }

    public static double getRandomDouble(){
        return ThreadLocalRandom.current().nextDouble(rangeStart, rangeEnd);
    }
    public static int getRandomInt(){
        return ThreadLocalRandom.current().nextInt(rangeStartInt, rangeEndInt +1);
    }

    public double[] getGenotype() {
        return genotype;
    }

}
