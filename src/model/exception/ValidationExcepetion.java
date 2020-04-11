package model.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationExcepetion extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private Map<String, String> mapErrors = new HashMap<>();
	
	public ValidationExcepetion(String str) {
		super(str);
	}
	
	public Map<String, String> getErrors(){
		
		return mapErrors;
	}
	
	public void setErrors(String fildName, String errorMessage){
		mapErrors.put(fildName, errorMessage);
	}
}
