/*
 *  Copyright 2007, Plutext Pty Ltd.
 *   
 *  This file is part of docx4j.

    docx4j is free software: you can use it, redistribute it and/or modify
    it under the terms of version 3 of the GNU Affero General Public License 
    as published by the Free Software Foundation.

    docx4j is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License   
    along with docx4j.  If not, see <http://www.fsf.org/licensing/licenses/>.
    
 */

package org.docx4j.jaxb;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

import org.apache.log4j.Logger;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;

public class JaxbValidationEventHandler implements 
ValidationEventHandler{
    
	private static Logger log = Logger.getLogger(JaxbValidationEventHandler.class);		
	
	
    public boolean handleEvent(ValidationEvent ve) {            
      if (ve.getSeverity()==ValidationEvent.FATAL_ERROR 
       || ve.getSeverity()==ValidationEvent.ERROR){
    	  
          ValidationEventLocator  locator = ve.getLocator();
          
          //print message from validation event
          if (log.isDebugEnabled() || ve.getMessage().length() < 120 ) {
        	  log.warn( printSeverity(ve) + ": " + ve.getMessage() );
          } else {
        	  /* These messages are long, for example:
        	   * 
        	   *    unexpected element (uri:"http://schemas.openxmlformats.org/wordprocessingml/2006/main", local:"style"). 
        	   *    Expected elements are <{http://schemas.openxmlformats.org/wordprocessingml/2006/main}annotationRef>,
        	   *    ....
        	   *  
        	   * so truncate it.
        	   */
        	  log.warn( printSeverity(ve) + ": " + ve.getMessage().substring(0, 120));        	  
          }
          
          if (ve.getLinkedException()!=null && log.isDebugEnabled() ) {
              ve.getLinkedException().printStackTrace();        	   
          }
          
          //output line and column number
//          System.out.println("Column is " + 
//                locator.getColumnNumber() + 
//                " at line number " + locator.getLineNumber());
       } else if (ve.getSeverity()==ve.WARNING) {
    	   log.warn(printSeverity(ve) + "Message is " + ve.getMessage());    	   
       }
      // JAXB provider should attempt to continue its current operation. 
      // (Marshalling, Unmarshalling, Validating)
      log.info("continuing (with possible element/attribute loss)");
       return true;
             
     }
    
    public String printSeverity(ValidationEvent ve) {
    	
    	String errorLevel;
    	
    	switch (ve.getSeverity()) {
    		case ValidationEvent.FATAL_ERROR: { 
              	// FATAL_ERROR is usually not actually fatal! 
    			errorLevel="(non)FATAL_ERROR"; 
    			break; } 
    		case ValidationEvent.ERROR: { errorLevel="ERROR"; break; }
    		case ValidationEvent.WARNING: { errorLevel="WARNING"; break; }
    		default: errorLevel = new Integer (ve.getSeverity()).toString() ;
    	}
    	
    	return "[" + errorLevel + "] ";
    	
    }
  
 }
