/*************************************************************************     
 *******************                                                           
 * HP CONFIDENTIAL *                                                           
 *******************                                                           
 *                                                                             
 * [2007] - [2013] Hewlett Packard Corporation                                 
 *                                                                             
 * All Rights Reserved.                                                        
 *                                                                             
 * NOTICE:                                                                     
 *     All information contained herein is, and remains the property of        
 * Hewlett Packard Corporation and its suppliers, if any.  The intellectual    
 * and technical concepts contained herein are proprietary and confidential to 
 * Hewlett Packard Corporation and its suppliers and may be covered by U.S. and
 * Foreign Patents, patents in process, and are protected by trade secret or   
 * copyright law. Dissemination of this information or reproduction of this    
 * material is strictly forbidden unless prior written permission is obtained  
 * from Hewlett Packard Corporation.                                           
 *                                                                             
 */
package com.hp.es.cto.sp.service.context.util;

import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


/**
 * Utility class for string processing.
 * 
 * @author Victor
 * 
 */
public class JaxbUtil {

	/**
	 * 
	 * @param inputStream
	 * @param jaxbContext
	 * @return
	 */
	public static Object unmashall(InputStream inputStream, String jaxbContext) {
		Object object = null;
		try {
			JAXBContext context = JAXBContext.newInstance(jaxbContext);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			JAXBElement<?> soElement = (JAXBElement<?>) unmarshaller.unmarshal(inputStream);
			object = (Object) soElement.getValue();
		}catch (Exception e) {
			System.out.print(e.toString());
		}
		return object;
	}
	
	/**
	 * 
	 * @param inputStream
	 * @param jaxbContext
	 * @return
	 */
	public static String mashall(Object obj, String jaxbContext) {
		try {
			java.io.StringWriter sw = new StringWriter();
			JAXBContext context = JAXBContext.newInstance(jaxbContext);
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(obj, sw);
			return sw.toString();
		}catch (Exception e) {
			System.out.print(e.toString());
			return null;
		}
	}

}
