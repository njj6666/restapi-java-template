package com.test.json;

import java.util.ArrayList;

public class ServiceDetails {
	
private static ArrayList<Parameters> parameters = new ArrayList<Parameters>();

public ArrayList<Parameters> getParameters() {
	return parameters;
}

public void setParameters(ArrayList<Parameters> parameters) {
	this.parameters = parameters;
}

public String toString(){
	return this.parameters.toString();
}
}
