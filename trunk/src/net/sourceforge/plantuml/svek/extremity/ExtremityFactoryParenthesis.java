/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
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
package net.sourceforge.plantuml.svek.extremity;

import java.awt.geom.Point2D;

import net.sourceforge.plantuml.OptionFlags;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.svek.AbstractExtremityFactory;

public class ExtremityFactoryParenthesis extends AbstractExtremityFactory implements ExtremityFactory {

	public UDrawable createUDrawable(Point2D p0, Point2D p1, Point2D p2) {
		final double ortho = atan2(p0, p2);
		if (OptionFlags.USE_INTERFACE_EYE2) {
			final Point2D center = new Point2D.Double((p0.getX() + p2.getX()) / 2, (p0.getY() + p2.getY()) / 2);
			return new ExtremityParenthesis2(center, ortho, p1);
		}
		return new ExtremityParenthesis(p1, ortho);
	}

}
