package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nerc implements Comparable<Nerc>{
	private int id;
	private String value;
	private Long bonus;
	private Map<Integer, LocalDateTime> donatori;
	private boolean impegnato;

	public Nerc(int id, String value) {
		this.id = id;
		this.value = value;
		this.bonus=(long) 0;
		donatori = new HashMap<>();
		this.impegnato = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getBonus() {
		return bonus;
	}

	public void aggiungiBonus(Long bonus) {
		this.bonus += bonus;
	}
	
	public void aggiungiDonatore(Nerc n, LocalDateTime data) {
		this.donatori.put(n.getId(), data); //si sovrascrive? si
	}
	
	public boolean cercaDonatore(Nerc n, LocalDateTime dataEvento, Integer k) {
		if(!this.donatori.containsKey(n.getId())) {
			return false;  //non esiste
		}
		else {
			LocalDateTime ultimo = this.donatori.get(n.getId()); //ultimo aiuto
			
			
			Long giorniPassati = Duration.between(ultimo, dataEvento).abs().toDays();  //per sicurezza uso modulo
			
			if(giorniPassati < (k*30))  { //conversione mesi k in giorni
				return true;
			}
			return false; //ti ho aiutato ma tempo fa
		}
	}
	
	
	
	public boolean isImpegnato() {
		return impegnato;
	}

	public void setImpegnato(boolean impegnato) {
		this.impegnato = impegnato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nerc other = (Nerc) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(value);
		return builder.toString();
	}

	@Override
	public int compareTo(Nerc o) {
		return this.value.compareTo(o.getValue());
	}

	
}
