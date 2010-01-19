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
package synesketch.emotion.util;

import java.io.IOException;
import java.util.List;

import synesketch.emotion.AffectWord;

/**
 * Utility class for some heuristic alghoritms used for text processing.
 * 
 * @author Uros Krcadinac email: uros@krcadinac.com
 * @version 1.0
 */
public class HeuristicsUtility {

	/**
	 * Computes emotiocon qoef for the sentence. Qoef is based on number of
	 * important chars in an emotion (e.g. ')' in ':)))))' ).
	 * 
	 * @param sentence
	 *            {@link String} representing the sentence
	 * @return double value of the emoticon coef
	 * @throws IOException
	 */
	public static double computeEmoticonCoefForSentence(String sentence)
			throws IOException {
		List<AffectWord> emoticons = LexicalUtility.getInstance()
				.getEmoticonWords(sentence);
		double value = 1.0;
		for (AffectWord emot : emoticons) {
			String emotWord = emot.getWord();
			value *= 1.0 + (0.2 * countChars(sentence, emotWord.charAt(emotWord
					.length() - 1)));
		}
		return value;
	}

	/**
	 * Computes emoticon qoef for the word. Qoef is based on number of important
	 * chars in an emotion (e.g. ')' in ':)))))' ).
	 * 
	 * @param word
	 *            {@link String} representing the word
	 * @param emoticon
	 *            {@link AffectWord} representing the emoticon
	 * @return double value of the emoticon qoef
	 */
	public static double computeEmoticonCoef(String word, AffectWord emoticon) {
		if (emoticon.startsWithEmoticon()) {
			String emotiveWord = emoticon.getWord();
			return 1.0 + (0.2 * countChars(word, emotiveWord.charAt(emotiveWord
					.length() - 1)));
		} else {
			return 1.0;
		}
	}

	/**
	 * Returns true if sentence has negation in it.
	 * 
	 * @param sentence
	 *            {@link String} representing the sentence
	 * @return boolean, true if sentence has negation in it
	 * @throws IOException
	 */
	public static boolean hasNegation(String sentence) throws IOException {
		return LexicalUtility.getInstance().hasNegation(sentence);
	}

	/**
	 * Computes the intensity modifier based on the word.
	 * 
	 * @param word
	 *            {@link String} representing the word
	 * @return double representing the modifier
	 * @throws IOException
	 */
	public static double computeModifier(String word) throws IOException {
		if (isIntensityModifier(word))
			return 1.5;
		else
			return 1.0;
	}

	/**
	 * Computes the upper case qoeficient.
	 * 
	 * @param word {@link String} representing the word
	 * @return double representing the upper case qoeficient
	 */
	public static double computeCapsLockQoef(String word) {
		if (isCapsLock(word))
			return 1.5;
		else
			return 1.0;
	}
	
	/**
	 * Computes the exclamination qoef -- function of a number of '!' chars in a sentence.
	 * 
	 * @param text {@link String} representing the sentence 
	 * @return double representing the exclamination qoef
	 */
	
	public static double computeExclaminationQoef(String text) {
		return 1.0 + (0.2 * countChars(text, '!'));
	}

	private static boolean isCapsLock(String word) {
		for (int i = 0; i < word.length(); i++) {
			if (Character.isLowerCase(word.charAt(i)))
				return false;
		}
		return true;
	}

	private static boolean isIntensityModifier(String word) throws IOException {
		return LexicalUtility.getInstance().isIntensityModifier(word);
	}

	private static int countChars(String arg, char c) {
		int count = 0;
		for (int i = 0; i < arg.length(); i++) {
			if (arg.charAt(i) == c)
				count++;
		}
		return count;
	}

}
