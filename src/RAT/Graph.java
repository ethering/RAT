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

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Graph extends JPanel
{
	public int top;
	public int bottom;
	public int left;
	public int right;
	int titleHeight;
	int labelWidth;
	FontMetrics fm;
	int padding = 10;
	String title;
	double min;
	double max;
	ArrayList items;

	/**
	 *
	 * @param title the name of the graph, displayed at the top of the graph
	 * @param min the minimum value to be displayed
	 * @param max the maximum value to be displayed
	 * @param items a ArrayList with x/y co-ords to be drawn onto the graph
	 */
	public Graph(String title, double min, double max, ArrayList items)
	{
		this.title = title;
		this.min = min;
		this.max = max;
		this.items = items;
	}

	/**
	 * Moves and resizes this component.
	 * The new location of the top-left corner is specified by x and y,
	 * and the new size is specified by width and height.
	 * @param x the new x-coordinate of this component
	 * @param y the new y-coordinate of this component
	 * @param width the new width of this component
	 * @param height the new height of this component
	 */
        @Override
	public void setBounds(int x, int y, int width, int height)
	{
		super.setBounds(x, y, width, height);
		fm = getFontMetrics(getFont());
		titleHeight = fm.getHeight();
		labelWidth = Math.max(fm.stringWidth(new Double(min).toString()),
				fm.stringWidth(new Double(max).toString())) + 2;
		top = padding + titleHeight;
		bottom = getSize().height - padding;
		left = padding + labelWidth;
		right = getSize().width - padding;
	} // end reshape

	/**draws a generic graph (Title, x + y axis, min, max)
	 * @param g - the graphic
	 */
        @Override
	public void paintComponent(Graphics g)
	{
		// draw the title
		g.setColor(Color.white);
		g.fillRect(0, 0, getSize().width, getSize().height);
		fm = getFontMetrics(getFont());
                Font newFont = fm.getFont();
                int fontSize = newFont.getSize();
                Font nf2 = new Font (newFont.getName(), newFont.getStyle(), (fontSize*2));
		g.setColor(Color.black);
		if (title.length() > 120)
		{
			String newName = title.substring(0, 120);
			String nameWithDots = newName.concat("....");
			title = nameWithDots;
		}
		String amendedTitle = title.replace('|', '_');
		String amendedTitleTwo = amendedTitle.replace('/', '_');
		title = amendedTitleTwo;
                g.setFont(nf2);
		g.drawString(title, (getSize().width - fm.stringWidth(title)) / 3, top - 8);
		// draw the max and min values
                g.setFont(newFont);
		g.drawString(new Double(min).toString(), padding, bottom);
		g.drawString(new Double(max).toString(), padding, (bottom - (bottom - (top + titleHeight))));
		// draw the vertical and horizontal lines
		g.drawLine(left, top, left, bottom);
		g.drawLine(left, bottom, right, bottom);
	}// end paint


	/**sets the output window to the full size of the screen
	 * gets the hight and width of the screen and outputs the dimension
	 * 80% of that size.
	 * @return - the new dimension
	 */
        @Override
	public Dimension getPreferredSize()
	{
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) (dim.getHeight() * 8) / 10;
		int width = (int) (dim.getWidth() * 8) / 10;

		return (new Dimension(width, height));
	}
} // end Graph



