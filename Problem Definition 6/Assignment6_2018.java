/*
 *
 * Author: Gaurav Shyam Sunder Kedia
*/
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

public class Assignment6_2018 {
  public static void main(String[] args) {
   //Test Q1 ==================================================
   Buffer<Integer> buffer1 = new Buffer<Integer>(10);
   Buffer<Integer> buffer2 = new Buffer<Integer>(10);
   new Thread(new Producer(buffer1)).start();
   new Thread(new Filter(buffer1, buffer2)).start();
   new Thread(new Consumer(buffer2)).start();
   //End Q1 ==================================================
   
   //Test Q2 ================================================= 
	int N = 4;
	LatchBarrier LB = new LatchBarrier(N);
    new Thread(new Worker(LB)).start();
	try{
		Thread.sleep(200);
	}catch(InterruptedException e){}
    new Thread(new Worker(LB)).start();
	try{
		Thread.sleep(400);
	}catch(InterruptedException e){}
    new Thread(new Worker(LB)).start();
	try{
		Thread.sleep(600);
	}catch(InterruptedException e){}
    new Thread(new Worker(LB)).start();
	try{
		Thread.sleep(800);
	}catch(InterruptedException e){}
   //End Q2 ==================================================
 }
}
//========================================================
// Code for Buffer class 
// Do not edit this class.

class Buffer<E>{
   private int max;
   private int size = 0;
   private ArrayList<E> buffer;
    private Lock lock = new ReentrantLock();
   private Condition notFull = lock.newCondition();
   private Condition notEmpty = lock.newCondition();
   public Buffer(int s){
   	 buffer = new ArrayList<E>();
   	 max = s;
   }
   public void put(E x){
   	lock.lock();
   	try{
	   	while(size == max){
	   		try{
	   			notFull.await();
	   		}catch(InterruptedException e){}
	   	}
	   	buffer.add(x);
	   	size++;
	   	if(size - 1 == 0) notEmpty.signal();
	  }finally{lock.unlock();}
   }
   public E get(){
   	lock.lock();
   	try{
	   	while(size == 0){
	   		try{
	   			notEmpty.await();
	   		}catch(InterruptedException e){}
	   	}
	   	E temp = buffer.get(0);
	   	buffer.remove(0);
	   	size--;
	   	if(size + 1 == max) notFull.signal();
	   	return temp;
	 }finally{lock.unlock();}
 }
}
//============================================================
// put thread code for Question 1 here
class Producer implements Runnable{
	Buffer<Integer> b1;
	public Producer(Buffer<Integer> k){
		b1 = k;
	}
	public void run(){
		for(int j=0;j<10;j++){
			Integer x = new Integer((int)(Math.random()*999));
			b1.put(x);
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){}
		}
	}
}

class Filter implements Runnable{
	Buffer<Integer> b1;
	Buffer<Integer> b2;
	public Filter(Buffer<Integer> k,Buffer<Integer> l){
		b1 = k;
		b2 = l;
	}
	public void run(){
		for(int j=0;j<10;j++){
			Integer x = b1.get();
			if((x%5==0) || (x%7==0) || (x%11==0))
				continue;
			else
				b2.put(x);
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){}
		}
	}
}

class Consumer implements Runnable{
	Buffer<Integer> b2;
	ArrayList<Integer> data = new ArrayList<Integer>();

	public Consumer(Buffer<Integer> k){
		b2 = k;
	}
	public void run(){
		System.out.print("Buffer data:");
		while (true) {
			Integer x = b2.get();
			data.add(x);
			Collections.sort(data);
			for(int j=0;j<data.size();j++){
				System.out.print(data.get(j));
				System.out.print(", ");
			}
			
			System.out.println("");
			
			try{
				Thread.sleep(200);
			}catch(InterruptedException e){}
		}
	}
}

//End Question 1 =============================================
//============================================================
//Question 2 =================================================
// put code for LatchBarrier class here
class LatchBarrier{
	private int n;
	private int count = 0;
	private Lock lock = new ReentrantLock();
	
	private Condition notFull = lock.newCondition();

	LatchBarrier(int capacity){
		n = capacity;
	}
	public void waitBarrier(){
		lock.lock();
		try{
			count++;
			while (count != n){
				try{
					notFull.await();
				}catch(InterruptedException e){}
			}
			if(count == n){
				notFull.signalAll();
			}
		} finally {
			lock.unlock();
		}
	}
}
//End LatchBarrier ==========================================
// put code for threads testing LatchBarrier here
class Worker implements Runnable{
	LatchBarrier latchbarrier;
	int num;
	public Worker(LatchBarrier LB){
		this.latchbarrier = LB;
	}
	public void run(){
		try{
			latchbarrier.waitBarrier();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		System.out.print("All Threads Released");
	}
}
//End Question 2  ===========================================