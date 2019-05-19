package com.hpst.threading;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 */

/**
 * @author harsingh14
 *
 */
public class CustomBlockingQueue<E> {

	private Queue<E> queue ; 
	private int max;
	private Lock lock = new ReentrantLock();
	private Condition notFull = lock.newCondition();
	private Condition notEmpty = lock.newCondition();
	
	CustomBlockingQueue(int size){
		queue = new LinkedList<>();
		this.max =size;
	}
	
	
	public void put(E e) throws InterruptedException {
		lock.tryLock();
		try {
			while(queue.size()==max) {
				//wait for someone to pick some items 
				notFull.await();
			}
			queue.add(e);
			notEmpty.signalAll();
		}finally {
			lock.unlock();
		}
	}
	
	public E get() throws InterruptedException {
		lock.tryLock();
		try {
			while(queue.size()==0) {
				//wait someone to put an item
				notEmpty.await();
			}
			E element = queue.remove();
			notFull.signalAll();
			return element;
			
		} 
		finally {
			lock.unlock();
		}
	}
}
