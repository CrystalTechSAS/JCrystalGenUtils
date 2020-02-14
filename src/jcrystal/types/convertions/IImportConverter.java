package jcrystal.types.convertions;

import jcrystal.types.IJType;
import jcrystal.utils.context.ITypeConverter;

public interface IImportConverter{

	String getImportLocation(String pathToRoot, IJType type);
}
