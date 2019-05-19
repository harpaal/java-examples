package com.hpst.threading;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;

public class TestCompletableFuture {

	public static void main(String[] args) {
		
		TestCompletableFuture testCompletable = new TestCompletableFuture();
		ExecutorService executors = Executors.newFixedThreadPool(10);
		testCompletable.hitDemo(executors);
		 
		 
	}

	
	
	private void hitDemo(ExecutorService executors) {
		CompletableFuture.supplyAsync(()-> getOrder() , executors)
						 .thenApply(order-> enrichOrder(order))
						 .thenApply(order->processOrder(order))
						 .whenComplete((order, ex)-> System.out.println(ex.getMessage()))
						 //.handle(handleExcpt())
						 .thenAccept(order->sendMail((Order) order));
		
	}



	private BiFunction<? super Order, Throwable, ? extends Object> handleExcpt() {
		return (order,expt)->{
			 if(expt!=null) {
				 System.out.println("expt"+expt.getMessage());
				 return expt;
			 }
			 else
				 return order;
		 };
	}
	

	




	private Order sendMail(Order order) {
		System.out.println("Sending Mail of order "+order.getOrder());
		return order;
	}



	


	private Order processOrder(Order order) {
		
		System.out.println("Processing of order "+order.getOrder());
		int c = 1/0;
		System.out.println("Processing of order "+order.getOrder() + c);
		
		return order;
		}



	private Order enrichOrder(Order order) {
		System.out.println("Enrich order "+order.getOrder());
		return order;
	}



	private Order getOrder() {
		System.out.println("getOrder()"+ 1);
		return new Order(1);
	}
}


 class Order{
	 
	 int order;
	 
	 public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	Order(int order){
		 this.order = order;
	 }
	 
	 
 }
 

 class FailedOrder extends Order{

	FailedOrder(int order) {
		super(order);
System.out.println("failed");
	}
	 
	 
	 
 }