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

This class searches for the parameters given in the main gui, and when found, reports them
to a JTextArea. The user has the options to pop up a MakeRecom.

*/


package RAT;

import RATUtil.DoubleArrayList;
import RATUtil.RATUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;


public class RecomSearch extends JFrame
{

	double minimum;
	double maximum;
	int windowSize;
	int windowIncrement;
	File file;
	int numberOfSequences;
	int startPos;
	int endPos;


	/**
	 *
	 * @param minimum the distance a sequence must be below to qualify
	 * @param maximum the distance a sequence must rise above to qualify
	 * @param file the file to examine
	 * @param windowSize the size in nucleotides of the window
	 * @param windowIncrement how far the window moves
	 * @param numberOfSequences the cut off limit
	 * @param startPos where to start looking on the sequence
	 * @param endPos where to stop looking on the sequence
	 */
	public RecomSearch(double minimum, double maximum, File file, int windowSize, int windowIncrement, int numberOfSequences, int startPos, int endPos)
	{
		this.minimum = minimum;
		this.maximum = maximum;
		this.file = file;
		this.windowSize = windowSize;
		this.windowIncrement = windowIncrement;
		this.numberOfSequences = numberOfSequences;
		this.startPos = startPos;
		this.endPos = endPos;
	}

	/**
	 *
	 * @return the report in a JTextArea
	 */
	public JFrame getResults()
	{
		System.out.println("startPos1 = " + startPos);
		ArrayList nameArray = new ArrayList();
		ArrayList sequenceArray = new ArrayList();
		//find out what type of alignment it is..
		String fileID = RATUtil.getFileExtension(file);
		//..and then read the alignment into nameArray and sequenceArray vectors
		RATUtil.chooseSequenceType(fileID, file, nameArray, sequenceArray);
		final ArrayList textAreaArrayList = new ArrayList();
		//set up the output for the search report
		JFrame frame = new JFrame("Auto Search Output");
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		JScrollPane outerScrollPane = new JScrollPane(innerPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (dim.height / 2);
		int width = (dim.width / 2);
		frame.getContentPane().setSize(height, width);

		final JTextArea topTextArea = new JTextArea();
		//the text area will be saveable, so print out the parameters used for reference
		topTextArea.setText("File: " + file + "\nParameters:" +
				"\nMin cut-off score = " + minimum + "\nMax cut-off score = " +
				maximum + "\nAmount of contributing sequences = " + numberOfSequences
				+ "\nWindow size = " + windowSize + "\nIncrement = " + windowIncrement);
		System.out.println("startPos2 = " + startPos);
		//make sure you can't edit the report
		topTextArea.setEditable(false);
		innerPanel.add(topTextArea);
		frame.getContentPane().add(outerScrollPane);
		//add the menus to save the text area
		JMenuBar menuBar = new JMenuBar();
		JMenu actionMenu = new JMenu("Save");
		menuBar.add(actionMenu);
		JMenuItem saveData = new JMenuItem("Save Data");
		actionMenu.add(saveData);
		frame.setJMenuBar(menuBar);
		frame.pack();
		frame.setVisible(true);
		textAreaArrayList.add(topTextArea);
		//saves the text area as a .txt file
		saveData.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent c)
			{
				saveJTextArea(textAreaArrayList);
			}
		});

		//for each sequence (nameArray has the name of every seq in the alignment)...
		for (int i = 0; i < nameArray.size(); i++)
		{
			//create a new text area to report on the current sequence
			JTextArea textArea = new JTextArea();
			textArea.append("\n\n\n" + (String) nameArray.get(i) + ":\n");
			textArea.setEditable(false);
			JButton button = new JButton("Click here to see");
			//...create recomCalc ArrayList which has a ArrayList of RGI's
			//(of which RGI.value is the distance array)
			QuickRecomCalc calc = new QuickRecomCalc(file, (String) nameArray.get(i), windowSize, windowIncrement, startPos, endPos);
			//returns a vector or RGI's
			ArrayList searchArrayList = calc.recomCalc();
                        System.out.println("nameArray.get(i) = " + nameArray.get(i));
			DoubleArrayList maxValues = new DoubleArrayList();
			DoubleArrayList position = new DoubleArrayList();
			ArrayList seqNames = new ArrayList();
			//get the first RGI...and see how long the distance array is
			RecomGraphItem searchItem = (RecomGraphItem) searchArrayList.get(0);
			double currentValue = 0;
			double atPosition = 0;
			double nextValue = 0;
			double thirdValue = 0;

			//iterate through each window of each RGI, e.g. window 1 of each RGI, then window
			//2 of each RGI, etc. -2 is used as we are looking 2 windows ahead all the time
			//so the end of the search is -2 windows from the last window
			for (int j = 0; j < (searchItem.doublePosition.size() - 2); j++)
			{
				ArrayList qualifyingSeqNames = new ArrayList();
				//for each RGI...
				for (int k = 0; k < searchArrayList.size(); k++)
				{
					//...cast it to a RGI object
					RecomGraphItem rgi = (RecomGraphItem) searchArrayList.get(k);

					//get the name of the current sequence being tested
					String localName = rgi.name;
					//get the value of the current, next, and next but one window
					currentValue = rgi.doubleValue.getDouble(j);
					nextValue = rgi.doubleValue.getDouble(j + 1);
					thirdValue = rgi.doubleValue.getDouble(j + 2);
					//...and find out where it is
					atPosition = rgi.doublePosition.getDouble(j);

					if (currentValue <= minimum / 100 && (nextValue >= maximum / 100 || thirdValue >= maximum / 100))
					{
						//if the 3 values above fit the criteria in the 'if' statement, add the sequence name to array
						qualifyingSeqNames.add(localName);

					}
					else if (currentValue >= maximum / 100 && (nextValue <= minimum / 100 || thirdValue <= minimum / 100))
					{
						//if the 3 values above fit the criteria in the 'if' statement, add the sequence name to array
						qualifyingSeqNames.add(localName);

					}
				}
				//add the current position to a DoubleArrayList and the ArrayList of names to another ArrayList
				position.addDouble(atPosition);
				seqNames.add(qualifyingSeqNames);  // an array of arrays

			}
			final ArrayList uniqueNames = new ArrayList();

			//Loop thru ArrayList seqNames (an array of arrays)...
			for (int q = 0; q < seqNames.size(); q++)
			{
				//..get the current array...
				ArrayList holdingArrayList = new ArrayList((ArrayList) seqNames.get(q));
				//..then loop through that array...
				for (int m = 0; m < holdingArrayList.size(); m++)
				{
					//..casting each object into a string
					String currentName = (String) holdingArrayList.get(m);
					boolean isUnique = true;
					//uniequeNames starts empty
					for (int n = 0; n < uniqueNames.size(); n++)
					{
						String testName = (String) uniqueNames.get(n);

						//test if the current sequence is already in
						if (currentName.equals(testName))
						{
							isUnique = false;
						}
					}
					//if it's not, add it to uniqueNames
					if (isUnique)
					{
						uniqueNames.add(currentName);
					}
				}
			}
			//if there are too many contributing sequences (set in the main gui) report nothing
			if (uniqueNames.size() > numberOfSequences || uniqueNames.size() == 0)
			{
				textArea.append("No recombination found \n");
				innerPanel.add(textArea);
			}
			//if there's not, add the output to the report
			else
			{
				for (int l = 0; l < position.size(); l++)
				{
					textArea.append("Window " + (l + 1) + " , at nucleotide position " + position.getDouble(l) + "\n");
					ArrayList holdSeqNames = new ArrayList((ArrayList) seqNames.get(l));
					for (int k = 0; k < holdSeqNames.size(); k++)
					{
						textArea.append("Possible Recombinant: " + holdSeqNames.get(k) + "\n");
					}
					//append the report on the current sequence to the main report area
					innerPanel.add(textArea);
					//add the button to view the Recom Graph
					innerPanel.add(button);
				}
			}

			final String sequenceName = (String) nameArray.get(i);

			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent c)
				{
					Recom.testSequence = sequenceName;
					System.out.println("startPos3 = " + startPos);
					QuickRecomCalc getCalc = new QuickRecomCalc(file, sequenceName, windowSize, windowIncrement, startPos, endPos);
					ArrayList calc = getCalc.recomCalc();

					for (int q = 0; q < calc.size(); q++)
					{
						RecomGraphItem rgi3 = (RecomGraphItem) calc.get(q);
						String bName = rgi3.name;
						boolean setSelection = false;
						int m = 0;
						while (m < uniqueNames.size() && setSelection == false)
						{
							String currentName = (String) uniqueNames.get(m);

							if (currentName.equals(bName))
							{
								setSelection = true;
							}
							rgi3.selected = setSelection;
							m++;
						}
					}
					MakeRecom mr = new MakeRecom(calc, sequenceName);
					mr.makeRecom();
				}
			});
			textAreaArrayList.add(textArea);
			currentValue = 0;
			//clear the vectors so that nothing is left in
			maxValues.clear();
			position.clear();
			seqNames.clear();
			searchArrayList.clear();
		}
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		return frame;
	}

	/**
	 * takes a vector of JTextAreas and saves them as .txt or .doc files
	 * @param vectorOfTextAreas
	 */
	public void saveJTextArea(ArrayList vectorOfTextAreas)
	{
		final JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new RATUtil.FileFilterExt("Word", new String[]{".doc"}));
		fc.addChoosableFileFilter(new RATUtil.FileFilterExt("Text", new String[]{".txt"}));

		File outFile = new File(file + ".txt");
		File dir = new File("D:/Data/RATData/ORF1");
		fc.setCurrentDirectory(dir);
		fc.setSelectedFile(outFile);
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				String filename = fc.getSelectedFile().getAbsolutePath();
				BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
				for (int i = 0; i < vectorOfTextAreas.size(); i++)
				{
					JTextArea theTextArea = (JTextArea) vectorOfTextAreas.get(i);
					String theText = theTextArea.getText();
					bw.write(theText);
				}
				bw.flush();
				bw.close();
			}
			catch (IOException e)
			{
				System.out.print(e);
			}
		}
	}
}

