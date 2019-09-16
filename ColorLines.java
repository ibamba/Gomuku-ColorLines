import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ColorLines extends Plateau{

    private Color [] tabCouleurs;
    private int score;
    private Case depart; //Cet attribut nous permet de pouvoir différencier une action qui choisit un pion ou qui déplace le pion
    private int compteur; //Pour diminuer la proportionnalité d'apparition du pion arc-en-ciel
    
    public ColorLines(int hauteur, int longueur, int nbCouleurs){
	
	super(hauteur, longueur, "ColorLines");
	this.tabCouleurs = new Color[nbCouleurs];//On stocke dès le départ toutes les couleurs qui seront utilisées dans un tableau
	this.score = 0;
	this.compteur = 0;
	
	//On s'assure que les couleurs chosies ne soient pas trop proches les unes des autres avant de les ajouter à la table
	tabCouleurs[nbCouleurs - 1] = Color.yellow; //Le pion arc-en-ciel est représenté par la couleur jaune
	for(int i = 0; i < (nbCouleurs - 1); i++){
	    boolean test = false;
	    do{
		tabCouleurs[i] = new Color( (int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255));
		for(int j = 0; j < i; j++){
		    if((tabCouleurs[i].equals(Color.yellow)) || !pasProches(tabCouleurs[i], tabCouleurs[j]))
			test = true; //Puisque les pion arc-en-ciel est jaune, on n'utilisera pas cette couleur pour les autres pions
		}
	    }
	    while(test);
	}	
	robotStupid();
	robotStupid();
	
    }
    
    /******************************************************************************************************************************/
    /**
       Cette méthode vérifie si deux couleurs sont proches l'une de l'autre.
       Pour cela, elle compare la distance entre les composantes rouge, vert et bleu de chacune des couleurs.
       @param les deux couleurs à comparer
       @retrun vrai si les couleurs ne sont pas proches et faux sinon
    */
    private boolean pasProches(Color c1, Color c2){
	
	int [] c1RGB = {c1.getRed(), c1.getGreen(), c1.getBlue()};
	int [] c2RGB = {c2.getRed(), c2.getGreen(), c2.getBlue()};
	for(int i = 0; i < c1RGB.length;){
	    for(int j = 0; j < c2RGB.length;){
		if(Math.abs(c1RGB[i] - c2RGB[j]) > 25)
		    return true;
		i++; j++;
	    }
	}
	return false;
	
    }

    /******************************************************************************************************************************/
    /**
       A chaque coup joué, la méthode miseAJour s'occupe des modifications à apporter sur le plateau. 
       Elle réalise le déplacement du joueur et le coup de l'ordinateur grâce à la méthode robotStupid.
       @param c est la case d'arrivée ou de départ du déplacement selon qu'on sélectionne la case pour prendre le pion pour
       le déplacer ou qu'on lui attribut le pion.
    */
    public void miseAJour(Case c){

	if(!c.estVide())		
	    depart = c;
	else{
	    if(depart != null){
		if(deplacementValide(depart, c)){
		    c.fill(depart.getPion());
		    c.repaint();
		    depart.removePion();
		    depart.repaint();
		    alignement(c, getCases());
		    jeuGagne();
		    robotStupid();
		}
	    }
	}
	
    }
    
    /*****************************************************************************************************************************/
    /**
       La méthode robotStupid réalise le coup de l'ordinateur. 
       Elle choisi aléatoirement trois pions qu'elle place aléatoirement sur 3 cases vides du plateau.
    */
    public void robotStupid(){

	int x, y;
	Case [][] cases = getCases();
	int [] c = new int [3];
	
	//On choisit 3 couleurs au hasard parmi les couleurs qu'on a

	/*On ne veut pas qu'il y ai beaucoup de pions arc-en-ciel dans une partie. Pour cela, on diminue la proportionnalité
	  de cette couleur par rapport aux autres grâce au code qui va suivre dans le if..else*/

	for(int i = 0; i < 3; i++){
	    if((compteur % 5) == 0)
		c[i] = (int) (Math.random()*tabCouleurs.length);
	    else
		c[i] = (int) (Math.random()*(tabCouleurs.length-1));
	}
	
	//On chosit les trois cases vides aléatoirement et on y place les pions
	for(int i = 0; i < c.length; i++){
	    do{
		x = (int) (Math.random()*longueur);
		y = (int) (Math.random()*hauteur);
	    }
	    while(!cases[y][x].estVide());
	    if(tabCouleurs[c[i]].equals(Color.yellow)) //Pion arc-en-ciel
		cases[y][x].fill(new Pion(tabCouleurs[c[i]], true));
	    else //Les autres pions
		cases[y][x].fill(new Pion(tabCouleurs[c[i]], false));
	    cases[y][x].repaint();
	    alignement(cases[y][x], cases);
	    jeuGagne();
	}
	
	compteur++;

    }
    
    
    /******************************************************************************************************************************/
    /**
       Elle vérifie si un déplacement est valide c'est-à-dire si la case de départ et celle d'arrivée sont de même couleur.
       @param les cases de départs et d'arrivée
       @return vrai si le déplacement est valide et faux sinon
    */
    public boolean deplacementValide(Case depat, Case arrivee){
	
	if(depart.getColor() == arrivee.getColor())
	    return true;
	return false;
	
    }
    
    /*****************************************************************************************************************************/
    /**
       Cette méthode vérifie si un alignement de 5 pions ou plus a été réalisé.
       L'alignement peut être vertical, horizontal ou diagonal. Si un alignement a eu lieu, la méthode octroie au joueur les
       points remportés puis supprime les pions comptabilisés du plateau. Cette méthode est appelée après chaque coup joué.
       @param c la case où on vient de placer le pion, cases l'ensemble des cases du plateau
    */
    public void alignement(Case c, Case[][] cases){
	
	boolean tmp1 = false, tmp2 = false, tmp3 = false, tmp4 = false; //Remplacent les 4 types d'alignements
	int bonus = 0; //Deux alignements simultanés rapportent plus que les deux fait séparement
	
	if(c.alignementHorizontal() >= 5){
	    tmp1 = true;
	    bonus++;
	    score += c.alignementHorizontal();
	}
	
	if(c.alignementVertical() >= 5){
	    tmp2 = true;
	    bonus++;
	    score += c.alignementVertical();
	}
	
	if(c.alignementDiagonalHDBG() >= 5){
	    tmp3 = true;
	    bonus++;
	    score += c.alignementDiagonalHDBG();
	}
	
	if(c.alignementDiagonalHGBD() >= 5){
	    tmp4 = true;
	    bonus++;
	    score += c.alignementDiagonalHGBD();
	}

	//Des alignements simultanés multiplient le score par le nombre d'alignement simultanés
	if(bonus > 1)
	    score = score * bonus;
	
	//On supprime les pions des cases
	
	if(tmp1 || tmp2 || tmp3 || tmp4){
	    
	    if(tmp1){ //Pareil que if(c.alignementHorizontal() >= 5){} pour ne pas faire de repétition
		int droit = c.getXCase()+c.alignementDroit();
		int gauche = c.getXCase()-c.alignementGauche();
		for(int j = (c.getXCase()+1); j <= droit; j++){
		    cases[c.getYCase()][j].removePion();
		    cases[c.getYCase()][j].repaint();
		}
		for(int j = (c.getXCase()-1); j >= gauche; j--){
		    cases[c.getYCase()][j].removePion();
		    cases[c.getYCase()][j].repaint();
		}
	    }
	    
	    if(tmp2){
		int bas = c.getYCase()+c.alignementBas();
		int haut = c.getYCase()-c.alignementHaut();
		for(int i = (c.getYCase()+1); i <= bas; i++){
		    cases[i][c.getXCase()].removePion();
		    cases[i][c.getXCase()].repaint();
		}
		for(int i = (c.getYCase()-1); i >= haut; i--){
		    cases[i][c.getXCase()].removePion();
		    cases[i][c.getXCase()].repaint();
		}
	    }
	    
	    if(tmp3){
		int yHautD = c.getYCase()-c.alignementDiagonalHautDroit();
		int xHautD = c.getXCase()+c.alignementDiagonalHautDroit();
		int yBasG = c.getYCase()+c.alignementDiagonalBasGauche();
		int xBasG = c.getXCase()-c.alignementDiagonalBasGauche();
		for(int i = (c.getYCase()-1); i >= yHautD;){
		    for(int j = (c.getXCase()+1); j <= xHautD;){
			cases[i][j].removePion();
			cases[i][j].repaint();
			i--; j++;
		    }
		}
		
		for(int i = (c.getYCase()+1); i <= yBasG;){
		    for(int j = (c.getXCase()-1); j >= xBasG;){
			cases[i][j].removePion();
			cases[i][j].repaint();
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
			cases[i][j].removePion();
			cases[i][j].repaint();
			i--; j--;
		    }
		}
		for(int i = (c.getYCase()+1); i <= yBasD;){
		    for(int j = (c.getXCase()+1); j <= xBasD;){
			cases[i][j].removePion();
			cases[i][j].repaint();
			i++; j++;
		    }
		}
	    }

	    c.removePion();
	    c.repaint();
	    
	}
	
    }
   
    /*****************************************************************************************************************************/
    /**
       Lorsque le jeu est fini, la méthode jeuGagne affiche le score final sur la fenêtre
    */
    public void jeuGagne(){
	
	JLabel texte;
	if(jeuFini()){
	    texte = new JLabel("Fin du jeu.           VOTRE SCORE : " + score);
	    etiquette.setLayout(new BorderLayout());
	    etiquette.add(texte, BorderLayout.CENTER);
	    etiquette.repaint();
	    contentPane.removeAll();
	    contentPane.repaint();
	    contentPane.setLayout(new BorderLayout());
	    contentPane.add(etiquette, BorderLayout.CENTER);
	    contentPane.repaint();
	}
	
    }

    
}
