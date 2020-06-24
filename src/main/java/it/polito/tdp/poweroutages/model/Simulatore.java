package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.poweroutages.model.Evento.TipoEvento;

public class Simulatore {
	
	//mondo
	private Graph<Nerc, DefaultWeightedEdge> grafo;
	private Map<Integer, Nerc> idMap;
	
	//input
	private Integer k; //numero di mesi
	
	//coda
	private PriorityQueue<Evento> coda;
	
	//output
	private Integer nCatastrofi;
	

	public Integer getnCatastrofi() {
		return nCatastrofi;
	}

	public void init(Graph<Nerc, DefaultWeightedEdge> grafo, Map<Integer, Nerc> idMap, PriorityQueue<Evento> coda, Integer k){
		this.grafo = grafo;
		this.idMap = idMap;
		this.coda = coda;
		this.k = k;
		this.nCatastrofi = 0;		
	}
	
	public void run() {
		while(!coda.isEmpty()) {
			processEvent(coda.poll());
		}
	}
	
	private void processEvent(Evento e) {
		switch(e.getTipo()){
			
			case INIZIO_INTERRUZIONE:
				
				List<Nerc> aiutanti = new ArrayList<>();
				Nerc aiutante=null;
				
				List<Nerc> vicini = Graphs.neighborListOf(this.grafo, e.getNerc());
				
				//criterio 1
				for(Nerc n : vicini) {
					if(!n.isImpegnato() && n.cercaDonatore(e.getNerc(), e.getData(), k)){
						aiutanti.add(n);
					}
				}
				
				//criterio 2
				if(aiutanti.size()==0) { //criterio 1 fallito
					aiutante = aiutante(vicini, e.getNerc());					
				}
				if(aiutanti.size()>1) {
					aiutante = aiutante(aiutanti, e.getNerc());
				}
				if(aiutanti.size()==1) {
					aiutante = aiutanti.get(0);
				}
			
				if(aiutante == null) {
					this.nCatastrofi++;
				}else {
					e.getNerc().aggiungiDonatore(aiutante, e.getData());
					aiutante.setImpegnato(true); //settare a impegnato
					
					//e.setDonatore => STO IMPOSTANDO DONATORE NELL'EVENTO INIZIO INTERRUZIONE
					//MA L'EVENTO FINE INTERRUZIONE AVRà SEMPRE DONATORE NULL
					//devo creare l'evento fine qui dentro!
					
					Evento fine = new Evento(TipoEvento.FINE_INTERRUZIONE, e.getNerc(), e.getInizio(), e.getFine(), e.getFine(), e.getDurata(), aiutante);
					coda.add(fine);
				}
			
				break;
				
			case FINE_INTERRUZIONE:
				
	
				if(e.getDonatore()!=null) {
					e.getDonatore().aggiungiBonus(e.getDurata());
					e.getDonatore().setImpegnato(false); //liberato
					
				}
				break;
		}
	}
	

	public Nerc aiutante(List<Nerc> lista, Nerc nercEvento) {
		Integer pesoMin = Integer.MAX_VALUE;
		Nerc aiuto=null;
		
		for(Nerc n : lista) {
			if(!n.isImpegnato()) {
			
				Integer peso = (int)this.grafo.getEdgeWeight(this.grafo.getEdge(nercEvento, n));
				if(peso < pesoMin) {
					pesoMin = peso;
					aiuto = n;
				}
			}
		}
		return aiuto;
		
		
	}
}
