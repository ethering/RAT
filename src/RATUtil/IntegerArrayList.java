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

package RATUtil;

import javax.swing.*;
import java.util.ArrayList;

public class IntegerArrayList extends ArrayList
{
	/**
	 *Forms, adds, and gets ints in an ArrayList
	 */
	public IntegerArrayList()
	{
		super();
	}

	/**
	 * adds an int to an IntegerArrayList
	 * @param f the int to add
	 */
	public void addInteger(int f)
	{
		this.add(new Integer(f));
	}

	/**
	 * gets an int from an IntegerArrayList
	 * @param index the index of the int in the IntegerArrayList
	 * @return int the int from IntegerArrayLIst
	 */
	public int getInteger(int index)
	{
		Object o;
		Integer f;
		int returnedInt = 0;
		try
		{
			o = this.get(index);
			f = (Integer) o;
			returnedInt = f.intValue();
		}
		catch (IndexOutOfBoundsException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage() + "Check that all the sequences\nare the same length - int");
			System.exit(0);
		}
		return returnedInt;
	}
}

