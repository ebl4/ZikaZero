// Este é um exemplo simples de implementação de grafo orientado representado por lista
// de adjacências

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Grafo {
	public class Vertice {
		String nome;
		String cor;
		Vertice pai;
		List<Aresta> adj;
		List<Integer> focos;

		Vertice(String nome) {
			this.nome = nome;
			this.adj = new ArrayList<Aresta>();
			this.focos = new ArrayList<Integer>();
		}

		void addAdj(Aresta e) {
			adj.add(e);
		}

		void addFoco(int foco){
			focos.add(foco);
		}
	}

	public class Aresta {
		Vertice origem;
		Vertice destino;

		Aresta(Vertice origem, Vertice destino) {
			this.origem = origem;
			this.destino = destino;
		}
	}

	List<Vertice> vertices;
	List<Aresta> arestas;
	List<Boolean> visitedFocos;

	public Grafo() {
		vertices = new ArrayList<Vertice>();
		arestas = new ArrayList<Aresta>();
		visitedFocos = new ArrayList<Boolean>();
	}

	Vertice addVertice(String nome) {
		Vertice v = new Vertice(nome);
		vertices.add(v);
		visitedFocos.add(false);
		return v;
	}	

	void addFoco(Vertice v, int foco){
		v.addFoco(foco);
	}

	Aresta addAresta(Vertice origem, Vertice destino) {
		Aresta e = new Aresta(origem, destino);
		origem.addAdj(e);
		arestas.add(e);
		return e;
	}

	public String toString() {
		String r = "";
		for (Vertice u : vertices) {
			r += u.nome + " -> ";
			for (Aresta e : u.adj) {
				Vertice v = e.destino;
				r += v.nome + ", ";
			}
			r += "\n";
		}
		return r;
	}

	//	public Vector<List<Vertice>> getTrees(Vertice v){
	//		Vector<List<Vertice>> trees = new Vector<List<Vertice>>();
	//		for (int i = 0; i < v.pai.size(); i++) {
	//			//indicePai = i;
	//			List<Vertice> currentTree = new ArrayList<Vertice>();
	//			Vertice paiCurrentTree = v.pai.get(i);
	//			System.out.println(paiCurrentTree.nome);
	//			
	//			currentTree.add(v);
	//			System.out.println("Arvore "+ i);
	//			System.out.println(v.nome);
	//			while(paiCurrentTree != null){
	//				currentTree.add(paiCurrentTree);
	//				System.out.println(paiCurrentTree.nome);
	//				if(paiCurrentTree.pai == null){
	//					paiCurrentTree = null;
	//				}
	//				else{
	//					//if(indicePai <= 0) indicePai = 0;
	//					//paiCurrentTree = paiCurrentTree.pai.get();
	//				}
	//			}
	//			trees.add(currentTree);
	//		}
	//		return trees;
	//	}

	public boolean allFocosVisited(){
		for (int i = 0; i < visitedFocos.size(); i++) {
			if(!visitedFocos.get(i)){
				return false;
			}
		}
		return true;
	}

	public void dfs_visit(Vertice v, Vertice origem){
		v.cor = "CINZA";

		//System.out.println("vertice: "+v.nome);

		//para cada foco na lista de focos do vertice, verificar
		//no hash se o mesmo foi visitado e marca-o
		for (int i = 0; i < v.focos.size(); i++) {
			int valueFoco = v.focos.get(i);
			System.out.println("foco: "+v.focos.get(i));

			//se o foco nao foi visto (atingido) 
			if(!visitedFocos.get(valueFoco-1)){
				//System.out.println("passou");
				visitedFocos.set(valueFoco-1, true); 
			}
		}    	    
		if(!allFocosVisited()){
			for (int i = 0; i < v.adj.size(); i++) {
				Vertice u = v.adj.get(i).destino; 
				//if(u.cor == "BRANCA"){
				u.pai = v;
				dfs_visit(u, origem);
				//}
			}
		}
		else{
			Vertice aux = v;
			while(aux.nome != origem.nome){
				System.out.println(aux.nome);
				aux = aux.pai;				
			}
			System.out.println(aux.nome);
		}
		v.cor = "PRETA";
		
	}

	public void clearVisistedFocos(){
		for (int i = 0; i < visitedFocos.size(); i++) {
			visitedFocos.set(i, false);
		}
	}

	public void dfs(Vertice v){
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).cor = "BRANCA";
			vertices.get(i).pai = null;
		}
		clearVisistedFocos();

		//for (int i = 0; i < vertices.size(); i++) {
		//if (vertices.get(i).cor == "BRANCA"){
		dfs_visit(v, v);
		//	}
		//}
	}

	public void dfsAll(){
		for (int i = 0; i < vertices.size(); i++) {
			System.out.println("Busca a partir do vertice: "+vertices.get(i).nome);
			dfs(vertices.get(i));
		}
	}

	public static void main(String[] args) {
		Grafo g = new Grafo();
		Vertice s = g.addVertice("s");
		Vertice t = g.addVertice("t");
		Vertice y = g.addVertice("y");
		Vertice k = g.addVertice("k");
		s.addFoco(1);s.addFoco(2);
		t.addFoco(1);t.addFoco(2);
		y.addFoco(3);y.addFoco(4);
		k.addFoco(3);k.addFoco(4);
		Aresta st = g.addAresta(s, t);
		Aresta sy = g.addAresta(s, y);
		Aresta ty = g.addAresta(t, y);
		Aresta yt = g.addAresta(y, t);
		Aresta tk = g.addAresta(t, k);
		Aresta ky = g.addAresta(k, y);
		g.dfsAll();
		//	g.getTrees(y);
		//System.out.println(g);
	}
}
