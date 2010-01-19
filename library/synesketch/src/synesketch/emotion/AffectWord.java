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
 * Represents one unit from the Synesketch Lexicon: a word associated with
 * emotional meaning, and it's emotional weights and valence.
 * <p>
 * Synesketch Lexicon, which consits of several thousand words (with emoticons),
 * associates these atributes to a word:
 * <ul>
 * <li>General emotional weight
 * <li>General emotional valence
 * <li>Happiness weight
 * <li>Sadness weight
 * <li>Fear weight
 * <li>Anger weight
 * <li>Disgust weight
 * <li>Surprise weight
 * </ul>
 * 
 * @author Uros Krcadinac email: uros@krcadinac.com
 * @version 1.0
 */
public class AffectWord {

	private String word;

	private double generalWeight = 0.0;

	private double generalValence = 0.0;

	private double happinessWeight = 0.0;

	private double sadnessWeight = 0.0;

	private double angerWeight = 0.0;

	private double fearWeight = 0.0;

	private double disgustWeight = 0.0;

	private double surpriseWeight = 0.0;

	private boolean startsWithEmoticon = false;

	/**
	 * Class constructor which sets the affect word
	 * 
	 * @param word
	 *            String representing the word
	 */
	public AffectWord(String word) {
		this.word = word;
	}

	/**
	 * Class constructor which sets the affect word and it's weights. Valence is
	 * calculated as a function of different emotion type weights.
	 * 
	 * @param word
	 *            {@link String} representing the word
	 * @param generalWeight
	 *            double representing the general emotional weight
	 * @param happinessWeight
	 *            double representing the happiness weight
	 * @param sadnessWeight
	 *            double representing the sadness weight
	 * @param angerWeight
	 *            double representing the anger weight
	 * @param fearWeight
	 *            double representing the fear weight
	 * @param disgustWeight
	 *            double representing the disgust weight
	 * @param surpriseWeight
	 *            double representing the surprise weight
	 */
	public AffectWord(String word, double generalWeight,
			double happinessWeight, double sadnessWeight, double angerWeight,
			double fearWeight, double disgustWeight, double surpriseWeight) {
		this.word = word;
		this.generalWeight = generalWeight;
		this.happinessWeight = happinessWeight;
		this.sadnessWeight = sadnessWeight;
		this.angerWeight = angerWeight;
		this.fearWeight = fearWeight;
		this.disgustWeight = disgustWeight;
		this.surpriseWeight = surpriseWeight;
		this.generalValence = getValenceSum();
	}

	/**
	 * Class constructor which sets the affect word and it's weights, adjusted
	 * by the quoeficient. Valence is calculated as a function of different
	 * emotion type weights.
	 * 
	 * @param word
	 *            {@link String} representing the word
	 * @param generalWeight
	 *            double representing the general emotional weight
	 * @param happinessWeight
	 *            double representing the happiness weight
	 * @param sadnessWeight
	 *            double representing the sadness weight
	 * @param angerWeight
	 *            double representing the anger weight
	 * @param fearWeight
	 *            double representing the fear weight
	 * @param disgustWeight
	 *            double representing the disgust weight
	 * @param surpriseWeight
	 *            double representing the surprise weight
	 * @param quoficient
	 *            double representing the quoficient for adjusting the weights
	 */
	public AffectWord(String word, double generalWeight,
			double happinessWeight, double sadnessWeight, double angerWeight,
			double fearWeight, double disgustWeight, double surpriseWeight,
			double quoficient) {
		this.word = word;
		this.generalWeight = generalWeight * quoficient;
		this.happinessWeight = happinessWeight * quoficient;
		this.sadnessWeight = sadnessWeight * quoficient;
		this.angerWeight = angerWeight * quoficient;
		this.fearWeight = fearWeight * quoficient;
		this.disgustWeight = disgustWeight * quoficient;
		this.surpriseWeight = surpriseWeight * quoficient;
		this.generalValence = getValenceSum();
	}

	/**
	 * Adjusts weights by the certain quoficient.
	 * 
	 * @param quoficient
	 *            double representing the quoficient for adjusting the weights
	 */

	public void adjustWeights(double quoficient) {
		this.generalWeight = generalWeight * quoficient;
		this.happinessWeight = happinessWeight * quoficient;
		this.sadnessWeight = sadnessWeight * quoficient;
		this.angerWeight = angerWeight * quoficient;
		this.fearWeight = fearWeight * quoficient;
		this.disgustWeight = disgustWeight * quoficient;
		this.surpriseWeight = surpriseWeight * quoficient;
		normalise();
	}

	private void normalise() {
		if (generalWeight > 1)
			generalWeight = 1.0;
		if (happinessWeight > 1)
			happinessWeight = 1.0;
		if (sadnessWeight > 1)
			sadnessWeight = 1.0;
		if (angerWeight > 1)
			angerWeight = 1.0;
		if (fearWeight > 1)
			fearWeight = 1.0;
		if (disgustWeight > 1)
			disgustWeight = 1.0;
		if (surpriseWeight > 1)
			surpriseWeight = 1.0;
	}

	/**
	 * Flips valence of the word -- calculates change from postive to negative
	 * emotion.
	 */
	public void flipValence() {
		generalValence = -generalValence;
		double temp = happinessWeight;
		happinessWeight = Math.max(Math.max(sadnessWeight, angerWeight), Math
				.max(fearWeight, disgustWeight));
		sadnessWeight = temp;
		angerWeight = temp / 2;
		fearWeight = temp / 2;
		disgustWeight = temp / 2;
	}

