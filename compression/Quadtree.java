

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;


public class Quadtree {
	
	private Quadtree a1,a2,a3,a4;
	
	private static int nb=0;
	
	public double ecartMaximum;

	private int maxValeur=-1;
	
	private int longueur; 
	
	private Quadtree pere;
	
	private int hauteur;
	
	private int n;	
	
	private int val;
	
	
	
	public Quadtree(String f) throws IOException {
	
		nb--;
		val=nb;
		
	
		int[][] tab=PgmToMatrice(new File("").getAbsolutePath()+"/src/images/"+f);
		
	
		
		Quadtree r;
		
		r=matriceToArbre(tab, 1, 1, longueur);
		
		a1=r.a1;
		a2=r.a2;
		a3=r.a3;
		a4=r.a4;
	
		
		if(a1!=null && a2!=null && a3!=null && a4!=null) {
		
			a1.pere=a2.pere=a3.pere=a4.pere=r;
		
			
		}

		val=r.val;
		ecartMaximum=-1;
		n=nbElement(this);		
	
		
	
	}
	

	public Quadtree(int val,Quadtree t1,Quadtree t2,Quadtree t3,Quadtree t4) {
		this.val=val;
		
		nb--;
		
		
		a1=t1;
		a2=t2;
		a3=t3;
		a4=t4;
		
		a1.pere=a2.pere=a3.pere=a4.pere=this;
	
		retraction(this);
		ecartMaximum=-1;

		
		
	}
	public Quadtree(int val) {
		this.val=val;
		
		a1=a2=a3=a4=null;
				
		ecartMaximum=-1;
	
		
	}

	private int setHauteur(Quadtree A) {
		if(A==null)
			return -1;
		
		if(!pasdeFils(A)) {

			A.a1.hauteur=A.hauteur+1;
			A.a2.hauteur=A.hauteur+1;
			A.a3.hauteur=A.hauteur+1;
			A.a4.hauteur=A.hauteur+1;
			
		}
		
		return 1+Math.max(setHauteur(A.a1), Math.max(setHauteur(A.a2), Math.max(setHauteur(A.a3), setHauteur(A.a4))));
	}



	
	private int nbElement(Quadtree A) { //O(n)
		if(A==null) {
			return 0;
		}
		
		return 1+nbElement(A.a1)+nbElement(A.a2)+nbElement(A.a3)+nbElement(A.a4);
	}	

	private boolean valEgal(Quadtree A) {
		return A.a1.val==A.a2.val && A.a1.val==A.a3.val && A.a1.val==A.a4.val;
		
	}
	private boolean retraction(Quadtree A) {
	
		if(A.a1!=null && A.a2!=null && A.a3!=null && A.a4!=null && valEgal(A)) {
			
			A.val=A.a1.val;
				
			A.a1=A.a2=A.a3=A.a4=null;
			
		
			return true;
		
		}
		return false;
	
	}
	

	private Couple<Quadtree,Integer> retractionPere(Quadtree A,int i) {
		
		
		if(A.pere!=null) {
			
			boolean rectra=retraction(A.pere);
			//retourner un true quand il y a eu une rectration puis avec ça enlever des n dans rho
			
			if(rectra) {
				
				return retractionPere(A.pere,i+1);
			}
			
			return new Couple<Quadtree,Integer>(A,i);
			
		}
		
		return new Couple<Quadtree,Integer>(A,i); //cass arrivé à la racine
		
	}
	
	

