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
 * Revision $Revision: 9786 $
 *
 */
package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorTransparent;

public class UParam {

	private HtmlColor color = null;
	private HtmlColor backcolor = null;
	private UStroke stroke = new UStroke(1);
	private boolean hidden = false;

	public void reset() {
		color = null;
		backcolor = null;
		stroke = new UStroke(1);
		hidden = false;
	}

	public void setColor(HtmlColor color) {
		this.color = color;
	}

	public HtmlColor getColor() {
		return color;
	}

	public void setBackcolor(HtmlColor color) {
		if (color instanceof HtmlColorTransparent) {
			throw new UnsupportedOperationException();
		}
		this.backcolor = color;
	}

	public HtmlColor getBackcolor() {
		return backcolor;
	}

	public void setStroke(UStroke stroke) {
		this.stroke = stroke;
	}

	public UStroke getStroke() {
		return stroke;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
}
