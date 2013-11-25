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

public class JTextFieldInfo
{
	/**
	 * gets a double from a JTextField and checks that it is a double
	 * @param textField the JTextField from which the double taken
	 * @return the checked double
	 */
	public static double getDoubleFromJTextField(JTextField textField, String fieldName)
	{
		double returnedDouble = 0;
		try
		{
			String textFieldString = textField.getText();
			returnedDouble = Double.parseDouble(textFieldString);
		}

		catch (NumberFormatException w)
		{
			JOptionPane.showMessageDialog(null, "Sorry, the character in '" + fieldName + "' appears not to be a number");
		}
		return returnedDouble;
	}

	/**
	 * gets an int from a JTextField and checks that it is an int
	 * @param textField the JTextField from which the int taken
	 * @return the checked int
	 */
	public static int getIntFromJTextField(JTextField textField, String fieldName)
	{
		int returnedInt = 0;
		try
		{
			String textFieldString = textField.getText();
			returnedInt = Integer.parseInt(textFieldString);
		}
		catch (NumberFormatException w)
		{
			JOptionPane.showMessageDialog(null, "Sorry, the character in '" + fieldName + "' appears not to be a number");
		}
		return returnedInt;
	}

	/**
	 * checks that an int from a JTextField is within a certain range.
	 * @param field the JTextField with the int to check
	 * @param from  lower check range
	 * @param to   upper check range
	 * @param fieldName the name of the JTextField
	 * @return  boolean true if the int is in range
	 */
	public static boolean checkNumberRange(JTextField field, int from, int to, String fieldName)
	{
		boolean numberGood = false;
		boolean intGood = true;
		int number = 0;
		try
		{
			String textFieldString = field.getText();
			number = Integer.parseInt(textFieldString);
		}
		catch (NumberFormatException w)
		{
			JOptionPane.showMessageDialog(null, "Sorry, the character in '" + fieldName + "' appears not to be a number");
			intGood = false;
		}
		if (intGood)
		{

			if (number >= from && number <= to)
			{
				numberGood = true;
			}
			else
			{
				numberGood = false;
				JOptionPane.showMessageDialog(null, "Sorry, " + number + " in " + fieldName + " is out of range. \n" +
						"Use a number between " + from + " and " + to);
			}
		}
		return numberGood;
	}

	/**
	 * checks that a double from a JTextField is within a certain range.
	 * @param field the JTextField with the double to check
	 * @param from  lower check range
	 * @param to   upper check range
	 * @param fieldName the name of the JTextField
	 * @return  boolean true if the double is in range
	 */
	public static boolean checkNumberRange(JTextField field, double from, double to, String fieldName)
	{
		boolean numberGood = false;
		boolean doubleGood = true;
		double number = 0;
		try
		{
			String textFieldString = field.getText();
			number = Double.parseDouble(textFieldString);
		}
		catch (NumberFormatException w)
		{
			JOptionPane.showMessageDialog(null, "Sorry, the character in '" + fieldName + "' appears not to be a number");
			doubleGood = false;
		}
		if (doubleGood)
		{

			if (number >= from && number <= to)
			{
				numberGood = true;
			}
			else
			{
				numberGood = false;
				JOptionPane.showMessageDialog(null, "Sorry, " + number + " in " + fieldName + " is out of range. \n" +
						"Use a number between " + from + " and " + to);
			}
		}
		return numberGood;
	}
}
