/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.graphic;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinSimple;
import net.sourceforge.plantuml.LineBreakStrategy;
import net.sourceforge.plantuml.creole.CreoleMode;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class TextBlockTitle implements TextBlock {

	private final double outMargin = 2;

	private final TextBlock textBlock;

	TextBlockTitle(FontConfiguration font, Display stringsToDisplay, ISkinSimple spriteContainer) {
		if (stringsToDisplay.size() == 1 && stringsToDisplay.get(0).length() == 0) {
			throw new IllegalArgumentException();
		}
		final LineBreakStrategy lineBreak = LineBreakStrategy.NONE;
		textBlock = stringsToDisplay.create(font, HorizontalAlignment.CENTER, spriteContainer, lineBreak,
				CreoleMode.FULL, null, null);
	}

	public final void drawU(UGraphic ug) {
		textBlock.drawU(ug.apply(new UTranslate(outMargin, 0)));
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D textDim = textBlock.calculateDimension(stringBounder);
		final double width = textDim.getWidth() + outMargin * 2;
		final double height = textDim.getHeight();
		return new Dimension2DDouble(width, height);
	}

	public MinMax getMinMax(StringBounder stringBounder) {
		throw new UnsupportedOperationException();
	}

	public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
		return null;
	}

}
