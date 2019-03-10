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
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;

public abstract class FishTower<T extends TextBlock> extends WBSTextBlock {

	protected final List<T> left;
	protected final List<T> right;

	protected final TextBlock main;

	final protected double delta1x = 10;
	final protected double deltay = 15;

	public FishTower(ISkinParam skinParam, WElement idea, List<T> left, List<T> right) {
		super(skinParam);
		this.left = left;
		this.right = right;
		this.main = buildBox(idea);
	}

	final protected double getw1(StringBounder stringBounder) {
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double mainWidth = mainDim.getWidth();
		return Math.max(mainWidth / 2, delta1x + getCollWidth(stringBounder, left));
	}

	final public Point2D getT1(StringBounder stringBounder) {
		final double x = getw1(stringBounder);
		final double y = 0;
		return new Point2D.Double(x, y);
	}

	final public Point2D getF1(StringBounder stringBounder) {
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double x = getw1(stringBounder) - mainDim.getWidth() / 2;
		final double y = mainDim.getHeight() / 2;
		return new Point2D.Double(x, y);
	}

	final public Point2D getF2(StringBounder stringBounder) {
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double x = getw1(stringBounder) + mainDim.getWidth() / 2;
		final double y = mainDim.getHeight() / 2;
		return new Point2D.Double(x, y);
	}

	public final Dimension2D calculateDimension(StringBounder stringBounder) {
		final Dimension2D mainDim = main.calculateDimension(stringBounder);
		final double mainWidth = mainDim.getWidth();
		final double height = mainDim.getHeight()
				+ Math.max(getCollHeight(stringBounder, left, deltay), getCollHeight(stringBounder, right, deltay));
		final double width = Math.max(mainWidth / 2, delta1x + getCollWidth(stringBounder, left))
				+ Math.max(mainWidth / 2, delta1x + getCollWidth(stringBounder, right));
		return new Dimension2DDouble(width, height);
	}

}
