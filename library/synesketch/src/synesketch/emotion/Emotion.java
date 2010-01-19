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
package synesketch.emotion;

/**
 * Represents one emotion, with it's type and weight.
 * <p>
 * Emotion types are the ones defined by Ekman: happiness, sadness, fear, anger,
 * disgust, surprise. These types are defines by the static attributes of this
 * class.
 * 
 * @author Uros Krcadinac email: uros@krcadinac.com
 * @version 1.0
 */

public class Emotion implements Comparable<Emotion> {

	public static int NEUTRAL = -1;

	public static int HAPPINESS = 0;

	public static int SADNESS = 1;

	public static int FEAR = 2;

	public static int ANGER = 3;

	public static int DISGUST = 4;

	public static int SURPRISE = 5;

	private double weight;

	private int type;

	/**
	 * Class constructor which sets weight and type of the emotion.
	 * 
	 * @param weight
	 *            double which represents the intensity of the emotion (can take
	 *            values between 0 and 1)
	 * @param type
	 *            type of the emotion (happiness, sadness, fear, anger, disgust,
	 *            or surprise)
	 */

	public Emotion(double weight, int type) {
		this.weight = weight;
		this.type = type;
	}

	/**
	 * Compares weights of current object and the one from the argument.
	 * 
	 * @param arg0
	 *            {@link Emotion} which is to compared to the current one
	 * @return integer representing the result
	 */
	public int compareTo(Emotion arg0) {
		int value = (int) (100 * (arg0.getWeight() - weight));
		// make sure each emotion will be considered, even if it is weight-even
		// with another one
		if (value == 0)
			return 1;
		return value;
	}

	/**
	 * Getter for the emotion type
	 * 
	 * @return emotion type (integer constant defined by this class)
	 */
	public int getType() {
		return type;
	}

	/**
	 * Setter for the emotion type
	 * 
	 * @param type
	 *            emotion type (integer constant defined by this class)
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Getter for the emotional weight
	 * 
	 * @return double representing the emotional weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * Setter for the emotional weight
	 * 
	 * @param value
	 *            double representing the emotional weight
	 */
	public void setWeight(double value) {
		this.weight = value;
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object
	 */
	public String toString() {
		return "Type number: " + type + ", weight: " + weight;
	}

}
