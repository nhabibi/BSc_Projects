package com.habibi.util;

import java.util.Iterator;
import java.util.List;

public interface NodeInterface{
	public void addChild(NodeInterface o);
	public NodeInterface getChildAt(int index);
	public Iterator getChildrenIterator();
	public void setRoot(Object o);
	public Object getRoot();
	public boolean hasChildren();
	public void setChilds(List childs);
	public void printLevelOrder();
	public void printPreOrder();
	public int getIntValueOfRoot();
}


