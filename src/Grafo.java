// Este Ã© um exemplo simples de implementaÃ§Ã£o de grafo orientado representado por lista
// de adjacÃªncias

// Revisão: Edson Barboza
// Data: 02/05/2016
// Descrição: Casos teste 5 e 7 não estão funcionando

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Grafo {
	public class Vertice {
		int d;
		int nome;
		Vertice pai;
		List<Aresta> adj;
		List<Integer> focos;
		List<Boolean> focosVisitados;		

		Vertice(int nome) {
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
	List<Vertice> vertices, currentTree, minTree;
	Vector<List<Vertice>> trees;
	List<Aresta> arestas;
	List<Boolean> visitedFocos;
	int minPath = Integer.MAX_VALUE;

	public Grafo() {
		vertices = new ArrayList<Vertice>();
		arestas = new ArrayList<Aresta>();
		visitedFocos = new ArrayList<Boolean>();
		trees = new Vector<List<Vertice>>(); 
		currentTree = new Vector<Grafo.Vertice>();
		minTree = new Vector<Grafo.Vertice>();
	}

	int partition(int arr[], int left, int right)
	{
		int i = left, j = right;
		int tmp;
		int pivot = arr[(left + right) / 2];

		while (i <= j) {
			while (arr[i] < pivot)
				i++;
			while (arr[j] > pivot)
				j--;
			if (i <= j) {
				tmp = arr[i];
				arr[i] = arr[j];
				arr[j] = tmp;
				i++;
				j--;
			}
		};

		return i;
	}

	void quickSort(int arr[], int left, int right) {
		int index = partition(arr, left, right);
		if (left < index - 1)
			quickSort(arr, left, index - 1);
		if (index < right)
			quickSort(arr, index, right);
	}

	/**
	 * 
	 * @return primeiro menor caminho da coleção de árvores de caminhos com todos os focos
	 */
	int[] minTree(){
		int minPath = Integer.MAX_VALUE; 
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).size() < minPath){
				minPath = trees.get(i).size();
				minTree = trees.get(i);
			}
		}
		int array[] = new int[minTree.size()]; 
		for (int i = 0; i < minTree.size(); i++) {
			array[i] = minTree.get(i).nome;
		}
		quickSort(array, 0, array.length-1);
		return array;
	}

	public List<Vertice> minSubsetAllFocos(int[] array, List<Vertice> v){
		int i = 0;		
		List<Vertice> result = new Vector<Grafo.Vertice>();
		this.clearVisistedFocos();
		while(i < array.length){
			Vertice aux = vertices.get(array[array.length-1-i]-1);
			for (int j = 0; j < aux.focos.size(); j++) {
				int foco = aux.focos.get(j);
				if(visitedFocos.get(foco-1).equals(false)){
					visitedFocos.set(foco-1, true);
				}
			}
			System.out.println(aux.nome);
			result.add(aux);
			if(allFocosVisited(visitedFocos)){
				return result;
			}
			i++;
		}
		return result;
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

	Vertice addVertice(int nome) {
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
				visitedFocos.set(valueFoco-1, true);
				//v.focosVisitados.set(valueFoco-1, true);
			}
		}    	    		
		if(v.adj.size() > 0){ //verifica antes se o vértice não é isolado
			for (int i = 0; i < v.adj.size(); i++) {
				Vertice u = v.adj.get(i).destino;
				System.out.println("Percorrendo adj de "+v.nome);
				System.out.println("vertice "+u.nome);
				//System.out.println(u.cor);
				if(!allFocosVisited(visitedFocos)){
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
					else{
						System.out.println("###################");
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
						//clearAllTime();
						clearVisistedFocos();
						//createTree(v, origem);
					}
				}
			}
		}
		else{
			if(allFocosVisited(visitedFocos)){
				trees.add(currentTree);
				currentTree = new Vector<Grafo.Vertice>();
				clearAllTime();
				clearVisistedFocos();
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
			vertices.get(i).pai = null;
			vertices.get(i).d = 0;
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

	public static void main(String[] args) {
		long inicio = System.currentTimeMillis();
		int V, E, F;
		Vertice vertices[];
		Grafo g = new Grafo();
		Arquivo arq = new Arquivo("in7", "saidas7.out");
		V = arq.readInt();
		E = arq.readInt();
		vertices = new Vertice[V];
		for (int i = 0; i < V; i++) {
			vertices[i] = g.addVertice(i+1);			
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
		int[] minTree = g.minTree();		
		List<Vertice> result = g.minSubsetAllFocos(minTree, g.minTree);
		StringBuffer resp = new StringBuffer();
		String respStr = "";
		if(result != null){
			for (int i = result.size()-1; i >= 0 ; i--) {
				resp.append(result.get(i).nome);
				resp.append(" ");
			}
		}		
		resp = resp.deleteCharAt(resp.length()-1);
		resp = resp.append("\n");
		respStr = resp.substring(0, resp.length());
		arq.print(respStr);
		long fim = System.currentTimeMillis();
		System.out.println(fim-inicio);
		arq.close();
	}
}
