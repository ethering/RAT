
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


/*
	The following sets up the graphical output. We call RecomSplitPane which
	takes two JScrollPanes (the graph in one, and the names of the sequences
	in another), and places them together as a split pane. The sequence names
	are displayed as colored JLabels
	*/
package RAT;

import RATUtil.RATUtil;
import RATUtil.RATUtil.FileFilterExt;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;




public class MakeRecom
{
	RecomGraph graphPane;
	RecomSplitPane splitPane;
	JScrollPane listScrollPane;
	ColoredSequenceButtons buttons;
	JFrame frame;
	String sequence;
	ArrayList vectorFromDoRecom;
	JButton redraw = new JButton("Redraw");
	JCheckBox selectAll = new JCheckBox("Select All");

	/**
	 *
	 * @param vectorFromDoRecom a ArrayList of RecomGraphItems
	 * @param sequence the sequence name to compare all other sequences against
	 */
	public MakeRecom(ArrayList vectorFromDoRecom, String sequence)
	{
		this.vectorFromDoRecom = vectorFromDoRecom;
		this.sequence = sequence;
	}

	/**adds the graph, listScrollPane, buttons, and menus to JSplitPane
	 * then adds it to a Jframe
	 *
	 * @return frame the final JFrame with all components on it
	 */
	public JFrame makeRecom()
	{
		selectAll.setSelected(true);
		frame = new JFrame("RAT Sequence Viewer: " + sequence);
		graphPane = new RecomGraph("RAT output for " + sequence, 0, 1, vectorFromDoRecom);
		buttons = new ColoredSequenceButtons(vectorFromDoRecom);
		listScrollPane = new JScrollPane(buttons.getCheckedList());
		//create a JFrame, name it
		JMenuBar menuBar = new JMenuBar();
		//create the menu bar and ad the items
		JMenu actionMenu = new JMenu("Save");
		menuBar.add(actionMenu);
		JMenuItem saveGraphic = new JMenuItem("Save Graphic");
		JMenu saveData = new JMenu("Save Data");
		JMenuItem saveCSV = new JMenuItem("Save as CSV File");
		JMenuItem saveExcel = new JMenuItem("Save as Excel file");
		saveData.add(saveExcel);
		saveData.add(saveCSV);
		actionMenu.add(saveGraphic);
		actionMenu.add(saveData);
		frame.setJMenuBar(menuBar);
		//add the two JScrollPanes together using RecomSplitPane
		splitPane = new RecomSplitPane(graphPane, listScrollPane, selectAll, redraw);
		//add the splitPane to the frame

		frame.getContentPane().add(splitPane.getSplitPane());
		frame.pack();
		frame.setVisible(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);


		saveCSV.addActionListener(new ActionListener()
		{
                        @Override
			public void actionPerformed(ActionEvent c)
			{
				saveTheData(buttons.box, vectorFromDoRecom, ",", "Comma Delimited", ".csv");
			}
		});

		saveExcel.addActionListener(new ActionListener()
		{
                        @Override
			public void actionPerformed(ActionEvent c)
			{
				saveTheData(buttons.box, vectorFromDoRecom, "\t", "Excel (Tab Delim)", ".xls");
			}
		});


		saveGraphic.addActionListener(new ActionListener()
		{
                        @Override
			public void actionPerformed(ActionEvent c)
			{
				saveTheGraphic(frame);
			}
		});
		return frame;
	}

	/**
	 * displays a JFileChooser and saves the frame as a .jpg file
	 * @param frame the frame to save
	 */
	public void saveTheGraphic(JFrame frame)
	{
		//pop up a file chooser to save the graphic in
		final JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileFilterExt("Jpeg", new String[]{".jpg"}));
		//get the name of the test sequence and make it the file name .jpg
		String newFileName = formatTitle(Recom.testSequence);
		File file = new File(newFileName + ".jpg");
		File dir = new File("~/");
		fc.setCurrentDirectory(dir);
		fc.setSelectedFile(file);
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			//save the image
			String filename = fc.getSelectedFile().getAbsolutePath();
                        File thisFile = new File (filename);
			BufferedImage image = RATUtil.createComponentImage(frame);
                        
