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
package net.sourceforge.plantuml.svek.image;

import java.awt.geom.Dimension2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.Direction;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.MathUtils;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graph2.GeomUtils;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockEmpty;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.posimo.DotPath;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.svek.AbstractEntityImage;
import net.sourceforge.plantuml.svek.Line;
import net.sourceforge.plantuml.svek.Shape;
import net.sourceforge.plantuml.svek.ShapeType;
import net.sourceforge.plantuml.ugraphic.UFont;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.ULine;
import net.sourceforge.plantuml.ugraphic.UPolygon;
import net.sourceforge.plantuml.ugraphic.UShape;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class EntityImageNote extends AbstractEntityImage {

	private final int cornersize = 10;
	private final HtmlColor noteBackgroundColor;
	private final HtmlColor borderColor;
	private final int marginX1 = 6;
	private final int marginX2 = 15;
	private final int marginY = 5;
	private final boolean withShadow;
	final private List<Url> url;

	private final TextBlock textBlock;

	public EntityImageNote(ILeaf entity, ISkinParam skinParam) {
		super(entity, getSkin(skinParam, entity));

		this.withShadow = skinParam.shadowing();
		final Display strings = entity.getDisplay();

		final Rose rose = new Rose();

		if (entity.getSpecificBackColor() == null) {
			noteBackgroundColor = rose.getHtmlColor(skinParam, ColorParam.noteBackground);
		} else {
			noteBackgroundColor = entity.getSpecificBackColor();
		}
		borderColor = rose.getHtmlColor(skinParam, ColorParam.noteBorder);
		final HtmlColor fontColor = rose.getFontColor(skinParam, FontParam.NOTE);
		final UFont fontNote = skinParam.getFont(FontParam.NOTE, null);

		if (strings.size() == 1 && strings.get(0).length() == 0) {
			textBlock = new TextBlockEmpty();
		} else {
			textBlock = TextBlockUtils.create(strings, new FontConfiguration(fontNote, fontColor),
					HorizontalAlignement.LEFT, skinParam);
		}
		for (Url u : textBlock.getUrls()) {
			entity.addUrl(u);
		}
		this.url = entity.getUrls();

	}

	private static ISkinParam getSkin(ISkinParam skinParam, IEntity entity) {
		final Stereotype stereotype = entity.getStereotype();
		HtmlColor back = entity.getSpecificBackColor();
		if (back != null) {
			return new SkinParamBackcolored(skinParam, back);
		}
		back = getColorStatic(skinParam, ColorParam.noteBackground, stereotype);
		if (back != null) {
			return new SkinParamBackcolored(skinParam, back);
		}
		return skinParam;
	}

	private static HtmlColor getColorStatic(ISkinParam skinParam, ColorParam colorParam, Stereotype stereo) {
		final String s = stereo == null ? null : stereo.getLabel();
		final Rose rose = new Rose();
		return rose.getHtmlColor(skinParam, colorParam, s);
	}

	final public double getPreferredWidth(StringBounder stringBounder) {
		final double result = getTextWidth(stringBounder);
		return result;
	}

	final public double getPreferredHeight(StringBounder stringBounder) {
		return getTextHeight(stringBounder);
	}

	// For cache
	private Dimension2D size;

	private Dimension2D getSize(StringBounder stringBounder, final TextBlock textBlock) {
		if (size == null) {
			size = textBlock.calculateDimension(stringBounder);
		}
		return size;
	}

	final protected double getTextHeight(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final Dimension2D size = getSize(stringBounder, textBlock);
		return size.getHeight() + 2 * marginY;
	}

	final protected TextBlock getTextBlock() {
		return textBlock;
	}

	final protected double getPureTextWidth(StringBounder stringBounder) {
		final TextBlock textBlock = getTextBlock();
		final Dimension2D size = getSize(stringBounder, textBlock);
		return size.getWidth();
	}

	final public double getTextWidth(StringBounder stringBounder) {
		return getPureTextWidth(stringBounder) + marginX1 + marginX2;
	}

	@Override
	public Dimension2D getDimension(StringBounder stringBounder) {
		final double height = getPreferredHeight(stringBounder);
		final double width = getPreferredWidth(stringBounder);
		return new Dimension2DDouble(width, height);
	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition) {
		ug = ug.apply(new UTranslate(xTheoricalPosition, yTheoricalPosition));
		if (url.size() > 0 && url.get(0).isMember() == false) {
			ug.startUrl(url.get(0));
		}
		if (opaleLine == null || opaleLine.isOpale() == false) {
			drawNormal(ug);
		} else {
			final StringBounder stringBounder = ug.getStringBounder();
			DotPath path = opaleLine.getDotPath();
			path.moveSvek(-shape.getMinX(), -shape.getMinY());
			Point2D p1 = path.getStartPoint();
			Point2D p2 = path.getEndPoint();
			final double textWidth = getTextWidth(stringBounder);
			final double textHeight = getTextHeight(stringBounder);
			final Point2D center = new Point2D.Double(textWidth / 2, textHeight / 2);
			if (p1.distance(center) > p2.distance(center)) {
				path = path.reverse();
				p1 = path.getStartPoint();
				p2 = path.getEndPoint();
			}
			final Direction strategy = getOpaleStrategy(textWidth, textHeight, p1);
			drawOpale(ug, xTheoricalPosition, yTheoricalPosition, path, strategy);
		}
		if (url.size() > 0 && url.get(0).isMember() == false) {
			ug.closeAction();
		}

	}

	// private Point2D translateShape(Point2D pt) {
	// return new Point2D.Double(pt.getX() - shape.getMinX(), pt.getY() -
	// shape.getMinY());
	// }

	private void drawOpale(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, DotPath path,
			Direction strategy) {
		final StringBounder stringBounder = ug.getStringBounder();

		final UPolygon polygon = getPolygonNormal(stringBounder);
		if (withShadow) {
			polygon.setDeltaShadow(4);
		}
		ug.getParam().setColor(borderColor);
		ug.getParam().setBackcolor(noteBackgroundColor);
		ug.drawOldWay(polygon);

		final Point2D pp1 = path.getStartPoint();
		final Point2D pp2 = path.getEndPoint();

		final UShape polygonOpale;
		if (strategy == Direction.LEFT) {
			polygonOpale = getPolygonLeft(stringBounder, pp1, pp2, path);
		} else if (strategy == Direction.RIGHT) {
			polygonOpale = getPolygonRight(stringBounder, pp1, pp2, path);
		} else if (strategy == Direction.UP) {
			polygonOpale = getPolygonUp(stringBounder, pp1, pp2, path);
		} else if (strategy == Direction.DOWN) {
			polygonOpale = getPolygonDown(stringBounder, pp1, pp2, path);
		} else {
			throw new IllegalArgumentException();
		}

//		if (withShadow && polygonOpale instanceof UPolygon) {
//			((UPolygon) polygonOpale).setDeltaShadow(4);
//		}
		ug.getParam().setColor(borderColor);
		ug.getParam().setBackcolor(noteBackgroundColor);
		ug.drawOldWay(polygonOpale);

		ug.drawNewWay(getTextWidth(stringBounder) - cornersize, 0, new ULine(0, cornersize));
		ug.drawNewWay(getTextWidth(stringBounder), cornersize, new ULine(-cornersize, 0));
		getTextBlock().drawU(ug, marginX1, marginY);
	}

	private void drawNormal(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		final UPolygon polygon = getPolygonNormal(stringBounder);
		if (withShadow) {
			polygon.setDeltaShadow(4);
		}
		ug.getParam().setColor(borderColor);
		ug.getParam().setBackcolor(noteBackgroundColor);
		ug.drawOldWay(polygon);

		ug.drawNewWay(getTextWidth(stringBounder) - cornersize, 0, new ULine(0, cornersize));
		ug.drawNewWay(getTextWidth(stringBounder), cornersize, new ULine(-cornersize, 0));
		getTextBlock().drawU(ug, marginX1, marginY);

	}

	private UPolygon getPolygonNormal(final StringBounder stringBounder) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, getTextHeight(stringBounder));
		polygon.addPoint(getTextWidth(stringBounder), getTextHeight(stringBounder));
		polygon.addPoint(getTextWidth(stringBounder), cornersize);
		polygon.addPoint(getTextWidth(stringBounder) - cornersize, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

	private final double delta = 4;

	private UPolygon getPolygonLeft(final StringBounder stringBounder, final Point2D pp1, final Point2D pp2,
			DotPath path) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);

		double y1 = pp1.getY() - delta;
		y1 = MathUtils.limitation(y1, 0, getTextHeight(stringBounder) - 2 * delta);
		polygon.addPoint(0, y1);
		polygon.addPoint(pp2.getX(), pp2.getY());
		polygon.addPoint(0, y1 + 2 * delta);

		polygon.addPoint(0, getTextHeight(stringBounder));
		polygon.addPoint(getTextWidth(stringBounder), getTextHeight(stringBounder));
		polygon.addPoint(getTextWidth(stringBounder), cornersize);
		polygon.addPoint(getTextWidth(stringBounder) - cornersize, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

	private UPolygon getPolygonRight(final StringBounder stringBounder, final Point2D pp1, final Point2D pp2, DotPath path) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, getTextHeight(stringBounder));
		polygon.addPoint(getTextWidth(stringBounder), getTextHeight(stringBounder));

		double y1 = pp1.getY() - delta;
		y1 = MathUtils.limitation(y1, cornersize, getTextHeight(stringBounder) - 2 * delta);
		polygon.addPoint(getTextWidth(stringBounder), y1 + 2 * delta);
		polygon.addPoint(pp2.getX(), pp2.getY());
		polygon.addPoint(getTextWidth(stringBounder), y1);

		polygon.addPoint(getTextWidth(stringBounder), cornersize);
		polygon.addPoint(getTextWidth(stringBounder) - cornersize, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

	private UPolygon getPolygonUp(final StringBounder stringBounder, final Point2D pp1, final Point2D pp2, DotPath path) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, getTextHeight(stringBounder));
		polygon.addPoint(getTextWidth(stringBounder), getTextHeight(stringBounder));
		polygon.addPoint(getTextWidth(stringBounder), cornersize);
		polygon.addPoint(getTextWidth(stringBounder) - cornersize, 0);

		double x1 = pp1.getX() - delta;
		x1 = MathUtils.limitation(x1, 0, getTextWidth(stringBounder) - cornersize);
		polygon.addPoint(x1 + 2 * delta, 0);
		polygon.addPoint(pp2.getX(), pp2.getY());

		polygon.addPoint(x1, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

	private UPolygon getPolygonDown(final StringBounder stringBounder, final Point2D pp1, final Point2D pp2, DotPath path) {
		final UPolygon polygon = new UPolygon();
		polygon.addPoint(0, 0);
		polygon.addPoint(0, getTextHeight(stringBounder));

		double x1 = pp1.getX() - delta;
		x1 = MathUtils.limitation(x1, 0, getTextWidth(stringBounder));
		polygon.addPoint(x1, getTextHeight(stringBounder));
		polygon.addPoint(pp2.getX(), pp2.getY());
		polygon.addPoint(x1 + 2 * delta, getTextHeight(stringBounder));

		polygon.addPoint(getTextWidth(stringBounder), getTextHeight(stringBounder));
		polygon.addPoint(getTextWidth(stringBounder), cornersize);
		polygon.addPoint(getTextWidth(stringBounder) - cornersize, 0);
		polygon.addPoint(0, 0);
		return polygon;
	}

	private Direction getOpaleStrategy(double width, double height, Point2D pt) {
		final double d1 = GeomUtils.getOrthoDistance(new Line2D.Double(width, 0, width, height), pt);
		final double d2 = GeomUtils.getOrthoDistance(new Line2D.Double(0, height, width, height), pt);
		final double d3 = GeomUtils.getOrthoDistance(new Line2D.Double(0, 0, 0, height), pt);
		final double d4 = GeomUtils.getOrthoDistance(new Line2D.Double(0, 0, width, 0), pt);
		if (d3 <= d1 && d3 <= d2 && d3 <= d4) {
			return Direction.LEFT;
		}
		if (d1 <= d2 && d1 <= d3 && d1 <= d4) {
			return Direction.RIGHT;
		}
		if (d4 <= d1 && d4 <= d2 && d4 <= d3) {
			return Direction.UP;
		}
		if (d2 <= d1 && d2 <= d3 && d2 <= d4) {
			return Direction.DOWN;
		}
		return null;

	}

	public ShapeType getShapeType() {
		return ShapeType.RECTANGLE;
	}

	public int getShield() {
		return 0;
	}

	private Line opaleLine;
	private Shape shape;

	public void setOpaleLine(Line line, Shape shape) {
		this.opaleLine = line;
		this.shape = shape;

	}

}
