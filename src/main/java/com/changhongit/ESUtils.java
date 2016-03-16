package com.changhongit;

import org.codehaus.jackson.map.ObjectMapper;

public class ESUtils {
	private static ObjectMapper objectMapper = new ObjectMapper();  
    public static String toJson(Object o){  
        try {  
            return objectMapper.writeValueAsString(o);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return "";  
    }  
}
