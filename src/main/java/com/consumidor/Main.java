package com.consumidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
	public static String host = "http://localhost:8080";

	public static String getFullResponse(String method, String path) throws IOException {
		URL url = new URL(host + path);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		StringBuilder fullResponseBuilder = new StringBuilder();
		con.setRequestMethod(method);
		fullResponseBuilder.append(con.getResponseCode())
				.append("\n");
		Reader streamReader = null;
		if (con.getResponseCode() > 299) {
			streamReader = new InputStreamReader(con.getErrorStream());
		} else {
			streamReader = new InputStreamReader(con.getInputStream());
		}
		BufferedReader in = new BufferedReader(streamReader);
		String inputLine;
		StringBuilder content = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		fullResponseBuilder.append("Response: ")
				.append(content);
		return fullResponseBuilder.toString();
	}

	public static void main(String[] args) {
		try {
			System.out.println("Starting server...");
			BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
			String str;
			System.out.println("Ingresa 'consumo' o 'consulta'.");
			System.out.println("Ingresa 'stop' para salir.");
			do {
				str = obj.readLine();
				if (str.equals("consumo")) {
					String path = "/consumo/";
					System.out.println("Inresa 'A' o 'B'");
					path += obj.readLine() + "/";
					System.out.println("Inresa la cantidad a ser trabajada");
					path += obj.readLine();
					System.out.println(path);
					System.out.println(getFullResponse("GET", path));
				}
				if (str.equals("consulta")) {
					System.out.println(getFullResponse("GET", "/consulta"));
				}
			} while (!str.equals("stop"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}