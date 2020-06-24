package it.polito.tdp.poweroutages.model;

public class Arco implements Comparable<Arco>{

	private Nerc nerc1;
	private Nerc nerc2;
	private Integer peso;
	
	public Arco(Nerc nerc1, Nerc nerc2, Integer peso) {
		super();
		this.nerc1 = nerc1;
		this.nerc2 = nerc2;
		this.peso = peso;
	}

	public Nerc getNerc1() {
		return nerc1;
	}

	public Nerc getNerc2() {
		return nerc2;
	}

	public Integer getPeso() {
		return peso;
	}

	@Override
	public String toString() {
		return  nerc2 + " " + peso ;
	}

	@Override
	public int compareTo(Arco o) {
		
		return -this.peso.compareTo(o.getPeso());
	}
	
	
	
}
