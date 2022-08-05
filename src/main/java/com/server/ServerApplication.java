package com.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.common.Transaction;

@SpringBootApplication
@RestController
public class ServerApplication {
	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	public static LocalDateTime now = LocalDateTime.now();

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@GetMapping("/llenado")
	public Response llenado() {
		Response resp = new Response();
		resp.setOK(true);
		resp.setMessage("llenando");
		return resp;
	}

	@GetMapping("/consumo/{tarro}/{cont}")
	public Response consumo(@PathVariable("tarro") String tarro, @PathVariable("cont") Integer cont) {
		Response resp = new Response();
		resp.setOK(true);
		resp.setMessage("consumiendo");
		return resp;
	}

	@GetMapping("/consulta")
	public Response consulta() {
		Response resp = new Response();
		resp.setOK(true);
		resp.setMessage("respuesta");
		resp.setList(new ArrayList<Transaction>());
		return resp;
	}

	@GetMapping("/action")
	public Response action() {
		Response resp = new Response();
		try {
			ArrayList<Transaction> list = new ArrayList<Transaction>();
			// MONTAR EL JSON
			list.add(new Transaction(12, "a", "Consumidor", 40, 60, new Date()));
			Socket s = new Socket("localhost", 12340);
			System.out.println(dtf.format(now) + " - Send Action");
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			dout.writeUTF("Action");
			dout.flush();
			System.out.println(dtf.format(now) + " - Sending List");
			ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
			os.writeObject(list);
			os.flush();
			System.out.println(dtf.format(now) + " - Waiting response");
			DataInputStream din = new DataInputStream(s.getInputStream());
			String str = din.readUTF();
			resp.setOK(str.equals("GOOD"));
			resp.setMessage(str);
			s.close();
		} catch (Exception e) {
			System.out.println(e);
			resp.setOK(false);
			resp.setMessage("Fatal Error");
		}
		return resp;
	}

	@GetMapping("/restaurar")
	public Response reset() {
		Response resp = new Response();
		try {
			Socket s = new Socket("localhost", 12340);
			System.out.println(dtf.format(now) + " - Send Action");
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			dout.writeUTF("Restaurar");
			dout.flush();
			System.out.println(dtf.format(now) + " - Waiting List");
			ObjectInputStream is = new ObjectInputStream(s.getInputStream());
			@SuppressWarnings("unchecked")
			ArrayList<Transaction> list = (ArrayList<Transaction>) is.readObject();
			// TRAJO EL JSON!
			System.out.println(dtf.format(now) + " - JSON Received");
			resp.setOK(true);
			resp.setMessage("JSON Received");
			resp.setList(list);
			s.close();
		} catch (Exception e) {
			System.out.println(e);
			resp.setOK(false);
			resp.setMessage("Fatal Error");
		}
		return resp;
	}
}