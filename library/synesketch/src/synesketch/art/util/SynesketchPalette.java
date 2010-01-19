/**
 * Synesketch 
 * Copyright (C) 2008  Uros Krcadinac
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package synesketch.art.util;

import java.util.Random;

import synesketch.util.PropertiesManager;

/**
 * Representation of a six-part color palette used for Synesketch sketches. Each
 * part of the palette represents colors -- defined by it's coded hex number --
 * used for visualization of a specific emotion by Ekman: happiness, sadness,
 * anger, fear, disgust, and surprise.
 * <p>
 * Palettes are property documents written in XML. Synesketch provides one
 * standard palette (data/palette/standard.xml), based on some phycology &
 * visual design theories, but everyone can write it's own palette by writing
 * one's own XML document similar to data/palette/standard.xml.
 * 
 * @author Uros Krcadinac email: uros@krcadinac.com
 * @version 1.0
 */
public class SynesketchPalette {

	private int[] fearColors;

	private int[] angerColors;

	private int[] disgustColors;

	private int[] happinessColors;

	private int[] sadnessColors;

	private int[] surpriseColors;

	private Random randomiser;

	/**
	 * Class contructor which sets six palettes -- one for each emotion type,
	 * happiness, sadness, anger, fear, disgust, and surprise -- by taking data
	 * from a XML file defined by palette's name.
	 * 
	 * @param paletteName
	 *            {@link String} name of the six-part palette -- XML file with
	 *            the color codes for each emotion type
	 */
	public SynesketchPalette(String paletteName) {
		PropertiesManager pm = new PropertiesManager("/data/palette/"
				+ paletteName.toLowerCase() + ".xml");
		happinessColors = pm.getIntArrayProperty("happiness.palette");
		sadnessColors = pm.getIntArrayProperty("sadness.palette");
		angerColors = pm.getIntArrayProperty("anger.palette");
		fearColors = pm.getIntArrayProperty("fear.palette");
		disgustColors = pm.getIntArrayProperty("disgust.palette");
		surpriseColors = pm.getIntArrayProperty("surprise.palette");
		randomiser = new Random();
	}
	
	/**
	 * Getter for the palette for the emotion of anger.
	 * 
	 * @return array of integers representing the palette for the emotion of anger
	 */
	public int[] getAngerColors() {
		return angerColors;
	}

	/**
	 * Getter for the palette for the emotion of disgust.
	 * 
	 * @return array of integers representing the palette for the emotion of disgust
	 */
	public int[] getDisgustColors() {
		return disgustColors;
	}

	/**
	 * Getter for the palette for the emotion of fear.
	 * 
	 * @return array of integers representing the palette for the emotion of fear
	 */
	public int[] getFearColors() {
		return fearColors;
	}

	/**
	 * Getter for the palette for the emotion of happiness.
	 * 
	 * @return array of integers representing the palette for the emotion of happiness
	 */
	public int[] getHappinessColors() {
		return happinessColors;
	}

	/**
	 * Getter for the palette for the emotion of sadness.
	 * 
	 * @return array of integers representing the palette for the emotion of sadness
	 */
	public int[] getSadnessColors() {
		return sadnessColors;
	}

	/**
	 * Getter for the palette for the emotion of surprise
	 * 
	 * @return array of integers representing the palette for the emotion of surprise.
	 */
	public int[] getSurpriseColors() {
		return surpriseColors;
	}

	/**
	 * Returns a random color from the hapiness palette.
	 * 
	 * @return integer which represents the color
	 */
	public int getRandomHappinessColor() {
		return happinessColors[randomiser.nextInt(happinessColors.length)];
	}

	/**
	 * Returns a random color from the sadness palette.
	 * 
	 * @return integer which represents the color
	 */
	public int getRandomSadnessColor() {
		return sadnessColors[randomiser.nextInt(sadnessColors.length)];
	}

	/**
	 * Returns a random color from the anger palette.
	 * 
	 * @return integer which represents the color
	 */
	public int getRandomAngerColor() {
		return angerColors[randomiser.nextInt(angerColors.length)];
	}

	/**
	 * Returns a random color from the fear palette.
	 * 
	 * @return integer which represents the color
	 */
	public int getRandomFearColor() {
		return fearColors[randomiser.nextInt(fearColors.length)];
	}

	/**
	 * Returns a random color from the disgust palette.
	 * 
	 * @return integer which represents the color
	 */
	public int getRandomDisgustColor() {
		return disgustColors[randomiser.nextInt(disgustColors.length)];
	}

	/**
	 * Returns a random color from the surprise palette.
	 * 
	 * @return integer which represents the color
	 */
	public int getRandomSurpriseColor() {
		return surpriseColors[randomiser.nextInt(surpriseColors.length)];
	}

}
