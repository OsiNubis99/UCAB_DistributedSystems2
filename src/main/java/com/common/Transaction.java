package com.common;

import java.util.Date;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Transaction implements Serializable {
	public Integer cantidad;
	public String tipo;
	public String actor;
	public Integer contenidoA;
	public Integer contenidoB;
	public Date fecha;

	public Transaction(Integer cantidad, String tipo, String actor, Integer contenidoA, Integer contenidoB, Date fecha) {
		this.cantidad = cantidad;
		this.tipo = tipo;
		this.actor = actor;
		this.contenidoA = contenidoA;
		this.contenidoB = contenidoB;
		this.fecha = fecha;
	}
}