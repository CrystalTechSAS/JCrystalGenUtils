package jcrystal.lang;

public enum Language {
	JAVA,
	SWIFT,
	TYPESCRIPT,
	JAVASCRIPT,
	DART;
	Language parent;
	public boolean is(Language type) {
		return this == type || (parent != null && parent.is(type));
	}
}
