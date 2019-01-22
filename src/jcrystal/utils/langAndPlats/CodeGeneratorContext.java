package jcrystal.utils.langAndPlats;

public class CodeGeneratorContext {
	public static final ThreadLocal<CodeGeneratorContext> userThreadLocal = new ThreadLocal<>();
	public ContextLang lang = null;
	public ContextType type = null;
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
	
	public enum ContextType{
		SERVER,
		CLIENT,
		WEB,
		MOBILE;
		ContextType parent;
		public void init() {
			CodeGeneratorContext.get().type = this;
		}
		public boolean is() {
			return CodeGeneratorContext.get().type.is(this);
		}
		public boolean is(ContextType type) {
			return this == type || (parent != null && parent.is(type));
		}
		static {
			WEB.parent = CLIENT;
			MOBILE.parent = CLIENT;
		}
		public static boolean isAndroid() {
			return ContextLang.JAVA.is() && MOBILE.is();
		}
		public static boolean isiOS() {
			return ContextLang.SWIFT.is() && MOBILE.is();
		}
	}
	public enum ContextLang{
		JAVA,
		SWIFT,
		TYPESCRIPT,
		JAVASCRIPT;
		ContextLang parent;
		public void init() {
			CodeGeneratorContext.get().lang = this;
		}
		public boolean is() {
			return CodeGeneratorContext.get().lang.is(this);
		}
		public boolean is(ContextLang type) {
			return this == type || (parent != null && parent.is(type));
		}
	}
}
