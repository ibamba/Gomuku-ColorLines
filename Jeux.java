import java.util.*;

/**
   Cette classe contient la fonction principale. Le main permet à l'utilisateur de choisir ses préférences;
   les dimensions du plateau, le jeu souhaité et le nombre de couleurs des pions pour le jeu Color Lines.
*/
public class Jeux{

    public static void main(String[] args){
	
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
		public void run() {

		    int hauteur, longueur, choix;
		    System.out.println("\n\nBienvenu dans les jeux d'alignements de Ibrahim et Matheus");
		    System.out.print("\nQuel jeu voulez-vous jouer ?\n-1- Gomuku\n-2- Color lines\nVotre choix : ");
		    Scanner sc = new Scanner(System.in);
		    do
			choix = sc.nextInt();
		    while(choix != 1 && choix != 2);
		    do{
			System.out.print("Taille du plateau\nhauteur (compris entre 5 et 15) : ");
			hauteur = sc.nextInt();
			System.out.print("longueur (compris entre 5 et 15) : ");
			longueur = sc.nextInt();
		    }
		    while(longueur < 5 || longueur > 15 || hauteur < 5 || hauteur > 15);
		    if(choix == 1)
			new Gomuku(hauteur, longueur);
		    else{
			int n;
			do{
			    System.out.print("Nombre de couleur que vous souhaitez dans votre partie (entre 3 et 10) : ");
			    n = sc.nextInt();
			}
			while( n < 3 && n > 10);
			new ColorLines(hauteur, longueur, n);
		    }
		    
		}
	    });
    }
    
}
