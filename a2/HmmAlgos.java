import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.lang.Math;
import java.lang.reflect.Array;


public class HmmAlgos {

    double[][] A;
    double[][] B;
    double[][] Pi;
    
    // double[][] Pi;
    int states;
    int emissions;
    int T;
    int[] Obs;
    double[][] A_hat;
    double[][] B_hat;
    double[][] Pi_hat;
    double[] c;

    int Ai = 0;
    int Aj = 0;
    int Bi = 0;
    int Bj = 0;
    int Pii = 0;
    int Pij = 0;
    double[][][] di_gamma;
    double[][] gamma;

    public static final int ITERATION_LIMIT = 1000;


    public void setA(double[][] mat){
        A=mat;
    }

    public void setB(double[][] mat){
        B=mat;
        
    }

    public void setP(double[][] mat){
        Pi=mat;

    }

    public void setStatesLength(int s){
        states=s;

    }

    public void setEmissionCount(int e){
        emissions=e;
    }

    public void setT(int t){
        T=t;
    }
   
    
 
    public void scanInputs(Scanner sc) {
        A = readLine(sc);
        B = readLine(sc);
        Pi = readLine(sc);
        Obs = readObs(sc);
        states = A.length;
        emissions = B[0].length;
        T = Obs.length;
    }

    public double[][] readLine(Scanner sc) {

        int row = sc.nextInt();
        int col = sc.nextInt();
        double[][] matrix = new double[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                matrix[i][j] = sc.nextDouble();

            }
        return matrix;
    }

    public int[] readObs(Scanner sc) {

        T = sc.nextInt();
        int[] obs = new int[T];
        for (int i = 0; i < T; i++)
            obs[i] = sc.nextInt();

        Obs=obs;
        return obs;
    }

