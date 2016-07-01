import java.util.*;
import java.io.*;
import java.util.Scanner;


public class MinCutContract {

	
	public static int getDegree (List<List<List<Integer>>> y, int i) {
//		System.out.println("degree of vertex = "+(y.get(i).size() - 1));
		return y.get(i).size() - 1; 
	}
	
	public static int cntVertices (List<List<List<Integer>>> y) {
//		System.out.println("# of vertices = "+(y.size()));
		return y.size();
	}
	
	public static void contract (List<List<List<Integer>>> y, int r1, int c) throws Throwable {
		//choose a random edge by choosing its two vertices
		//identify the four Array elements
		//List<Integer> firstVertex = y.get(r1).get(0);			//this line of code will lead to aliasing
		
		//System.out.println("r1 = "+r1+"; c = "+c);
		
		List<Integer> firstVertex = new ArrayList<>();			//val1 is the 1st vertex, which could be a supernode
		//System.out.println("size of 1st vertex is: "+y.get(r1).get(0).size());
			for (int i = 0; i < y.get(r1).get(0).size(); i++) {
				firstVertex.add(y.get(r1).get(0).get(i).intValue());
			}
		
		//List<Integer> secondVertex = y.get(r1).get(c);		//this line of code will lead to aliasing
			
		List<Integer> secondVertex = new ArrayList<>();			//val2 is the 2nd vertex, which could be a supernode
			for (int i = 0; i < y.get(r1).get(c).size(); i++) {
				secondVertex.add(y.get(r1).get(c).get(i).intValue());
		}
			
		
		//System.out.println("1st vertex = "+firstVertex);
		//System.out.println("2nd vertex = "+secondVertex);
		
		int r2 = 0; 											//row # where val2 is the 1st element
		
		for (int i = 0; i < cntVertices(y); i++) {
			if (i != r1 && Arrays.equals(y.get(i).get(0).toArray(),secondVertex.toArray())) {
				r2 = i;							
				break;
			}
		}
		
		//System.out.println("r1 with the 2nd vertex = "+y.get(r1));
		//System.out.println("r2 with the 2nd vertex = "+y.get(r2));

		
				
		//for the two Array elements in the first chosen Array, expand the first to include contents of the both elements, eliminate the 2nd chosen element
		y.get(r1).get(0).addAll(secondVertex);
		
		//System.out.println("r1 after adding the 2nd vertex = "+y.get(r1));
		
		List<Integer> newSupernode = y.get(r1).get(0);
		y.get(r1).remove(c);      
		
		//System.out.println("r1 after removing the 2nd vertex = "+y.get(r1));
				

		
		//add additional elements from the 2nd chosen Array; delete the 2nd Array
		int c2 = 0;												//this is the column index of the 4th vertex 
		for (int i = 1; i < y.get(r2).size(); i++) {
			//System.out.println("r2 row element: "+y.get(r2).get(i));
			//System.out.println("1st vertex is "+firstVertex);
			//if (Arrays.equals(y.get(r2).get(i).toArray(), firstVertex.toArray())) {
			if (y.get(r2).get(i).containsAll(firstVertex)) {	
				c2 = i;
				//System.out.println("Im here!");
			}
		}
		
		//System.out.println("r2 with the 2nd vertex = "+y.get(r2));
		
		//int c2 = y.get(r2).indexOf(firstVertex);				//this is the column index of the 4th vertex 
		//System.out.println("c2 for the 4th vertex = "+c2+", is element "+y.get(r2).get(c2));			

		if (c2 <= 0) {
			throw new Throwable("error in finding the 4th vertex!"); 					//System.out.println("error in finding 4th vertex!");
		}
		
		//System.out.println("r2 has degree: "+r2+"; c2 = "+c2);
		y.get(r2).remove(c2);
		y.get(r2).remove(0); 
		
		
		y.get(r1).addAll(y.get(r2));
		y.remove(r2);
		
		
		//System.out.println("2nd vertex = "+secondVertex);

		
		
		//expand all remaining reference to the two vertices to include contents of the both vertices
		for (int i = 0; i < y.size(); i++) {
			for (int j = 1; j < y.get(i).size(); j++) {
				if (Arrays.equals(y.get(i).get(j).toArray(), secondVertex.toArray()) || Arrays.equals(y.get(i).get(j).toArray(), firstVertex.toArray())) {
					y.get(i).set(j,newSupernode);
				}
			}
		}
		
		//delete any element that repeats / belongs to the first element
		for (int i = 0; i < y.size(); i++) {
			for (int j = y.get(i).size()-1; j > 0 ; j--) {
				//System.out.println("comparing "+y.get(i).get(0) + " and "+y.get(i).get(j));
				
				if (y.get(i).get(0).containsAll(y.get(i).get(j))) {
					//System.out.println("Inside "+y.get(i));
					//System.out.println(y.get(i).get(j) + " will be removed!!!");
					y.get(i).remove(j);
				}
				
			}
		}

	}
	
