package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{
	
	public enum TipoEvento{
		INIZIO_INTERRUZIONE,
		FINE_INTERRUZIONE
	}
	
	private TipoEvento tipo;
	private Nerc nerc;
	private LocalDateTime inizio;
	private LocalDateTime fine;
	private LocalDateTime data;
	private Long durata;
	private Nerc donatore;
	
	/**
	 * inizio
	 * @param tipo
	 * @param nerc
	 * @param data
	 * @param durata
	 */
	public Evento(TipoEvento tipo, Nerc nerc, LocalDateTime inizio, LocalDateTime fine, LocalDateTime data, Long durata) {
		super();
		this.tipo = tipo;
		this.nerc = nerc;
		this.inizio = inizio;
		this.fine= fine;
		this.data = data;
		this.durata = durata;
		this.donatore = null;
	}
	
	/**
	 * fine
	 * @param tipo
	 * @param nerc
	 * @param data
	 * @param durata
	 */
	public Evento(TipoEvento tipo, Nerc nerc, LocalDateTime inizio, LocalDateTime fine,LocalDateTime data, Long durata, Nerc donatore) {
		super();
		this.tipo = tipo;
		this.nerc = nerc;
		this.inizio = inizio;
		this.fine= fine;
		this.data = data;
		this.durata = durata;
		this.donatore = donatore;
	}


	public TipoEvento getTipo() {
		return tipo;
	}

	
	public Nerc getNerc() {
		return nerc;
	}

	
	public LocalDateTime getInizio() {
		return inizio;
	}

	public LocalDateTime getFine() {
		return fine;
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


	
	@Override
	public String toString() {
		return tipo +" ";
	}

	@Override
	public int compareTo(Evento o) {
		return this.data.compareTo(o.getData());
	}
	
	
}
