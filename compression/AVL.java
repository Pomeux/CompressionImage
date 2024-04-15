public class AVL {
	
	private Quadtree  racine;
	
	private int bal;
	
	private AVL g;
	private AVL d;
	
	
	
	public AVL ( Quadtree x) {
		
		this.racine=x;
		this.bal=0;
		
		this.g=null;
		this.d=null;
	 }
	public String toString() {
		String s=racine+ " ecart: "+racine.getEcartMaximum();
		if(g!=null) {
			s+=" g "+g.racine+" ecart: "+g.racine.getEcartMaximum();
		} else {
			s+=" g null";
		}
		if(d!=null) {
			s+=" d "+d.racine+" ecart: "+d.racine.getEcartMaximum();
		} else {
			s+=" d null";
		}
		return s;
	}

	public int nbElement(AVL avl) {
		if(avl==null) {
			return 0;
		}
		return nbElement(avl.g)+nbElement(avl.d)+1;
	}
	
	 public Quadtree getracine(){
		 return this.racine;
	 }
	 public int getbal (){
		 return this.bal;
	 }
	 
	 public AVL getg(){
		 return this.g;
	 }
	 
	  public AVL getd (){
		 return this.d;
	 }
	 
	 

	
	
	public Couple<AVL,Integer> inserer(AVL avl,Quadtree x) {
	
	int h;
	
	if(avl==null || avl.racine==null) {
		
		AVL A=new AVL(x);
		A.bal=0;	
		
		return new Couple<AVL,Integer>(A,1);
	}

	else if(x.getEcartMaximum()>=avl.racine.getEcartMaximum()) {
	
		Couple<AVL,Integer> c=inserer(avl.d,x);
		avl.d=c.getT();
		h=c.getA();
	
		
	
	} else {
		Couple<AVL,Integer> c=inserer(avl.g,x);
		avl.g=c.getT();
		h=c.getA();
		h=-h;
	}
	
	
	if(h==0) {
	
		return new Couple<AVL,Integer>(avl,0);
	} else {
		

	
		avl.bal=avl.bal+h;

	
		
		avl=equilibrer(avl);
		
	
		
		
		if(avl.bal==0) 
			return new Couple<AVL,Integer>(avl,0);
		else
			return new Couple<AVL,Integer>(avl,1);
	}
		
	
	
	

  }
  



	
	public AVL ROTG (AVL A){
		 AVL B ;
		 int a;
		 int b;
		
		 
		 
		
		
		 B= A.d;
		
		
		 a= A.bal;
		 b= B.getbal();
		 
		 A.d=B.getg();
		 B.g=A;
		
		 A.bal= a - Math.max(b,0)-1;
		 B.bal= Math.min(Math.min(a-2,a+b-2),b-1);
		
		
		
		 
		 return B;
	}

	public AVL ROTD (AVL A){

		int a;
		int b;
		
		AVL B=A.g;
		AVL C=B.d;
		
		B.d=A;
		A.g=C;
		
		a= A.bal;
		b=B.getbal();
		
		A.bal= a+1+Math.max(-b,0);
		B.bal= b+1+Math.max(0,A.bal);
		
		return B;
	}
	

		 
	

	public AVL equilibrer(AVL A){
		
		if (A.bal== 2) {
			if (A.d.bal >= 0) {
			
				return ROTG(A);
			}
			else{
				
				A.d=ROTD(A.d);
		        return ROTG(A);
			}
		}
		else if ( A.bal == -2){
			if (A.g.bal<=0){
			
				return ROTD(A);
			}
			else {
				A.g=ROTG(A.g);
				
				return ROTD(A);
			}
		}
		return A;
	
	}
	

	
		
	public Couple<AVL,Integer> suppr_min(AVL A){
	
		
		if (A.g== null){ 
			
			return new Couple<AVL,Integer>(A.d,-1);
		}
		else {
			
			Couple<AVL,Integer> c=suppr_min(A.g);
			
			A.g=c.getT();
			int h=c.getA();
			h=-h;
			
			if(h==0) {
				return new Couple<AVL,Integer>(A,0);
			} else {
				A.bal=A.bal+h;
				A=equilibrer(A);
				if(A.bal==0) {
					return new Couple<AVL,Integer>(A,-1);
				} else {
					return new Couple<AVL,Integer>(A,0);
				}
				
			}
			
		}
	}
	

	

	
	public Couple<AVL,Integer> enlever(AVL A,Quadtree x) {
		int h=0;
		if(A==null) {
			return new Couple<AVL,Integer>(A,0);
		} 
		else if(x.getEcartMaximum()>A.racine.getEcartMaximum()) {
			Couple<AVL,Integer> c=enlever(A.d,x);
			A.d=c.getT();
			h=c.getA();
		} 
		else if(x.getEcartMaximum()<A.racine.getEcartMaximum()) {
			Couple<AVL,Integer> c=enlever(A.g,x);
			A.g=c.getT();
			h=c.getA();
			h=-h;
		}
		else if(A.g==null) {
			return new Couple<AVL,Integer>(A.d,-1);
		}
		else if(A.d==null) {
			return new Couple<AVL,Integer>(A.g,-1);
		}		
		else {
			
			A.racine=minimum(A.d);
		
			Couple<AVL,Integer> c=suppr_min(A.d);
			
			A.d=c.getT();
			h=c.getA();
		} 
		
		if(h==0) {
			return new Couple<AVL,Integer>(A,0);
		} else {
			A.bal=A.bal+h;
			A=equilibrer(A);
			if(A.bal==0) {
				return new Couple<AVL,Integer>(A,-1);
			} else {
				return new Couple<AVL,Integer>(A,0);
			}
		}
	}
	


	public Quadtree minimum(AVL A){
		if ( A ==null) {
			throw new IllegalArgumentException("Arbre est vide");
		}
		else {
			if(A.g==null) {
				return A.racine;
		
			} else {
				return minimum(A.g);
			}
			
		
		}
		
	}
	
}
	 
 
	
