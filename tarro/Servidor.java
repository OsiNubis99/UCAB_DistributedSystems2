import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Servidor {
	private static final int PUERTO = 1100;

	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		Remote remote = UnicastRemoteObject.exportObject(new Interfaz() {
			@Override
			public synchronized String LeerTarro(String tipo, String archivo) throws RemoteException {
				JSONParser jsonParser = new JSONParser();
				try {
					FileReader reader = new FileReader(archivo);
					Object obj = jsonParser.parse(reader);

					JSONObject tarro = (JSONObject) obj;
					System.out.println(tarro.get(tipo));

				} catch (FileNotFoundException e) {
					// TODO: handle exception
					e.printStackTrace();

				} catch (IOException e) {
					e.printStackTrace();

				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return tipo;
			};

			@Override
			public synchronized String PonerTarro(String mensaje, String archivo) throws RemoteException {
				return mensaje;
			}

			@Override
			public synchronized void QuitarTarro(String archivo) {
				JSONObject value = new JSONObject();
				try (Reader in = new InputStreamReader(getClass().getResourceAsStream(archivo))) {
					JSONParser parser = new JSONParser();
					value = (JSONObject) parser.parse(in);
				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject TipoA = (JSONObject) value.get("tipoA");
				JSONObject TipoB = (JSONObject) value.get("tipoB");

				// update id
				TipoA.put("tipoA", 0);
				TipoB.put("tipoB", 0);

				// write to output file
				try (Writer out = new FileWriter("output.json")) {
					out.write(value.toJSONString());
				} catch (IOException e) {
					e.printStackTrace();

				}
			}

		}, 0);
		Registry registry = LocateRegistry.createRegistry(PUERTO);
		System.out.println("Servidor escuchando en el puerto " + String.valueOf(PUERTO));
		registry.bind("LeerTarro", remote); // Registrar servicio de escritura
	}
}