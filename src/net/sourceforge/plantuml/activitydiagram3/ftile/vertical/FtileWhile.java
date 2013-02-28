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
 * Revision $Revision: 8475 $
 *
 */
package net.sourceforge.plantuml.activitydiagram3.ftile.vertical;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.SpriteContainerEmpty;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.activitydiagram3.LinkRendering;
import net.sourceforge.plantuml.activitydiagram3.ftile.Arrows;
import net.sourceforge.plantuml.activitydiagram3.ftile.Diamond;
import net.sourceforge.plantuml.activitydiagram3.ftile.Ftile;
import net.sourceforge.plantuml.activitydiagram3.ftile.FtileMarged;
import net.sourceforge.plantuml.activitydiagram3.ftile.Snake;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.HtmlColorUtils;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.ugraphic.UEmpty;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;

class FtileWhile implements Ftile {

	private final double smallArrow = 30;
	final private double heightbottom = 45;

	private final Ftile whileBlock;
	private final TextBlock test;

	private final HtmlColor borderColor;
	private final HtmlColor arrowColor;
	private final HtmlColor endInlinkColor;
	private final HtmlColor afterEndwhileColor;

	public FtileWhile(Ftile whileBlock, Display test, VerticalFactory factory, HtmlColor borderColor,
			HtmlColor backColor, HtmlColor arrowColor, UFont font, HtmlColor endInlinkColor,
			HtmlColor afterEndwhileColor) {
		this.borderColor = borderColor;
		this.arrowColor = arrowColor;
		this.endInlinkColor = endInlinkColor;
		this.afterEndwhileColor = afterEndwhileColor;

		final HtmlColor firstArrowColor = LinkRendering.getColor(whileBlock.getInLinkRendering(), arrowColor);

		final Ftile tmp = new FtileAssemblySimple(new FtileDiamond(borderColor, backColor), new FtileAssemblySimple(
				new FtileVerticalArrow(smallArrow, firstArrowColor), whileBlock));

		this.whileBlock = new FtileMarged(tmp, 10);
		// final UFont font = new UFont("Serif", Font.PLAIN, 14);
		final FontConfiguration fc = new FontConfiguration(font, HtmlColorUtils.BLACK);
		if (test == null) {
			this.test = TextBlockUtils.empty(0, 0);
		} else {
			this.test = TextBlockUtils.create(test, fc, HorizontalAlignement.LEFT, new SpriteContainerEmpty());
		}
	}

	public void drawU(UGraphic ug, final double x, final double y) {
		final StringBounder stringBounder = ug.getStringBounder();
		final Dimension2D dimTotal = calculateDimension(stringBounder);

		whileBlock.drawU(ug, x, y);

		final Dimension2D dimWhile = whileBlock.calculateDimension(stringBounder);
		ug.getParam().setStroke(new UStroke(1.5));
		final Snake s1 = new Snake();
		ug.getParam().setColor(LinkRendering.getColor(afterEndwhileColor, arrowColor));
		s1.addPoint(x + dimTotal.getWidth() / 2 - Diamond.diamondHalfSize, y + Diamond.diamondHalfSize);
		s1.addPoint(x + 1, y + Diamond.diamondHalfSize);
		s1.addPoint(x + 1, y + dimTotal.getHeight());
		s1.addPoint(x + dimTotal.getWidth() / 2 - 1, y + dimTotal.getHeight());
		s1.drawU(ug);

		final Snake s2 = new Snake();
		ug.getParam().setColor(LinkRendering.getColor(endInlinkColor, arrowColor));
		s2.addPoint(x + dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, y + Diamond.diamondHalfSize);
		s2.addPoint(x + dimTotal.getWidth() - 2, y + Diamond.diamondHalfSize);
		s2.addPoint(x + dimTotal.getWidth() - 2, y + 12 + dimWhile.getHeight());
		s2.addPoint(x + dimTotal.getWidth() / 2, y + 12 + dimWhile.getHeight());
		s2.addPoint(x + dimTotal.getWidth() / 2, y + dimWhile.getHeight());
		s2.drawU(ug);
		ug.getParam().setStroke(new UStroke());
		ug.drawNewWay(x + dimTotal.getWidth() / 2, y + dimWhile.getHeight(), new UEmpty(20, 20));

		ug.getParam().setColor(LinkRendering.getColor(endInlinkColor, arrowColor));
		ug.getParam().setBackcolor(LinkRendering.getColor(endInlinkColor, arrowColor));
		ug.drawNewWay(x + dimTotal.getWidth() / 2 + Diamond.diamondHalfSize, y + Diamond.diamondHalfSize, Arrows.asToLeft());
		ug.drawNewWay(x + dimTotal.getWidth() - 2, y + dimTotal.getHeight() / 2, Arrows.asToUp());

		ug.getParam().setColor(LinkRendering.getColor(afterEndwhileColor, arrowColor));
		ug.getParam().setBackcolor(LinkRendering.getColor(afterEndwhileColor, arrowColor));
		ug.drawNewWay(x + 1, y + dimTotal.getHeight() / 2, Arrows.asToDown());

		final Dimension2D dimLabel = test.calculateDimension(stringBounder);
		test.drawU(ug, x + dimTotal.getWidth() / 2 - dimLabel.getWidth() / 2, y + 2 * Diamond.diamondHalfSize - 5);

	}

	public Dimension2D calculateDimension(StringBounder stringBounder) {
		Dimension2D dim = whileBlock.calculateDimension(stringBounder);
		dim = Dimension2DDouble.delta(dim, 0, heightbottom);
		return dim;
	}

	public List<Url> getUrls() {
		throw new UnsupportedOperationException();
	}

	public boolean isKilled() {
		return false;
	}

	public LinkRendering getInLinkRendering() {
		// if (endInlinkColor == null) {
		// return null;
		// }
		// return new LinkRendering(endInlinkColor);
		return null;
	}

}
