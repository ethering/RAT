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
*Thanks to Jenn Conn for this code
*/
package RATUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BioUtil
{
	/** reads in a fasta file to two arrays:- one for header and one for sequence
	 The filled ArrayLists will have an equal number of elements
	 @param name empty ArrayList - will be filled with names
	 @param seq empty ArrayList - will be filled with sequences
	 @return true if file was read successfully
	 */
	public static boolean readFastaFileToArray(File fastaFile, String nameIndicatorChar, ArrayList name, ArrayList seq)
	{
		boolean debug = false;
		boolean success = false;
		int seqCount = -1;
		StringBuilder sequence = new StringBuilder();

		// empty ArrayLists
		name.clear();
		seq.clear();

		// construct two arrays - one of names, one of sequences
		try
		{
			if (fastaFile.canRead())
			{
				BufferedReader bf = new BufferedReader(new FileReader(fastaFile));
				// loop through all the lines in the file
				// does NOT return end of line chars
				int lineCount = 0;
				for (String s = bf.readLine(); s != null; s = bf.readLine(), lineCount++)
				{
					if (s.startsWith(nameIndicatorChar))
					{
						if (seqCount > -1)
						{
							// write the sequence to the array
							seq.add(sequence.toString());
							sequence.delete(0, sequence.length());
						}
						seqCount++;
						// get the name
						name.add(s.substring(1, s.length()));
					}
					else if (seqCount > -1)
					{
						sequence.append(s);
					}

					if (lineCount % 6000 == 0)
					{
						//System.out.println("BioUtil.readFastaFileToArray: loaded seq: "+seqCount+" lines read: "+lineCount);
					}
				}
				// write the last sequence to the file
				seq.add(sequence.toString());
				sequence.delete(0, sequence.length());

				// if the resulting arraylists ahve the same number of elements
				if (name.size() > 0 && name.size() == seq.size())
				{
					success = true;
					// remove all spaces from seq strings
					for (int i = 0; i < seq.size(); i++)
					{
						String str = (String) seq.remove(i);
						seq.add(i, removeAllxFromString(str, " "));
					}
				}

				if (debug)
				{
					System.out.println("name.size: " + name.size() + " seq.size: " + seq.size());
					for (int i = 0; i < name.size(); i++)
					{
						System.out.println(name.get(i));
					}
				}
			}
		}

		catch (IOException e)
		{
			System.out.println("BioUtil.readFastaFileToArray: IOException caught");
			System.out.println(e.getMessage());
		}
		return success;
	}

	/** reads in a phylip file to two arrays:- one for header and one for sequence
	 The filled ArrayLists will have an equal number of elements

	 A phylip file has 6 chars, the last of which hold the number of sequences
	 then 7 chars, the last of which hold the length of each sequence
	 (so ignore top line when reading in seq).
	 Next line: the name of each seq (max 10 chars), char 11 is a space,
	 char 12 begins the seq, which is in blocks of 10 with a single space
	 between each block and 5 blocks on a line
	 the seq names are only listed once
	 all blocks of 10 are aligned vertically
	 There is any EMPTY LINE between each block off all sequences

	 @param name empty ArrayList - will be filled with names
	 @param seq empty ArrayList - will be filled with sequences
	 @return true if file was read successfully
	 */
	public static boolean readPhylipFileToArray(File phyFile, ArrayList name, ArrayList seq)
	{
		boolean debug = false;
		boolean success = false;
		int seqCount = -1;

		// empty ArrayLists
		name.clear();
		seq.clear();

		// construct two arrays - one of names, one of sequences
		try
		{
			if (phyFile.canRead())
			{
				BufferedReader bf = new BufferedReader(new FileReader(phyFile));
				// read the first line
				String s = bf.readLine(); // contains num of seq info and length of seq info
				System.out.println("First line length = " + s.length());
				int numOfSeq = -1;
				int seqLength = -1;
				try
				{ // check this is a phy file - if not, this operation will fail
					numOfSeq = (new Integer(s.substring(0, 6).trim())).intValue();
					System.out.println("numOfSeqs = " + numOfSeq);
					seqLength = (new Integer(s.substring(6, 13).trim())).intValue();
					System.out.println("seqLength = " + seqLength);
				}
				catch (Exception e)
				{
					System.err.println("BioUtil.readPhylipFileToArray: error: " + phyFile.getAbsolutePath() + " does not have a recognised .phy format");
					System.err.println(e.getMessage());
					// don't worry about this exception - success is already false,
					// the calling program will know the upload failed
				}
				if (numOfSeq > -1)
				{
					// loop through all the lines in the file
					// does NOT return end of line chars
					boolean namesComplete = false;
					for (s = bf.readLine(), seqCount = 0; s != null; s = bf.readLine(), seqCount++)
					{
						if (seqCount == numOfSeq)
						{
							seqCount = -1;
						}
						else if (!namesComplete) // get name, then sequence
						{
							// add name and seq with NO spaces anywhere
							name.add(s.substring(0, 10).trim());
							seq.add(removeAllxFromString(s.substring(10, s.length()), " "));
							if (seqCount == numOfSeq - 1)
							{
								namesComplete = true;
							}
						}
						else
						{ // update the sequence with this new line of sequence
							String sq = (String) seq.remove(seqCount);
							sq += removeAllxFromString(s.trim(), " ");
							seq.add(seqCount, sq);
						}
					}

					// if the resulting arraylists ahve the same number of elements
					if (name.size() > 0 && name.size() == seq.size())
					{
						success = true;
					}

					if (debug)
					{
						System.out.println("name.size: " + name.size() + " seq.size: " + seq.size());
						for (int i = 0; i < name.size(); i++)
						{
							System.out.println(name.get(i));
						}
					}
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("BioUtil.readPhyFileToArray: IOException caught");
			System.out.println(e.getMessage());
		}

		return success;
	}

	/** reads in a clustalw file to two arrays:- one for header and one for sequence
	 The filled ArrayLists will have an equal number of elements

	 Ignore first 3 lines which have NO relevent sequence info
	 Then the last space indicates the begining of the sequence
	 EACH line has a sequence name
	 There are 2 lines to IGNORE between each block of sequences

	 @param name empty ArrayList - will be filled with names
	 @param seq empty ArrayList - will be filled with sequences
	 @return true if file was read successfully
	 */
	public static boolean readClustalwFileToArray(File clustalFile, ArrayList name, ArrayList seq)
	{
		boolean debug = false;
		boolean success = false;
		int seqCount = -1;

		int lastSpace = -1;

		// empty ArrayLists
		name.clear();
		seq.clear();

		// construct two arrays - one of names, one of sequences
		try
		{
			if (clustalFile.canRead())
			{
				BufferedReader bf = new BufferedReader(new FileReader(clustalFile));
				int numOfSeq = -10;  // arbitrary high number so it can be set later by loop
				String s = "";
				// ignore first 3 lines
				for (int i = 0; i < 3; i++)
				{
					bf.readLine();
				}
				// loop through all the lines in the file
				// does NOT return end of line chars
				boolean namesComplete = false;
				for (s = bf.readLine(), seqCount = 0; s != null; s = bf.readLine(), seqCount++)
				{
					//System.out.println(s);
					if (seqCount == numOfSeq)
					{
						// do nothing
					}
					else if (seqCount == numOfSeq + 1)
					{
						// reset seqCount
						seqCount = -1;
					}
					else if (!namesComplete) // get name, then sequence
					{
						if (lastSpace > 0 && s.substring(0, lastSpace).trim().length() == 0)
						{
							// if there is no name. this is the *.: line
							numOfSeq = seqCount;
							namesComplete = true;
							//System.out.println("BioUtil.readClustalwFileToArray: numOfSeq="+numOfSeq);
						}
						else
						{
							// sequences always start at the same place
							if (lastSpace < 0)
							{
								lastSpace = s.lastIndexOf(" ");
							}
							// add name and seq with NO spaces anywhere
							name.add(s.substring(0, lastSpace).trim());
							seq.add(removeAllxFromString(s.substring(lastSpace), " "));
						}
					}
					else
					{ // update the sequence with this new line of sequence
						String sq = (String) seq.remove(seqCount);
						sq += removeAllxFromString(s.substring(lastSpace).trim(), " ");
						seq.add(seqCount, sq);
					}
				}

				// if the resulting arraylists ahve the same number of elements
				if (name.size() > 0 && name.size() == seq.size())
				{
					success = true;
				}

				if (debug)
				{
					System.out.println("name.size: " + name.size() + " seq.size: " + seq.size());
					for (int i = 0; i < name.size(); i++)
					{
						System.out.println(name.get(i) + "seq length=" + ((String) seq.get(i)).length());
					}
				}
			}
		}
		catch (IOException e)
		{
			System.out.println("BioUtil.readClustalFileToArray: IOException caught");
			System.out.println(e.getMessage());
		}
		return success;
	}

	/** to read in a gde file into two ArrayLists
	 - one for names, the other for sequences.  The filled ArrayLists will
	 have the same number of elements
	 @param name empty ArrayList - will be filled with names
	 @param seq empty ArrayList - will be filled with sequences
	 @return true if file was read successfully
	 */
	public static boolean readFileGdePercent(File gdeFile, ArrayList name, ArrayList seq)
	{
		return readFastaFileToArray(gdeFile, "%", name, seq);
	}

        /**
         * 
         * @param gdeFile a GDE formated file
         * @param name empty ArrayList - will be filled with names
         * @param seq empty ArrayList - will be filled with sequences
         * @return true if everything ran OK
         */
	public static boolean readFileGdeHash(File gdeFile, ArrayList name, ArrayList seq)
	{
		return readFastaFileToArray(gdeFile, "#", name, seq);
	}
        
        /**
         * 
         * @param orig the string to work on
         * @param charToBeRemoved the character to remove from string
         * @return the formated string
         */
	public static String removeAllxFromString(String orig, String charToBeRemoved)
	{
		StringBuilder str = new StringBuilder();
		String ch = "";
		for (int i = 0; i < orig.length(); i++)
		{
			ch = orig.substring(i, i + 1);
			if (ch.compareTo(charToBeRemoved) != 0)
			{
				str.append(ch);
			}
		}
		return str.toString();
	}


}
