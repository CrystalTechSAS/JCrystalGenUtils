package jcrystal.utils.context;

import jcrystal.lang.Language;
import jcrystal.types.convertions.IImportConverter;

public class CodeGeneratorContext {
	public static final ThreadLocal<CodeGeneratorContext> userThreadLocal = new ThreadLocal<>();
	public Language lang = null;
	public ContextType type = null;
	public ITypeConverter typeConverter;
	public IImportConverter importConverter;
	public static void set() {
		userThreadLocal.set(new CodeGeneratorContext());
	}
	public static CodeGeneratorContext get() {
		CodeGeneratorContext ret = userThreadLocal.get();
		if(ret == null)
			userThreadLocal.set(ret = new CodeGeneratorContext());
		return ret;
	}
	public static <T extends Exception> void extend(ExtendingTypeConverter typeConverter, IRunnableWithException<T> code) throws T {
		CodeGeneratorContext context = get();
		typeConverter.parent = context.typeConverter;
		context.typeConverter = typeConverter;
		code.run();
		context.typeConverter = typeConverter.parent;
		typeConverter.parent = null;
	}
	public static void clear() {
		get().typeConverter = null;
	}
	public static void set(Language lang, ITypeConverter typeConverter) {
		set(lang, typeConverter, null);
	}
	public static void set(Language lang, ITypeConverter typeConverter, IImportConverter importConverter) {
		get().typeConverter = typeConverter;
		get().importConverter = importConverter;
		get().lang = lang;
	}
	public static boolean is(Language lang) {
		return get().lang == lang;
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
			return CodeGeneratorContext.is(Language.JAVA) && MOBILE.is();
		}
		public static boolean isiOS() {
			return CodeGeneratorContext.is(Language.SWIFT) && MOBILE.is();
		}
	}
}
