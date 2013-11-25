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
import RATUtil.IntegerArrayList;
import RATUtil.RATUtil;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class QuickRecomCalc
{
	File file;
	String testSequence;
	int windowSize;
	int incrementSize;
	int startPos;
	int endPos;

	/**
	 *
	 * @param file the file to open
	 * @param testSequence the sequence to use
	 * @param windowSize the size of the slidingWindow
	 * @param incrementSize   the size of the slidingWindow increment
	 * @param startPos  start position
	 * @param endPos end position
	 */
	public QuickRecomCalc(File file, String testSequence, int windowSize, int incrementSize, int startPos, int endPos)
	{
		this.file = file;
		this.testSequence = testSequence;
		this.windowSize = windowSize;
		this.incrementSize = incrementSize;
		this.startPos = startPos;
		this.endPos = endPos;
	}

	/** Reads in all the fields from DoRecomCalc and produces the ArrayLists of sequences
	 * and names. These are then compared and a distance array produce.
	 *
	 * @return  recomCalcArrayList - a vector of RecomGraphItems
	 */
	public ArrayList recomCalc()
	{
		ArrayList recomCalcArrayList = new ArrayList();
		ArrayList nameArray = new ArrayList();
		ArrayList sequenceArray = new ArrayList();
		String seqName;
		int newStart;
		int newEnd;

		try
		{
			String fileID = RATUtil.getFileExtension(file);
			RATUtil.chooseSequenceType(fileID, file, nameArray, sequenceArray);

			if (startPos > 1)
			{
				newStart = startPos;
			}
			else
			{
				newStart = 0;
			}


			int windowStart = newStart;

			int windowEnd = newStart + windowSize;
			String seqString = (String) sequenceArray.get(0);
			int seqLength = seqString.length();

			//if the user changes the "End at position" box then make newEnd

			if (endPos < seqLength)
			{
				newEnd = endPos;
			}

			else
			{
				newEnd = seqLength;
			}

			//find the index of the test sequence and assign it to testSeqIndex
			int testSeqIndex = 0;
			for (int i = 0; i < nameArray.size(); i++)
			{
				if (nameArray.get(i).equals(testSequence))
				{
					testSeqIndex = i;
				}
			}

			//for each sequence
			for (int i = 0; i < nameArray.size(); i++)
			{
				seqName = (String) nameArray.get(i);
				//don't test the test sequence against itself!
				if (!nameArray.get(i).equals(testSequence))
				{

					//whether it will be selected or not
					boolean chooseSelection;
					DoubleArrayList arrayValue = new DoubleArrayList();
					DoubleArrayList realPosition = new DoubleArrayList();

					int actualPosition; // the midway point of the window


					if (startPos > 1)
					{
						newStart = startPos;

					}
					else
					{
						newStart = 0;
					}

					windowStart = newStart;
					windowEnd = newStart + windowSize;
					seqString = (String) sequenceArray.get(0);
					seqLength = seqString.length();

					//if the user changes the "End at position" box then make newEnd
					if (endPos < seqLength)
					{
						newEnd = endPos;
					}

					else
					{
						newEnd = seqLength;
					}

					//until the end of the sequence is reached
					while (windowEnd < (newEnd + incrementSize))
					{
						//if the end of the window is beyond that of the end of the sequence
						//make the end of the sequence the end of the window.
						if (windowEnd > newEnd)
						{
							windowEnd = newEnd;
						}
						if (((String) sequenceArray.get(i)).length() < windowEnd)
						{
							windowEnd = ((String) sequenceArray.get(i)).length();
							newEnd = ((String) sequenceArray.get(i)).length();
						}
						//find out the current positon and add it to an ArrayList

						actualPosition = windowEnd;
						//System.out.println("actualPosition = " + actualPosition);
						realPosition.addDouble(actualPosition);
						//System.out.println("actualPosition = " + actualPosition);

						//make a substring of the test sequence and a substring of the comparing sequence
						String testFrag = ((String) sequenceArray.get(testSeqIndex)).substring(windowStart, windowEnd);
						String compareFrag = ((String) sequenceArray.get(i)).substring(windowStart, windowEnd);


						IntegerArrayList distanceMeasure = new IntegerArrayList();
						//for the length of each fragment, compare them and add the 0/1 result to distance Measure
						for (int j = 0; j < testFrag.length(); j++)
						{
							Character charCompareString = new Character(compareFrag.charAt(j));
							Character charTestString = new Character(testFrag.charAt(j));
							Character hyphen = new Character('-');


							//..and test to see if the pairwise char is equal
							//if it is, add a 1 to the distance matrix.....
							if (charTestString.equals(hyphen))
							{
								distanceMeasure.add(new Integer(0));
							}

							else if (charCompareString.equals(charTestString))
							{
								distanceMeasure.add(new Integer(1));
							}
							//...if it's not, add a 0
							else
							{
								distanceMeasure.add(new Integer(0));
							}
						}

						double fragDistance = 0;
						double finalDistance = 0;
						//go through the Array, add up the total...
						for (int k = 0; k < distanceMeasure.size(); k++)
						{
							int currentInt = distanceMeasure.getInteger(k);
							fragDistance += currentInt;
						}
						//...and find the average over the window
						finalDistance = fragDistance / (windowEnd - windowStart);
						//add the average to an Array
						arrayValue.addDouble(finalDistance);
						//increment the start and end of the window
						windowStart += incrementSize;
						windowEnd += incrementSize;
					}
					chooseSelection = true;
					RecomGraphItem item = new RecomGraphItem(seqName, realPosition, arrayValue, chooseSelection);

					recomCalcArrayList.add(item);
				}
			}
		}
		catch (OutOfMemoryError e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage() + "RecomCalc out of memory - see help for further details");
			System.out.println("DoRecomCalc out of memory");
		}

		return recomCalcArrayList;
	}

}
