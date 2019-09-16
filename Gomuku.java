import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Gomuku extends Plateau{

    private Joueur joueur1, joueur2;
    private int tour;
    
    public Gomuku(int hauteur, int longueur){

	super(hauteur, longueur, "Gomuku");
	joueur1 = new Joueur(Color.blue);
	joueur2 = new Joueur(Color.red);
	tour = 1;

    }

    /**
       Un joueur a une couleur et un score
    */
    private class Joueur{

	Color couleur;
	int score;

	public Joueur(Color c){
	    
	    this.couleur = c;
	    this.score = 0;

	}
	
    } //Fin de la classe Joueur
    
    /***********************************************************************************************************************************/
    /**
       Lorsqu'un joueur joue, cette méthode s'occupe des modifications à apporter sur le plateau.
       Elle place le pion dans la case sélectionnée, réalise l'alignement, change de joueur et joue le coup de l'ordianteur.
       @param la case sélectionnée pour y placer le pion
    */
    public void miseAJour(Case c){

	if(c.estVide()){
	    c.fill(new Pion(joueur1.couleur, false));
	    alignement(c, getCases());
	    changeJoueur();
	    jeuGagne();
	    robotStupid();
	    //on affiche le score à chaque fois sur le terminal pour avoir une idée de son amélioration
	    System.out.println("joueur : " + joueur1.score + "\nrobot stupide : " + joueur2.score);
	    jeuGagne();
	}
	
    }
    
    /***********************************************************************************************************************************/
    /**
       Cette méthode réalise le coup de l'ordianteur. Elle choisit aléatoirement une case vide sur le plateau et y place son pion.
    */
    public void robotStupid(){

	Case [][] cases = getCases();
	int x, y;
	do{
	    x = (int) (Math.random()*cases.length);
	    y = (int) (Math.random()*cases[0].length);
	}
	while(!cases[y][x].estVide());
	cases[y][x].fill(new Pion(joueur2.couleur, false));
	alignement(cases[y][x], cases);
	changeJoueur();
	
    }
    
    /***********************************************************************************************************************************/
    
    private void changeJoueur(){

	if(tour == 1)
	    tour = 2;
	else
	    tour = 1;

    }

    /***********************************************************************************************************************************/
     /**
       Cette méthode vérifie si un alignement de 5 pions ou plus a été réalisé.
       L'alignement peut être vertical, horizontal ou diagonal. Si un alignement a eu lieu, la méthode octroie au joueur les
       points remportés puis invalides les pions comptabilisés. Cette méthode est appelée après chaque coup joué.
       @param c la case où on vient de placer le pion, cases l'ensemble des cases du plateau
    */
    public void alignement(Case c, Case [][] cases){
	
	boolean tmp1 = false, tmp2 = false, tmp3 = false, tmp4 = false; //Remplacent les 4 types d'alignements
	int bonus = 0; //Pour les alignements simultanés
	
	if(c.alignementHorizontal() >= 5){
	    tmp1 = true;
	    bonus++;
	    if(tour == 1)
		joueur1.score += c.alignementHorizontal();
	    else
		joueur2.score += c.alignementHorizontal();
	}
	
	if(c.alignementVertical() >= 5){
	    tmp2 = true;
	    bonus++;
	    if(tour == 1)
		joueur1.score += c.alignementVertical();
	    else
		joueur2.score += c.alignementVertical();
	}
	
	if(c.alignementDiagonalHDBG() >= 5){
	    tmp3 = true;
	    bonus++;
	    if(tour == 1)
		joueur1.score += c.alignementDiagonalHDBG();
	    else
		joueur2.score += c.alignementDiagonalHDBG();
	}
	
	if(c.alignementDiagonalHGBD() >= 5){
	    tmp4 = true;
	    bonus++;
	    if(tour == 1)
		joueur1.score += c.alignementDiagonalHGBD();
	    else
		joueur2.score += c.alignementDiagonalHGBD();
	}

	
	//Des alignements simultanés rapportent le nombre d'alignements fois plus que s'ils étaient fait séparement
	if(bonus > 1){
	    if(tour == 1)
		joueur1.score = joueur1.score * bonus;
	    else
		joueur2.score = joueur2.score * bonus;
	}
	
	//On rend les cases déjà comptalisées invalides
	
	if(tmp1 || tmp2 || tmp3 || tmp4){
	    c.setStatut();
	    if(tmp1){ //Pareil que if(c.alignementHorizontal() >= 5){} pour ne pas faire de repétition
		int droit = c.getXCase()+c.alignementDroit();
		int gauche = c.getXCase()-c.alignementGauche();
		for(int j = c.getXCase()+1; j <= droit; j++)
		    cases[c.getYCase()][j].setStatut();
		for(int j = c.getXCase()-1; j >= gauche; j--)
		    cases[c.getYCase()][j].setStatut();
	    }
	    
	    if(tmp2){
		int bas = c.getYCase()+c.alignementBas();
		int haut = c.getYCase()-c.alignementHaut();
		for(int i = c.getYCase()+1; i <= bas; i++)
		    cases[i][c.getXCase()].setStatut();
		for(int i = c.getYCase()-1; i >= haut; i--)
		    cases[i][c.getXCase()].setStatut();
	    }
	    
	    if(tmp3){
		int yHautD = c.getYCase()-c.alignementDiagonalHautDroit();
		int xHautD = c.getXCase()+c.alignementDiagonalHautDroit();
		int yBasG = c.getYCase()+c.alignementDiagonalBasGauche();
		int xBasG = c.getXCase()-c.alignementDiagonalBasGauche();
		for(int i = (c.getYCase()-1); i >= yHautD;){
		    for(int j = (c.getXCase()+1); j <= xHautD;){
			cases[i][j].setStatut();
			i--; j++;
		    }
		}
		
		for(int i = (c.getYCase()+1); i <= yBasG;){
		    for(int j = (c.getXCase()-1); j >= xBasG;){
			cases[i][j].setStatut();
			i++; j--;
		    }
		}
	    }
	    
	    if(tmp4){
		int yHautG = c.getYCase()-c.alignementDiagonalHautGauche();
		int xHautG = c.getXCase()-c.alignementDiagonalHautGauche();
		int yBasD = c.getYCase()+c.alignementDiagonalBasDroit();
		int xBasD = c.getXCase()+c.alignementDiagonalBasDroit();
		for(int i = (c.getYCase()-1); i >= yHautG;){
		    for(int j = (c.getXCase()-1); j >= xHautG;){
			cases[i][j].setStatut();
			i--; j--;
		    }
		}
		for(int i = (c.getYCase()+1); i <= yBasD;){
		    for(int j = (c.getXCase()+1); j <= xBasD;){
			cases[i][j].setStatut();
			i++; j++;
		    }
		}
	    }
	    
	}
	
	
    }
    
    /***********************************************************************************************************************************/
     /**
       Lorsque le jeu est fini, la méthode jeuGagne affiche le vainqueur et les score finaux de la partie dans la fenêtre.
    */
    public void jeuGagne(){

	JLabel texte;
	if(jeuFini()){
	    if(joueur1.score >= joueur2.score)
		texte = new JLabel("Félicitation ! Vous avez gagné !!!           VOTRE SCORE : "+joueur1.score + "               Score ROBOT : "+joueur2.score);
	    else
		texte = new JLabel("Vous avez perdu !!!             VOTRE SCORE : "+joueur1.score + "                  Score ROBOT : "+joueur2.score);
	    etiquette.setLayout(new BorderLayout());
	    etiquette.add(texte, BorderLayout.CENTER);
	    contentPane.removeAll();
	    contentPane.repaint();
	    contentPane.setLayout(new BorderLayout());
	    contentPane.add(etiquette, BorderLayout.CENTER);
	    contentPane.repaint();
	}
	
    }

    
}
