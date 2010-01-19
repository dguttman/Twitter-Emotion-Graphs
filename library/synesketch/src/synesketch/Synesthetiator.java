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

import java.lang.reflect.Method;
import processing.core.PApplet;

/**
 * Defines common behavior for transfering textual information into visual
 * output and notifying Processing applet (PApplet) about that new information.
 * <p>
 * For example, {@link synesketch.emotion.SynesthetiatorEmotion} is a subclass of this class, where
 * textual information refers to the new emotion recognised in text, which is to
 * be transfered into visual patterns.
 * <p>
 * All subclasses of Synesthetiator should redefine the 'synesthetise' method
 * and implement the synesthesia alghoritm, the concrete way text is interpreted
 * and transfered into visual output.
 * 
 * @author Uros Krcadinac email: uros@krcadinac.com
 * @version 1.0
 */
public abstract class Synesthetiator {

	private PApplet parent;

	private Method updateMethod;

	/**
	 * Class constructor that sets parent Processing applet (PApplet). Parent
	 * applet is to be notified about some text event (for example, recognition
	 * of a current emotion in text).
	 * 
	 * @param parent
	 *            PApllet, a parent Processing applet
	 * @throws Exception
	 */
	public Synesthetiator(PApplet parent) throws Exception {
		this.parent = parent;
		updateMethod = parent.getClass().getMethod("synesketchUpdate",
				new Class[] { SynesketchState.class });
	}

	/**
	 * Notifies the parent Processing applet (PApplet) about some text event, by
	 * calling applet's method 'synesketchUpdate'.
	 * 
	 * @param state
	 *            a SynesketchState object, which contains the data
	 *            synestheticaly interpreted from the text
	 */
	public void notifyPApplet(SynesketchState state) {
		if (updateMethod != null) {
			try {
				updateMethod.invoke(parent, state);
			} catch (Exception e) {
				e.printStackTrace();
				updateMethod = null;
			}
		}
	}

	/**
	 * Defines behaviour of transferring textual information into visual
	 * information. In other words -- defines the synesthetic ablilites of the
	 * subclass.
	 * 
	 * @param text
	 *            String containing the text which is to be analyzed
	 * @throws Exception
	 */
	public abstract void synesthetise(String text) throws Exception;

}
