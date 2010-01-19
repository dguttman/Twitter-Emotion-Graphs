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
package synesketch.art.sketch;

import processing.core.PApplet;
import synesketch.SynesketchState;
import synesketch.art.util.SynesketchPalette;
import synesketch.emotion.Emotion;
import synesketch.emotion.EmotionalState;

public class Hooloovoo extends PApplet  {

	private static final long serialVersionUID = 1L;

	SynesketchPalette palette = new SynesketchPalette("standard");

	int[] currentPalette = new int[38];

	int[] bwPalette = {-10461088, -7303024, -6579301, -10987432, -7368817, -9868951, 
			-5921371, -10526881, -8421505, -8224126, -6381922, -8224126, -8816263, 
			-10724260, -11645362, -9934744, -5658199, -8947849, -5395027, -6579301, 
			-9868951, -6842473, -11053225, -9276814, -6645094, -8816263, -6710887, 
			-5921371, -10987432, -8092540, -7039852, -7697782, -5789785, -8750470, 
			-10197916, -6381922, -8750470, -5855578};

	int dim = 400;
	int size = 40;
	int delay = 1500;
	int trans = 50;
	float sat = 1.0f;
	
	public Hooloovoo(int dim) {
		super();
		this.dim = dim;
	}

	public void setup() {
		colorMode(HSB, 1.0f);
		size(dim, dim);
		noStroke();
		//noLoop();
		//smooth();
		currentPalette = bwPalette;
	}

	public void draw() {
		//colorMode(RGB);
		for (int i = 0; i < dim/size + 1; i++) {
			for (int j = 0; j < dim/size + 1; j++) {
				int color = getRandomColor();
				noStroke();
				color = color(hue(color), saturation(color) * sat * 0.3f, brightness(color));
				colorMode(RGB);
				fill(red(color), green(color), blue(color), 1);
				//fill(hue(color), saturation(color), brightness(color), trans);
				rect(i*size, j*size, size, size);
				colorMode(HSB, 1.0f);
			}	
		}
		//colorMode(HSB, 1.0f);
		delay(delay);
	}

	public void synesketchUpdate(SynesketchState state) {
		colorMode(HSB, 1.0f);
		EmotionalState currentState = (EmotionalState) state;
		System.out.println(currentState);
		Emotion emo = currentState.getStrongestEmotion();
		int currentEmotion = emo.getType();
		setSize(emo.getWeight());
		sat = (float) Math.sqrt(emo.getWeight());
		if (currentEmotion == Emotion.NEUTRAL) {
			currentPalette = bwPalette;
			delay = 1500;
			sat = 0.5f;
		} else if (currentEmotion == Emotion.HAPPINESS) {
			currentPalette = palette.getHappinessColors();
			delay = 400;
		} else if (currentEmotion == Emotion.SADNESS) {
			currentPalette = palette.getSadnessColors();
			delay = 1500;
		} else if (currentEmotion == Emotion.ANGER) {
			currentPalette = palette.getAngerColors();
			delay = 300;
		} else if (currentEmotion == Emotion.FEAR) {
			currentPalette = palette.getFearColors();
			delay = 800;
		} else if (currentEmotion == Emotion.DISGUST) {
			currentPalette = palette.getDisgustColors();
			delay = 800;
		} else if (currentEmotion == Emotion.SURPRISE) {
			currentPalette = palette.getSurpriseColors();
			delay = 200;
		}
	}
	
	public void setSize(double w) {
		if (w > 0.75) {
			size = 100;
		} else if (w > 0.5) { 
			size = 80;
		} else if (w > 0.25) {
			size = 60;
		} else {
			size = 40;
		}
	}

	public int getRandomColor() {
		return currentPalette[(int)random(currentPalette.length)];
	}

}