			RATUtil.encodeImage(image, thisFile);


		}
	}

	/**
	 * saves the data of any checked JCheckBoxes to a flat file (text, csv, or tab delim)
	 * @param box an array of JCheckBoxes
	 * @param vector the ArrayList containing all the RecomGraphItems
	 * @param delimiter the character that acts as a delimeter (e.g. comma, tab, etc)
	 * @param FileFilterName a string that represents the type of file associated with the delimeter
	 * @param FileExtension a string for the file extensions (e.g. .xls, .txt, .csv, etc)
	 */
	public void saveTheData(JCheckBox box[], ArrayList vector, String delimiter, String FileFilterName, String FileExtension)
	{
		//pop up a file chooser to save the file
		final JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new RATUtil.FileFilterExt(FileFilterName, new String[]{FileExtension}));
		//get the name of the test sequence and make it the file name
		String dataFileName = formatTitle(Recom.testSequence);
		File outFile = new File(dataFileName + FileExtension);
		File dir = new File("D:/Data/RATData");
		fc.setCurrentDirectory(dir);
		fc.setSelectedFile(outFile);
		int returnVal = fc.showSaveDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				String filename = fc.getSelectedFile().getAbsolutePath();
				BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
				//uses a tab for exel files and a comma for csv files
				bw.write("Position " + delimiter);
				ArrayList items = new ArrayList();
				//finds out which boxes are selected and
				for (int i = 0; i < box.length; i++)
				{
					if (box[i].isSelected())
					{
						//if the box is checked, get the RGI for that seq and add to vector
						RecomGraphItem oneItem = (RecomGraphItem) vector.get(i);
						items.add(oneItem);
						//reformat the name and print the name to file
						String string = oneItem.name;
						String newString = string.replace(',', '+');
						bw.write(newString + delimiter);
					}
				}
				bw.write("\n");
				//get the first RGI from vector items
				RecomGraphItem outputItem = (RecomGraphItem) items.get(0);
				//find out how long the RGI DoubleArrayList is
				int arrayLength = outputItem.doublePosition.size();
				//for each 'position' value (nt position)
				for (int j = 0; j < arrayLength; j++)
				{
					RecomGraphItem justX = (RecomGraphItem) items.get(0);
					double xValue = justX.doublePosition.getDouble(j);
					String stringValueOfX = Double.toString(xValue);
					//write the position
					bw.write(stringValueOfX);
					bw.write(delimiter);
					//followed by the distance at that location for each sequence
					for (int k = 0; k < items.size(); k++)
					{
						RecomGraphItem getValues = (RecomGraphItem) items.get(k);
						double yValue = getValues.doubleValue.getDouble(j);
						String stringValueOfY = Double.toString(yValue);
						bw.write(stringValueOfY);
						bw.write(delimiter);
					}
					bw.write("\n");
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
	//
	/**
	 * Used when naming files.
	 * formatString makes the title less than 35 chars long, and  takes out the
	 * characters '|', '.', and '/' and replaces them with '_'
	 *
	 * @param string the unformatted string
	 * @return newString the formated string
	 */
	public String formatTitle(String string)
	{
		if (string.length() > 35)
		{
			String shortString = string.substring(0, 35);
			string = shortString;
		}
                String newString = string.replaceAll("[|./]", "_");
		return newString;
	}

	/**
	 * inner class, produces the left hand pane of the Recom Graph
	 */
	public class ColoredSequenceButtons extends JPanel
	{
		JCheckBox box [];
		JPanel buttonPanel;
		ArrayList items;

		public ColoredSequenceButtons(ArrayList items)
		{
			this.items = items;
		}

		/**
		 *
		 * @return buttonPanel a JPanel with a JCheckBox for each sequence
		 */
		public JPanel getCheckedList()
		{

			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridLayout(0, 1));
			//create an array of checkboxes the size of vector items (with all the RGI's in)
			box = new JCheckBox[items.size()];
			//for each box
			for (int i = 0; i < box.length; i++)
			{
				//get the corresponding RGI
				RecomGraphItem newItem = (RecomGraphItem) items.get(i);
				//get its name and colour
				String seqName = newItem.name;
				Color col = newItem.color;
				//create the box, its name and whether it's selected
				box[i] = new JCheckBox(seqName, newItem.selected);
				//add the sequence name as a tool tip - this allows the user to see the name
				//without having to scroll the scrollpane
				box[i].setToolTipText(seqName);
				//set the colour of the name to that of the corresponding graph line colour
				box[i].setForeground(col);
				//add the checkbox to the JPanel
				buttonPanel.add(box[i]);
				selectAll.addActionListener(new ActionListener()
				{
                                        @Override
					public void actionPerformed(ActionEvent evt)
					{
						// Determine status
						boolean isSel = selectAll.isSelected();
						//if 'select all' box is checked
						if (isSel)
						{
							//select all the boxes
							for (int i = 0; i < box.length; i++)
							{
								box[i].setSelected(true);
							}
						}
						else
						{
							//if it's not, unselect all the boxes
							for (int i = 0; i < box.length; i++)
							{
								box[i].setSelected(false);
							}
						}
					}
				});
			}
			//redraws the RecomGraph with the newly selected sequences
			redraw.addActionListener(new ActionListener()
			{
                                @Override
				public void actionPerformed(ActionEvent e)
				{
					update();
				}
			});
			return buttonPanel;
		}

		/**
		 * checks to see which checkboxes are checked and returns a new graph accordingly
		 */
		public void update()
		{
			Graphics g = this.getGraphics();
			super.update(g);
			//make a new ArrayList of checked sequences
			ArrayList newRecom = new ArrayList();

			//check at least one box is selected....
			int counter = 0;
			for (int x = 0; x < box.length; x++)
			{
				if (box[x].isSelected())
				{
					counter++;
				}
			}
			//...if there is, then proceed...
			if (counter > 0)
			{
				for (int i = 0; i < box.length; i++)
				{
					if (box[i].isSelected())
					{
						RecomGraphItem oneItem = (RecomGraphItem) items.get(i);
						oneItem.selected = true;
						newRecom.add(oneItem);
					}
				}

				//produce a new Recom Graph with the new vector
				RecomGraph updatedGraph = new RecomGraph("RAT output for " + Recom.testSequence, 0, 1, newRecom);
				//remove the old graph from the split pane
				splitPane.getSplitPane().remove(graphPane);
				//replace the old one with the new one
				graphPane = updatedGraph;
				splitPane.getSplitPane().add(graphPane);
				splitPane.getSplitPane().setDividerLocation(200);
			}

			//...if there's not, tell the user they're being stupid!
			else
			{
				JOptionPane.showMessageDialog(null, "Select at least one sequence");
			}
		}
	}
}