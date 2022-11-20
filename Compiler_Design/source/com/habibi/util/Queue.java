package com.habibi.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections.PriorityQueue;

public class Queue implements PriorityQueue {
	
	private List data	;
	
	protected List getData()
	{
		return data;
	}
	
	public Queue (){
		data=new LinkedList();
	}
	public Queue (Collection c){
		data=new LinkedList(c);
	}
	public void insert(Object p0){
		getData().add(p0);
	}
	
	public void clear(){
		getData().clear();
	}
	
	public Object peek() throws java.util.NoSuchElementException{
		return getData().iterator().next();
	}
	
	public Object pop() throws java.util.NoSuchElementException
	{
		Object result=peek();
		getData().remove(0);
		return result;
	}
	
	public boolean isEmpty()
	{
		return getData().isEmpty();
	}
	
	
}