	@SuppressWarnings("resource")
	private int[][] PgmToMatrice(String file) throws IOException {
		
		FileReader f=new FileReader(file);
		
		BufferedReader image=new BufferedReader(f);

		int[][] tab;
		
	
		String s="";
		
		s=image.readLine();
		if(!s.equals("P2")) {
			throw new IllegalArgumentException("Le fichier n'est pas P2");
		}
		if(!image.ready()) {
			throw new IllegalArgumentException("fichier fini ");
		}
		image.readLine();
		s="";
	
		//longueur
		s=image.readLine();
		longueur=Integer.parseInt(s.substring(0, s.length()/2));
		//s.length/2 car division entière donc arrondi à l'inférieur+ le caractère va de 0 à s.length
		tab=new int[longueur][longueur];
		
		maxValeur=Integer.parseInt(image.readLine());
		int lon=1;
		int lar=1;
		
		char actuelle;
		s="";
		while(image.ready()) {
			actuelle=(char) image.read();
	
			
			while(actuelle=='0' || actuelle=='1' || actuelle=='2' || actuelle=='3' || actuelle=='4' || actuelle=='5' || actuelle=='6' || actuelle=='7' || actuelle=='8' || actuelle=='9') {
				
				s+=actuelle;
				
				actuelle=(char) image.read();
				if(actuelle==' ' ||actuelle=='\n' || image.ready()==false) {
					if(!image.ready() && (actuelle=='0' || actuelle=='1' || actuelle=='2' || actuelle=='3' || actuelle=='4' || actuelle=='5' || actuelle=='6' || actuelle=='7' || actuelle=='8' || actuelle=='9')) {
						s+=actuelle;
					}
				
					
					int val=Integer.parseInt(s);
					
					
					if(0<=val && val<=maxValeur) {
						tab[lar-1][lon-1]=Integer.parseInt(s);
					} else {
						throw new IllegalArgumentException("val pas compris entre 0 et max-1");
					}
					lon++;
				
					s="";
					break;
				}
				
				
			}
			
			if(lon==longueur+1) {
				
				lon=1;
				lar++;
			}
			
			
		}
		
	
	
		
		
		return tab;
	}


	private Quadtree matriceToArbre(int[][] tab,int x,int y,int taille  ) {
		
		int t=taille/2;
		
				if(taille==1) {
				
					return new Quadtree(tab[y-1][x-1]);
				} else {
				
					Quadtree t1,t2,t3,t4=t1=t2=t3=t4=null;
					
					t1=matriceToArbre(tab,x,y,t);
				
					
					t2=matriceToArbre(tab,x+t,y,t);
					
					
					t3=matriceToArbre(tab,x,y+t,t);			
					
					
					t4=matriceToArbre(tab, x+t, y+t, t);
					
					
					return new Quadtree(nb,t1,t2,t3,t4);
				}
	
	}
	
	
	public static void ArbreToImage(Quadtree ancien,Quadtree nouveau,String nom) throws IOException {
		int[][] tab=new int[ancien.longueur][ancien.longueur];
		
		int hauteur=ancien.setHauteur(ancien); 
		

		 nouveau.ArbreToMatrice(nouveau,tab,ancien.longueur,1,1,0, hauteur);
		 
		 
	
		
		MatriceToImage(nouveau,tab,nom);
	}
	private void completeMatrice(int val,int[][] tab,int taille,int x,int y,int h,int hauteur) {
		int t=taille/2;
		//System.out.println((x-1)+" "+(y-1));
	
			if(h==hauteur) {
				
				tab[x-1][y-1]=val;
				
			} else {
			
				completeMatrice(val,tab,t,x,y,h+1,hauteur);
				completeMatrice(val,tab,t,x+t,y,h+1,hauteur);
				completeMatrice(val,tab,t,x,y+t,h+1,hauteur);
				completeMatrice(val,tab,t,x+t,y+t,h+1,hauteur);
			}
		
	}
	
	
	private void  ArbreToMatrice(Quadtree A,int[][] tab,int taille,int x,int y,int h,int hauteur ) //n est la hauteur courant cas où il y a eu retraction
	{
		
		int t=taille/2;
		if(A!=null) {
			
			if(pasdeFils(A) && h!=hauteur) {
				
			
				completeMatrice(A.val,tab,taille,x,y,h,hauteur);
			}
			
			else if(pasdeFils(A)) {
				
				tab[x-1][y-1]=A.val;
				
				
			} 
			
			else {
				ArbreToMatrice(A.a1,tab,t,x,y,h+1,hauteur);
				
				ArbreToMatrice(A.a2,tab,t,x+t,y,h+1,hauteur);
				
				ArbreToMatrice(A.a3,tab,t,x,y+t,h+1,hauteur);
				
				ArbreToMatrice(A.a4,tab,t,x+t,y+t,h+1,hauteur);
				
				
			}
			
		}
		
		
	}
	private static void MatriceToImage(Quadtree A, int[][] tab,String nom) throws IOException {
		String chemin=new File("").getAbsolutePath()+"/src/resultats/";
		
		File file=new File(chemin+nom);
		if(!file.exists()) {
			file.createNewFile();
		}
		
		PrintWriter writer=new PrintWriter(file.getAbsoluteFile());
		
		writer.println("P2");
		writer.println("#a");
		writer.println(""+A.longueur+" "+A.longueur);
		
		writer.println(A.maxValeur);
		for(int j=0;j<tab.length;j++) {
			for(int i=0;i<tab.length;i++) {
				if(i==tab.length-1) {
					writer.print(tab[i][j]);
				} else {
					writer.print(tab[i][j]+" ");
				}
				
			}
			
			writer.println("");
		}
		
		writer.close();
		
		
		
		
	}

