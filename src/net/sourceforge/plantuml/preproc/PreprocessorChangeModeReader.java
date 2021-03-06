/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.preproc;

import java.io.IOException;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.preproc2.PreprocessorMode;
import net.sourceforge.plantuml.preproc2.PreprocessorModeSet;

public class PreprocessorChangeModeReader implements ReadLine {

	private final ReadLine raw;
	private final PreprocessorModeSet mode;

	public PreprocessorChangeModeReader(ReadLine source, PreprocessorModeSet mode) {
		this.raw = source;
		this.mode = mode;
	}

	public void close() throws IOException {
		raw.close();
	}

	public StringLocated readLine() throws IOException {
		final StringLocated line = raw.readLine();
		if (line != null && line.getTrimmed().getString().endsWith("!preprocessorV2")) {
			this.mode.setPreprocessorMode(PreprocessorMode.V2_NEW_TIM);
			return new StringLocated("", line.getLocation());
		}
		return line;
	}

}
