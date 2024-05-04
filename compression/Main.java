


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("Programme Compression Image PGM");
		//si programme lancer en arguments, premier argument lien de image pgm, 2ème entier pour compression rho
		if(args.length==2) {
			
			
			try {
		
			int p=Integer.parseInt(args[1]);
			//rho compris entre 0 et 100 
			if(0>p || p>100) {
				throw new Exception("Rho n'est pas compris entre 0 et 100 ");
			}
			
			Quadtree t=new Quadtree(args[0]);
				
			//applique compression lambda
			Quadtree A=t.compressionLambda(t);

			//applique compression rho
			Quadtree B=t.compressionRho(p);
			
		
			
			System.out.println("Noeud initial: "+t.getN());
			System.out.println("Noeud final lambda: "+A.getN());
			System.out.println("taux lambda: "+100*((double)A.getN()/(double)t.getN()));
			
	
			System.out.println("Noeud final rho: "+B.getN());
			System.out.println("taux rho: "+100*((double)B.getN()/(double)t.getN()));
			
			System.out.println("Fichier dans résultats "+args[0]+"Lambda" +" et "+args[0]+"Rho"+args[1]);

			//traduit l'arbre lambda en image
			Quadtree.ArbreToImage(t, A, args[0]+"Lambda");

			//traduit l'arbre rho en image
			Quadtree.ArbreToImage(t, B, args[0]+"Rho"+args[1]);
			}
			catch(Exception e) {
				System.out.println("pgm mauvais lien");
			}
		}
		
		else {
			//cas si le nombre d'argument n'est pas égale à 2, menu 
			boolean enMarche=true;
			//on récpère tous les fichiers qui se finit par .pgm dans le dossier image
			File[] f=chargerFichier();
			
			while(enMarche) {
				//on affiche tous les fichiers .pgm présent
				for(int i=0;i<f.length;i++) {
					System.out.println(f[i].getName()+ ":" + (i+1));
				}
			
				System.out.println("Veuillez choisir une image entre 1 et "+f.length);
		
				int nb=Main.recupererNumero(f.length,1);
		
				Quadtree tree=new Quadtree(f[nb-1].getName());
		
				System.out.println(tree.toString());
	
		
				int choix=choixCompression();
		
		
				if(choix==1) 
					compressionLambda(tree,f[nb-1].getName().substring(0,f[nb-1].getName().length()-4));
				else if(choix==2)
					compressionRho(tree,f[nb-1].getName().substring(0,f[nb-1].getName().length()-4));
			
			
			
				System.out.println("1 stop, 2 continuez");
				int stop=Main.recupererNumero(2,1);
				if(stop==1) {
					enMarche=false;
				} 
			}
			
	
		}
		
	}
	private static int choixCompression() {
		System.out.println("Veuillez choisir entre la compression Lambda et Rho");
		System.out.println("1:Lambda");
		System.out.println("2:Rho");
		
		int choix=recupererNumero(2,1);
		
		return choix;
	}
	private static void compressionLambda(Quadtree A,String nom) throws IOException {
		Quadtree tree=A.compressionLambda(A);
		
		System.out.println(tree);
		
		System.out.println("Noeud initial: "+A.getN());
		System.out.println("Noeud final: "+tree.getN());
		System.out.println("taux: "+100*((double)tree.getN()/(double)A.getN()));
		
		Quadtree.ArbreToImage(A,tree,nom+"Lambda");
		System.out.println("Fichier dans résultats "+nom+"Lambda");
	}
	/*Fonction qui réalise la compression Rho
 	Prend en argument un quadtree et un stri
 	*/
	private static void compressionRho(Quadtree A,String nom) throws IOException {
		System.out.println("Nombre entre 0 et 100");
		int p=recupererNumero(101,-1);
		Quadtree tree=A.compressionRho(p);
		
		System.out.println(tree);
		
		System.out.println("Noeud initial: "+A.getN());
		System.out.println("Noeud final: "+tree.getN());
		System.out.println("taux: "+100*((double)tree.getN()/(double)A.getN()));
		
		Quadtree.ArbreToImage(A,tree,nom+"Rho"+p);
		System.out.println("Fichier dans résultats "+nom+"Rho"+p);
	}
	/*
 	Fonction qui permet de récuperer fichiers .pgm présent dans le dossier images
 	*/
	// on récupere tous les fichiers .pgm présents dans le fichiers images
	private static File[] chargerFichier() {

		String chemin=new File("").getAbsolutePath()+"/src/images/";
		File fichier=new File(chemin);
		
		
		File[] f=fichier.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File file,String name) {
				
				String verif=name.substring(name.length()-4);
				if(!verif.equals(".pgm"))
					return false;
				return true;
			}
		});
		
		
		return f;
	}
	/*
 	Fonction générique qui permet de de récuperer entré utilisateur,  un int compris entre inf et a 
 	*/
	private static int recupererNumero(int a,int inf) {
		int s=pasString();
		
			 
		while (s<inf || s>a) {
			System.out.println("Votre numéro incorrect ");
			s=pasString();
			
		}
			 
		
		return s;
	}
	
	/*
 	Fonction qui permet de récuperer un int en entré utilisateur
 	*/
	private static int pasString() {
		Scanner sc=new Scanner(System.in);
		
		while(sc.hasNextInt()==false) {
			System.out.println("On souhaite un entier");
			String s=sc.next();
			
		
		}
		
		int s=sc.nextInt();
		return s;	
	}
}
