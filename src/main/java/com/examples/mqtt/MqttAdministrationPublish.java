package com.examples.mqtt;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttAdministrationPublish {

	private static MqttClient sampleClient ;

	public static void main(String[] args) {	
		
		//Register Patient Administration Service	
		String service_type = "_medical._tcp.local";
		String service_name = "patient_administration_service";
	    String service_description = "service for patient administration";
	    int service_port = (int) 50052;
		
	    String serviceDetails = ("service_type: " + service_type + 
				", service_name: " + service_name + 
				", service_description: " + service_description + 
				", administration_service_port: " + service_port);
	    
		String topic = "/administration";
		String content = ("General Patient Administration service info ");
		
		//Registering Patients
		String name = "Gary Skidmore";
		int age = (int) 51;
		String gender = "male";
		
		String patDetails = ("Patient Name: " + name + 
									", Age: " + age + 
									", Gender: " + gender);		
				
		//Displaying the list of patients		
		ArrayList<String> patList = new ArrayList<String>();		
		patList.add("Patient Name: Gary Skidmore, Age: 51, Gender: male");
		patList.add("Patient Name: Lisa Hogan, Age: 45, Gender: female");
		patList.add("Patient Name: Peter Mark, Age: 57, Gender: male");					
			
		//Calculating the price of patients' accommodation
		String patName = "";	
		int nrDays ;
		float priceDay = (float) 0.00;	
		String room = " ";
		float totalPrice = (float) 0.00;		
		
		//Room type
		String PUBLIC = " ";
		String SEMIPRIVATE = " ";
		String PRIVATE = " ";
	
		//Patient details for calculate accommodation price
		patName = "Lisa Hogan";	
		nrDays = 3;
		room = "PUBLIC";		

		String broker = "tcp://localhost:1883";
		String clientId = "Publisher";

		MemoryPersistence persistence = new MemoryPersistence();

		try {

			sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();

			//if cleanSession is true before connecting the client, 
			//then all pending publication deliveries for the client are removed 
			//when the client connects.
			connOpts.setCleanSession(true);
			connOpts.setKeepAliveInterval(180);			
			
			//connect the service			 
			System.out.println("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected\n");

			//sending message for the main service 
			publishMessage(topic, content, 2, false);
			
			publishMessage("/administration", "Administration Service registered with, " + serviceDetails, 1, false);
			System.out.println("--------------------------------------------------------\n");
						
			//sending messages for the patient registration			 						
			publishMessage("/administration/registerPatient", "Patient registered with, " + patDetails, 1, false);	
			System.out.println("--------------------------------------------------------\n");
			
			//sending messages for displaying patients list	
			for (int i = 0; i < patList.size(); i++) {
				publishMessage("/administration/displayPatients", "The list patients is: " + patList.get(i), 1, false);			
			}	
			System.out.println("--------------------------------------------------------\n");
			
			//sending messages for calculate the accommodation price										
			if (room.equals("PUBLIC")) {
				priceDay = 100;
				
				totalPrice = nrDays * priceDay;				
			}
			
			publishMessage("/administration/calculatePrice", "Total accommodation price for: " 
													+ patName + ", for: " + nrDays + " days, in a: " 
													+ room + " room is: € " + totalPrice, 1, false);	
			System.out.println("--------------------------------------------------------\n");
			
			//disconnect the service			         		   
			sampleClient.disconnect();

			System.out.println("Disconnected");
			
			sampleClient.close();
	        
			System.exit(0);

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc-msg " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("exception " + me);
			me.printStackTrace();
		}
	}

	private static void publishMessage(String topic, String payload, int qos, boolean retained) {

		System.out.println("Publishing message:\n" + payload + ", on topic " + topic ); 

		MqttMessage message = new MqttMessage(payload.getBytes());
		message.setRetained(retained);
		message.setQos(qos);     

		try {

			sampleClient.publish(topic, message);

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc-msg " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("exception " + me);
			me.printStackTrace();
		}

		System.out.println("Message published.");  
	}
}
