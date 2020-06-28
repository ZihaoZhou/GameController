package inputHandler;

import java.io.*;
import java.net.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

import inputHandler.GameController.ControllerInput;

public class Main {
	
	static Controller cont;
	static InetAddress address;
	static DatagramSocket socket;

	public static void main(String[] args) {
		
		try {
			Controllers.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		Controllers.poll();
		
		for(int i = 0; i < Controllers.getControllerCount(); i++) {
			cont = Controllers.getController(i);
			if(cont.getName() == "Wireless Controller") {
				break;
			}
		}
		
		/**
		for(int i = 0; i < cont.getButtonCount(); i++) {
			System.out.println(cont.getButtonName(i));
		}*/
		
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
			cib.setXAxisValue(cont.getXAxisValue());
			cib.setYAxisValue(cont.getYAxisValue());
			ControllerInput ci = cib.build();
			
			byte[] buf = ci.toByteArray();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8888);
			
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			System.out.println(cont.getXAxisValue() + ", " + cont.getYAxisValue());
		}
	}

}
