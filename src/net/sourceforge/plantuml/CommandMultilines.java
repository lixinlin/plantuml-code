/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques (for Atos Origin).
 *
 * Project Info:  http://plantuml.sourceforge.net
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
package net.sourceforge.plantuml;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class CommandMultilines<S extends PSystem> implements Command {

	private final S system;

	private final Pattern starting;
	private final Pattern ending;

	public CommandMultilines(final S system, String patternStart, String patternEnd) {
		this.system = system;
		this.starting = Pattern.compile(patternStart);
		this.ending = Pattern.compile(patternEnd);
	}

	final public CommandControl isValid(List<String> lines) {
		Matcher m1 = starting.matcher(lines.get(0));
		if (m1.matches() == false) {
			return CommandControl.NOT_OK;
		}

		m1 = ending.matcher(lines.get(lines.size() - 1));
		if (m1.matches() == false) {
			return CommandControl.OK_PARTIAL;
		}

		return CommandControl.OK;
	}

	protected S getSystem() {
		return system;
	}

	protected Pattern getStartingPattern() {
		return starting;
	}
}
