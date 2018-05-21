package com.pl.runner.genes;

import com.badlogic.gdx.utils.Timer;
import com.pl.runner.entities.Runner;

import static com.pl.runner.stages.GameStage.sensorDownClose;
import static com.pl.runner.stages.GameStage.sensorDownFar;
import static com.pl.runner.stages.GameStage.sensorUpClose;
import static com.pl.runner.stages.GameStage.sensorUpFar;

public class GeneController {

    private double[] topGenotype;
    private static long topScore;

    private static int topGeneration;


    public static void calculateOutput(Runner runner){
        double parsedOutput = 1/(1+Math.exp(-sumFunction(runner)));
        makeDecision(runner, parsedOutput);
    }

    private static double sumFunction(Runner runner){
        double[] weight = runner.genotype.getGenotype();
        return (weight[0]*sensorDownClose.isSensorEnabled()+weight[1]*sensorUpClose.isSensorEnabled()+weight[2]*sensorDownFar.isSensorEnabled()+weight[3]*sensorUpFar.isSensorEnabled());
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



    public double[] getTopGenotype() {
        return topGenotype;
    }

    public void setTopGenotype(double[] topGenotype) {
        this.topGenotype = topGenotype;
    }

    public static long getTopScore() {
        return topScore;
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
