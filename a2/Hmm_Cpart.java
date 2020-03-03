
import java.io.File;
import java.util.Scanner;

public class Hmm_Cpart {
    public static void main(final String[] args) {

        HmmAlgos hmm3 = new HmmAlgos();
        Scanner sc = null;
        try {
             sc=new Scanner(new File("hmm_c_N10000.in"));
            //sc = new Scanner(System.in);

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //read inputss
        //hmm3.scanInputs(sc);
        q10a(sc);
    }

    public static void q7(Scanner sc){
        HmmAlgos hmm = new HmmAlgos();
        double[][] A={
            {0.54,0.26,0.20},
            {0.19,0.53,0.28},
            {0.22,0.18,0.6},
                    };

        double [][] B={
            {0.5,0.2,0.11,0.19},
            {0.22,0.28,0.23,0.27},
            {0.19,0.21,0.15,0.45},
                      };
        double[][] P={
            {0.3,0.2,0.5},
        };
        hmm.setA(A);
        hmm.setB(B);
        hmm.setP(P);
        hmm.readObs(sc);
        hmm.setStatesLength(A.length);
        hmm.setEmissionCount(B[0].length);
        
        //execute Baum Welch
        hmm.baumWelchAlgo_Cpart("q7-10k");
       
        hmm.printMatrix(hmm.A);
        hmm.printMatrix(hmm.B);
        

    }


    public static void q8(Scanner sc){
        HmmAlgos hmm = new HmmAlgos();
        double[][] A={
            {0.1,0.2,0.7},
            {0.5,0.1,0.4},
            {0.4,0.3,0.3},
                    };

        double [][] B={
            {0.6,0.1,0.1,0.2},
            {0.1,0.3,0.4,0.4},
            {0.5,0.2,0.2,0.1},
                      };
        double[][] P={
            {0.3,0.4,0.3},
        };
       
        hmm.setA(A);
        hmm.setB(B);
        hmm.setP(P);
        hmm.readObs(sc);
        hmm.setStatesLength(A.length);
        hmm.setEmissionCount(B[0].length);
        
        //execute Baum Welch
        hmm.baumWelchAlgo_Cpart("q8-10k");
       
        hmm.printMatrix(hmm.A);
        hmm.printMatrix(hmm.B);
        

    }


    public static void q9(Scanner sc){
        HmmAlgos hmm = new HmmAlgos();
        double[][] A={
            {0.1,0.9},
            {0.2,0.8},
              };

        double [][] B={
            {0.6,0.1,0.1,0.2},
            {0.1,0.3,0.4,0.4},
           
                      };
        double[][] P={
            {0.3,0.7},
        };
        double[][] A4={
            {0.1,0.2,0.4,0.3},
            {0.5,0.1,0.2,0,0.2},
            {0.4,0.3,0.1,0.2},
            {0.2,0.3,0.1,0.4},
                    };

        double [][] B4={
            {0.6,0.1,0.1,0.2},
            {0.1,0.3,0.4,0.4},
            {0.5,0.2,0.2,0.1},
            {0.4,0.2,0.1,0.3}
                      };
        double[][] P4={
            {0.3,0.3,0.2,0.1},
        };

        hmm.setA(A);
        hmm.setB(B);
        hmm.setP(P);
        hmm.readObs(sc);
        hmm.setStatesLength(A.length);
        hmm.setEmissionCount(B[0].length);
        
        //execute Baum Welch
        hmm.baumWelchAlgo_Cpart("q9-2_10k");
       
        hmm.printMatrix(hmm.A);
        hmm.printMatrix(hmm.B);
        

    }

    public static void q10a(Scanner sc){
        HmmAlgos hmm = new HmmAlgos();
        double[][] A={
            {0.33,0.33,0.34},
            {0.33,0.33,0.34},
            {0.33,0.33,0.34},
                    };

        double [][] B={
            {0.25,0.25,0.25,0.25},
            {0.25,0.25,0.25,0.25},
            {0.25,0.25,0.25,0.25},
                      };
        double[][] P={
            {0.33,0.33,0.34},
        };
        hmm.setA(A);
        hmm.setB(B);
        hmm.setP(P);
        hmm.readObs(sc);
        hmm.setStatesLength(A.length);
        hmm.setEmissionCount(B[0].length);
        
        //execute Baum Welch
        hmm.baumWelchAlgo_Cpart("q10-a-10k");
       
        hmm.printMatrix(hmm.A);
        hmm.printMatrix(hmm.B);
        

    }

}