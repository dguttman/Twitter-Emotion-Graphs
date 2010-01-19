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

import synesketch.*;
import synesketch.art.util.SynesketchPalette;
import synesketch.emotion.*;

public class Synemania extends PApplet {
	
	private static final long serialVersionUID = 1L;
	
	int dim = 400;
	
	int maxHappies = 500;
	int maxSaddies = 800;
	int maxAngries = 800;
	int maxSurprises = 100;
	int maxFearies = 200;
	int maxDisgusties = 800;
	int maxNeutrals = 750;
	
	EmotionalState currentEmotionalState = new EmotionalState(); 
	
	SynesketchPalette palette = new SynesketchPalette("standard");
	
	SynesthetiatorEmotion syne;
	
	Particle neutrals[] = new NeutralParticle[maxNeutrals];
	Particle happies[] = new HappyParticle[maxHappies];
	Particle saddies[] = new SadParticle[maxSaddies];
	Particle angries[] = new AngryParticle[maxAngries];
	Particle surprises[] = new SupriseParticle[maxSurprises];
	Particle fearies[] = new FearParticle[maxFearies];
	Particle disgusties[] = new DisgustParticle[maxDisgusties];
	
	Particle currentParticles[];
	
	float sadTheta;
	
	float saturationFactor = 1.0f;
	
	StringBuffer currentText;
	
	public Synemania() {
		super();
	}
	
	public Synemania(int dim) {
		super();
		this.dim = dim;
	}
	
