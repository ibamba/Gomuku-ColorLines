import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


/******************************************************************************************************************************************/

public abstract class Plateau extends JFrame{
    
    protected final int hauteur, longueur;
    protected Container contentPane;
    private Controleur controleur;
    protected JPanel etiquette;
    
    public Plateau(int hauteur, int longueur, String titre){

	this.hauteur = hauteur;
	this.longueur = longueur;
	setTitle(titre);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setSize(600, 600);
	controleur = new Controleur();
	etiquette = new JPanel();
	contentPane = this.getContentPane();
	contentPane.setLayout(new GridLayout(hauteur, longueur));
	for(int i = 0; i < hauteur; i++){
	    for(int j = 0; j < longueur; j++){
		if((i+j) % 2 == 0)
		    contentPane.add(new Case(j, i, Color.white));
		else
		    contentPane.add(new Case(j, i, Color.black));
	    }
	}
	
	setVisible(true);
	
    }

    /*******************************************************************************************************************************/
    /**
       Chaque case est un bouton cliquable et entend l'action. 
       En cliquant sur une case, on peut soit y mettre un pion, soit sélectionner le pion.
       Une case a des coordonnées abscisse et ordonnée sur le plateau, une couleur (soit noir ou blanche), peut contenir un pion
       et peut-être invalidée (spécifique au jeu Gomuku).
    */
    public class Case extends JButton{

	private int x, y;
	private Color caseCouleur;
	private Pion pion;
	private boolean invalide;
	
	public Case(int x, int y, Color c){

	    super();
	    this.x = x;
	    this.y = y;
	    this.caseCouleur = c;
	    this.setBackground(caseCouleur);
	    this.setLayout(null);
	    addActionListener(controleur);
	    this.pion = null;
	    this.invalide = false;
	    
	}
	
	public boolean estVide(){
	    
	    return (this.pion == null);
	    
	}

	public boolean isInvalid(){
	    return this.invalide;
	}
	
	public int getXCase(){
	    return this.x;
	}

	public int getYCase(){
	    return this.y;
	}
	
	public void setStatut(){
	    this.invalide = true;
	}
	
	public Pion getPion(){
	    return this.pion;
	}

	public Color getColor(){
	    return this.caseCouleur;
	}
	
	public void removePion(){	    
	    
	    this.removeAll();
	    this.pion = null;
	    
	}
	
	public void fill(Pion p){
	    
	    this.pion = p;
	    this.add(p);
	    this.repaint();
	    
	}

	/*********************Les alignements***********************/
	/**
	   Ces méthodes renvoient le nombre de cases succesives valides contenant un pion de même couleur que la case tout en 
	   prenant compte bien-sûr, du pion arc-en-ciel. Le nombre de cases horzontaux, verticaux et diagonaux.
	*/
	
	public int alignementDroit(){
	    
	    int res = 0, j = x+1;
	    Case [][] cases = getCases();
	    while(j<longueur && !cases[y][j].estVide() && !cases[y][j].isInvalid() && ((pion.getArc() && cases[y][x+1].getPion().getCouleur().equals(cases[y][j].getPion().getCouleur())) || (pion.getCouleur().equals(cases[y][j].getPion().getCouleur()) || cases[y][j].getPion().getArc()))){
		res++;
		j++;
	    }
	    
	    return res;
	    
	}
	
	public int alignementGauche(){

	    int res = 0, j = x-1;
	    Case [][] cases = getCases();
	    while(j >= 0 && !cases[y][j].estVide() && !cases[y][j].isInvalid() && ((pion.getArc() && cases[y][x-1].getPion().getCouleur().equals(cases[y][j].getPion().getCouleur())) || (pion.getCouleur().equals(cases[y][j].getPion().getCouleur()) || cases[y][j].getPion().getArc()))){
		res++;
		j--;
	    }
	    
	    return res;
	    
	}
	