	/**
	 * Makes duplicate of the object.
	 * 
	 * @return {@link AffectWord}, new duplicated object
	 */
	public AffectWord clone() {
		AffectWord value = new AffectWord(word, generalWeight, happinessWeight,
				sadnessWeight, angerWeight, fearWeight, disgustWeight,
				surpriseWeight);
		value.setStartsWithEmoticon(startsWithEmoticon);
		return value;
	}

	/**
	 * Returns true if the word starts with the emoticon.
	 * 
	 * @return boolean (true if the word starts with the emoticon, false if not)
	 */

	public boolean startsWithEmoticon() {
		return startsWithEmoticon;
	}

	/**
	 * Sets does the word start with emoticon.
	 * 
	 * @param startsWithEmoticon
	 *            boolean (true if the word starts with the emoticon, false if
	 *            not)
	 */
	public void setStartsWithEmoticon(boolean startsWithEmoticon) {
		this.startsWithEmoticon = startsWithEmoticon;
	}

	/**
	 * Getter for the anger weight.
	 * 
	 * @return double which represents the anger weight
	 */
	public double getAngerWeight() {
		return angerWeight;
	}

	/**
	 * Getter for the anger weight.
	 * 
	 * @param angerWeight
	 *            double which represents the anger weight
	 */
	public void setAngerWeight(double angerWeight) {
		this.angerWeight = angerWeight;
	}

	/**
	 * Getter for the disgust weight.
	 * 
	 * @return double which represents the disgust weight
	 */
	public double getDisgustWeight() {
		return disgustWeight;
	}

	/**
	 * Setter for the disgust weight.
	 * 
	 * @param disgustWeight
	 *            double which represents the disgust weight
	 */
	public void setDisgustWeight(double disgustWeight) {
		this.disgustWeight = disgustWeight;
	}

	/**
	 * Getter for the fear weight.
	 * 
	 * @return double which represents the fear weight
	 */
	public double getFearWeight() {
		return fearWeight;
	}

	/**
	 * Getter for the fear weight.
	 * 
	 * @param fearWeight
	 *            double which represents the fear weight
	 */
	public void setFearWeight(double fearWeight) {
		this.fearWeight = fearWeight;
	}

	/**
	 * Getter for the happiness weight.
	 * 
	 * @return double which represents the happiness weight
	 */
	public double getHappinessWeight() {
		return happinessWeight;
	}

	/**
	 * Setter for the happiness weight.
	 * 
	 * @param happinessWeight
	 *            double which represents the happiness weight
	 */
	public void setHappinessWeight(double happinessWeight) {
		this.happinessWeight = happinessWeight;
	}

	/**
	 * Getter for the sadness weight.
	 * 
	 * @return double which represents the sadness weight
	 */
	public double getSadnessWeight() {
		return sadnessWeight;
	}

	/**
	 * Setter for the sadness weight.
	 * 
	 * @param sadnessWeight
	 *            double which represents the sadness weight
	 */
	public void setSadnessWeight(double sadnessWeight) {
		this.sadnessWeight = sadnessWeight;
	}

	/**
	 * Getter for the surprise weight.
	 * 
	 * @return double which represents the surprise weight
	 */
	public double getSurpriseWeight() {
		return surpriseWeight;
	}

	/**
	 * Setter for the surprise weight.
	 * 
	 * @param surpriseWeight
	 *            double which represents the surprise weight
	 */
	public void setSurpriseWeight(double surpriseWeight) {
		this.surpriseWeight = surpriseWeight;
	}

	/**
	 * Getter for the word.
	 * 
	 * @return {@link String} which represents the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Getter for the general weight.
	 * 
	 * @return double which represents the general weight
	 */
	public double getGeneralWeight() {
		return generalWeight;
	}

	/**
	 * Setter for the general weight.
	 * 
	 * @param generalWeight
	 *            double which represents the general weight
	 */
	public void setGeneralWeight(double generalWeight) {
		this.generalWeight = generalWeight;
	}

	/**
	 * Getter for the general valence.
	 * 
	 * @return double which represents the general valence
	 */
	public double getGeneralValence() {
		return generalValence;
	}

	/**
	 * Setter for the general valence
	 * 
	 * @param generalValence
	 *            double which represents the general valence
	 */
	public void setGeneralValence(int generalValence) {
		this.generalValence = generalValence;
	}

	/**
	 * Sets the boolean value which determines does a word have specific
	 * emotional weight for emotion types defined by Ekman: happiness, sadness,
	 * fear, anger, disgust, and surprise.
	 * 
	 * @return boolean value, true if all specific emotional weight have the
	 *         value of zero
	 */
	public boolean isZeroEkman() {
		if (getWeightSum() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns a string representation of the object.
	 * 
	 * @return a string representation of the object
	 */
	public String toString() {
		return word + " " + generalWeight + " " + happinessWeight + " "
				+ sadnessWeight + " " + angerWeight + " " + fearWeight + " "
				+ disgustWeight + " " + surpriseWeight;
	}

	private double getValenceSum() {
		return happinessWeight - sadnessWeight - angerWeight - fearWeight
				- disgustWeight;
	}

	private double getWeightSum() {
		return happinessWeight + sadnessWeight + angerWeight + fearWeight
				+ disgustWeight + surpriseWeight;
	}

}
