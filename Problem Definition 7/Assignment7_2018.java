/**
 * @author: Gaurav Shyam Sunder Kedia
 */
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class Assignment7_2018{
 public static void main(String[] args) {
   //Q1 =======================================
		int[] data = new int[10];
		System.out.print("Original Data    :");		
		for(int j = 0; j < data.length; j++) {
			data[j] = (int)(Math.random()*10);
			System.out.printf("%d   ",data[j]);
		}
		System.out.println();
		ThreadQueue tq = new ThreadQueue();
		new TA(tq,1,data).start();
		new TB(tq,2,data).start();
		new TC(tq,3,data).start();
		new TA(tq,4,data).start();
		new TB(tq,5,data).start();
		new TC(tq,6,data).start();
		new TA(tq,7,data).start();
		new TB(tq,8,data).start();
		new TC(tq,9,data).start();
		new TA(tq,10,data).start();
		for(int j =0;j<10;j++){
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){}
			tq.next();
		}
   //==========================================
 }
}
//Threads for Question 1 here ===========================
class ThreadQueue{
	Queue<Semaphore>queue = new LinkedList<Semaphore>();
	Lock lock = new ReentrantLock();
	public void join(Semaphore sem){
		lock.lock();
		try{
			queue.add(sem);
		}finally{lock.unlock();}
		try{
			sem.acquire();
		}catch(InterruptedException e){}
	}
	public void next(){
		lock.lock();
		try{
			if(queue.size()>0){
				Semaphore ss = queue.poll();
				ss.release();
			}
		}finally{lock.unlock();}
	}
	public int size(){return queue.size();}
}
// To join the queue a thread creates a binary Semaphore with an initial value of 0 and waits on it.

class TA extends Thread{
	Semaphore sem = new Semaphore(0);
	ThreadQueue tqueue;
	int number;
	int d[];
	public TA(ThreadQueue tq,int n,int d1[]){tqueue = tq;number = n; d=d1;}
	public void run(){
		//join queue and wait turn
		tqueue.join(sem);
			System.out.print("Incremented by 2 :");
			for(int j = 0; j < d.length; j++) {
				d[j] = d[j] + 2;
				System.out.printf("%d   ",d[j]);
			}
			System.out.println();
	}
}

class TB extends Thread{
	Semaphore sem = new Semaphore(0);
	ThreadQueue tqueue;
	int number;
	int d[];
	public TB(ThreadQueue tq,int n,int d1[]){tqueue = tq;number = n;d=d1;}
	public void run(){
		//join queue and wait turn
		tqueue.join(sem);
		System.out.print("Decrementing by 1:");
			for(int j = 0; j < d.length; j++) {
				d[j] = d[j] - 1;
				System.out.printf("%d   ",d[j]);
			}
			System.out.println();
	}
}

class TC extends Thread{
	Semaphore sem = new Semaphore(0);
	ThreadQueue tqueue;
	int number;
	int d[];
	public TC(ThreadQueue tq,int n,int d1[]){tqueue = tq;number = n; d=d1;}
	public void run(){
		//join queue and wait turn
		tqueue.join(sem);
			System.out.print("Squared          :");
			for(int j = 0; j < d.length; j++) {
				d[j] = (int) Math.pow(d[j], 2);
				System.out.printf("%d   ",d[j]);
			}
			System.out.println();
			System.out.println();
			System.out.println("Next Cycle:");
	}
}
//=======================================================
//=======================================================
//class WaitEventBarrier{} here
/*
class WaitEventBarrier{
	Semaphore sem = new Semaphore(0);
	Queue<Semaphore>queue = new LinkedList<Semaphore>();
	Lock lock = new ReentrantLock();
	public void waitEvent()
	{
		lock.lock();
		try{
			queue.add(sem);
		}finally{lock.unlock();}
		try{
			sem.acquire();
		}catch(InterruptedException e){}
	}
	
	public void releaseAll(){
		while(queue.size()>0){
			Semaphore ss = queue.poll();
			ss.release();
		}
	}
}
*/
//=======================================================