	public int alignementHaut(){
	    
	    int res = 0, i = y-1;
	    Case [][] cases = getCases();
	    while(i >= 0 && !cases[i][x].estVide() && !cases[i][x].isInvalid() && ((pion.getArc() && cases[y-1][x].getPion().getCouleur().equals(cases[i][x].getPion().getCouleur())) || (pion.getCouleur().equals(cases[i][x].getPion().getCouleur()) || cases[i][x].getPion().getArc()))){
		res++;
		i--;
	    }
	    
	    return res;
	    
	}
	
	public int alignementBas(){
	    
	    int res = 0, i = y+1;
	    Case [][] cases = getCases();
	    while(i<hauteur && !cases[i][x].estVide() && !cases[i][x].isInvalid() && ((pion.getArc() && cases[y+1][x].getPion().getCouleur().equals(cases[i][x].getPion().getCouleur())) || (pion.getCouleur().equals(cases[i][x].getPion().getCouleur()) || cases[i][x].getPion().getArc()))){
		res++;
		i++;
	    }
	    
	    return res;
	    
	}
	
	public int alignementDiagonalHautDroit(){
	    
	    int res = 0, i = y-1, j = x+1;
	    Case [][] cases = getCases();
	    while(i >= 0 && j < longueur && !cases[i][j].estVide() && !cases[i][j].isInvalid() && ((pion.getArc() && cases[y-1][x+1].getPion().getCouleur().equals(cases[i][j].getPion().getCouleur())) || (pion.getCouleur().equals(cases[i][j].getPion().getCouleur()) || cases[i][j].getPion().getArc()))){
		res++;
		i--; j++;
	    }
  
	    return res;
	    
	}
	
	public int alignementDiagonalHautGauche(){
	    
	    int res = 0, i = y-1, j = x-1;
	    Case [][] cases = getCases();
	    while(i >= 0 && j >= 0 && !cases[i][j].estVide() && !cases[i][j].isInvalid() && ((pion.getArc() && cases[y-1][x-1].getPion().getCouleur().equals(cases[i][j].getPion().getCouleur())) || (pion.getCouleur().equals(cases[i][j].getPion().getCouleur()) || cases[i][j].getPion().getArc()))){
		res++;
		i--; j--;
	    }
	    
	    return res;
	    
	}
	
	public int alignementDiagonalBasDroit(){
	    
	    int res = 0, i = y+1, j = x+1;
	    Case [][] cases = getCases();
	    while(i < hauteur && j < longueur && !cases[i][j].estVide() && !cases[i][j].isInvalid() && ((pion.getArc() && cases[y+1][x+1].getPion().getCouleur().equals(cases[i][j].getPion().getCouleur())) || (pion.getCouleur().equals(cases[i][j].getPion().getCouleur()) || cases[i][j].getPion().getArc()))){
		res++;
		i++; j++;
	    }
	    
	    return res;
	    
	}
	
	public int alignementDiagonalBasGauche(){
	    
	    int res = 0, i = y+1, j = x-1;
	    Case [][] cases = getCases();
	    while(i < hauteur && j >= 0 && !cases[i][j].estVide() && !cases[i][j].isInvalid() && ((pion.getArc() && cases[y+1][x-1].getPion().getCouleur().equals(cases[i][j].getPion().getCouleur())) || (pion.getCouleur().equals(cases[i][j].getPion().getCouleur()) || cases[i][j].getPion().getArc()))){
		res++;
		i++; j--;
	    }
	    
	    return res;
	    
	}

	public int alignementHorizontal(){

	    return (alignementDroit() + alignementGauche() + 1);

	}

	public int alignementVertical(){

	    return (alignementBas() + alignementHaut() + 1);

	}

	public int alignementDiagonalHGBD(){ //alignement diagonal du haut à gauche vers le bas à droite

	    return (alignementDiagonalHautGauche() + alignementDiagonalBasDroit() + 1);

	}

	public int alignementDiagonalHDBG(){ //alignement diagonal du haut à droite vers le bas à gauche
	    
	    return (alignementDiagonalHautDroit() + alignementDiagonalBasGauche() + 1);
	    
	}
	
    } //Fin de la classe Case
    
    
    /******************************************************************************************************************************/
    /**
       Un pion est un JPanel. Il est caractérisé par sa couleur. On peut avoir aussi un pion arc-en-ciel
    */
    public class Pion extends JPanel{
	
