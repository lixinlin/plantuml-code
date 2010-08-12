/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5115 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.Skin;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UGraphic;

class DrawableSet {

	private final Map<Participant, LivingParticipantBox> participants = new LinkedHashMap<Participant, LivingParticipantBox>();
	private final Map<Event, GraphicalElement> events = new HashMap<Event, GraphicalElement>();
	private final List<Event> eventsList = new ArrayList<Event>();
	private final Skin skin;
	private final ISkinParam skinParam;
	private Dimension2D dimension;

	private double groupingMargin;

	DrawableSet(Skin skin, ISkinParam skinParam) {
		if (skin == null) {
			throw new IllegalArgumentException();
		}
		if (skinParam == null) {
			throw new IllegalArgumentException();
		}
		this.skin = skin;
		this.skinParam = skinParam;
	}

	public final Skin getSkin() {
		return skin;
	}

	public final ISkinParam getSkinParam() {
		return skinParam;
	}

	public Collection<Event> getAllEvents() {
		return Collections.unmodifiableCollection(eventsList);
	}

	public Set<Participant> getAllParticipants() {
		return Collections.unmodifiableSet(participants.keySet());
	}

	public Collection<LivingParticipantBox> getAllLivingParticipantBox() {
		return Collections.unmodifiableCollection(participants.values());
	}

	public Collection<GraphicalElement> getAllGraphicalElements() {
		final Collection<GraphicalElement> result = new ArrayList<GraphicalElement>();
		for (Event ev : eventsList) {
			result.add(events.get(ev));
		}
		return Collections.unmodifiableCollection(result);
	}

	public LivingParticipantBox getLivingParticipantBox(Participant p) {
		return participants.get(p);
	}

	public GraphicalElement getEvent(Event ev) {
		return events.get(ev);
	}

	public double getHeadHeight(StringBounder stringBounder) {
		double r = 0;
		for (LivingParticipantBox livingParticipantBox : participants.values()) {
			final double y = livingParticipantBox.getParticipantBox().getHeadHeight(stringBounder);
			r = Math.max(r, y);
		}
		return r;
	}

	public double getTailHeight(StringBounder stringBounder, boolean showTail) {
		if (showTail == false) {
			return 1;
		}
		double r = 0;
		for (LivingParticipantBox livingParticipantBox : participants.values()) {
			final double y = livingParticipantBox.getParticipantBox().getTailHeight(stringBounder);
			r = Math.max(r, y);
		}
		return r;
	}

	public void addParticipant(Participant p, LivingParticipantBox box) {
		participants.put(p, box);
	}

	public void addEvent(Event event, GraphicalElement object) {
		if (events.keySet().contains(event) == false) {
			eventsList.add(event);
		}
		events.put(event, object);
	}

	public void addEvent(Newpage newpage, GraphicalNewpage object, Event justBefore) {
		final int idx = eventsList.indexOf(justBefore);
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		eventsList.add(idx, newpage);
		events.put(newpage, object);
		assert events.size() == eventsList.size();
	}

	void setDimension(Dimension2D dim) {
		if (dimension != null) {
			throw new IllegalStateException();
		}
		this.dimension = dim;
	}

	public Dimension2D getDimension() {
		return dimension;
	}

	void drawU(UGraphic ug, final double delta, double width, Page page, boolean showTail) {

		final double atX = ug.getTranslateX();
		final double atY = ug.getTranslateY();

		final int height = (int) page.getHeight();

		clipAndTranslate(delta, width, page, ug);
		this.drawPlaygroundU(ug, height, new SimpleContext2D(true));

		ug.setClip(null);
		ug.setTranslate(atX, atY);

		this.drawLineU(ug, showTail, page);
		this.drawHeadTailU(ug, showTail ? height : 0, page);

		clipAndTranslate(delta, width, page, ug);
		this.drawPlaygroundU(ug, height, new SimpleContext2D(false));
	}

	private void clipAndTranslate(final double delta, double width, Page p, final UGraphic ug) {
		ug.setClip(new UClip(0, p.getBodyRelativePosition(), width, p.getBodyHeight() + 1));
		if (delta > 0) {
			ug.translate(0, -delta);
		}
	}

	private void drawLineU(UGraphic ug, boolean showTail, Page page) {
		ug.translate(groupingMargin, 0);
		for (LivingParticipantBox box : getAllLivingParticipantBox()) {
			final double create = box.getCreate();
			final double startMin = page.getBodyRelativePosition() - box.magicMargin(ug.getStringBounder());
			// final double endMax = page.getHeight() - 1;
			final double endMax = startMin + page.getBodyHeight() + 2 * box.magicMargin(ug.getStringBounder());
			double start = startMin;
			if (create > 0) {
				if (create > page.getNewpage2()) {
					continue;
				}
				if (create >= page.getNewpage1() && create < page.getNewpage2()) {
					start += create - page.getNewpage1() + 2 * box.magicMargin(ug.getStringBounder());
				}
			}
			box.drawLineU(ug, start, endMax, showTail);
		}
		ug.translate(-groupingMargin, 0);

	}

	private void drawHeadTailU(UGraphic ug, double height, Page page) {
		ug.translate(groupingMargin, 0);
		for (LivingParticipantBox box : getAllLivingParticipantBox()) {
			final double create = box.getCreate();
			boolean showHead = true;
			if (create > 0) {
				if (create > page.getNewpage2()) {
					continue;
				}
				if (create >= page.getNewpage1() && create < page.getNewpage2()) {
					showHead = false;
				}
			}
			box.getParticipantBox().drawHeadTailU(ug, height, showHead);
		}
		ug.translate(-groupingMargin, 0);
	}

	private double getMaxX() {
		return dimension.getWidth();
	}

	private double getMaxY() {
		return dimension.getHeight();
	}

	private void drawPlaygroundU(UGraphic ug, double height, Context2D context) {
		ug.translate(groupingMargin, 0);

		for (Participant p : getAllParticipants()) {
			drawLifeLineU(ug, p);
		}
		for (GraphicalElement element : getAllGraphicalElements()) {
			element.drawU(ug, getMaxX(), context);
		}
		ug.setClip(null);
		ug.translate(-groupingMargin, 0);
	}

	private void drawLifeLineU(UGraphic ug, Participant p) {
		final LifeLine line = getLivingParticipantBox(p).getLifeLine();

		line.finish(getMaxY());
		// skinParam.overideBackColor(new HtmlColor(("#00FF00")));
		// final Component comp =
		// getSkin().createComponent(ComponentType.ALIVE_LINE, skinParam, null);
		// skinParam.overideBackColor(null);
		line.drawU(ug, getSkin(), skinParam);
	}

}
