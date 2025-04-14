package jcrystal.utils.context;

import jcrystal.types.IJType;

public abstract class ExtendingTypeConverter implements ITypeConverter{
	public ITypeConverter parent;
	public ExtendingTypeConverter() {
	}
	@Override
	public final IJType convert(IJType type) {
		IJType ret = extend(type);
		if(ret == null && parent != null)
			return parent.convert(type);
		return ret;
	}
	
	public abstract IJType extend(IJType type);
	
	public <T extends Exception> void extending(IRunnableWithException<T> code) throws T {
		CodeGeneratorContext.extend(this, code);
	}
	
}
