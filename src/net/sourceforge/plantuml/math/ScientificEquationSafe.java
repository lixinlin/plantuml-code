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
package net.sourceforge.plantuml.math;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.SvgString;
import net.sourceforge.plantuml.api.ImageDataSimple;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;
import net.sourceforge.plantuml.ugraphic.ImageBuilder;

public class ScientificEquationSafe {

	private final ScientificEquation equation;
	private final String formula;

	private ScientificEquationSafe(String formula, ScientificEquation equation) {
		this.formula = formula;
		this.equation = equation;
	}

	public static ScientificEquationSafe fromAsciiMath(String formula) {
		try {
			return new ScientificEquationSafe(formula, new AsciiMath(formula));
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Error parsing " + formula);
			return new ScientificEquationSafe(formula, null);
		}
	}

	public static ScientificEquationSafe fromLatex(String formula) {
		try {
			return new ScientificEquationSafe(formula, new LatexBuilder(formula));
		} catch (Exception e) {
			e.printStackTrace();
			Log.info("Error parsing " + formula);
			return new ScientificEquationSafe(formula, null);
		}
	}

	private ImageData dimSvg;

	public SvgString getSvg(double scale, Color foregroundColor, Color backgroundColor) {

		try {
			final SvgString svg = equation.getSvg(scale, foregroundColor, backgroundColor);
			dimSvg = new ImageDataSimple(equation.getDimension());
			return svg;
		} catch (Exception e) {
			printTrace(e);
			final ImageBuilder imageBuilder = getRollback();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				dimSvg = imageBuilder.writeImageTOBEMOVED(new FileFormatOption(FileFormat.SVG), 42, baos);
			} catch (IOException e1) {
				return null;
			}
			return new SvgString(new String(baos.toByteArray()), scale);
		}
	}

	public BufferedImage getImage(double scale, Color foregroundColor, Color backgroundColor) {
		try {
			return equation.getImage(scale, foregroundColor, backgroundColor);
		} catch (Exception e) {
			printTrace(e);
			final ImageBuilder imageBuilder = getRollback();
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				imageBuilder.writeImageTOBEMOVED(new FileFormatOption(FileFormat.PNG), 42, baos);
				return ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
			} catch (IOException e1) {
				return null;
			}
		}
	}

	private void printTrace(Exception e) {
		System.err.println("formula=" + formula);
		if (formula != null) {
			System.err.println("Latex=" + equation.getSource());
		}
		e.printStackTrace();
	}

	private ImageBuilder getRollback() {
		final TextBlock block = GraphicStrings.createBlackOnWhiteMonospaced(Arrays.asList(formula));
		final ImageBuilder imageBuilder = new ImageBuilder(new ColorMapperIdentity(), 1.0, null, null, null, 0, 0,
				null, false);
		imageBuilder.setUDrawable(block);
		return imageBuilder;
	}

	public ImageData export(OutputStream os, FileFormatOption fileFormat, float scale, Color foregroundColor,
			Color backgroundColor) throws IOException {
		if (fileFormat.getFileFormat() == FileFormat.PNG) {
			final BufferedImage image = getImage(scale, foregroundColor, backgroundColor);
			ImageIO.write(image, "png", os);
			return new ImageDataSimple(image.getWidth(), image.getHeight());
		}
		if (fileFormat.getFileFormat() == FileFormat.SVG) {
			os.write(getSvg(1, foregroundColor, backgroundColor).getSvg(true).getBytes());
			return dimSvg;
		}
		return null;
	}

}
