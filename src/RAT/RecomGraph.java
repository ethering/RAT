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

import RATUtil.RATUtil;


import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class RecomGraph extends Graph
{
	int increment;
	int position;
	int position2;
        
        
        /**
         * 
         * @param name the name of the graph
         * @param min the minimum value on the x-axis
         * @param max the maximum value on the y-axis
         * @param items a collection of RecomGraphItem objects
         */
	
        public RecomGraph(String name, double min, double max, ArrayList items)
	{
		super(name, min, max, items);
	}

	/**
	 * @param g paints the x/y scale and the lines on the graph
	 */
        
        
        @Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//declare doubles that can be used as 1/10 measures on the y axis.
		double a = RATUtil.roundDouble(((max / 10) * 9), 1);
		double b = RATUtil.roundDouble(((max / 10) * 8), 1);
		double c = RATUtil.roundDouble(((max / 10) * 7), 1);
		double d = RATUtil.roundDouble(((max / 10) * 6), 1);
		double half = RATUtil.roundDouble((max / 2), 1);
		double e = RATUtil.roundDouble(((max / 10) * 4), 1);
		double f = RATUtil.roundDouble(((max / 10) * 3), 1);
		double h = RATUtil.roundDouble(((max / 10) * 2), 1);
		double l = RATUtil.roundDouble(((max / 10) * 1), 1);

		g.drawString(new Double(a).toString(), (padding), (bottom - ((bottom - (top + titleHeight)) / 10) * 9));
		g.drawString(new Double(b).toString(), (padding), (bottom - ((bottom - (top + titleHeight)) / 10) * 8));
		g.drawString(new Double(c).toString(), (padding), (bottom - ((bottom - (top + titleHeight)) / 10) * 7));
		g.drawString(new Double(d).toString(), (padding), (bottom - ((bottom - (top + titleHeight)) / 10) * 6));
		g.drawString(new Double(half).toString(), (padding), (bottom - ((bottom - (top + titleHeight)) / 10) * 5));
		g.drawString(new Double(e).toString(), (padding), (bottom - ((bottom - (top + titleHeight)) / 10) * 4));
		g.drawString(new Double(f).toString(), (padding), (bottom - ((bottom - (top + titleHeight)) / 10) * 3));
		g.drawString(new Double(h).toString(), (padding), (bottom - ((bottom - (top + titleHeight)) / 10) * 2));
		g.drawString(new Double(l).toString(), (padding), (bottom - ((bottom - (top + titleHeight)) / 10) * 1));

		//items is a ArrayList with all the RecomGraphItems(name, colour, x + Y cords) in it
		RecomGraphItem posItem = (RecomGraphItem) items.get(0);
		increment = (right - left) / (posItem.doublePosition.size());
		position = left - labelWidth - increment;
		position2 = left - labelWidth - increment;
		for (int z = 0; z < posItem.doublePosition.size(); z++)
		{
			//get the nt position of the sequence to display
			double doubleLength = posItem.doublePosition.getDouble(z);
			int intLength = (int) doubleLength;
			//get the position to display it
			g.drawString("|", (position2 += increment) + padding, bottom);
			g.drawString(new Integer(intLength).toString(), position += increment, bottom + padding);
		}
		for (int j = 0; j < items.size(); j++)
		{
			RecomGraphItem oneItem = (RecomGraphItem) items.get(j);
			if (oneItem.selected == true)
			{
				//System.out.println ("RecomGraphItem value = " + oneItem.doubleValue);
				g.setColor(oneItem.color);

				for (int k = 0; k < oneItem.doubleValue.size(); k++)
				{
					//get size context of canvas
					// increment works out how many x-co-ords we have and adjusts the space between
					//points accordingly
					increment = (right - left) / (oneItem.doublePosition.size());
					position = left;
					//if using doubles
					for (int i = 0; i < oneItem.doubleValue.size() - 1; i++)
					{
						//find where to padouble the lines
						//get the first(then current) y co-ord
						double thisItem = oneItem.doubleValue.getDouble(i);
						double thisAdjustedValue = bottom - (((thisItem - min) * (bottom - top)) / (max - min));
						//get the second (then current +1) y co-ord
						double nextItem = oneItem.doubleValue.getDouble(i + 1);
						double nextAdjustedValue = bottom - (((nextItem - min) * (bottom - top)) / (max - min));
						//draw a 'thick' line between them
						Graphics2D g2d = (Graphics2D) g;
						float width = 2;
						g2d.setStroke(new BasicStroke(width));
						Line2D.Double doubleLine = new Line2D.Double(position, thisAdjustedValue, position += increment, nextAdjustedValue);
						g2d.draw(doubleLine);
						g.setColor(oneItem.color);
					}
				}
			}
		}
		System.out.println("Done");
	}
}