	private Color pionCouleur;
	private boolean arcenciel;
	
	public Pion(Color c, boolean a){

	    this.pionCouleur = c;
	    this.arcenciel = a;
	    
	    //Pion arc-en-ciel : pour le différencier, ce pion est un petit carré de couleur jaune qui s'associe avec tous les pions de toute couleurs
	    if(arcenciel){//couleur arc-en-ciel
		this.setBackground(Color.yellow);
		this.setBounds(20, 20, 15, 15);
	    }

	    else{ //Les autres pions
		this.setBackground(pionCouleur);
		this.setBounds(15, 15, 25, 25);
	    }
	    
	}

	public Color getCouleur(){

	    return pionCouleur;

	}

	public boolean getArc(){

	    return this.arcenciel;

	}
	
    } //Fin de la classe Pion	

    /******************************************************************************************************************************/
    /**
       Le controleur est une action. Il détecte la case qui émet l'action et applique la miseAJour sur cette case.
    */
    private class Controleur implements ActionListener{
	
	public void actionPerformed(ActionEvent event) {
	    
	    Case[][] cases = getCases();
	    for(int i = 0; i < cases.length; i++){
		for(int j = 0; j < cases[0].length; j++){
		    if(cases[i][j] == event.getSource())
			miseAJour(cases[i][j]);
		}
	    }

	}

    } //Fin de la classe Controleur
    

    /******************************************************************************************************************************/
    /**
       Pour faciliter la méthode alignement, on a besoin d'un tableau qui contient l'ensemble des cases telles qu'elles sont sur
       le Plateau; on a donc besoin d'un tableau à deux dimensions. Cependant, La méthode getComponents() renvoie les cases dans un 
       tableau à une dimension. On doit donc transformer ce tableau d'une dimension à un tableau à deux dimensions.
       La méthode getCases() renvoie les cases du Plateau dans un tableau à deux dimension ainsi, on conserve leur position 
       réelle tel que sur le Plateau.
       @return le tableau de Case
    */
    
    public Case[][] getCases(){
	
	Case [][] res = new Case[hauteur][longueur];
	Component [] lesCases = contentPane.getComponents();
	int i = 0, tmp = 0;
	for(int j = 0; j < lesCases.length; j++){ //On parcours le tableau contenant les cases
	    //C'est un peu comme si on avait : for(int i = 0; i < longueur; i++){for(int tmp = 0; tmp < largeur; tmp++)}
	    if(tmp == longueur){
		i++;
		tmp = 0;
	    }
	    if(lesCases[i] instanceof Case) //On s'assure qu'on manipule bien des Cases et pas autre chose
		res[i][tmp] = (Case)lesCases[j]; //On copie les cases dans notre tableau de résultat en les castants
	    tmp++;                               //car getComponents les renvoie sous forme de Component
	}
	
	return res;
	
    }
    
    /******************************************************************************************************************************/
    
    public abstract void miseAJour(Case c);
    
     
    /*****************************************************************************************************************************/
    
    public abstract void robotStupid();

    
    /*****************************************************************************************************************************/
    
    public abstract void alignement(Case c, Case[][] cases);
   
    /*****************************************************************************************************************************/
    
    public abstract void jeuGagne();

    /*****************************************************************************************************************************/
    /**
       Cette méthode vérifie si le jeu est fini c'est-à-dire si toutes les cases du plateau sont occupées.
       @return vrai si le jeu est fini et faux sinon.
    */
    public boolean jeuFini(){
      
	Case [][] cases = getCases();
	for(int i = 0; i < cases.length; i++){
	    for(int j = 0; j < cases[0].length; j++){
		//On retourne faux si on rencontre au moins une case vide
		if(cases[i][j].estVide())
		    return false;
	    }
	}
	
	return true;
	
    }

    
} //Fin de la classe Plateau