	public Quadtree compressionRho(int p) {
		
		
		if(p>100 || p<0) {
			throw new IllegalArgumentException("taux p pas compris entre 0 et 100");
		}

		Quadtree A = (Quadtree) clone(); //O(n)
	
		Quadtree reste=new Quadtree(-1);
		reste.ecartMaximum=Double.MAX_VALUE; //comme les valeurs max d'un pgm est de 255 on ne peut pas avoir un ecart max de max_value 
		
		AVL avl=new AVL(reste);
		
		avl=QuadtreeToAVL(A, avl);
	
		double tauxCompression=1;
		
		
		while(A.n>1 &&tauxCompression> (double) p/100) { //O(n)
			
			Quadtree c=avl.minimum(avl); // O(log(n))
		
			avl=avl.suppr_min(avl).getT();
		
			c.val=compressOne(c);
				
			c.a1=null;
			c.a2=null;
			c.a3=null;
			c.a4=null;
							
			A.n-=4;	
		
			tauxCompression=(double)A.n/(double)n;
			
			Couple<Quadtree,Integer> couple=retractionPere(c,0);
			c=couple.getT();
			int nbRetraction=couple.getA();
			
			A.n=A.n-nbRetraction*4;
			
		
			if(c.pere!=null && filsFeuille(c.pere)) 
			{
			
				c.pere.ecartMaximum=ecartMaximum(c.pere);
				
				avl=avl.inserer(avl, c.pere).getT();		
				
			}
		}
		
	
	
		return A;
		
		
	}
	
	private double ecartMaximum(Quadtree A) {
		if(!pasdeFils(A)) {
			
			double moyenneLogarithe=Math.exp( 0.25*( Math.log(0.1+A.a1.val) + Math.log(0.1+A.a2.val) + Math.log(0.1+A.a3.val) + Math.log(0.1+A.a4.val)) );
			return Math.max(Math.abs(moyenneLogarithe-A.a1.val), Math.max(Math.abs(moyenneLogarithe-A.a2.val), Math.max(Math.abs(moyenneLogarithe-A.a3.val), Math.abs(moyenneLogarithe-A.a4.val))));
		} else {
			throw new IllegalArgumentException("n'a pas de fils");
		}
	}
	
	private static boolean pasdeFils(Quadtree A) {
		
		
		return A.a1==null && A.a2==null && A.a3==null && A.a4==null;
	}
	private static boolean filsFeuille(Quadtree A) //regarde si brindille
	{
	
		
		return pasdeFils(A.a1) && pasdeFils(A.a2) && pasdeFils(A.a3) && pasdeFils(A.a4);
	}