	public void setup() {
		size(dim, dim, P3D);
		background(255);
		noStroke();
		
		for (int i = 0; i < maxNeutrals; i++) {
			neutrals[i] = new NeutralParticle();
		}
		for (int i = 0; i < maxSaddies; i++) {
			saddies[i] = new SadParticle();
		}
		for (int i = 0; i < maxHappies; i++) {
			happies[i] = new HappyParticle();
		}
		for (int i = 0; i < maxAngries; i++) {
			angries[i] = new AngryParticle();
		}
		for (int i = 0; i < maxSurprises; i++) {
			surprises[i] = new SupriseParticle();
		}
		for (int i = 0; i < maxFearies; i++) {
			fearies[i] = new FearParticle();
		}
		for (int i = 0; i < maxDisgusties; i++) {
			disgusties[i] = new DisgustParticle();
		}
		
		sadTheta = random(TWO_PI);
		currentParticles = neutrals;
		try {
			syne = new SynesthetiatorEmotion(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void synesketchUpdate(SynesketchState state) {
		currentEmotionalState = (EmotionalState) state;
		//System.out.println(currentEmotionalState);
		currentParticles = 
			getCurrentParticles(currentEmotionalState.getStrongestEmotion());
	}
	
	public void draw() { 
		Emotion strongest = 
			currentEmotionalState.getStrongestEmotion();
		float weight = (float) strongest.getWeight();
		saturationFactor = (float) Math.sqrt(weight);
		int numberOfParticles = Math.round(currentParticles.length * saturationFactor); 
		for (int i = 0; i < numberOfParticles; i++) {
			currentParticles[i].move();
		}
		//if (random(1000) > 999) {
		//	saveFrame();
		//}
	}
	
	public Particle[] getCurrentParticles(Emotion e) {
		int currentEmotion = e.getType();
		if (currentEmotion == Emotion.HAPPINESS) {
			return happies;
		} else if (currentEmotion == Emotion.SADNESS) {
			return saddies;
		} else if (currentEmotion == Emotion.ANGER) {
			return angries;
		} else if (currentEmotion == Emotion.FEAR) {
			return fearies;
		} else if (currentEmotion == Emotion.DISGUST) {
			return disgusties;
		} else if (currentEmotion == Emotion.SURPRISE) {
			return surprises;
		} else {
			return neutrals;
		}
	}
	
	public int saturate(int color) {
		colorMode(HSB, 1.0f);
		color = color(hue(color), saturation(color) * 0.98f, brightness(color));
		colorMode(RGB, 255);
		return color;
	}

	/*
	 * Classes which describe emotion-specific particles, that is visual representation of each emotion.
	 */
	
	public abstract class Particle {
		
		int color;
		float x, y, vx, vy, theta, 
			speed, speedD, thetaD, thetaDD;
		
		Particle() {
			x = dim/2;
			y = dim/2;
		}
		
		abstract void collide();
		
		abstract void move();
		
	}
		
	public class NeutralParticle extends Particle {

		float gray;
		
		NeutralParticle() {
			super();
			gray = random(255);
		}
		
		void collide() {
			x = dim/2;
			y = dim/2;
			theta = random(TWO_PI);
			speed = random(0.5f, 3.5f);
			speedD = random(0.996f, 1.001f);
			thetaD = 0;
			thetaDD = 0;
			while (abs(thetaDD)<0.00001) {
				thetaDD = random(-0.001f, 0.001f);
			}
		}

		void move() {
			stroke(gray, 28);
			point(x, y-1);
			x += vx;
			y += vy;
			vx = speed * sin(theta);
			vy = speed * cos(theta);
			if (random(1000) > 990) {
				x = dim/2;
				y = dim/2;
				collide();
			}
			if ((x<-dim) || (x>dim*2) || (y<-dim) || (y>dim*2)) {
				x = dim/2;
				y = dim/2;
				collide();
			}
		}	
	}
	
	public class HappyParticle extends Particle {
		
		void collide() {
			x = dim/2;
			y = dim/2;
			theta = random(TWO_PI);
			speed = random(0.5f, 3.5f);
			speedD = random(0.996f, 1.001f);
			thetaD = 0;
			thetaDD = 0;
			while (abs(thetaDD)<0.00001) {
				thetaDD = random(-0.001f, 0.001f);
			}
			color = palette.getRandomHappinessColor();
		}
		
		void move() {
			stroke(red(color), green(color), blue(color), 
					30*saturationFactor);
			point(x,y-1);
			stroke(0, 25*saturationFactor);
			point(x,y+1);

			x+=vx;
			y+=vy;
			vx = speed*sin(theta);
			vy = speed*cos(theta);
			theta+=thetaD;
			thetaD+=thetaDD;
			speed*=speedD;

			if (random(1000)>997) {
				speedD = 1.0f;
				thetaDD = 0.00001f;
				if (random(100)>70) {
					x = dim/2;
					y = dim/2;
					collide();
				}
			}
			if ((x<-dim) || (x>dim*2) || (y<-dim) || (y>dim*2)) {
				collide();
			}
		}
	}
	
	public class SadParticle extends Particle {
		
		SadParticle() {
			collide();
		}
		
		void collide() {
			x = dim/2;
			y = dim/2;
			speed = random(2,32);
			speedD = random(0.0001f, 0.001f);
			theta = sadTheta + random(-0.1f, 0.1f);
			thetaD = 0;
			thetaDD = 0;

			while (abs(thetaDD) < 0.001) {
				thetaDD = random(-0.1f, 0.1f);
			}

			color = palette.getRandomSadnessColor();
		}

		void move() {
			int mya = 0;			
			stroke(red(color),green(color),blue(color), 42*saturationFactor);
			point(x,y);
			stroke(red(mya), green(mya), blue(mya), 5*saturationFactor);
			point(x, y);
			stroke(red(mya), green(mya), blue(mya), 15*saturationFactor);
			point(dim-x, y);
			x+=speed*sin(theta);
			y+=speed*cos(theta);
			theta+=thetaD;
			thetaD+=thetaDD;
			speed-=speedD;

			if ((x<-dim) || (x>dim*2) || (y<-dim) || (y>dim*2)) {
				collide();
			}
		}
	}
	
	public class AngryParticle extends Particle {

		void collide() {
			x = dim/2;
			y = dim/2;
			theta = random(TWO_PI);
			speed = random(0.5f, 3.5f);
			speedD = random(0.996f, 1.001f);
			thetaD = 0;
			thetaDD = 0;
			while (abs(thetaDD)<0.00001) {
				thetaDD = random(-0.001f, 0.001f);
			}
			color = palette.getRandomAngerColor();
		}

		void move() {
			stroke(255, 8);
			point(x, y-1);
			float f = 1.0f;
			stroke(red(color)*f, green(color)*f, blue(color)*f, 42*saturationFactor);
			point(x, y+1);
			x += vx;
			y += vy;
			vx = speed * sin(theta);
			vy = speed * cos(theta);
			theta += thetaD;
			if (random(100) > 95) {
				thetaD += thetaDD;
			}
			speed *= speedD;
			
			if (random(100) > 98) {
				speedD = 1.0f;
				if (random(100) > 50) {
					collide();
				}
			} 
			if ((x<-dim) || (x>dim*2) || (y<-dim) || (y>dim*2)) {
				collide();
			}
		}		
	}
	
	public class DisgustParticle extends Particle {
		
		void collide() {
			x = dim/2;
			y = dim/2;
			theta = random(TWO_PI);
			speed = random(1, 6);
			speedD = random(0.95f, 1);
			thetaD = 0;
			thetaDD = 0;
			color = palette.getRandomDisgustColor();

			while (abs(thetaDD) < 0.00001) {
				thetaDD = random(-0.001f, 0.001f);
			}
		}

		void move() {
			
			stroke(red(color), green(color), blue(color), 20*saturationFactor);
			point(x,y);
			stroke(random(100, 200), 7*saturationFactor);
			point(x,y-1);
			stroke(0, 25*saturationFactor);
			point(x,dim - y);

			x += vx;
			y += vy;
			vx = speed * sin(theta);
			vy = speed * cos(theta);
			theta += thetaD;
			thetaD += thetaDD;
			if (random(100) > 90) {
				speed *= speedD;
				speedD *= 0.999999;
			}			


			if (random(1000)>995) {
				speed*=-1;
				speedD=2-speedD;
				if (random(100)>30) {
					x = dim/2;
					y = dim/2;
					collide();
				}
			}
			

		}
	
		
	}
	
	public class SupriseParticle extends Particle {
		
		void collide() {
			x = dim/2;
			y = dim/2;
			theta = random(TWO_PI);
			speed = random(1.0f, 6.0f);

			speedD = random(0.95f, 1.001f);
			thetaD = 0;
			thetaDD = 0;
			color = palette.getRandomSurpriseColor();

			while (abs(thetaDD)<0.00001) {
				thetaDD = random(-0.001f, 0.001f);
			}
		}

		void move( ) {
			stroke(red(color), green(color), blue(color), 50*saturationFactor);
			point(x, y);
			stroke(0, 25*saturationFactor);
			point(x, y+1);
			for (int dy=1; dy<3; dy++) {
				stroke(red(color), green(color), blue(color), (80-dy*4)*saturationFactor);
				point(x, y-dy);
			}

			x += vx;
			y += vy;
			vx = speed*sin(theta);
			vy = speed*cos(theta);
			theta+=thetaD;

			thetaD += thetaDD;
			speed *= speedD;
			speedD *= 0.9999f;
			
			if (random(1000) > 980) {
				speed *= -1;
				speedD = 2-speedD;
				if (random(100)>30) {
					x = dim/2;
					y = dim/2;
					collide();
				}
			}
		}
	}
	
	public class FearParticle extends Particle {
		
		void collide() {
			theta = random(TWO_PI);
			speed = random(0.5f, 3.5f);
			speedD = random(0.996f, 1.001f);
			thetaD = 0;
			thetaDD = 0;
			color = palette.getRandomFearColor();

			while (abs(thetaDD)<0.00001) {
				thetaDD = random(-0.001f, 0.001f);
			}
		}

		void move() {
			stroke(red(color), green(color), blue(color), 50*saturationFactor);
			point(x, y);
			stroke(0, 30*saturationFactor);
			point(x, y-1);
			stroke(255, 20*saturationFactor);
			point(x, y+1);

			x += vx;
			y += vy;
			vx = speed * sin(theta);
			vy = speed * cos(theta);

			if (random(1000) > 950) {
				speedD = 1.0f;
				thetaDD = 0.00001f;
				if (random(100)>70) {
					collide();
				}
			}
			if ((x<-dim) || (x>dim*2) || (y<-dim) || (y>dim*2)) {
				x = dim/2;
				y = dim/2;
				collide();
			}
		}
		
	}
	
}