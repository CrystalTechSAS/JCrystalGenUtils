package jcrystal.utils.langAndPlats;

public class CodeGeneratorContext {
	public static final ThreadLocal<CodeGeneratorContext> userThreadLocal = new ThreadLocal<>();
	public static void set() {
		userThreadLocal.set(new CodeGeneratorContext());
	}
	public static void clear() {
		userThreadLocal.remove();
	}
	public static CodeGeneratorContext get() {
		CodeGeneratorContext ret = userThreadLocal.get();
		if(ret == null)
			userThreadLocal.set(ret = new CodeGeneratorContext());
		return ret;
	}
	
	public Tipo tipo = null;
	
	public static void init_iOS(){
		get().tipo = Tipo.IOS;
	}
	public static void init_Android(){
		get().tipo = Tipo.ANDROID;
	}
	public static void init_Server(){
		get().tipo = Tipo.SERVER;
	}
	public static void init_Web(){
		get().tipo = Tipo.WEB;
	}
	public static boolean is_iOS(){ 
		return get().tipo == Tipo.IOS;
	}
	public static boolean is_Android(){
		return get().tipo == Tipo.ANDROID;
	}
	public static boolean is_Server(){
		return get().tipo == Tipo.SERVER;
	}
	public static boolean is_Web(){
		return get().tipo == Tipo.WEB;
	}
	
    private enum Tipo{
    	SERVER,
    	ANDROID,
    	IOS,
    	WEB
    }
}
