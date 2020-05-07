package jcrystal.utils.context;

import jcrystal.types.IJType;
import jcrystal.utils.langAndPlats.AbsICodeBlock;

public interface ITypeConverter {

	public IJType convert(IJType type);
	default public String $toString(IJType type, AbsICodeBlock parent) {
		return null;
	}
}
