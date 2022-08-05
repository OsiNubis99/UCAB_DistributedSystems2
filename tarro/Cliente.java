package com.tarro;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Cliente {
	private static final String IP = "localhost";
	private static final int PUERTO = 1100;

	public static void main(String[] args) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(IP, PUERTO);
		Interfaz interfaz = (Interfaz) registry.lookup("LeerTarro"); // Buscar en el registro...
		Scanner sc = new Scanner(System.in);

		// fecha del sistema
		LocalDateTime fechaHora = LocalDateTime.now();

		System.out.println("Escriba el tipo de tarro a consultar:");
		String mensaje = sc.nextLine();
		sc.close();

		interfaz.LeerTarro(mensaje, "Tarro.json");
	}
}