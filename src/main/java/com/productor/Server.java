package com.productor;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;
import com.common.Transaction;

public class Server {
	public static void main(String[] args) {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			ServerSocket ss = new ServerSocket(12341);
			System.out.println(dtf.format(now) + " - Server Running");
			while (true) {
				Socket s = ss.accept();
				System.out.println(dtf.format(now) + " - Server Received request");
				DataInputStream din = new DataInputStream(s.getInputStream());
				String str = din.readUTF();
				if (str.equals("VOTE_REQUEST") && Math.random() < 0.8) {
					System.out.println(dtf.format(now) + " - I will respond VOTE_COMMIT");
					DataOutputStream dout = new DataOutputStream(s.getOutputStream());
					dout.writeUTF("VOTE_COMMIT");
					dout.flush();
					str = din.readUTF();
					if (str.equals("GLOBAL_COMMIT")) {
						System.out.println(dtf.format(now) + " - Request is GLOBAL_COMMIT");
						ObjectInputStream is = new ObjectInputStream(s.getInputStream());
						@SuppressWarnings("unchecked")
						ArrayList<Transaction> list = (ArrayList<Transaction>) is.readObject();
						// WORK WITH LIST
					} else {
						System.out.println(dtf.format(now) + " - Request is GLOBAL_ABORT");
					}
				} else if (str.equals("REQUEST")) {
					System.out.println(dtf.format(now) + " - It's a REQUEST");
					// CREATE OBJECT
					ArrayList<Transaction> list = new ArrayList<Transaction>();
					list.add(new Transaction(12, "a", "test", 40, 60, new Date()));
					//
					System.out.println(dtf.format(now) + " - I will send JSON");
					ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
					os.writeObject(list);
					os.flush();
				} else {
					System.out.println(dtf.format(now) + " - I will respond VOTE_ABORT");
					DataOutputStream dout = new DataOutputStream(s.getOutputStream());
					dout.writeUTF("VOTE_ABORT");
					dout.flush();
				}
				din.close();
				s.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}