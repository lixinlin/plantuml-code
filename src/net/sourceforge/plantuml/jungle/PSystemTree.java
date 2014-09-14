/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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
 */
package net.sourceforge.plantuml.jungle;

import java.io.IOException;
import java.io.OutputStream;

import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.DiagramDescriptionImpl;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

public class PSystemTree extends AbstractPSystem {

	private GNode root;

	public DiagramDescription getDescription() {
		return new DiagramDescriptionImpl("(Tree)", getClass());
	}

	public ImageData exportDiagram(OutputStream os, int num, FileFormatOption fileFormat) throws IOException {
		final ImageBuilder builder = new ImageBuilder(new ColorMapperIdentity(), 1.0, HtmlColorUtils.WHITE, null,
				null, 5, 5, null);
		builder.addUDrawable(getGTile());
		return builder.writeImageTOBEMOVED(fileFormat.getFileFormat(), os);
	}

	public GNode createRoot(String id) {
		root = new GNode(id, Display.create(id));
		return root;
	}

	private GTile getGTile() {
		return new GTileOneLevelFactory().createGTile(root);
		// return new GTileNode(root);
	}

	public GNode getNode(String id) {
		return GNodeUtils.getIndirectChild(root, id);
	}

}
