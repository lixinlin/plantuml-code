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
package net.sourceforge.plantuml.wbs;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class Fish extends FishTower<TextBlock> {

	public Fish(ISkinParam skinParam, WElement idea) {
		super(skinParam, idea, new ArrayList<TextBlock>(), new ArrayList<TextBlock>());
		for (WElement child : idea.getChildren(Direction.RIGHT)) {
			this.right.add(buildBox(child));
		}
		for (WElement child : idea.getChildren(Direction.LEFT)) {
			this.left.add(buildBox(child));
		}
	}

	public void drawU(final UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double wx = getw1(stringBounder) - mainDim.getWidth() / 2;
		main.drawU(ug.apply(new UTranslate(wx, 0)));
		final double x = getw1(stringBounder);
		double y = mainDim.getHeight();
		double lastY1 = y;
		for (TextBlock child : left) {
			y += deltay;
			final Dimension2D childDim = child.calculateDimension(stringBounder);
			lastY1 = y + childDim.getHeight() / 2;
			drawLine(ug, x - delta1x, lastY1, x, lastY1);
			child.drawU(ug.apply(new UTranslate(x - childDim.getWidth() - delta1x, y)));
			y += childDim.getHeight();
		}

		y = mainDim.getHeight();
		double lastY2 = y;
		for (TextBlock child : right) {
			y += deltay;
			final Dimension2D childDim = child.calculateDimension(stringBounder);
			lastY2 = y + childDim.getHeight() / 2;
			drawLine(ug, x, lastY2, x + delta1x, lastY2);
			child.drawU(ug.apply(new UTranslate(x + delta1x, y)));
			y += childDim.getHeight();

		}
		drawLine(ug, x, mainDim.getHeight(), x, Math.max(lastY1, lastY2));
	}

}
