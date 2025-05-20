package com.example.GestionEventosEmpresa3eva;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class GestionEventos {

	// Método para crear el archivo de ejemplo si no existe
	public static void crearArchivoEjemploSiNoExiste(String archivo) throws IOException {
		File f = new File(archivo);
		if (!f.exists()) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
			writer.write("Concierto,2024-07-10T20:00,Auditorio Nacional,Concierto de música clásica\n");
			writer.write("Feria,2024-08-15T10:00,Centro de Convenciones,Feria de tecnología\n");
			writer.write("Seminario,2024-09-05T09:30,Hotel Central,Seminario de negocios internacionales\n");
			writer.close();
			System.out.println("Archivo 'eventos.txt' creado con datos de ejemplo.");
		}
	}

	public static List<Evento> leerEventos(String archivo) throws IOException {
		List<Evento> eventos = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		List<String> lineas = Files.readAllLines(Paths.get(archivo));
		for (String linea : lineas) {
			String[] partes = linea.split(",", 4);
			if (partes.length == 4) {
				String nombre = partes[0];
				LocalDateTime fecha = LocalDateTime.parse(partes[1], formatter);
				String ubicacion = partes[2];
				String descripcion = partes[3];
				eventos.add(new Evento(nombre, fecha, ubicacion, descripcion));
			}
		}
		return eventos;
	}

	public static void escribirEventos(String archivo, List<Evento> eventos) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
		for (Evento e : eventos) {
			writer.write(e.toString());
			writer.newLine();
		}
		writer.close();
	}

	public static void exportarExcel(String archivo, List<Evento> eventos) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Eventos");
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Nombre");
		header.createCell(1).setCellValue("Fecha");
		header.createCell(2).setCellValue("Ubicación");
		header.createCell(3).setCellValue("Descripción");

		int rowNum = 1;
		for (Evento e : eventos) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(e.getNombre());
			row.createCell(1).setCellValue(e.getFecha().toString());
			row.createCell(2).setCellValue(e.getUbicacion());
			row.createCell(3).setCellValue(e.getDescripcion());
		}

		FileOutputStream fileOut = new FileOutputStream(archivo);
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}

	public static void exportarPDF(String archivo, List<Evento> eventos) throws Exception {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(archivo));
		document.open();

		Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
		Font fontEvento = FontFactory.getFont(FontFactory.HELVETICA, 12);

		Paragraph titulo = new Paragraph("Resumen de Eventos", fontTitulo);
		titulo.setAlignment(Element.ALIGN_CENTER);
		document.add(titulo);
		document.add(new Paragraph(" "));

		for (Evento e : eventos) {
			Paragraph p = new Paragraph("Nombre: " + e.getNombre() + "\n" + "Fecha: " + e.getFecha().toString() + "\n"
					+ "Ubicación: " + e.getUbicacion() + "\n" + "Descripción: " + e.getDescripcion()
					+ "\n-----------------------------\n", fontEvento);
			document.add(p);
		}

		document.close();
	}

	public static void main(String[] args) {
		try {
			crearArchivoEjemploSiNoExiste("eventos.txt"); // Crea el archivo si no existe
			List<Evento> eventos = leerEventos("eventos.txt");
			escribirEventos("salida_eventos.txt", eventos);
			exportarExcel("eventos.xlsx", eventos);
			exportarPDF("resumen_eventos.pdf", eventos);
			System.out.println("¡Todo generado correctamente!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}