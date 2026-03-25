package ru.tet;

import javax.naming.Context;
import javax.naming.InitialContext;

public class MiscWebServices {

	private static final MiscWebServices instance = new MiscWebServices();

	private MiscWebServices() {
	}

	public static MiscWebServices getInstance() {
		return instance;
	}
	
	
  private Context envCtx=null;
  
  public <E> E getJNDIVal(String varName, Class<E> cl) {
    
    if (envCtx==null){
      
      try {
        envCtx = new InitialContext();
        envCtx = (Context) envCtx.lookup("java:comp/env");
      } catch (Exception e) {
        throw new RuntimeException("Cannot find JNDI context!",e);
      }
    }

    try {
      return (E)envCtx.lookup(varName);
    } catch (Exception e) {
      return null;
    }
  }	  
  
	
}
