package com.test.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {

	public static void main(String[] args) throws JsonProcessingException, IOException {
		// TODO Auto-generated method stub
		Parameters param;
		ServiceDetails serviceDetails = new ServiceDetails();
		String response = "[{\"node_type\":\"ChefServer\",\"os\":\"RHEL6\"},"
				+ "{\"node_type\":\"FTPSServer\",\"os\":\"Windows\",\"public_ip\":\"52.34.44.40\",\"username\":\"Administrator\",\"password\":\"t$Sda%.K@2(\"},"
				+ "{\"node_type\":\"HadoopNode1\",\"os\":\"RHEL6\"},"
				+ "{\"node_type\":\"HadoopNode2\",\"os\":\"RHEL6\"},"
				+ "{\"node_type\":\"HadoopNode3\",\"os\":\"RHEL6\"},"
				+ "{\"node_type\":\"HUEServer\",\"os\":\"RHEL6\"},"
				+ "{\"node_type\":\"IPAServer\",\"os\":\"RHEL6\"}]";
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode machineJson = objectMapper.readTree(response);
		if (machineJson.isArray()) {
			for (JsonNode objNode : machineJson) {
				String publicip = objNode.path("public_ip").asText();
				String username =  objNode.path("username").asText();
				String password=  objNode.path("password").asText();
				if (publicip.isEmpty() && username.isEmpty() && password.isEmpty()){continue;}
			
				param = new Parameters();
				param.setName("publicIP");
				param.setValue(objNode.path("public_ip").asText());
				serviceDetails.getParameters().add(param);

				param = new Parameters();
				param.setName("User Name");
				param.setValue(objNode.path("username").asText());
				serviceDetails.getParameters().add(param);

				param = new Parameters();
				param.setName("Password");
				param.setValue(objNode.path("password").asText());
				serviceDetails.getParameters().add(param);
				System.out.println("hello");
			//	break;
			}
		}
		System.out.println(serviceDetails);

	}

}
