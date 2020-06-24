package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.poweroutages.db.PowerOutagesDAO;

public class Model {

	private PowerOutagesDAO dao;
	private Graph<Nerc, DefaultWeightedEdge> grafo;
	private Map<Integer, Nerc> idMap;
	
	public Model() {
		dao = new PowerOutagesDAO();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		
		Graphs.addAllVertices(this.grafo, dao.loadAllNercs(idMap));
		
		for(Arco a : dao.getArchi(idMap)) {
			Graphs.addEdge(this.grafo, a.getNerc1(), a.getNerc2(), a.getPeso());
		}
		
		for(Arco a : dao.getArchiPeso(idMap)) {
			if(this.grafo.containsEdge(a.getNerc1(), a.getNerc2())) {
				this.grafo.setEdgeWeight(a.getNerc1(), a.getNerc2(), a.getPeso());
			}
		}
	}
	
	public Integer nVertici() {
		return this.grafo.vertexSet().size();
		
	}
	public Integer nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Nerc> tendina(){
		List<Nerc> vertici = new ArrayList<>(idMap.values());
		Collections.sort(vertici);
		return vertici;
	}
	
	public List<Arco> vicini(Nerc n){
		List<Arco> adiacenti = new ArrayList<Arco>();

		List<Nerc> vicini = Graphs.neighborListOf(this.grafo, n);
		
		for(Nerc nerc : vicini) {
			adiacenti.add(new Arco(n, nerc, (int)this.grafo.getEdgeWeight(this.grafo.getEdge(n, nerc))));
		}
		
		Collections.sort(adiacenti);
		return adiacenti;
	}
	
	public Integer simula(Integer k) {
		Simulatore simulatore = new Simulatore();
		simulatore.init(grafo, idMap, dao.listaEventi(idMap), k);
		simulatore.run();
		
		return simulatore.getnCatastrofi();
	}
}

