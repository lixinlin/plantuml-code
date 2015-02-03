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
 * Revision $Revision: 5183 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.util.Iterator;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.UDrawable;
import net.sourceforge.plantuml.sequencediagram.graphic.Stairs;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class LiveBoxes implements UDrawable {

	private final EventsHistory eventsHistory;
	private final Skin skin;
	private final ISkinParam skinParam;
	private final double totalHeight;
	private final Context2D context;

	public LiveBoxes(EventsHistory eventsHistory, Skin skin, ISkinParam skinParam, double totalHeight, Context2D context) {
		this.eventsHistory = eventsHistory;
		this.skin = skin;
		this.skinParam = skinParam;
		this.totalHeight = totalHeight;
		this.context = context;
	}

	public void drawU(UGraphic ug) {
		final Stairs stairs = eventsHistory.getStairs(totalHeight);
		final int max = stairs.getMaxValue();
		for (int i = 1; i <= max; i++) {
			drawOneLevel(ug, i, stairs, context);
		}
	}

	private void drawOneLevel(UGraphic ug, int levelToDraw, Stairs stairs, Context2D context) {
		final Component comp = skin.createComponent(ComponentType.ALIVE_BOX_CLOSE_CLOSE, null, skinParam, null);
		final double width = comp.getPreferredWidth(ug.getStringBounder());
		ug = ug.apply(new UTranslate((levelToDraw - 1) * width / 2.0, 0));

		double y1 = Double.MAX_VALUE;
		for (Iterator<Double> it = stairs.getYs().iterator(); it.hasNext();) {
			final double y = it.next();
			final int level = stairs.getValue(y);
			if (y1 == Double.MAX_VALUE && level == levelToDraw) {
				y1 = y;
			} else if (y1 != Double.MAX_VALUE && (it.hasNext() == false || level < levelToDraw)) {
				final double y2 = y;
				final Area area = new Area(width, y2 - y1);
				comp.drawU(ug.apply(new UTranslate(-width / 2, y1)), area, context);
				y1 = Double.MAX_VALUE;
			}
		}
	}

}
