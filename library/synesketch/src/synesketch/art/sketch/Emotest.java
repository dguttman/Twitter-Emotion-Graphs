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

import java.io.IOException;

import processing.core.PApplet;

import synesketch.*;
import synesketch.art.util.SynesketchPalette;
import synesketch.emotion.*;

public class Emotest extends PApplet {
	
	private static final long serialVersionUID = 1L;
	
	int dim = 400;
	
	
	EmotionalState currentEmotionalState = new EmotionalState(); 
	
	SynesketchPalette palette = new SynesketchPalette("standard");
	
	SynesthetiatorEmotion syne;
	
	EmotionalState state;
	
	float sadTheta;
	
	float saturationFactor = 1.0f;
	
	StringBuffer currentText;
	
	public Emotest() {
		super();
	}
	
	public Emotest(int dim) {
		super();
		this.dim = dim;
	}
	
	public void setup() {
		size(dim, dim, P3D);
		background(0);
		noStroke();
		String words;
		words = "wow I am so very happy right now.";
		try {
			state = Empathyscope.getInstance().feel(words);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public void draw() { 
		println(state.toString());
	}
	

	
}