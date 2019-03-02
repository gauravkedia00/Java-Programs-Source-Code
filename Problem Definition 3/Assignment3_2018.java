/**
 *
 * Author: Gaurav Shyam Sunder Kedia
 *
 */
import java.util.*;
import java.util.concurrent.*;

public class Assignment3_2018 {
	static final int N = 1000000;
	static final int RangeSize = 1000;
	public static void main(String[] args) {
    //Test for Question 1 
		int dataQ1[] = new int[1000000];
        for(int j = 0; j < dataQ1.length;j++)
            dataQ1[j] = (int)(Math.random()*dataQ1.length);
        //claculate min value and frequency of occurrence
		int nProc = Runtime.getRuntime().availableProcessors();
		ExecutorService pool = Executors.newFixedThreadPool(nProc);
		ArrayList <Future<Pair>> future = new ArrayList <Future<Pair>>();
			
		int[] index = new int[N/RangeSize+1];
		for (int i=0; i<=index.length-1; i++) {
       		index[i] = i*RangeSize;
		}
		for(int j = 0; j < index.length-1 ;j++){
		   Future<Pair> f = pool.submit(new MinFreqFinder(dataQ1,index[j],index[j+1]));
		   future.add(f);
		}
		Pair result[] = new Pair[index.length - 1];
		for(int j = 0; j < result.length; j++){
		    try {
		      Future<Pair> f = future.get(j);
		      result[j] = f.get();
		    }
		    catch (InterruptedException e) {}
		    catch (ExecutionException e) {};
		}
		pool.shutdown();
		
		int min = result[0].min;
		for(int j=1;j<result.length;j++)
			if(result[j].min < min)
				min = result[j].min;
		int freq = 0;
		for(int j=0;j<result.length;j++)
			if(result[j].min == min)
				freq = freq + result[j].freq;
			
		System.out.println("Min: "+min);
		System.out.println("Freq: "+freq);
    
    //=======================================================
    //Question 2 - use fork-join pool to optimise algorithm
    //=======================================================
	ForkJoinPool fjPool = new ForkJoinPool();
	fjPool.invoke(new mergeSort(dataQ1,0,dataQ1.length));
	// wait for the pool to finish 
	fjPool.shutdown();
	// check if array is sorted
	for (int j = 0; j < dataQ1.length - 1; j++) {
		if (dataQ1[j] > dataQ1[j + 1]) {
            System.out.println("Elements are not sorted") ;
            break;
        }
    }
	System.out.print("Elements are sorted");
	}
}

//***************Question 1*********************
class MinFreqFinder implements Callable<Pair>{
	private int f[];
	private int lb, ub;
	private Pair result;
	MinFreqFinder(int d[], int a, int b){
		f = d; lb = a; ub = b;
	}
	public Pair call(){//assume lb <=ub
		int min = f[lb]; int freq = 1;
		for(int j = lb+1; j < ub; j++){
		if(f[j] == min) freq++;
			else if(f[j] < min){
				min = f[j];
			 freq = 1;
			}
		else ; //skip
		}
	return new Pair(min,freq);
	} 
}

class Pair{
	public int min;
	public int freq;
	public Pair(int x, int y){min = x; freq = y;}
	public int min(){return min;}
	public int freq(){return freq;}
}
//=================================================

class mergeSort extends RecursiveAction{
	static final int BaseBlockSize = 500;
	int lb, ub;
	int[] data;
	mergeSort(int[] dt, int a, int b){
		data = dt;
		lb=a; ub=b;
	}
	protected void compute(){
		if((ub-lb) <= BaseBlockSize){
			insertionSort(data, lb, ub);
		}
		else{
			int mid = (lb+ub)/2;
			mergeSort left = new mergeSort(data,lb,mid);
			mergeSort right = new mergeSort(data,mid,ub);
			left.fork();
			right.fork();
			right.join();
			left.join();
			merge(data,lb,mid,ub);
		}
	}
	private void insertionSort(int dt[], int a, int b){
		for(int i = a; i < b; i++){
			int j = i;
			while(j > a && dt[j] < dt[j-1]){
				int temp = dt[j]; dt[j] = dt[j-1]; dt[j-1] = temp;
				j--;
			}
		}
    }
	
	private void merge(int f[], int lb, int mid, int ub){
		int c[] = new int[ub-lb];
        int k = 0;int j = lb; int h = mid;
        while(j < mid && h < ub){
            if(f[j] <= f[h]){
                c[k] = f[j];
                j++;
            }
            else{
                c[k] = f[h];
                h++;
            }
            k++;
        }
        while(j < mid){ c[k] = f[j];  k++; j++; }
        while(h < ub){c[k] = f[h]; k++; h++;}
        //Now copy data back to array
        for(int p = 0; p < c.length;p++)
            f[lb+p] = c[p];
    }
}