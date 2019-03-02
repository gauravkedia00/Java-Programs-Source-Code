/*********************
*
*	Author: Gaurav Shyam Sunder Kedia
*/

public class Assignment1_2018{
   public static void main(String[] args){
	//=====================================================
    //Test Code for Question 1
	
	 Thread t1 = new Coin();
	 t1.start();
	 
	//=====================================================
	//Test Code for Question 2
	
	 int[] array = new int[1000];
	 int[] result = new int[6];
	 int count = 0;
	 Thread t2 = new Question2(array,0,array.length/2);
	 Thread t3 = new Question2(array,array.length/2,array.length);
	 t2.start();
	 t3.start();
	 
	 try{
		t2.join();
		t3.join();
	 }catch(InterruptedException e){}
	 
	 for(int i=1;i<7;i++){
		for(int j=0;j<array.length;j++){
			if(array[j] == i)
				count++;
		}
		System.out.println("Frequency of " +i+" is :"+count);
		count = 0;
	 }
	 
	//=====================================================
	Test Code for Question 1
	
	int[] array = new int[1000];
	int result1,result2,max;
	
	for(int k=0;k<array.length;k++){
		array[k] = (int)(Math.random()*100000);
	}
	 Question3 t4 = new Question3(array,0,array.length/2);
	 Question3 t5 = new Question3(array,array.length/2,array.length);
	 t4.start();
	 t5.start();
	 
	 try{
		t4.join();
		t5.join();
	 }catch(InterruptedException e){}
	
	result1=t4.getResult();
	result2=t5.getResult();
	
	System.out.println(result1);
	System.out.println(result2);
	
	if((result1>result2) || (result1==result2))
		max = result1;
	else max = result2;
	System.out.println("Max element is :"+max);
   }
}

	//=====================================================
class Coin extends Thread{
   private int head,tail;
    Coin(){
		head=0;tail=0;
    }
    public void run(){
    for(int k=0;k<1000;k++)
	{
		int coin = (int)(Math.random()*2);
		if (coin == 0)
			head++;
		else tail++;
	}
	if( Math.abs(head - tail) > 50)
       System.out.println("biased coin");
    else
	   System.out.println("fair coin");
  }
 }
	//=========================================================

class Question2 extends Thread{
	private int[] sharedarray;
	private int lowerband; 
	private int upperband;
	Question2(int[] a, int lb, int ub){
	sharedarray = a;
	lowerband = lb;
	upperband = ub;
	}
	public void run(){
    for(int k=lowerband;k<upperband;k++){
		sharedarray[k] = (int)(Math.random()*6+1);	
		}
	}
}
	//=========================================================
	
class Question3 extends Thread{
	private int[] sharedarray;
	private int lowerband; 
	private int upperband;
	private int result;
	Question3(int[] a, int lb, int ub){
	sharedarray = a;
	lowerband = lb;
	upperband = ub;
	}
	public void run(){
	int max = -1;
    for(int i=lowerband;i<upperband;i++){
		if(sharedarray[i]>max)
			max = sharedarray[i];
	}
	result = max;
 }
	public int getResult(){
		return result;
	}
}