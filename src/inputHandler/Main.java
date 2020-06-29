package inputHandler;

import java.io.*;
import java.net.*;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import inputHandler.GameController.ControllerInput;

public class Main {
	
	static Controller cont;
	static InetAddress address;
	static DatagramSocket socket;
	
	static Component xAxis;
	static Component yAxis;

	public static void main(String[] args) {
		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller[] ca = ce.getControllers();
		/*for(int i =0; i < ca.length; i++){
			System.out.println(ca[i].getName());
		}*/
		cont = ca[3];
		
		try {
			address = InetAddress.getByName("173.230.158.61");
			socket = new DatagramSocket();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		

		ControllerInput.Builder cib = ControllerInput.newBuilder();
		while(true) {
			cont.poll();
			
			Component[] comps = cont.getComponents();
			/*for(int i = 0; i < comps.length; i++) {
				System.out.println(comps[i].getName());
			}*/
			
			xAxis = comps[14];
			yAxis = comps[15];
			
			cib.setXAxisValue(xAxis.getPollData());
			cib.setYAxisValue(yAxis.getPollData());
			ControllerInput ci = cib.build();
			
			byte[] buf = ci.toByteArray();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8888);
			
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println(xAxis.getPollData() + ", " + yAxis.getPollData());
		}
	}

}
