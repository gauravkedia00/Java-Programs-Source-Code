/**
 *
 * Author: Gaurav Shyam Sunder Kedia
 *
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Assignment5_2018{
    public static void main(String[] args) {
    }
}

//Question 1
class ViewingStand{
	private boolean seats[];
	private int freeSeat;
	private ReentrantLock locks[];
	int i=0;
	ViewingStand(int n) {
		seats = new boolean[n];
		locks= new ReentrantLock[n];
	}

	public void findSeat()  {
		int i=0;

		while(!locks[i].tryLock()) {
			i += 1;
			i %= locks.length;
		}
		freeSeat = i;
		locks[i].lock();
	}

	public void leaveSeat() {
		try {
		}
		finally  {
			locks[i].unlock();
		}
	}
}

//Question 2
class SharedArray{
    private int data[];
    private Lock locks[];
    public SharedArray(int f[]){
        data = new int[f.length];
        locks = new ReentrantLock[f.length];
        for(int j = 0; j < f.length;j++){
            data[j] = f[j];
            locks[j] = new ReentrantLock();
        }
    }
    public void assign(int j, int x){
        //assume 0 <= j < data.length
        locks[j].lock();
        try{
            data[j] = x;
        }finally{locks[j].unlock();}
    }
	// Thread Safe sum
    public int sum(){
       int sum = 0;

        for (int i = 0; i < data.length; i++) {
		
            locks[i].lock();
            try {
                sum = sum + data[i];
            } finally {
                locks[i].unlock();
            }
        }
		return sum;
    }
}
