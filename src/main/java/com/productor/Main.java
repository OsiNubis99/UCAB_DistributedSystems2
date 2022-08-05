package com.productor;

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
			System.out.println("Enter 'llenado', 'action' o 'restaurar' to quit.");
			System.out.println("Enter 'stop' to quit.");
			do {
				str = obj.readLine();
				if (str.equals("llenado")) {
					System.out.println(getFullResponse("GET", "/llenado"));
				}
				if (str.equals("action")) {
					System.out.println(getFullResponse("GET", "/action"));
				}
				if (str.equals("restaurar")) {
					System.out.println(getFullResponse("GET", "/restaurar"));
				}
			} while (!str.equals("stop"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}