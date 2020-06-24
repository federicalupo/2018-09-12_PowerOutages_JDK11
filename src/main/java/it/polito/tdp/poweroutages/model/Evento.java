package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{
	
	public enum TipoEvento{
		INIZIO_INTERRUZIONE,
		FINE_INTERRUZIONE
	}
	
	private TipoEvento tipo;
	private Nerc nerc;
	private LocalDateTime data;
	private Long durata;
	private Nerc donatore;
	
	public Evento(TipoEvento tipo, Nerc nerc, LocalDateTime data, Long durata) {
		super();
		this.tipo = tipo;
		this.nerc = nerc;
		this.data = data;
		this.durata = durata;
		
	}

	public TipoEvento getTipo() {
		return tipo;
	}

	
	public Nerc getNerc() {
		return nerc;
	}

	public LocalDateTime getData() {
		return data;
	}

	public Long getDurata() {
		return durata;
	}
	

	public Nerc getDonatore() {
		return donatore;
	}

	public void setDonatore(Nerc donatore) {
		this.donatore = donatore;
	}

	@Override
	public int compareTo(Evento o) {
		return this.data.compareTo(o.getData());
	}
	
	
}
