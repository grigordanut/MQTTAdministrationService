
package com.examples.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


class SampleSubscriber implements MqttCallback {

    public SampleSubscriber() {
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
    	System.out.println("connection is lost " + thrwbl.getStackTrace());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println(new String(message.getPayload()) + "\narrived from topic " + topic + " with qos " + message.getQos() );
        System.out.println("--------------------------------------------------------\n");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    	 System.out.println("delivery is complete " +  token.isComplete());
    }
    
}
