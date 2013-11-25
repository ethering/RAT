/*
* Copyright (C) John Innes Centre/ Institute of Food Research 2000-2004
* All rights reserved
* Written by Graham Etherington 2000-2004
* By copying and using this software you agree to be bound by the following terms.
* Failure to comply with the terms may amount to infringement of intellectual property
* rights and could result in legal action being taken against you.
* The copying and use of this software in any form, including source and binary forms
* and any derivative forms is freely permitted provided that such copying and use is
* for academic or research purposes only and is not for profit or in connection with
* the carrying on of any business and in all cases the above copyright notice and
* date of work and this paragraph are duplicated in all such forms and that neither
* this software nor software based in whole or in part on this software is sold or
* redistributed for profit or in connection with the carrying on of any business
* without the prior express written permission of the John Innes Centre.
* You are not granted any other rights and the John Innes Centre reserves all
* other rights. THIS SOFTWARE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
* IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
*/

//Creates, adds, and gets doubles in an ArrayList

package RATUtil;

import javax.swing.*;
import java.util.ArrayList;

public class DoubleArrayList extends ArrayList
{
	/**
	 *Forms, adds, and gets doubles in an ArrayList
	 */
	public DoubleArrayList()
	{
		super();
	}

	/**
	 * adds a double to a DoubleArrayList
	 * @param f  the double to add
	 */
	public void addDouble(double f)
	{
		this.add(new Double(f));
	}


	/**
	 * gets double from a DoubleArrayList
	 * @param index the index of the double in the DoubleArrayList
	 * @return  double from DoubleArrayList
	 */
	public double getDouble(int index)
	{
		Object o;
		Double f;
		double returnedDouble = 0;
		try
		{
			o = this.get(index);
			f = (Double) o;
			returnedDouble = f.doubleValue();
		}
		catch (IndexOutOfBoundsException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage() + "Check that all the sequences\nare the same length - double");
			System.exit(0);
		}
		return returnedDouble;
	}
}