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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.geom.Dimension2D;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UChangeBackColor;
import net.sourceforge.plantuml.ugraphic.UChangeColor;
import net.sourceforge.plantuml.ugraphic.UEllipse;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UStroke;

public class Control implements TextBlock {

	private final double margin = 4;

	private final double radius = 12;
	private final HtmlColor backgroundColor;
	private final HtmlColor foregroundColor;
	private final double thickness;

	private final double deltaShadow;

	public Control(HtmlColor backgroundColor, HtmlColor foregroundColor, double deltaShadow, double thickness) {
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.deltaShadow = deltaShadow;
		this.thickness = thickness;
	}

	public void drawUNewWayINLINED(UGraphic ug) {
		double x = 0;
		double y = 0;
		x += margin;
		y += margin;
		ug = ug.apply(new UStroke(thickness));
		ug = ug.apply(new UChangeBackColor(backgroundColor)).apply(new UChangeColor(foregroundColor));
		final UEllipse circle = new UEllipse(radius * 2, radius * 2);
		circle.setDeltaShadow(deltaShadow);
		ug.drawNewWay(x, y, circle);
		ug = ug.apply(new UStroke());
		
		ug = ug.apply(new UChangeBackColor(foregroundColor));
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		final int xAile = 6;
		final int yOuverture = 5;
		polygon.addPoint(xAile, -yOuverture);
		final int xContact = 4;
		polygon.addPoint(xContact, 0);
		polygon.addPoint(xAile, yOuverture);
		polygon.addPoint(0, 0);
		
		ug.drawNewWay(x + radius - xContact, y, polygon);
	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		return new Dimension2DDouble(radius * 2 + 2 * margin, radius * 2 + 2 * margin);
	}

	public List<Url> getUrls() {
		return Collections.emptyList();
	}

}
