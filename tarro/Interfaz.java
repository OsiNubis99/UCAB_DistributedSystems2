package com.tarro;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
	Declarar firma de métodos que serán sobrescritos
*/
public interface Interfaz extends Remote {
	String LeerTarro(String tipo, String ruta) throws RemoteException;

	String PonerTarro(String mensaje, String archivo) throws RemoteException;

	void QuitarTarro(String archivo) throws RemoteException;

}
