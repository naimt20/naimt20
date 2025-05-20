package com.example.GestionEventosEmpresa3eva;

import java.time.LocalDateTime;

public class Evento {
	private String nombre;
	private LocalDateTime fecha;
	private String ubicacion;
	private String descripcion;

	public Evento(String nombre, LocalDateTime fecha, String ubicacion, String descripcion) {
		this.nombre = nombre;
		this.fecha = fecha;
		this.ubicacion = ubicacion;
		this.descripcion = descripcion;
	}

	// Getters y setters
	public String getNombre() {
		return nombre;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	@Override
	public String toString() {
		return nombre + "," + fecha + "," + ubicacion + "," + descripcion;
	}
}
