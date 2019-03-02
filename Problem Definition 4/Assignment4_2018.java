/**
 *
 * Author: Gaurav Shyam Sunder Kedia
 *
 */

import java.util.*;
import java.util.concurrent.*;

public class Assignment4_2018{
	public static void main(String args[]){
     //No test code required for this assignment
	}
}
//Question 1 class =======================================
final class Point{
	private final int x, y;
	public Point(int x0, int y0){x = x0; y = y0;}
 	public int x(){return x;}
	public int y(){return y;}
	public String String(){return "("+x+","+y+")";}
	public boolean equals(Object ob){
		if(!(ob instanceof Point)) return false;
		Point p = (Point)ob;
		return p.x == x && p.y == y;
	}
	public int hashCode(){return 31*x*y;}
}

class SetPoint{
	Set<Point> set = new HashSet<>();
	
	public synchronized void add(Point p){
		set.add(p);
	}
	public synchronized boolean search(Point p){
		return set.contains(p);
	}
	public synchronized List<Point> getAllX(int x) {
		List<Point> l= new ArrayList<Point>();
		for(Point p : set){
			if(p.x()== x){
				l.add(p);
			}
		}
		return l;	
	}
	public synchronized String toString(){
		return set.toString();
	}
}

//Question 2 ==============================================
//Implementation of class CircularQueue<E>

class CircularQueue<T> implements Iterable<T>{
	private T queue[];
	private int head, tail, size;
	
	public CircularQueue(){
		queue = (T[])new Object[20];
		head = 0; tail = 0; size = 0;
	}	
	
	public CircularQueue(int n){ //assume n >=0
		queue = (T[])new Object[n];
		size = 0; head = 0; tail = 0;
	}
	
	public synchronized boolean join(T x){
		if(size < queue.length){
			queue[tail] = x;
			tail = (tail+1)%queue.length;
			size++;
			return true;
		}
		else return false;
	}
	
	public synchronized T top(){
		if(size > 0)
			return queue[head];
		else
			return null;
	}
	
	public synchronized boolean leave(){
		if(size == 0) return false;
		else{
			head = (head+1)%queue.length;
			size--;
			return true;
		}
	}
	public synchronized boolean full(){return (size == queue.length);}
	public synchronized boolean empty(){return (size == 0);}
	
	public synchronized Iterator<T> iterator(){
		return new QIterator<T>(queue.clone(), head, size);
	}
	
	private static class QIterator<T> implements Iterator<T>{
		private T[] d; private int index; 
		private int size; private int returned = 0;
		QIterator(T[] dd, int head, int s){
			d = dd; index = head; size = s;
		}
		
		public boolean hasNext(){ return returned < size;}
		public T next(){
			if(returned == size) throw new NoSuchElementException();
			T item = (T)d[index];
			index = (index+1) % d.length;
			returned++;
			return item;
		}
		public void remove(){}
	}
}