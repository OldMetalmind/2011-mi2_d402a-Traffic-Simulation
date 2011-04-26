package utils;

public class Pair<L,R> {
	
	L left;
	R right; 
	
	public Pair(L left , R right){
		this.left = left;
		this.right = right;
	}	
	
	public void setLeft(L left){
		this.left = left;
	}
	
	public void setRight(R right){
		this.right = right;
	}
	
	public L getLeft(){
		return this.left;
	}
	
	public R getRight(){
		return this.right;
	}
	
	public String toString(){
		return this.left+" "+this.right;
	}	
}
