/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 11432 $
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import java.util.List;

import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class CommandAddMethod extends SingleLineCommand<ClassDiagram> {

	public CommandAddMethod() {
		super("(?i)^([\\p{L}0-9_.]+|\"[^\"]+\")\\s*:\\s*(.*)$");
	}

	@Override
	protected CommandExecutionResult executeArg(ClassDiagram system, List<String> arg) {
		final IEntity entity = system.getOrCreateLeaf(Code.of(arg.get(0)), null);

		final String field = arg.get(1);
		if (field.length() > 0 && VisibilityModifier.isVisibilityCharacter(field.charAt(0))) {
			system.setVisibilityModifierPresent(true);
		}
		entity.addFieldOrMethod(field);
		return CommandExecutionResult.ok();
	}
}
