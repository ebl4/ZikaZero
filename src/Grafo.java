// Este Ã© um exemplo simples de implementaÃ§Ã£o de grafo orientado representado por lista
// de adjacÃªncias

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Grafo {
	public class Vertice {
		int d;
		String nome;
		String cor;
		Vertice pai;
		List<Aresta> adj;
		List<Integer> focos;
		List<Boolean> focosVisitados;		

		Vertice(String nome) {
			this.d = 0;
			this.nome = nome;
			this.adj = new ArrayList<Aresta>();
			this.focos = new ArrayList<Integer>();
			this.focosVisitados = new ArrayList<Boolean>();
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
	int countClear;
	List<Vertice> currentTree;
	Vector<List<Vertice>> trees;
	List<Vertice> vertices;
	List<Aresta> arestas;
	List<Boolean> visitedFocos;
	List<Vertice> minPathVertices;
	int minPath = Integer.MAX_VALUE;

	public Grafo() {
		this.countClear = 0;
		vertices = new ArrayList<Vertice>();
		arestas = new ArrayList<Aresta>();
		minPathVertices = new ArrayList<Vertice>();
		visitedFocos = new ArrayList<Boolean>();
		trees = new Vector<List<Vertice>>(); 
		currentTree = new Vector<Grafo.Vertice>();
	}
	
	public List<Vertice> intercala(List<Vertice> v, int p, int r){
		Vertice aux = null;
		for (int i = p; i < r-p; i++) {
			if(Integer.valueOf(v.get(i-p).nome) > Integer.valueOf(v.get(r).nome)){
				aux = v.get(i-p);
				v.set(i-p, v.get(r));
				v.set(r, aux);
			}
		}
		return v;
	}
	
	public List<Vertice> quickSort(List<Vertice> v, int p, int r){
		if(p < r){
			int q = (p+r)/2;
			quickSort(v, p, q);
			quickSort(v, q+1, r);
			v = intercala(v, p, r);
		}
		return v;
	}
	
	public void sortTree(List<Vertice> v){
		
	}

	/**
	 * 
	 * @return primeiro menor caminho da coleção de árvores de caminhos com todos os focos
	 */
	List<Vertice> minTree(){
		int minPath = Integer.MAX_VALUE; 
		List<Vertice> result = null;
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).size() < minPath){
				minPath = trees.get(i).size();
				result = trees.get(i);
			}
		}
		result = quickSort(result, 0, result.size()-1);
		return result;
	}

	void createTree(Vertice v, Vertice origem){
		//Aresta ultimaAresta = null;
		Vertice aux = v;
		markFocusFromVertex(aux);
		List<Vertice> verticeTree = new Vector<Grafo.Vertice>();
		verticeTree.add(aux);			
		System.out.println("Arvore: ");
		System.out.println(aux.nome);
		while (aux.pai != null){
			//ultimaAresta = new Aresta(aux, aux.pai);
			if(aux.nome == origem.nome)
				break;
			System.out.println(aux.pai.nome);
			verticeTree.add(aux.pai);
			markFocusFromVertex(aux.pai);
			aux = aux.pai;

		}
		if(allFocosVisited(visitedFocos)){
			trees.add(verticeTree);
		}
		//		if(!allFocosVisited(aux.focosVisitados)){
		//
		//			Aresta reversa = new Aresta(ultimaAresta.destino, ultimaAresta.origem);
		//			this.removeAresta(aux, reversa);
		//			dfs(aux);
		//		}
	}

	void removeAresta(Vertice v, Aresta aresta){
		for (int i = 0; i < v.adj.size(); i++) {
			if(v.adj.get(i).origem.nome == aresta.origem.nome && v.adj.get(i).destino.nome 
					== aresta.destino.nome){
				v.adj.remove(i);
			}
		}
	}

	void createVisitedFocos(int F){		
		for (int i = 0; i < F; i++) {
			visitedFocos.add(false);			
		}
		inicializeFocosVisitados();
	}

	void inicializeFocosVisitados(){
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).focosVisitados = visitedFocos;
		}
	}

	Vertice addVertice(String nome) {
		Vertice v = new Vertice(nome);
		vertices.add(v);
		return v;
	}	

	void addFoco(Vertice v, int foco){		
		v.addFoco(foco);		
	}

	//Modificando método para incluir a aresta em ambos os sentidos, como
	//sendo um grafo 'não orientado'
	void addAresta(Vertice origem, Vertice destino) {
		Aresta e = new Aresta(origem, destino);
		Aresta f = new Aresta(destino, origem);
		origem.addAdj(e);
		destino.addAdj(f);
		arestas.add(e);
		arestas.add(f);
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

	public void markFocusFromVertex(Vertice v){
		for (int i = 0; i < v.focos.size(); i++) {
			int foco = v.focos.get(i);
			visitedFocos.set(foco-1, true);
		}
	}

	public boolean allFocosVisited(List<Boolean> visitedFocos){
		for (int i = 0; i < visitedFocos.size(); i++) {
			if(!visitedFocos.get(i)){
				return false;
			}
		}
		return true;
	}

	public void dfs_visit(Vertice v, Vertice origem){
		//	v.cor = "CINZA";

		System.out.println("vertice: "+v.nome);
		currentTree.add(v);
		for (int i = 0; i < v.focos.size(); i++) {
			int valueFoco = v.focos.get(i);
			System.out.println("foco: "+v.focos.get(i));

			//se o foco nao foi visto (atingido) 
			if(!visitedFocos.get(valueFoco-1)){
				//System.out.println("passou");
				visitedFocos.set(valueFoco-1, true);
				//v.focosVisitados.set(valueFoco-1, true);
			}
		}    	    
		//if(!allFocosVisited(visitedFocos2)){
		for (int i = 0; i < v.adj.size(); i++) {
			Vertice u = v.adj.get(i).destino;
			//u.focosVisitados = v.focosVisitados;
			//System.out.println(u.nome);
			//System.out.println(u.cor);
			if(u.cor == "BRANCA" && !allFocosVisited(visitedFocos)){
				if(u.d == 0){
					v.d = v.d+1;
					u.d = v.d;
					u.pai = v;
					if(v.pai == null){
						dfs_visit(u, origem);
					}
					else if (!u.equals(origem) && !v.pai.equals(u))
						dfs_visit(u, origem);
				}
			}
			//else if(!u.cor.equalsIgnoreCase("BRANCA")){
			//retornar arvore a partir de v
			//createTree a partir de v
			else{
				System.out.println("Lista de adju de "+v.nome);
				if(allFocosVisited(visitedFocos)){
					System.out.println("Todos os focos visitados");
					trees.add(currentTree);
					currentTree = new Vector<Grafo.Vertice>();
					clearAllTime();
					clearVisistedFocos();
					//createTree(v, origem);
				}
			}
		}
		//	v.cor = "PRETA";

	}

	public void clearAllTime(){
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).d = 0;
		}
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

	public String showMinPath(){
		StringBuffer resp = new StringBuffer();
		for (int i = minPathVertices.size()-1; i >= 0; i--) {
			resp.append(minPathVertices.get(i).nome);
			resp.append(" ");
		}
		String respStr = resp.substring(0, resp.length());
		return respStr;
	}

	public static void main(String[] args) {
		int V, E, F;
		Vertice vertices[];
		Grafo g = new Grafo();
		Arquivo arq = new Arquivo("in1", "saidas1.out");
		V = arq.readInt();
		E = arq.readInt();
		vertices = new Vertice[V];
		for (int i = 0; i < V; i++) {
			String nome = String.valueOf(i+1); 
			vertices[i] = g.addVertice(nome);			
		}
		for (int i = 0; i < E; i++) {
			int v = arq.readInt();
			int u = arq.readInt();
			g.addAresta(vertices[v-1], vertices[u-1]);
		}

		F = arq.readInt();	
		g.createVisitedFocos(F);
		for (int i = 0; i < V; i++) {
			vertices[i] = g.vertices.get(i);
			int foco = arq.readInt();
			vertices[i].addFoco(foco);
			while(!arq.isEndOfLine()){				
				foco = arq.readInt();
				vertices[i].addFoco(foco);
			}
			g.vertices.set(i, vertices[i]);
		}
		//g.dfs(g.vertices.get(3));
		g.dfsAll();
		List<Vertice> minTree = g.minTree();
		StringBuffer resp = new StringBuffer();
		String respStr = "";
		if(minTree != null){
			for (int i = 0; i < minTree.size() ; i++) {
				resp.append(minTree.get(i).nome);
				resp.append(" ");
			}
			respStr = resp.substring(0, resp.length()-1);
		}
		arq.print(respStr);
		arq.close();
		/*
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
		//	g.dfsAll();
		//System.out.println(g.showMinPath());
		//	g.getTrees(y);
		//System.out.println(g);
		 * 
		 */
	}
}