	private static int compressOne (Quadtree s) {
		if ( s.a1 !=null &&  s.a2 !=null && s.a3 !=null && s.a4 !=null) {
			int l =  (int) Math.round (Math.exp( 0.25*( Math.log(0.1+s.a1.val) + Math.log(0.1+s.a2.val) + Math.log(0.1+s.a3.val) + Math.log(0.1+s.a4.val)) ) ) ;
			
			return l;
		
			
		} 
		else {
			return s.val;
		}
	}
	
	
	private AVL QuadtreeToAVL(Quadtree tree, AVL avl) {
		
		if(tree!=null && !pasdeFils(tree)) {
			
			if(filsFeuille(tree)) { // est une brindille
				
				tree.ecartMaximum=ecartMaximum(tree);
				
				avl=avl.inserer(avl,tree).getT();
				
				return avl;
				
			
			} else  {
				avl=QuadtreeToAVL(tree.a1,avl);
				avl=QuadtreeToAVL(tree.a2, avl);
				avl=QuadtreeToAVL(tree.a3, avl);
				avl=QuadtreeToAVL(tree.a4, avl);
				return avl;
				
			}
			
			
		} 
		return avl;
		
	}
	
	public Quadtree compressionLambda(Quadtree A) {
		Quadtree tree=compressionLambdaAux(A);
		tree.n=nbElement(tree);
		tree.longueur=A.longueur;
		tree.ecartMaximum=A.ecartMaximum;
		tree.maxValeur=A.maxValeur;
		return tree;
		
		
	}
	private static Quadtree compressionLambdaAux(Quadtree A) {
		if(A!=null && !pasdeFils(A)) {
			if(filsFeuille(A)) {
				return new Quadtree(Quadtree.compressOne(A));
			} else {
				Quadtree t1,t2,t3,t4=t1=t2=t3=t4=null;
				
				t1=compressionLambdaAux(A.a1);
				t2=compressionLambdaAux(A.a2);
				t3=compressionLambdaAux(A.a3);
				t4=compressionLambdaAux(A.a4);
			
				

				return new Quadtree(nb,t1,t2,t3,t4);
			}
		}
		return A; // cas où un il y a un décallement par exemple ( 1 ( 1 2 3 4) (3 4 5 6) (2 4 5 1) ) on ne peut pas appliquer l'algo sur le premier noeud
		
	}
	
	
	public int getValeur() {
		return val;
	}	
	/*
	 * Complexité O(n)
	 * 
	 * **/
	private String auxString(Quadtree A) { 
		
		if(A!=null) {
			
			if(A.val>=0) {
				
				return ""+A.val;
			}
			else {
				

				
				String s1=auxString(A.a1);
				
				String s2=auxString(A.a2);
				String s3=auxString(A.a3);
				String s4=auxString(A.a4);
				
				
				
				return "( "+s1+" "+s2+" "+s3+" "+s4+") ";
			}
		}
		return "";
	}

	
	private Quadtree cloneAux(Quadtree A) {
		if(A!=null ) {
			if(pasdeFils(A)) {
				
				return new Quadtree(A.val);
			} 
			else  {
				Quadtree t1,t2,t3,t4;
			
				t1=cloneAux(A.a1);
				t2=cloneAux(A.a2);
				t3=cloneAux(A.a3);
				t4=cloneAux(A.a4);
			
				return new Quadtree(A.val,t1,t2,t3,t4);
			}
		
		} 
		return A;

	}
	public int getN() {
		return n;
	}
	public double getEcartMaximum() {
		return ecartMaximum;
	}
	
	@Override
	public Object clone() {
		Quadtree A=cloneAux(this);
		A.n=n;
		A.longueur=longueur;
		A.ecartMaximum=ecartMaximum;
		A.maxValeur=maxValeur;
		return A;
	}
	
	public String toString() {
		return auxString(this);
	}
	
}
