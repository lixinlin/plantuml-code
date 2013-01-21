/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2012, Arnaud Roques
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
 * Revision $Revision: 5424 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.sequencediagram.Message;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.skin.ArrowConfiguration;
import net.sourceforge.plantuml.skin.ArrowDecoration;

public class CommandArrowCrossX extends SingleLineCommand2<SequenceDiagram> {

	public CommandArrowCrossX(SequenceDiagram sequenceDiagram) {
		super(sequenceDiagram, getRegexConcat());
	}

	static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexOr("PART1", //
						new RegexLeaf("PART1CODE", "([\\p{L}0-9_.@]+)"), //
						new RegexLeaf("PART1LONG", "\"([^\"]+)\""), //
						new RegexLeaf("PART1LONGCODE", "\"([^\"]+)\"\\s*as\\s+([\\p{L}0-9_.@]+)"), //
						new RegexLeaf("PART1CODELONG", "([\\p{L}0-9_.@]+)\\s+as\\s*\"([^\"]+)\"")), new RegexLeaf(
						"\\s*"), //
				new RegexLeaf("\\s+"), // 
				new RegexLeaf("ARROW", "([=-]+(>?x)|(x<?)[=-]+)"), //
				new RegexLeaf("\\s+"), // 
				new RegexOr("PART2", // 
						new RegexLeaf("PART2CODE", "([\\p{L}0-9_.@]+)"), // 
						new RegexLeaf("PART2LONG", "\"([^\"]+)\""), // 
						new RegexLeaf("PART2LONGCODE", "\"([^\"]+)\"\\s*as\\s+([\\p{L}0-9_.@]+)"), // 
						new RegexLeaf("PART2CODELONG", "([\\p{L}0-9_.@]+)\\s+as\\s*\"([^\"]+)\"")), new RegexLeaf(
						"\\s*"), // 
				new RegexLeaf("MESSAGE", "(?::\\s*(.*))?$"));
	}

	private Participant getOrCreateParticipant(RegexResult arg2, String n) {
		final String code;
		final Display display;
		if (arg2.get(n + "CODE", 0) != null) {
			code = arg2.get(n + "CODE", 0);
			display = Display.getWithNewlines(code);
		} else if (arg2.get(n + "LONG", 0) != null) {
			code = arg2.get(n + "LONG", 0);
			display = Display.getWithNewlines(code);
		} else if (arg2.get(n + "LONGCODE", 0) != null) {
			display = Display.getWithNewlines(arg2.get(n + "LONGCODE", 0));
			code = arg2.get(n + "LONGCODE", 1);
		} else if (arg2.get(n + "CODELONG", 0) != null) {
			code = arg2.get(n + "CODELONG", 0);
			display = Display.getWithNewlines(arg2.get(n + "CODELONG", 1));
			return getSystem().getOrCreateParticipant(code, display);
		} else {
			throw new IllegalStateException();
		}
		return getSystem().getOrCreateParticipant(code, display);
	}

	@Override
	protected CommandExecutionResult executeArg(RegexResult arg2) {

		final String arrow = StringUtils.manageArrowForSequence(arg2.get("ARROW", 0));

		Participant p1;
		Participant p2;

		if (arrow.endsWith("x")) {
			p1 = getOrCreateParticipant(arg2, "PART1");
			p2 = getOrCreateParticipant(arg2, "PART2");
		} else if (arrow.startsWith("x")) {
			p2 = getOrCreateParticipant(arg2, "PART1");
			p1 = getOrCreateParticipant(arg2, "PART2");
		} else {
			throw new IllegalStateException(arg2.toString());
		}

		// final boolean sync = arrow.endsWith(">>") || arrow.startsWith("<<")
		// || arrow.contains("\\\\")
		// || arrow.contains("//");

		final boolean dotted = arrow.contains("--");

		final Display labels;
		if (arg2.get("MESSAGE", 0) == null) {
			labels = Display.asList("");
		} else {
			labels = Display.getWithNewlines(arg2.get("MESSAGE", 0));
		}

		ArrowConfiguration config = ArrowConfiguration.withDirectionNormal();
		if (dotted) {
			config = config.withDotted();
		}
		config = config.withDecorationEnd(ArrowDecoration.CROSSX_toberemoved);
		// if (sync) {
		// config = config.withAsync();
		// }
		// if (arrow.endsWith("\\") || arrow.startsWith("/")) {
		// config = config.withPart(ArrowPart.TOP_PART);
		// }
		// if (arrow.endsWith("/") || arrow.startsWith("\\")) {
		// config = config.withPart(ArrowPart.BOTTOM_PART);
		// }

		final String error = getSystem().addMessage(
				new Message(p1, p2, labels, config, getSystem().getNextMessageNumber()));
		if (error != null) {
			return CommandExecutionResult.error(error);
		}

		// if (getSystem().isAutoactivate()) {
		// if (p1 != p2 && config.isASync() == false) {
		// if (config.isDotted()) {
		// getSystem().activate(p1, LifeEventType.DEACTIVATE, null);
		// } else {
		// getSystem().activate(p2, LifeEventType.ACTIVATE, null);
		// }
		// }
		// } else {
		// if (deactivatep1) {
		// getSystem().activate(p1, LifeEventType.DEACTIVATE, null);
		// }
		// if (activatep2) {
		// getSystem().activate(p2, LifeEventType.ACTIVATE, null);
		// }
		// }
		return CommandExecutionResult.ok();
	}

}