    public void scanInputs(boolean useFile, String file) {     
        Scanner sc = null;
        if (useFile) {
            try {
                sc = new Scanner(new File(file));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
            sc = new Scanner(System.in);

        if(!sc.hasNext())return;
        //read A
        Ai = sc.nextInt();
        Aj = sc.nextInt();
        A = new double[Ai][Aj];
        states = Ai;
        for (int i = 0; i < Ai; i++) {
            for (int j = 0; j < Aj; j++) {
                A[i][j] = sc.nextDouble();
            }
        }
        //read B
        Bi = sc.nextInt();
        Bj = sc.nextInt();
        B = new double[Bi][Bj];
        emissions = Bi;
        for (int i = 0; i < Bi; i++) {
            for (int j = 0; j < Bj; j++) {
                B[i][j] = sc.nextDouble();
            }
        }
        //read Pi
        Pii = sc.nextInt();
        Pij = sc.nextInt();
        Pi = new double[Pii][Pij];
        for (int i = 0; i < Pii; i++) {
            for (int j = 0; j < Pij; j++) {
                Pi[i][j] = sc.nextDouble();
            }
        }
        //read observations
        T = sc.nextInt();
        Obs = new int[T];
        for (int i = 0; i < T; i++) {
            Obs[i] = sc.nextInt();
        }
        if (sc != null)
            sc.close();
    }


/**
 * Viterbi algorithm for state estimation.Runs on class variables
 * @return
 */
    public int[] viterbi() {

        HashMap<Integer, int[]> path = new HashMap<Integer, int[]>();
        double[][] delta = new double[1][Aj];
        double[][] deltaTminus1 = new double[1][Aj];
        double prob = 0;
        double maxProb = 0;
        double v = 0;
        double maxV = 0;
        int[] sequencePath = new int[T];
        int[] idx = new int[T];

        for (int j = 0; j < Ai; j++) {
            deltaTminus1[0][j] = (Pi[0][j] * B[j][Obs[0]]);
        }
        maxV = 0;
        ;
        v = 0;

        for (int t = 1; t <= T - 1; t++) // for observation
        {
            delta = new double[1][Aj];
            idx = new int[T];
            for (int i = 0; i < Ai; i++) // for row
            {
                maxProb = 0;
                prob = 0;
                for (int j = 0; j < Aj; j++) // for columns
                {
                    prob = deltaTminus1[0][j] * A[j][i] * B[i][Obs[t]];
                    if (prob > maxProb) {
                        delta[0][i] = prob;
                        maxProb = prob;
                        idx[i] = j;
                        path.put(t, idx);

                    }
                }
            }
            deltaTminus1 = delta;

        }
        // maxT-1
        maxV = 0;
        v = 0;
        int idxPrev = 0;
        for (int i = 0; i < deltaTminus1[0].length; i++) {
            v = deltaTminus1[0][i];
            if (v > maxV) {
                idxPrev = i;
                maxV = v;
            }

        }
        sequencePath[T - 1] = idxPrev;
        
        // backtrack
        int[] currState = null;
        for (int t = T - 1; t > 0; t--) {
            currState = path.get(t);
            idxPrev = currState[idxPrev];
            sequencePath[t - 1] = idxPrev;

        }
        return sequencePath;

    }

    /**
     * Forward algorithm-without scaling
     * @return
     */
    public double[][] alphaPass() {

        double[][] alpha = new double[Ai][T];
        for (int i = 0; i < T; i++) {
            if (i == 0) {
                for (int j = 0; j < Ai; j++) {
                    alpha[j][i] = Pi[i][j] * B[j][Obs[i]];
                }
            } else {
                for (int j = 0; j < Ai; j++) {
                    alpha[j][i] = 0.0;
                    for (int k = 0; k < Ai; k++) {
                        alpha[j][i] += (alpha[k][i - 1] * A[k][j] * B[j][Obs[i]]);
                    }
                }
            }
        }

        return alpha;
    }

    /**
     * Forward algorithm WITH SCALING
     * @return
     */
    public double[][] alphaPassScaled() {

        double[][] alpha = new double[states][T];
        c = new double[T];
        c[0] = 0;
        for (int i = 0; i < T; i++) {
            if (i == 0) // for t=0,initialise
            {
                for (int j = 0; j < states; j++) {
                    alpha[j][i] = Pi[i][j] * B[j][Obs[i]];
                    c[0] += alpha[j][i];
                }
                // scaling
                c[0] = 1 / c[0];
                for (int j = 0; j < states; j++) {
                    alpha[j][i] = c[0] * alpha[j][i];
                }
            } else {
                c[i] = 0;
                for (int j = 0; j < states; j++) {
                    alpha[j][i] = 0.0;
                    for (int k = 0; k < states; k++) {
                        alpha[j][i] += (alpha[k][i - 1] * A[k][j] * B[j][Obs[i]]);
                    }
                    c[i] += alpha[j][i]; // assign c
                }
                // scaling
                c[i] = 1 / c[i];
                for (int j = 0; j < states; j++) {
                    alpha[j][i] = c[i] * alpha[j][i];
                }
            }
        }

        return alpha;
    }


    /**
     * backward algorithm WITH scaling
     * @return
     */
    public double[][] betaPassScaled() {

        double[][] beta = new double[states][T];
        // Let betaT-1(i) = 1, scaled by cT􀀀1
        for (int j = 0; j < states; j++) {
            beta[j][T - 1] = c[T - 1];
        }

        // beta pass
        for (int t = T - 2; t >= 0; t--) {
            for (int i = 0; i < states; i++) {
                beta[i][t] = 0;
                for (int j = 0; j < states; j++) {
                    beta[i][t] += A[i][j] * B[j][Obs[t + 1]] * beta[j][t + 1];
                }
                // scaling with same factor as alpha
                beta[i][t] = c[t] * beta[i][t];
            }
        }

        return beta;
    }

    /**
     * Update matrix estimates 
     */
    public void estimateUpdate() {

        double gammaSum = 0;
        double diGammaSum = 0;
        // update A
        for (int i = 0; i < states; i++) {
            for (int j = 0; j < states; j++) {
                gammaSum = 0;
                diGammaSum = 0;
                for (int t = 0; t < T - 1; t++) { //
                    diGammaSum += di_gamma[t][i][j];
                    gammaSum += gamma[t][i];
                }
                A[i][j] = diGammaSum / gammaSum;
            }
        }

        // update B
        for (int j = 0; j < states; j++) {
            for (int k = 0; k < emissions; k++) {
                gammaSum = 0;
                diGammaSum = 0;
                for (int t = 0; t < T - 1; t++) {
                    gammaSum += gamma[t][j];
                    if (Obs[t] == k) {
                        diGammaSum += gamma[t][j];
                    }
                }
                B[j][k] = diGammaSum / gammaSum;
            }
        }
        for (int i = 0; i < states; i++) {
            Pi[0][i] = gamma[0][i];
        }     
    }


    public void printMatrix(double[][] mat) {
        System.out.print(mat.length); 
        System.out.print(" ");
        System.out.print(mat[0].length);
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                System.out.print(" ");              
                System.out.print(mat[i][j]);
            }
        }
        System.out.println();
    }

