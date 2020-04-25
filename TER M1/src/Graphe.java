import java.util.ArrayList;
import java.util.Random;

//A REFAIRE, c'était juste pour tester
public class Graphe {
	private ArrayList<String> sommets = new ArrayList<String>();
	private ArrayList<String[]> aretes = new ArrayList<String[]>();
	
	public Graphe() {
	}
	
	//constructeur pour générer un graphe pas si random
	public Graphe(int nbSommets) {
		Graphe rand = this.generateRandomGraphe(nbSommets);
		this.sommets = rand.sommets;
		this.aretes = rand.aretes;
	}
	
	public Graphe(ArrayList<String> s, ArrayList<String[]> a) {
		sommets=s;
		aretes=a;
	}
	
	public ArrayList<String> getSommets() {
		return sommets;
	}
	
	public ArrayList<String[]> getAretes(){
		return aretes;
	}
	
	public void setSommets(ArrayList<String> s) {
		sommets=s;
	}
	
	public void setAretes(ArrayList<String []> a) {
		aretes=a;
	}
	
	public Graphe generateRandomGraphe(int nbSommets) {
		//pas si random
		//les aretes peuvent apparaitre en doublon
		Graphe random = new Graphe();
		Random rand = new Random();
		int nbAretes = rand.nextInt(((nbSommets*(nbSommets-1))/2)+1);
		ArrayList<String> sommets = new ArrayList<String>();
		ArrayList<String[]> aretes = new ArrayList<String[]>();
		for(int i=0; i<nbSommets;i++) {
			sommets.add("s"+i);
		}
		for(int i=0; i<nbAretes;i++) {
			int s1 = rand.nextInt(nbSommets);
			int s2 = rand.nextInt(nbSommets);
			String tab[] = new String[2];
			aretes.add(tab);
			aretes.get(i)[0]=sommets.get(s1);
			aretes.get(i)[1]=sommets.get(s2);
		}
		random.setSommets(sommets);
		random.setAretes(aretes);
		return random;
	}
	
	public String toString() {
		String res="";
		res += "Ce graphe possède "+this.getSommets().size()+" sommets.\n";
		for(int i=0; i<this.getSommets().size();i++) {
			res += this.getSommets().get(i)+" ";
		}
		res += "\n";
		res += "Ce graphe possède "+this.getAretes().size()+" aretes.\n";
		for(int i=0; i<this.getAretes().size();i++) {
			res += "("+this.getAretes().get(i)[0]+";"+this.getAretes().get(i)[1]+") ";
		}
		res+="\n";
		return res;
	}
}
