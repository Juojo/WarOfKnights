package red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;

import jugadores.Movimiento;
import screens.GameScreen;
import utilidades.Global;

public class HiloCliente extends Thread {

	private DatagramSocket conexion;
	private InetAddress ipServer;
	private int puerto = 42071;
	private boolean fin = false;
	private GameScreen app;
	private boolean conexionEstablecida = false;
	
	public HiloCliente() {
		try {
			ipServer = InetAddress.getByName("255.255.255.255"); // Manda un msg a todas las pc (broadcast)
			conexion = new DatagramSocket();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
			
		}
		
		do {
		enviarMensaje("Conexion");
		//System.out.println("Se esta tratando de conectar");
		// Esperar 5 seg
		// Indicarle al usuario que verique si el server esta levantado
		conexionEstablecida = true;
		} while (!conexionEstablecida);
	}
	
	public void setApp(GameScreen app) {
		this.app = app;
	}
	
	public void enviarMensaje(String msg) {
		byte[] data = msg.getBytes(); // Se almacena el mensaje en un array de byte
		DatagramPacket dp = new DatagramPacket(data, data.length, ipServer, puerto);
		try {
			conexion.send(dp);
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}

	@Override
	public void run() {
		do {
			byte[] data = new byte[1024];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			try {
				conexion.receive(dp);
			} catch (IOException e) {
				e.printStackTrace();
			}
			procesarMensaje(dp);
		} while (!fin);
	}

	private void procesarMensaje(DatagramPacket dp) {
		String msg = (new String(dp.getData())).trim();
		
		String[] msgConParamaetros = msg.split("#");
		
		if (msgConParamaetros.length < 2) {
			if (msg.equals("OK")) {
				conexionEstablecida = true;
				ipServer = dp.getAddress();
			} else if (msg.equals("Empieza")) {
				Global.empieza = true;
				System.out.println("El cliente recibe empieza");
			}
		} else {
			if (msgConParamaetros[0].equals("Actualizar") && app != null && app.getJugador(1) != null && app.getJugador(2) != null) {
				if (msgConParamaetros[1].equals("J1X")) {
					final float x1 = Float.parseFloat(msgConParamaetros[2]);
					
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							if (app.getJugador(1).nHitbox.getX() > x1) app.getJugador(1).setMovimientoActual(Movimiento.ATRAS);
							if (app.getJugador(1).nHitbox.getX() < x1) app.getJugador(1).setMovimientoActual(Movimiento.ADELANTE);
							if (app.getJugador(1).nHitbox.getX() == x1) app.getJugador(1).setMovimientoActual(Movimiento.QUIETO);
							app.getJugador(1).mover(x1);
						}
					});
					
				} else if (msgConParamaetros[1].equals("J2X")) {
					final float x2 = Float.parseFloat(msgConParamaetros[2]);

					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							if (app.getJugador(2).nHitbox.getX() > x2) app.getJugador(2).setMovimientoActual(Movimiento.ADELANTE);
							if (app.getJugador(2).nHitbox.getX() < x2) app.getJugador(2).setMovimientoActual(Movimiento.ATRAS);
							if (app.getJugador(2).nHitbox.getX() == x2) app.getJugador(2).setMovimientoActual(Movimiento.QUIETO);
							app.getJugador(2).mover(x2);
						}
					});
					
				} else if (msgConParamaetros[1].equals("J1Brazo")) {
					final float anguloBrazo = Float.parseFloat(msgConParamaetros[2]);
					
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							app.getJugador(1).moverAnguloBrazo(anguloBrazo);
						}
					});
					
				} else if (msgConParamaetros[1].equals("J2Brazo")) {
					final float anguloBrazo = Float.parseFloat(msgConParamaetros[2]);
					
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							app.getJugador(2).moverAnguloBrazo(anguloBrazo);
						}
					});
					
				} else if (msgConParamaetros[1].equals("Reloj")) {
					if (app.getHud() != null) {
						final int tiempoPartida = Integer.parseInt(msgConParamaetros[2]);
						
						Gdx.app.postRunnable(new Runnable() {
							@Override
							public void run() {
								app.getHud().setTiempoPartida(tiempoPartida);
							}
						});
					}
					
				} else if (msgConParamaetros[1].equals("VidaJ1")) {
					final int vida = Integer.parseInt(msgConParamaetros[2]);
					
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							app.getJugador(1).setVida(vida);
						}
					});
					
				} else if (msgConParamaetros[1].equals("VidaJ2")) {
					final int vida = Integer.parseInt(msgConParamaetros[2]);
					
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							app.getJugador(2).setVida(vida);
						}
					});
				}
				
			} else if (msgConParamaetros[0].equals("Ganador") && app != null && app.getJugador(1) != null && app.getJugador(2) != null) {
				if (msgConParamaetros[1].equals("J1")) {
					Global.ganador = 1;
				} else if (msgConParamaetros[1].equals("J2")) {
					Global.ganador = 2;
				} else if (msgConParamaetros[1].equals("J3")) {
					Global.ganador = 3;
				} else if (msgConParamaetros[1].equals("Reiniciar")) {
					System.out.print("Cliente reinicia ganador");
					Global.ganador = -1;
					System.out.println(Global.ganador);
				}
			}
		}
	}

	public void reiniciar() {
		fin = true;
		fin = false;
	}
	
}
