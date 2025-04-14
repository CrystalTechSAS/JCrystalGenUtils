package jcrystal.gen.exporters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jcrystal.utils.langAndPlats.AbsCodeBlock;

public class FileExporter {

	public static void export(AbsCodeBlock code, File out) throws IOException {
		Files.write(out.toPath(), code.getCode());
	}
}
