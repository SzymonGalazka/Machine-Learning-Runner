package com.pl.runner.genes;

import com.badlogic.gdx.utils.Timer;
import com.pl.runner.entities.Runner;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

import static com.pl.runner.stages.GameStage.sensorDownClose;
import static com.pl.runner.stages.GameStage.sensorDownFar;
import static com.pl.runner.stages.GameStage.sensorUpClose;
import static com.pl.runner.stages.GameStage.sensorUpFar;

public class GeneController {

    private static double[] alphaGene = new double[4];
    private static long topScore;
    private static double[] bestGene1, bestGene2;
    private static int topGeneration;


    public static void calculateOutput(Runner runner){
        double parsedOutput = 1/(1+Math.exp(-sumFunction(runner)));
        makeDecision(runner, parsedOutput);
    }

    private static double sumFunction(Runner runner){
        double[] weight = runner.getGenotype().getGenotype();
        return (weight[0]*sensorDownClose.isSensorEnabled()+weight[1]*sensorUpClose.isSensorEnabled()+
                weight[2]*sensorDownFar.isSensorEnabled()+weight[3]*sensorUpFar.isSensorEnabled());
    }

    private static void makeDecision(final Runner runner, double parsedOutput) {
        if(parsedOutput <0.45){
            System.out.println(parsedOutput);
            runner.dodge();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        runner.stopDodge();
                    }
                }, 0.7f);
        } else if(parsedOutput>0.55) {
            System.out.println(parsedOutput);
            runner.jump();
        }
    }

    public static void setBestGenes(Genotype gene1, Genotype gene2) {
        bestGene1 = gene1.getGenotype();
        bestGene2 = gene2.getGenotype();
    }

    public static double[] mutate(double[] gene){
        BigDecimal[] g = new BigDecimal[4];
        BigDecimal u = new BigDecimal(ThreadLocalRandom.current().nextDouble(1.1,1.2));
        BigDecimal d = new BigDecimal(ThreadLocalRandom.current().nextDouble(0.8,0.9));
        for(int i=0;i<4;i++){
            g[i] = BigDecimal.valueOf(gene[i]);
            if(Math.random() < 0.5) g[i] = g[i].multiply(u);
            else g[i] = g[i].multiply(d);
            if(Math.random() < 0.2) g[i] = g[i].multiply(new BigDecimal(-1));
            gene[i] = g[i].doubleValue();
        }
        return gene;
    }

    public static long getTopScore() {
        return topScore;
    }

    public static double[] getAlphaGene() {
        for(int i=0;i<4;i++){
            if(Math.random() < 0.5) alphaGene[i] = bestGene1[i];
            else alphaGene[i] = bestGene2[i];
        }
        return alphaGene;
    }

    public static void setTopScore(long topScore) {
        GeneController.topScore = topScore;
    }

    public static int getTopGeneration() {
        return topGeneration;
    }

    public static void setTopGeneration(int topGeneration) {
        GeneController.topGeneration = topGeneration;
    }
}
