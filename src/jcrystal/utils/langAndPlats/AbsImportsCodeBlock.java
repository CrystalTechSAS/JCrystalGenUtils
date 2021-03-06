package jcrystal.utils.langAndPlats;

import java.util.Collections;
import java.util.TreeSet;

import jcrystal.types.IJType;
import jcrystal.utils.context.CodeGeneratorContext;

public abstract class AbsImportsCodeBlock extends AbsCodeBlock{

	private static final long serialVersionUID = 4822043135587835911L;
	
	protected TreeSet<IJType> imports = new TreeSet<>();
	
	public AbsImportsCodeBlock(){}
    public AbsImportsCodeBlock(int level){
        super(level);
    }
	
	public void $import(IJType type) {
		imports.add(type);
	}
	public void $imports(int stepsToRoot) {
		String path = String.join("", Collections.nCopies(stepsToRoot, "../"));
		CodeGeneratorContext cnt = CodeGeneratorContext.get();
		if(cnt.importConverter != null) {
			imports.stream().map(type->cnt.importConverter.getImportLocation(path, type)).filter(type->type != null).forEach(type->{
				$(type);
			});
		}
	}
	public void $imports() {
		$imports(1);
	}
}
