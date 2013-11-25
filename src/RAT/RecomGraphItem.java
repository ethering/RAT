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

package RAT;

import RATUtil.DoubleArrayList;

import java.awt.*;
import java.util.ArrayList;


public class RecomGraphItem
{
	String name;
	Color color;
	DoubleArrayList doublePosition;
	DoubleArrayList doubleValue;
	ArrayList items;
	boolean selected;

        /**
         * 
         * @param name the name of the feature (e.g. the accession number and description)
         * @param color the colour to paint it
         * @param doubleX the x-axis values
         * @param doubleValue the y-axis values (distance)
         * @param selected should this sequence appears as selected once rendered
         */
	public RecomGraphItem(String name, Color color, DoubleArrayList doubleX, DoubleArrayList doubleValue, boolean selected)
	{
		this.name = name;
		this.color = color;
		this.doublePosition = doubleX;
		this.doubleValue = doubleValue;
		this.selected = selected;
	}
        /**
         * 
         * @param name the name of the feature (e.g. the accession number and description)
         * @param doubleX the x-axis values
         * @param doubleValue the y-axis values (distance)
         * @param selected should this sequence appears as selected once rendered
         */

	public RecomGraphItem(String name, DoubleArrayList doubleX, DoubleArrayList doubleValue, boolean selected)
	{
		this(name, getRandomColour(), doubleX, doubleValue, selected);
	}

	//get a random colour
	/**
	 *
	 * @return a random colour
	 */
	public static Color getRandomColour()
	{
		Color c;

		int red = (int) (Math.random() * 255.);
		int green = (int) (Math.random() * 255.);
		int blue = (int) (Math.random() * 255.);

		c = new Color(red, green, blue);
		if ((red+green+blue == 765))
		{
			c = new Color (000,000,000);
		}
		return c;
	}

	/**
	 *
	 * @param items ArrayList containing RecomGraphItems
	 * @param index the index of the ArrayList
	 * @return col the color at that index
	 */
}