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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import synesketch.emotion.AffectWord;
import synesketch.util.PropertiesManager;

/**
 * Utility class for some text processing alghoritms
 * 
 * @author Uros Krcadinac email: uros@krcadinac.com
 * @version 1.0
 */
public class LexicalUtility {

	private static LexicalUtility instance;

	private String fileNameLexicon = "/data/lex/synesketch_lexicon.txt";
	private String fileNameEmoticons = "/data/lex/synesketch_lexicon_emoticons.txt";
	private String fileNameProperties = "/data/lex/keywords.xml";

	private List<AffectWord> affectWords;
	private List<AffectWord> emoticons;

	private List<String> negations;

	private List<String> intensityModifiers;

	private double normalisator = 0.75;

	private LexicalUtility() throws IOException {
		affectWords = new ArrayList<AffectWord>();
		emoticons = new ArrayList<AffectWord>();
		PropertiesManager pm = new PropertiesManager(fileNameProperties);
		negations = ParsingUtility
				.splitWords(pm.getProperty("negations"), ", ");
		intensityModifiers = ParsingUtility.splitWords(pm
				.getProperty("intensity.modifiers"), ", ");
		parseLexiconFile(affectWords, fileNameLexicon);
		parseLexiconFile(emoticons, fileNameEmoticons);
	}

	/**
	 * Returns the Singleton instance of the {@link LexicalUtility}.
	 * 
	 * @return the instance of {@link LexicalUtility}
	 * @throws IOException
	 */
	public static LexicalUtility getInstance() throws IOException {
		if (instance == null) {
			instance = new LexicalUtility();
		}
		return instance;
	}

	private void parseLexiconFile(List<AffectWord> wordList, String fileName)
			throws IOException {
		// URL fileURL = this.getClass().getResource(fileName);
		// File file = new File(fileURL.getFile());
		// BufferedReader in = new BufferedReader(new InputStreamReader(new
		// FileInputStream(file), "UTF8"));
		BufferedReader in = new BufferedReader(new InputStreamReader(this
				.getClass().getResourceAsStream(fileName), "UTF8"));
		String line = in.readLine();
		while (line != null) {
			AffectWord record = parseLine(line);
			wordList.add(record);
			line = in.readLine();
		}
		in.close();
	}

	/**
	 * Parses one line of the Synesketch Lexicon and returns the
	 * {@link AffectWord}
	 * 
	 * @param line
	 *            {@link String} representing the line of the Synesketch Lexicon
	 * @return {@link AffectWord}
	 */
	private AffectWord parseLine(String line) {
		AffectWord value;
		String[] text = line.split(" ");
		String word = text[0];
		double generalWeight = Double.parseDouble(text[1]);
		double happinessWeight = Double.parseDouble(text[2]);
		double sadnessWeight = Double.parseDouble(text[3]);
		double angerWeight = Double.parseDouble(text[4]);
		double fearWeight = Double.parseDouble(text[5]);
		double disgustWeight = Double.parseDouble(text[6]);
		double surpriseWeight = Double.parseDouble(text[7]);
		value = new AffectWord(word, generalWeight, happinessWeight,
				sadnessWeight, angerWeight, fearWeight, disgustWeight,
				surpriseWeight, normalisator);
		return value;
	}

	/**
	 * Returns the instance of {@link AffectWord} for the given word.
	 * 
	 * @param word
	 *            {@link String} representing the word
	 * @return {@link AffectWord}
	 */
	public AffectWord getAffectWord(String word) {
		for (AffectWord affectWord : affectWords) {
			if (affectWord.getWord().equals(word)) {
				return affectWord.clone();
			}
		}
		return null;
	}

	/**
	 * Returns the instance of {@link AffectWord} for the given word, which is
	 * emoticon.
	 * 
	 * @param word
	 *            {@link String} representing the word
	 * @return {@link AffectWord}
	 */
	public AffectWord getEmoticonAffectWord(String word) {
		for (AffectWord affectWordEmoticon : emoticons) {
			if (affectWordEmoticon.getWord().equals(word)) {
				return affectWordEmoticon.clone();
			}
		}
		for (AffectWord affectWordEmoticon : emoticons) {
			String emoticon = affectWordEmoticon.getWord();
			if (ParsingUtility.containsFirst(word, emoticon)) {
				affectWordEmoticon.setStartsWithEmoticon(true);
				return affectWordEmoticon.clone();
			}
		}
		return null;
	}

	/**
	 * Returns all instances of {@link AffectWord} which represent emoticons for
	 * the given sentence.
	 * 
	 * @param sentence
	 *            {@link String} representing the sentence
	 * @return the list of {@link AffectWord} instances
	 */
	public List<AffectWord> getEmoticonWords(String sentence) {
		List<AffectWord> value = new ArrayList<AffectWord>();
		for (AffectWord emoticon : emoticons) {
			String emoticonWord = emoticon.getWord();
			if (sentence.contains(emoticonWord)) {
				emoticon.setStartsWithEmoticon(true);
				value.add(emoticon);
			}
		}
		return value;
	}

	/**
	 * Returns all instances of {@link AffectWord}
	 * 
	 * @return the list of {@link AffectWord} instances
	 */
	public List<AffectWord> getAffectWords() {
		return affectWords;
	}

	/**
	 * Returns true if the word is a negation.
	 * 
	 * @param word
	 *            {@link String} which represents a word
	 * @return boolean, true is the word is a negation
	 */
	public boolean isNegation(String word) {
		return negations.contains(word);
	}

	/**
	 * Returns true if the sentence contains a negation word in it.
	 * 
	 * @param sentence
	 *            {@link String} which represents a sentence
	 * @return boolean, true is the sentence contains negations
	 */
	public boolean hasNegation(String sentence) {
		for (String negation : negations) {
			if (sentence.contains(negation)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the word is an intensity modifier.
	 * 
	 * @param word
	 *            {@link String} which represents a word
	 * @return boolean, true is the word is an intensity modifier
	 */
	public boolean isIntensityModifier(String word) {
		return intensityModifiers.contains(word);
	}

}
