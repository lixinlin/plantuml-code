/* ========================================================================
 * Plantuml : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of Plantuml.
 *
 * Plantuml is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Plantuml distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques (for Atos Origin).
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

import net.sourceforge.plantuml.PngError;

public class Graphviz {

	private static File dotExe;

	static {
		final File programFile = new File("c:/Program Files");
		if (programFile.exists()) {
			for (File f : programFile.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					return pathname.isDirectory() && pathname.getName().startsWith("Graphviz");
				}
			})) {
				final File binDir = new File(f, "bin");
				dotExe = new File(binDir, "dot.exe");
			}
		}
	}

	static File getDotExe() {
		return dotExe;
	}

	private final File dotFile;

	public Graphviz(File dotFile) {
		this.dotFile = dotFile;
	}

	public void createPng(File pngFile) throws IOException, InterruptedException {
		if (dotExe == null) {
			createPngNoGraphviz(pngFile);
			return;
		}
		final String cmd = getCommandLine(pngFile);
		final Process process = Runtime.getRuntime().exec(cmd);
		process.waitFor();

	}

	private void createPngNoGraphviz(File pngFile) throws IOException {
		final PngError errorResult = new PngError(Arrays.asList("Cannot find Graphviz in \"C:\\Program Files\""));
		errorResult.writeError(pngFile);
	}

	String getCommandLine(File pngFile) {
		// String exp = "\"C:/Program Files/Graphviz2.18/bin/dot\" -Tgif
		// c:/fileDot1.dot -o c:/fileDot1.gif";
		final StringBuilder sb = new StringBuilder();
		sb.append('\"');
		sb.append(dotExe.getAbsolutePath());
		sb.append('\"');
		sb.append(" -Tpng ");
		sb.append('\"');
		sb.append(dotFile.getAbsolutePath());
		sb.append('\"');
		sb.append(" -o ");
		sb.append('\"');
		sb.append(pngFile.getAbsolutePath());
		sb.append('\"');
		return sb.toString();
	}
}
