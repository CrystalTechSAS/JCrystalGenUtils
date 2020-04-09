package jcrystal.utils.langAndPlats.delegates;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

import jcrystal.types.JType;
import jcrystal.types.loaders.JClassLoader;
import jcrystal.utils.langAndPlats.JavaCode;
import jcrystal.utils.langAndPlats.JavascriptCode;
import jcrystal.utils.langAndPlats.TypescriptCode;

public class DelegateGenerator {
	static JClassLoader loader = new JClassLoader();
	public static void main(String[] args) throws Exception{
		for(Class<?> clase : new Class<?>[] {JavascriptCode.class, TypescriptCode.class, JavaCode.class}) {
			JavaCode code = new JavaCode() {{
				$("package jcrystal.utils.langAndPlats.delegates;");
				$("public interface " + clase.getSimpleName()+"Delegator extends jcrystal.utils.langAndPlats.AbsICodeBlock",()->{
					$(clase.getName()+" getDelegator();");
					for(Method m : clase.getMethods()) {
						if(!m.getDeclaringClass().getName().startsWith("java.lang.")) {
							System.out.println(m.getDeclaringClass().getName());
							Parameter[] ps = m.getParameters();
							$("default "+$(new JType(loader, m.getReturnType(), m.getGenericReturnType())) + " "+m.getName()+"("+Arrays.stream(ps).map(p->{
								return $(new JType(loader, p.getType(), p.getParameterizedType())) + " " + p.getName();
							}).collect(Collectors.joining(", "))+")",()->{
								if(m.getReturnType().equals(Void.TYPE)) {
									$("getDelegator()."+m.getName()+"("+Arrays.stream(ps).map(p->{
										return p.getName();
									}).collect(Collectors.joining(", "))+");");
								}else {
									$("return getDelegator()."+m.getName()+"("+Arrays.stream(ps).map(p->{
										return p.getName();
									}).collect(Collectors.joining(", "))+");");
								}
							});
						}
					}
				});
			}};
			Files.write(Paths.get("C:\\Users\\G\\Documents\\GitHub\\jcrystal\\JCrystalGenUtils\\src\\jcrystal\\utils\\langAndPlats\\delegates\\"+clase.getSimpleName()+"Delegator.java"), code.getCode());
		}
	}
}
