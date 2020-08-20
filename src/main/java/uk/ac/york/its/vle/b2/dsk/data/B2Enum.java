/**
 *  Copyright (C) 2016 University of York, UK.
 *
 *  This project was initiated through a donation of source code by the
 *  University of York, UK. It contains free software; you can redistribute
 *  it and/or modify it under the terms of the GNU General Public License as
 *  published by the Free Software Foundation; either version 2 of the
 *  License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  For more information please contact:
 *
 *  Web Services Group
 *  IT Service
 *  University of York
 *  YO10 5DD
 *  United Kingdom
 */
package uk.ac.york.its.vle.b2.dsk.data;

/**
 * @author Kelvin Hai {@link <a href="mailto:kelvin.hai@york.ac.uk">kelvin.hai@york.ac.uk</a>}
 * @version $Revision$ $Date$
 */

public enum B2Enum {
	Enabled,
	Disabled,
	Integrated,
	Removed,
	Available,
	Unavailable,
	RECEIPT_SUCCESS_TYPE,
	RECEIPT_FAIL_TYPE;
	
	public String getName(){
		
		switch (this) {
		case Enabled: return "Enabled";
		case Disabled: return "Disabled";
		case Integrated: return "Integrated";
		case Removed: return "Removed";
		case Available: return "Available";
		case Unavailable: return "Unavailable";
		case RECEIPT_SUCCESS_TYPE: return "SUCCESS";
		case RECEIPT_FAIL_TYPE: return "FAIL";
		default:return name();
		}
	}
}
