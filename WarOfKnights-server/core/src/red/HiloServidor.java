package red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.badlogic.gdx.Gdx;

import jugadores.Movimiento;
import screens.GameScreen;
import utilidades.Global;

public class HiloServidor extends Thread {

	private DatagramSocket conexion;
	private int puerto = 42071;
	private boolean fin = false;
	private DireccionRed[] clientes = new DireccionRed[2];
	private int clientesConectados = 0;
	private GameScreen app;
	
	public HiloServidor() {
		try {
			conexion = new DatagramSocket(puerto);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void setApp(GameScreen app) {
		this.app = app;
	}
	
	public void enviarMensaje(String msg, InetAddress ip, int puerto) {
		byte[] data = msg.getBytes(); // Se almacena el mensaje en un array de byte
		DatagramPacket dp = new DatagramPacket(data, data.length, ip, puerto);
		try {
			conexion.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarMensajeATodos(String msg) {
		for (int i = 0; i < clientes.length; i++) {
			enviarMensaje(msg, clientes[i].getIp(), clientes[i].getPuerto());
		}
	}

	@Override
	public void run() {
		do {
			byte[] data = new byte[1024];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			try {
				conexion.receive(dp);
			} catch (SocketException e) {
				// Un cliente se cierra cuando el servidor está esperando la conexión de los 2 jugadores
				System.out.println("Un cliente se desconecto");
			} catch (IOException  e) {
				e.printStackTrace();
			}
			procesarMensaje(dp);
		} while (!fin);
	}

	private void procesarMensaje(DatagramPacket dp) {
		String msg = (new String(dp.getData())).trim();
		String[] msgConParamaetros = msg.split("#");
		
		int nroCliente = -1; // Se inicializa en -1 para evitar posibles problemas
		
		if (clientesConectados > 1) {
			for (int i = 0; i < clientes.length; i++) {
				if (dp.getPort() == clientes[i].getPuerto() && dp.getAddress().equals(clientes[i].getIp())) {
					nroCliente = i;
				}
			}
		}
		
		if (clientesConectados<2) {
			if (msg.equals("Conexion")) {
				if (clientesConectados<2) {
					clientes[clientesConectados] = new DireccionRed(dp.getAddress(), dp.getPort());
					enviarMensaje("OK", clientes[clientesConectados].getIp(), clientes[clientesConectados].getPuerto());
					
					System.out.println("Se recibio un msg del puerto " + clientes[clientesConectados].getPuerto());
					
					clientesConectados++;
					
					if (clientesConectados == 2) {
						for (int i = 0; i < clientes.length; i++) {
							enviarMensaje("Empieza", clientes[i].getIp(), clientes[i].getPuerto());
							System.out.println("Sen envia msg de empieza desde el server");
							Global.empieza = true;
						}
						if (clientes.length == 2) Global.empieza = true;
					} else if (clientesConectados >= 2) {
						System.out.println("Ya se conectaron el maximo de clientes: " + clientesConectados);
						// handle maximo permitido ya cumplido
					}
					
				}
			}
		} else {
			if (nroCliente != -1 ) {
				if (msg.equals("Reiniciar")) {
					reiniciar();
				}
				
				if (msgConParamaetros.length < 2) {
					if (msg.equals("MoverDerecha")) {
						if (nroCliente == 0) {
							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									app.derecha1 = true;
								}
							});
							
						} else {
							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									app.derecha2 = true;
								}
							});
							
						}
					} else if (msg.equals("MoverIzquierda")) {
						if (nroCliente == 0) {
							
							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									app.izquierda1 = true;
								}
							});
						} else {
							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									app.izquierda2 = true;
								}
							});
						}
					} else if (msg.equals("Quieto")) {
						if (nroCliente == 0) {
							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									app.derecha1 = false;
									app.izquierda1 = false;
								}
							});
						} else {
							Gdx.app.postRunnable(new Runnable() {
								@Override
								public void run() {
									app.derecha2 = false;
									app.izquierda2 = false;
								}
							});
						}
					}
				} else {
					if (msgConParamaetros[0].equals("Brazo") && app != null) {
						if (msgConParamaetros[1].equals("AltoPantalla")) {
							if (nroCliente == 0) {
								final float altoPantalla = Float.parseFloat(msgConParamaetros[2]);
								
								Gdx.app.postRunnable(new Runnable() {
									@Override
									public void run() {
										app.getJugador(1).setAltoPantalla(altoPantalla);
									}
								});
							} else {
								final float altoPantalla = Float.parseFloat(msgConParamaetros[2]);
								
								Gdx.app.postRunnable(new Runnable() {
									@Override
									public void run() {
										app.getJugador(2).setAltoPantalla(altoPantalla);
									}
								});
							}
						} else if (msgConParamaetros[1].equals("MouseX")) {
							if (nroCliente == 0) {
								final float mouseX = Float.parseFloat(msgConParamaetros[2]);
								
								Gdx.app.postRunnable(new Runnable() {
									@Override
									public void run() {
										app.getJugador(1).setMouseX(mouseX);
									}
								});
							} else {
								final float mouseX = Float.parseFloat(msgConParamaetros[2]);
								
								Gdx.app.postRunnable(new Runnable() {
									@Override
									public void run() {
										app.getJugador(2).setMouseX(mouseX);
									}
								});
							}
						} else if (msgConParamaetros[1].equals("MouseY")) {
							if (nroCliente == 0) {
								final float mouseY = Float.parseFloat(msgConParamaetros[2]);
								
								Gdx.app.postRunnable(new Runnable() {
									@Override
									public void run() {
										app.getJugador(1).setMouseY(mouseY);
									}
								});
							} else {
								final float mouseY = Float.parseFloat(msgConParamaetros[2]);
								
								Gdx.app.postRunnable(new Runnable() {
									@Override
									public void run() {
										app.getJugador(2).setMouseY(mouseY);
									}
								});
							}
							
						}
					}
					
				}
			} else {
				System.out.println("Un cliente no reconocido esta enviando mensajes");
				// (Rastrear ip, Devolver ataque dos)
			}
		}
	}

	public void reiniciar() {
		System.out.println("Se reinicia el server");
		//enviarMensajeATodos("Ganador#Reiniciar"); // Se manda antes de limpiar el array clientes
		fin = true;
		clientesConectados = 0;
		clientes[0] = null;
		clientes[1] = null;
		Global.empieza = false;
		fin = false;
	}

	public void setFin(boolean b) {
		fin = b;
	}
	
}