	public static void print_Array(List<List<List<Integer>>> y) {
		System.out.println("_____________________");
		for (int i = 0; i < cntVertices(y); i++) {
			for (int j = 0; j < y.get(i).size(); j++) {
				System.out.print(y.get(i).get(j)+ "  "); 
			}
			System.out.println("");
		}
		System.out.println("=====================");
	}
	
	
	public static int MinCut(List<List<List<Integer>>> y) throws Throwable {
		Random generator = new Random();
		int r, c = 0;				//r is the random row, c is the random column
		
		if (cntVertices(y) == 2) {
			return getDegree(y,0);
		}
		
		while (cntVertices(y) > 2) {
			r = generator.nextInt(cntVertices(y));				//this gets the random row
			while (c == 0) {
				c = generator.nextInt(getDegree(y,r));			//this gets the random column
			}
			//r = 0;
			//c = 2;
			//System.out.println("r = "+r);
			//System.out.println("c = "+c);
			
			//following is the contraction step
			//print_Array(y);
			contract(y, r, c);
			c = 0;		
			//print_Array(y);
		}
		
		return getDegree(y,0);
	}
	
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
        
        //List<List<List<Integer>>> z = new ArrayList<>(x);
        //System.out.println("here is z: ");
        //print_Array(z);
         
        
        int n = 200;
        int minCut = n*n; 
        
        
        for (int i = 0; i < (int) Math.round((n*n-n)/2.); i++) {
 	
    		
    		// 1. Create a File and Scanner objects
            File inputFile = new File("kargerMinCut.txt");		//kargerMinCut
            Scanner input = new Scanner(inputFile); 
            Scanner line;
            
            // 2. read the content and then store in an array using a loop
            
        	List<List<List<Integer>>> x = new ArrayList<>();
            
        	
            while (input.hasNextLine()) {  
            	
                  String inputEdges = input.nextLine();   
                  line = new Scanner(inputEdges);    
                  
                  List<List<Integer>> row = new ArrayList<>();
                  
                  //System.out.println("line: "+line);
                  
                  while (line.hasNextInt()) {
                	  List<Integer> intArray = new ArrayList<>();
                	  intArray.add(line.nextInt());
                      row.add(intArray);
                      
                  }
                  
                  x.add(row);
                       
               }       
             // 3. close the file and print the result
            input.close(); 
            
            
        	
/*    		List<Integer> intArray1 = new ArrayList<>();
    		intArray1.add(1);

    		List<Integer> intArray2 = new ArrayList<>();
    		intArray2.add(2);

    		List<Integer> intArray3 = new ArrayList<>();
    		intArray3.add(3);

    		List<Integer> intArray4 = new ArrayList<>();
    		intArray4.add(4);
            
            List<List<Integer>> row1 = new ArrayList<>();
            row1.add(intArray1);
            row1.add(intArray2);
            row1.add(intArray3);
            
            List<List<Integer>> row2 = new ArrayList<>();
            row2.add(intArray2);
            row2.add(intArray1);
            row2.add(intArray3);
            row2.add(intArray4);
            
            List<List<Integer>> row3 = new ArrayList<>();
            row3.add(intArray3);
            row3.add(intArray1);
            row3.add(intArray2);
            row3.add(intArray4);
            
            List<List<Integer>> row4 = new ArrayList<>();
            row4.add(intArray4);
            row4.add(intArray2);
            row4.add(intArray3);
    		
            x.add(row1);
            x.add(row2);
            x.add(row3);
            x.add(row4);  */
            
            //System.out.println("here is x: ");
            //print_Array(x);
            
            
        	//System.out.println("i = "+i);
        	int iCut = MinCut(x);

            
//            System.out.println("here is z: ");
//            print_Array(z);
        	//System.out.println("iCut = "+iCut);
        	if (iCut < minCut) {
        		minCut = iCut;
        	}
        }
        
        System.out.println("final minimum cut is "+minCut);
        
	}

}