    /**
     * Calculate di-gamma and gamma values for Baum Welch
     * @param alpha
     * @param beta
     */
    public void calcGammas(double[][] alpha, double[][] beta) {

        di_gamma = new double[T][states][states];
        gamma = new double[T][states];

        for (int t = 0; t < T - 1; t++) {
            for (int i = 0; i < states; i++) {
                gamma[t][i] = 0;
                for (int j = 0; j < states; j++) {
                    di_gamma[t][i][j] = alpha[i][t] * A[i][j] * B[j][Obs[t + 1]] * beta[j][t + 1];
                    gamma[t][i] += di_gamma[t][i][j];

                }
            }
        }
        // T-1 Special case
        for (int i = 0; i < states; i++) {
            gamma[T - 1][i] = alpha[i][T - 1];
        }

    }

    /**
     * Baum Welch implementations.Uses scaled alpha-beta.Convergence is estimated by 
     * a threshold of non improvement of log probabilities and max count re attempts
     *  after non improvments 
     * @return
     */
    public double baumWelchAlgo() {
        
        int iterations = 0;
        double log_prob = Double.NEGATIVE_INFINITY;
        double log_probPrev = Double.NEGATIVE_INFINITY;
        double log_threshold = 0.001; 
        double[][] alpha;
        double[][] beta;
        final int MAX_ATTEMPT = 10;
        int count = MAX_ATTEMPT;

        while (iterations < ITERATION_LIMIT) {

            // Forward pass
            alpha = alphaPassScaled();
            // Backward pass
            beta = betaPassScaled();
            // calculate gammas;
            calcGammas(alpha, beta);
            // estimate update
            estimateUpdate();

            // P(O | lambda)
            log_prob = 0;
            for (int t = 0; t < c.length; t++) {
                log_prob -= Math.log(c[t]);
            }

            // check for re-evaluation/convergence
            if (iterations != 0) {
                if ((log_prob - log_probPrev) >= log_threshold) {//
                    log_probPrev = log_prob;
                    count = MAX_ATTEMPT;
                } else {
                    if (count >= 0) {
                        count--;
                        log_probPrev = log_prob;
                    } else
                        return log_prob;
                }
            }

            iterations++;
            //System.err.println(iterations);
        }

        return log_prob;
    }

    public void write(String filename,ArrayList<Double> x){
        BufferedWriter outputWriter = null;
        try {
            outputWriter = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < x.size(); i++) {
                // Maybe:
                outputWriter.write(x.get(i)+" ");
                // Or:
                //outputWriter.write(Integer.toString(x[i]);
                //outputWriter.newLine();
              }

            //outputWriter.write(x.toString());
            outputWriter.close();  
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      

    }

    public double baumWelchAlgo_Cpart(String filename) {
        
        int iterations = 0;
        double log_prob = Double.NEGATIVE_INFINITY;
        double log_probPrev = Double.NEGATIVE_INFINITY;
        double log_threshold = 0.01; 
        double[][] alpha;
        double[][] beta;
        final int MAX_ATTEMPT = 10;
        int count = MAX_ATTEMPT;
        double[] history=new double[ITERATION_LIMIT];
        ArrayList<Double> hist=new ArrayList<Double>();
        while (iterations < ITERATION_LIMIT) {

            // Forward pass
            alpha = alphaPassScaled();
            // Backward pass
            beta = betaPassScaled();
            // calculate gammas;
            calcGammas(alpha, beta);
            // estimate update
            estimateUpdate();

            // P(O | lambda)
            log_prob = 0;
            for (int t = 0; t < c.length; t++) {
                log_prob +=  Math.log(c[t]);
            }
            log_prob=-log_prob;
            // check for re-evaluation/convergence
            if (iterations != 0) {
                if ((log_prob - log_probPrev) >= log_threshold) {//
                    log_probPrev = log_prob;
                    count = MAX_ATTEMPT;
                } else {
                    if (count >= 0) {
                        count--;
                        log_probPrev = log_prob;
                    } else{
                        System.err.println(iterations);
                        System.err.println(log_prob);
                       // history[iterations]=log_prob;
                        hist.add(log_prob);
                        write(filename, hist);
                        return log_prob;
                    }
                        
                }
            }
            hist.add(log_prob);
           // history[iterations]=log_prob;
            iterations++;history[iterations]=log_prob;
            //System.err.println(iterations);
        }
        System.err.println(iterations);
        System.err.println(log_prob);
        write(filename, hist);
        return log_prob;
    }
     
  

}