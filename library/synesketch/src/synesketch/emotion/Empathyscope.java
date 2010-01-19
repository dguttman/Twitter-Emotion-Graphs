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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import synesketch.emotion.util.HeuristicsUtility;
import synesketch.emotion.util.LexicalUtility;
import synesketch.emotion.util.ParsingUtility;

/**
 * Defines logic for transfering textual affect information -- emotional
 * manifestations recognised in text -- into visual output.
 * 
 * @author Uros Krcadinac email: uros@krcadinac.com
 * @version 1.0
 */
public class Empathyscope {

	private static Empathyscope instance;

	private LexicalUtility lexUtil;

	private Empathyscope() throws IOException {
		lexUtil = LexicalUtility.getInstance();
	}

	/**
	 * Returns the Singleton instance of the {@link Empathyscope}.
	 * 
	 * @return {@link Empathyscope} instance
	 * @throws IOException
	 */
	public static Empathyscope getInstance() throws IOException {
		if (instance == null) {
			instance = new Empathyscope();
		}
		return instance;
	}

	/**
	 * Textual affect sensing behavior, the main NLP alghoritm which uses
	 * Synesketch Lexicon and several heuristic rules.
	 * 
	 * @param text
	 *            String representing the text to be analysed
	 * @return {@link EmotionalState} which represents data recognised from the
	 *         text
	 * @throws IOException
	 */
	public EmotionalState feel(String text) throws IOException {

		text = text.replace('\n', ' ');
		List<AffectWord> affectWords = new ArrayList<AffectWord>();
		List<String> sentences = ParsingUtility.parseSentences(text);

		for (String sentence : sentences) {

			// we imploy 5 heuristic rules to adjust emotive weights of the
			// words:
			// (1) negation in a sentence => flip valence of the affect words in
			// it
			boolean hasNegation = HeuristicsUtility.hasNegation(sentence
					.toLowerCase());

			// (2) more exclamination signs in a sentence => more intensive
			// emotive weights
			double exclaminationQoef = HeuristicsUtility
					.computeExclaminationQoef(sentence.toLowerCase());

			List<String> splittedWords = ParsingUtility.splitWords(sentence,
					" ");
			String previousWord = "";
			for (String splittedWord : splittedWords) {

				AffectWord emoWord = lexUtil
						.getEmoticonAffectWord(splittedWord);

				if (emoWord != null) {

					// (3) more emoticons with more 'emotive' signs (e.g. :DDDD)
					// => more intensive emotive weights
					double emoticonCoef = HeuristicsUtility
							.computeEmoticonCoef(splittedWord, emoWord);
					emoWord.adjustWeights(exclaminationQoef * emoticonCoef);
					affectWords.add(emoWord);
				} else {

					List<String> words = ParsingUtility
							.parseWords(splittedWord);
					for (String word : words) {
						emoWord = lexUtil.getAffectWord(word.toLowerCase());
						if (emoWord != null) {

							// (4) word is upper case => more intensive emotive
							// weights
							double capsLockCoef = HeuristicsUtility
									.computeCapsLockQoef(word);

							// (5) previous word is a intensity modifier (e.g.
							// "extremly") => more intensive emotive weights
							double modifierCoef = HeuristicsUtility
									.computeModifier(previousWord);

							// change the affect word!
							if (hasNegation)
								emoWord.flipValence();
							emoWord.adjustWeights(exclaminationQoef
									* capsLockCoef * modifierCoef);

							affectWords.add(emoWord);
						}
						previousWord = word;
					}
				}
			}
		}
		return createEmotionalState(text, affectWords);
	}

	private EmotionalState createEmotionalState(String text,
			List<AffectWord> affectWords) {
		TreeSet<Emotion> emotions = new TreeSet<Emotion>();
		int generalValence = 0;
		double valence, generalWeight, happinessWeight, sadnessWeight, angerWeight, fearWeight, disgustWeight, surpriseWeight;
		// valence = generalWeight = happinessWeight = sadnessWeight =
		// angerWeight = fearWeight = disgustWeight = surpriseWeight = 0.0;
		valence = 0.0;
		generalWeight = 0.0;
		happinessWeight = 0.0;
		sadnessWeight = 0.0;
		angerWeight = 0.0;
		fearWeight = 0.0;
		disgustWeight = 0.0;
		surpriseWeight = 0.0;

		// compute weights. maximum weights for the particular emotion are
		// taken.
		for (AffectWord affectWord : affectWords) {
			valence += affectWord.getGeneralValence();
			if (affectWord.getGeneralWeight() > generalWeight)
				generalWeight = affectWord.getGeneralWeight();
			if (affectWord.getHappinessWeight() > happinessWeight)
				happinessWeight = affectWord.getHappinessWeight();
			if (affectWord.getSadnessWeight() > sadnessWeight)
				sadnessWeight = affectWord.getSadnessWeight();
			if (affectWord.getAngerWeight() > angerWeight)
				angerWeight = affectWord.getAngerWeight();
			if (affectWord.getFearWeight() > fearWeight)
				fearWeight = affectWord.getFearWeight();
			if (affectWord.getDisgustWeight() > disgustWeight)
				disgustWeight = affectWord.getDisgustWeight();
			if (affectWord.getSurpriseWeight() > surpriseWeight)
				surpriseWeight = affectWord.getSurpriseWeight();
		}
		if (valence > 0)
			generalValence = 1;
		else if (valence < 0)
			generalValence = -1;

		if (happinessWeight > 0)
			emotions.add(new Emotion(happinessWeight, Emotion.HAPPINESS));
		if (sadnessWeight > 0)
			emotions.add(new Emotion(sadnessWeight, Emotion.SADNESS));
		if (angerWeight > 0)
			emotions.add(new Emotion(angerWeight, Emotion.ANGER));
		if (fearWeight > 0)
			emotions.add(new Emotion(fearWeight, Emotion.FEAR));
		if (disgustWeight > 0)
			emotions.add(new Emotion(disgustWeight, Emotion.DISGUST));
		if (surpriseWeight > 0)
			emotions.add(new Emotion(surpriseWeight, Emotion.SURPRISE));
		if (emotions.isEmpty())
			emotions.add(new Emotion((0.2 + generalWeight) / 1.2,
					Emotion.NEUTRAL));
		return new EmotionalState(text, emotions, generalWeight, generalValence);
	}

}
