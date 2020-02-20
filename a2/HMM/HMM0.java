import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class HMM0 { 
    public static void main(String[] args) {

        HMM hmm = new HMM();
        //Scanner sc =new Scanner(System.in);
        Scanner sc =null;
        
        try {
         sc =new Scanner(new File("sample_00.in"));
        hmm.read_inputs(sc);
      
              
        sc.close();
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          
 
   
        
        
       
     

        hmm.next_emission();
    } 
} 