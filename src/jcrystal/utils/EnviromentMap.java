package jcrystal.utils;

import java.util.TreeMap;

public class EnviromentMap extends TreeMap<String, Object>{
	public static EnviromentMap $(){
		return new EnviromentMap();
	}
	public EnviromentMap $(String key, Object val){
		put(key, val);
		return this;
	}
}
