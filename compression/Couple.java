
public class Couple<T,A> {
	private T t;
	private A a;
	
	public Couple(T t,A a) {
		this.t=t;
		this.a=a;
	}
	
	public T getT() {
		return t;
	}
	public A getA() {
		return a;
	}
	public void setT(T t) {
		this.t=t;
	}
	public void setA(A a) {
		this.a=a;
	}
	
}
