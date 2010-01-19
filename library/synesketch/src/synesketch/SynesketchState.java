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

package synesketch;

/**
 * An abstract class which contains data textualy interpreted by the
 * {@link Synesthetiator}.
 * <p>
 * For example, it's subclass {@link synesketch.emotion.EmotionalState} contains
 * emotional information about the piece of text.
 * 
 * @author Uros Krcadinac email: uros@krcadinac.com
 * @version 1.0
 */
public abstract class SynesketchState {

	protected String text;

	/**
	 * Class constructor that sets text which is to be synestheticaly
	 * interpreted
	 * 
	 * @param text
	 *            {@link String} representing the text which is to be synestheticaly
	 *            interpreted
	 */

	public SynesketchState(String text) {
		this.text = text;
	}

	/**
	 * Getter for the text used as an interpretation resource
	 * 
	 * @return {@link String} representing the text
	 */

	public String getText() {
		return text;
	}

}
