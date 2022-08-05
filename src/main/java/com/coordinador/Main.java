package com.coordinador;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import com.common.Transaction;

public class Main {
	public static void main(String[] args) {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();
			ServerSocket ss = new ServerSocket(12340);
			System.out.println(dtf.format(now) + " - Coordinator Running");
			while (true) {
				Socket s0 = ss.accept();
				Socket s1 = new Socket("localhost", 12341);
				Socket s2 = new Socket("localhost", 12342);
				System.out.println(dtf.format(now) + " - New Request");
				DataInputStream din0 = new DataInputStream(s0.getInputStream());
				String str = din0.readUTF();
				if (str.equals("Action")) {
					System.out.println(dtf.format(now) + " - Action REQUEST");
					ObjectInputStream is0 = new ObjectInputStream(s0.getInputStream());
					@SuppressWarnings("unchecked")
					ArrayList<Transaction> list = (ArrayList<Transaction>) is0.readObject();
					System.out.println(dtf.format(now) + " - I will send VOTE_REQUEST to both");
					DataOutputStream dout1 = new DataOutputStream(s1.getOutputStream());
					dout1.writeUTF("VOTE_REQUEST");
					dout1.flush();
					DataOutputStream dout2 = new DataOutputStream(s2.getOutputStream());
					dout2.writeUTF("VOTE_REQUEST");
					dout2.flush();
					DataInputStream din1 = new DataInputStream(s1.getInputStream());
					String resp1 = din1.readUTF();
					DataInputStream din2 = new DataInputStream(s2.getInputStream());
					String resp2 = din2.readUTF();
					if (resp1.equals("VOTE_COMMIT") && resp2.equals("VOTE_COMMIT")) {
						System.out.println(dtf.format(now) + " - It's a GLOBAL_COMMIT, I will notify to servers");
						dout1.writeUTF("GLOBAL_COMMIT");
						dout1.flush();
						dout2.writeUTF("GLOBAL_COMMIT");
						dout2.flush();
						System.out.println(dtf.format(now) + " - I'm sending data now");
						DataOutputStream dout0 = new DataOutputStream(s0.getOutputStream());
						dout0.writeUTF("GOOD");
						dout0.flush();
						ObjectOutputStream os1 = new ObjectOutputStream(s1.getOutputStream());
						os1.writeObject(list);
						os1.flush();
						ObjectOutputStream os2 = new ObjectOutputStream(s2.getOutputStream());
						os2.writeObject(list);
						os2.flush();
					} else {
						System.out.println(dtf.format(now) + " - It's a GLOBAL_ABORT, I will notify to servers");
						DataOutputStream dout0 = new DataOutputStream(s0.getOutputStream());
						dout0.writeUTF("BAD");
						dout0.flush();
						dout1.writeUTF("GLOBAL_ABORT");
						dout1.flush();
						dout2.writeUTF("GLOBAL_ABORT");
						dout2.flush();
					}
				} else {
					System.out.println(dtf.format(now) + " - Restaurar REQUEST");
					System.out.println(dtf.format(now) + " - I will send REQUEST to both");
					DataOutputStream dout1 = new DataOutputStream(s1.getOutputStream());
					dout1.writeUTF("REQUEST");
					dout1.flush();
					DataOutputStream dout2 = new DataOutputStream(s2.getOutputStream());
					dout2.writeUTF("REQUEST");
					dout2.flush();
					ObjectInputStream is1 = new ObjectInputStream(s1.getInputStream());
					ObjectInputStream is2 = new ObjectInputStream(s2.getInputStream());
					@SuppressWarnings("unchecked")
					ArrayList<Transaction> list1 = (ArrayList<Transaction>) is1.readObject();
					@SuppressWarnings("unchecked")
					ArrayList<Transaction> list2 = (ArrayList<Transaction>) is2.readObject();
					System.out.println(dtf.format(now) + " - I have both JSON, I will send it to server");
					ObjectOutputStream os0 = new ObjectOutputStream(s0.getOutputStream());
					os0.writeObject(list1);
					os0.flush();
				}
				din0.close();
				s1.close();
				s2.close();
				s0.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}