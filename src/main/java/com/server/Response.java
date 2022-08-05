package com.server;

import java.util.ArrayList;

import com.common.Transaction;

public class Response {
	String message;
	boolean ok;
	ArrayList<Transaction> list;

	public Response() {
		this.list = new ArrayList<Transaction>();
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getOK() {
		return this.ok;
	}

	public void setList(ArrayList<Transaction> list) {
		this.list = list;
	}

	public void setOK(boolean ok) {
		this.ok = ok;
	}

	public ArrayList<Transaction> getList() {
		return list;
	}

	public String toString() {
		String resp = '[' + "transactions";
		for (Transaction t : list) {
			resp += ',' + t.toString();
		}
		resp += ']';
		return "[" + message + "," + Boolean.toString(ok) + resp + "]";
	}
}
