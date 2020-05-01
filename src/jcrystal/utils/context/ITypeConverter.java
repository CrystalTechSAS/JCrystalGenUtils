package jcrystal.utils.context;

import jcrystal.types.IJType;

public interface ITypeConverter {

	public IJType convert(IJType type);
	default public String $toString(IJType type) {
		return null;
	}
}
