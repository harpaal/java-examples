package com.hpst.threading;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestThreadLocal {

	public static void main(String args[]) {
		ExecutorService executors = Executors.newFixedThreadPool(10);
		CompletableFuture<Integer> completTableFututre = CompletableFuture.supplyAsync(()->new DateTask(),executors)
						.thenApply(future->future.call())
						.thenApply(x->{System.out.println(x);return x;});
						
		List<Integer> collect = IntStream.range(0,10).mapToObj(processTask(completTableFututre)).collect(Collectors.toList());			
		System.out.println(collect);
		executors.shutdown();
							
		
	}

	private static IntFunction<? extends Integer> processTask(CompletableFuture<Integer> completTableFututre) {
		return x->{
			try {
				return completTableFututre.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return x;
		};
	}
}


class ThreadSafeDateFormatter{
	public static ThreadLocal<SimpleDateFormat> threadLocaldateFormat= 
			ThreadLocal.withInitial(()-> new SimpleDateFormat("yyyy-MM-dd"));	
	
}

class DateTask implements Callable<Integer> {

	@Override
	public Integer call() {
		SimpleDateFormat simpleDateFormat = ThreadSafeDateFormatter.threadLocaldateFormat.get();
		System.out.println(simpleDateFormat.getCalendar().getTime() + this.getClass().getName());
		return 1;
	}
	
}
