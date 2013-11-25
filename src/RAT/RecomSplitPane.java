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

//RecomSplitPane itself is not a visible component. It takes graph components and puts them
//together as a JSplitPane

public class RecomSplitPane
{


	private JSplitPane splitPane;

	/**
	 *
	 * @param graphPane the graph
	 * @param listScrollPane the JScrollPane to add the graphPane, selectButton, and redrawButton to
	 * @param selectButton the list of sequences with a JCheckBox
	 * @param redrawButton
	 */
	public RecomSplitPane(RecomGraph graphPane, JScrollPane listScrollPane, JCheckBox selectButton, JButton redrawButton)
	{
		JPanel panel = new JPanel();
		JPanel controlPanel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		controlPanel.add(selectButton);
		controlPanel.add(redrawButton);
		panel.add(controlPanel);
		panel.add(listScrollPane);

		//Create a split pane with the two scroll panes in it
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel, graphPane);
		splitPane.setOpaque(true);

		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(200);
	}

	/**
	 *
	 * @return JSplitPane splitePane
	 */
	public JSplitPane getSplitPane()
	{
		return splitPane;
	}
}

