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
  This class creates the main gui and accepts input parameters, which include buttons
  that call other methods.
 */

package RAT;

import RATUtil.JTextFieldInfo;
import RATUtil.RATUtil;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.net.URL;
import java.net.MalformedURLException;


public class Recom extends JFrame
{
    /**
     * Has one method - showRecom() which returns a JFrame for the main GUI
     */
    //create all the items for the gui
    JFrame frame = new JFrame("RAT: Recombination Analysis Tool");
    File file;
    static String testSequence;
    int defaultWindowSize;
    int defaultIncrementSize;
    int seqLength = 0;
    int amountOfSequences;
    ArrayList name = new ArrayList();
    ArrayList seq = new ArrayList();
    final Box box1 = new Box(BoxLayout.X_AXIS);//File box
    final Box box1b = new Box(BoxLayout.Y_AXIS);// Test seq
    final Box box2 = new Box(BoxLayout.Y_AXIS);//Seq info
    final Box box3 = new Box(BoxLayout.X_AXIS);//Cancel/exec
    final Box box4 = new Box(BoxLayout.Y_AXIS);//search components
    final Box box5 = new Box(BoxLayout.Y_AXIS);//add all boxes to this
    final Dimension minSize = new Dimension(100, 50);
    final Dimension prefSize = new Dimension(100, 50);
    final Dimension maxSize = new Dimension(100, 50);
    final JLabel chosenFile = new JLabel("Choose Alignment File");
    final JLabel sequence = new JLabel("Test sequence name:");
    final JLabel window = new JLabel("Window size:");
    final JLabel increment = new JLabel("Increment size:");
    final JLabel startAt = new JLabel("Start at position:");
    final JLabel endAt = new JLabel("End at position:");
    final JComboBox pickedSequence = new JComboBox();
    final JTextArea fileLocationField = new JTextArea();
    final JTextField windowField = new JTextField(defaultWindowSize);
    final JTextField incrementField = new JTextField(defaultIncrementSize);
    final JTextField startField = new JTextField("1");
    final JTextField endField = new JTextField(seqLength);
    final JButton chooseFile = new JButton("...");
    final JButton cancel = new JButton("Cancel");
    final JButton execute = new JButton("Execute");
    final JLabel minValueLabel = new JLabel("Find sequences that start below the following similarity (%):");
    final JTextField minValue = new JTextField("82");
    final JLabel maxValueLabel = new JLabel("then jump to over (%):");
    final JTextField maxValue = new JTextField("92");
    final JLabel maxValueLabel2 = new JLabel("Maximum number of contributing sequences");
    final JTextField noOfSeqs = new JTextField(amountOfSequences);
    final JButton search = new JButton("Search");
    ArrayList calc = new ArrayList();
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Help");
    JMenuItem fileSelection = new JMenuItem("Help on file selection");
    JMenuItem testSequenceHelp = new JMenuItem("Test sequence");
    JMenu subMenuOne = new JMenu("Input Parameters");
    JMenuItem allInputParams = new JMenuItem("All input parameters");
    JMenuItem windowSize = new JMenuItem("Window size");
    JMenuItem incrementSize = new JMenuItem("Increment size");
    JMenuItem startAtPos = new JMenuItem("Start at position");
    JMenuItem endAtPos = new JMenuItem("End at position");
    JMenuItem autoSearchFields = new JMenuItem("Auto Search Parameters");
    JMenuItem memoryAllocation = new JMenuItem("Memory allocation");
    
    JLabel autoSearch = new JLabel("Auto Search", JLabel.CENTER);
    
    public JFrame showRecom()
    {
        /** Displays the main GUI which takes the input for parameters needed in
         * DoRecomCalc, MakeRecom, and RecomSearch
         */
        //stick them all together
        autoSearch.setText("<html><u>Auto Search</u></html>");
        box1.add(chosenFile);
        box1.add(chooseFile);
        chooseFile.setToolTipText("Click here to choose alignment file (Fasta files are best)");
        //box1.add(fileLocationField);
        box1b.add(sequence);
        box1b.add(pickedSequence);
        pickedSequence.setToolTipText("Choose sequence to examine");
        box2.add(window);
        window.setToolTipText("Choose lenght of window. Default = 0.1 of sequence length");
        box2.add(windowField);
        windowField.setToolTipText("Choose length of window. Default = 0.1 of sequence length");
        box2.add(increment);
        increment.setToolTipText("Choose window increment size. Default = 0.5 of Window size");
        box2.add(incrementField);
        incrementField.setToolTipText("Choose window increment size. Default = 0.5 of Window size");
        box2.add(startAt);
        startAt.setToolTipText("..starting at nucleotide position..");
        box2.add(startField);
        startField.setToolTipText("..starting at nucleotide position..");
        box2.add(endAt);
        box2.add(endField);
        endAt.setToolTipText("..ending at nucleotide position..");
        endField.setToolTipText("..ending at nucleotide position..");
        box3.add(cancel);
        box3.add(execute);
        box4.add(autoSearch);
        box4.add(minValueLabel);
        box4.add(minValue);
        box4.add(maxValueLabel);
        box4.add(maxValue);
        box4.add(maxValueLabel2);
        box4.add(noOfSeqs);
        box4.add(search);
        box5.add(box1);
        box5.add(fileLocationField);
        box5.add(box1b);
        box5.add(box2);
        box5.add(box3);
        box5.add(new Box.Filler(minSize, prefSize, maxSize));
        box5.add(box4);
        menuBar.add(menu);
        menu.add(fileSelection);
        menu.add(testSequenceHelp);
        menu.add(subMenuOne);
        subMenuOne.add(allInputParams);
        subMenuOne.add(windowSize);
        subMenuOne.add(incrementSize);
        subMenuOne.add(startAtPos);
        subMenuOne.add(endAtPos);
        menu.add(autoSearchFields);
        menu.add(memoryAllocation);
        
        Container contentPane = getContentPane();
        contentPane.add(box5);
        contentPane.setVisible(true);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(contentPane);
        frame.pack();
        frame.setVisible(true);
        
        
        
        
        //makes the contentPane exit the system if 'x' is pressed
        
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                System.exit(0);
            }
        });
        
        //make help files available for the user
        fileSelection.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                URL url = getClass().getResource("/ReadMeFiles/fileSelection.txt");
                System.out.println("class = " + getClass().toString());
                System.out.println("File Selection url = " + url);
                getReadmePane(url);
                
            }
        });
        testSequenceHelp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                URL url = getClass().getResource("/ReadMeFiles/testSequenceName.txt");
                getReadmePane(url);
            }
        });
        allInputParams.addActionListener(new ActionListener()
        {
            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                URL url = getClass().getResource("/ReadMeFiles/ParameterInput.txt");
                getReadmePane(url);
            }
        });
        windowSize.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                URL url = getClass().getResource("/ReadMeFiles/WindowSize.txt");
                getReadmePane(url);
            }
        });
        incrementSize.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                URL url = getClass().getResource("/ReadMeFiles/IncrementSize.txt");
                getReadmePane(url);
            }
        });
        startAtPos.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                URL url = getClass().getResource("/ReadMeFiles/StartAt.txt");
                getReadmePane(url);
            }
        });
        endAtPos.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                URL url = getClass().getResource("/ReadMeFiles/EndAt.txt");
                getReadmePane(url);
            }
        });
        autoSearchFields.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                URL url = getClass().getResource("/ReadMeFiles/AutoSearch.txt");
                getReadmePane(url);
            }
        });
        memoryAllocation.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                URL url = getClass().getResource("/ReadMeFiles/OutOfMemory.txt");
                getReadmePane(url);
            }
        });
        
        chooseFile.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //choose Clustal X/W file
                pickedSequence.removeAllItems();
                name.clear();
                seq.clear();
                final JFileChooser fc = new JFileChooser();
                File location = new File("D:/Data/RATData");
                
                fc.setCurrentDirectory(location);
                fc.addChoosableFileFilter(new RATUtil.FileFilterExt("Phylip 4", new String[]{".phy"}));
                fc.addChoosableFileFilter(new RATUtil.FileFilterExt("Fasta_GDE", new String[]{".gde"}));
                fc.addChoosableFileFilter(new RATUtil.FileFilterExt("Clustal", new String[]{".aln"}));
                fc.addChoosableFileFilter(new RATUtil.FileFilterExt("Fasta", new String[]{".fas", ".fsa", ".fasta", ".fst"}));
                
                int returnVal = fc.showOpenDialog(null);
                
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        file = fc.getSelectedFile();
                        System.out.println("File selected: " + file);
                        //add file location and name to the GUI fileLocationField
                        fileLocationField.setText("");
                        fileLocationField.append(file.getPath());
                        // get the file extention of the chosen file
                        String fileID = RATUtil.getFileExtension(file);
                        //id the file type and make a sequence array
                        RATUtil.chooseSequenceType(fileID, file, name, seq);
                        
                        //add each sequence name to the JComboBox. This will be used to select the
                        //test sequence later on
                        for (int i = 0; i < name.size(); i++)
                        {
                            pickedSequence.addItem(name.get(i));
                        }
                        //add tool tips to sequence names
                        pickedSequence.setRenderer(new MyComboBoxRenderer());
                        //find out how long the first sequence is (all sequences should be the same
                        //length). Cast the first sequence found in the array 'seq'
                        //sequence into a String and then find out how long it is
                        String seqString = (String) seq.get(0);
                        seqLength = seqString.length();
                        amountOfSequences = seq.size();
                        //create default values for the window size and increment size
                        defaultWindowSize = seqLength / 10;
                        defaultIncrementSize = defaultWindowSize / 2;
                        //make sure there aren't any values of zero or null
                        if (defaultWindowSize == 0)
                        {
                            defaultWindowSize = 1;
                        }
                        
                        if (defaultIncrementSize == 0)
                        {
                            defaultIncrementSize = 1;
                        }
                        System.out.println("Sequence length = " + seqLength);
                        
                        //get the string values of the parameters....
                        String length = String.valueOf(seqLength);
                        String deWindowSize = String.valueOf(defaultWindowSize);
                        String deIncrementSize = String.valueOf(defaultIncrementSize);
                        String amountOfSeqs = String.valueOf(amountOfSequences);
                        
                        //and add them to the relavent gui fields
                        endField.setText(length);
                        windowField.setText(deWindowSize);
                        incrementField.setText(deIncrementSize);
                        noOfSeqs.setText(amountOfSeqs);
                        
                    }
                    catch (OutOfMemoryError f)
                    {
                        JOptionPane.showMessageDialog(null, f.getMessage() + "File chooser out of memory - see help for further details");
                        System.out.println("File Chooser out of memory");
                    }
                }
            }
        });
        
        //create an Action Listener for the Cancel Button
        cancel.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent c)
            {
                System.exit(0);
            }
        });
        
        
        //listen for the name of the test sequence (sequenceName)
        pickedSequence.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent p)
            {
                JComboBox pickedSequence = (JComboBox) p.getSource();
                testSequence = (String) pickedSequence.getSelectedItem();
                System.out.println("Sequence picked: " + testSequence);
            }
        });
        
        execute.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //check that all the fields have valid numbers within the correct range
                boolean finalCheck = false;
                boolean getCheck1 = JTextFieldInfo.checkNumberRange(windowField, 1, seqLength, "Window size");
                if (getCheck1 == true)
                {
                    boolean getCheck2 = JTextFieldInfo.checkNumberRange(incrementField, 1, seqLength, "Increment size");
                    if (getCheck2 == true)
                    {
                        boolean getCheck3 = JTextFieldInfo.checkNumberRange(startField, 1, seqLength, "Start at position");
                        if (getCheck3 == true)
                        {
                            boolean getCheck4 = JTextFieldInfo.checkNumberRange(endField, 1, seqLength, "End at position");
                            if (getCheck4 == true)
                            {
                                finalCheck = true;
                            }
                        }
                    }
                }
                if (finalCheck == true)
                {
                    
                    //create a new instance of DeRecomCalc
                    QuickRecomCalc getCalc = new QuickRecomCalc(file, testSequence, JTextFieldInfo.getIntFromJTextField(windowField, "WindowSize"), JTextFieldInfo.getIntFromJTextField(incrementField, "Increment size"), JTextFieldInfo.getIntFromJTextField(startField, "Start at position"), JTextFieldInfo.getIntFromJTextField(endField, "End at position"));
                                        /*pass in the file, the testSequence (the seq to compare all others to)
                                          This returns a vector of RGI's*/
                    calc = getCalc.recomCalc();
                    MakeRecom makeNewRecom = new MakeRecom(calc, testSequence);
                    makeNewRecom.makeRecom();
                }
            }
        });
        search.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent d)
            {
                //check that all the fields have valid numbers within the correct range
                boolean finalCheck = false;
                boolean getCheck1 = JTextFieldInfo.checkNumberRange(windowField, 1, seqLength, "Window size");
                if (getCheck1 == true)
                {
                    boolean getCheck2 = JTextFieldInfo.checkNumberRange(incrementField, 1, seqLength, "Increment size");
                    if (getCheck2 == true)
                    {
                        boolean getCheck3 = JTextFieldInfo.checkNumberRange(startField, 1, seqLength, "Start at position");
                        if (getCheck3 == true)
                        {
                            boolean getCheck4 = JTextFieldInfo.checkNumberRange(endField, 1, seqLength, "End at position");
                            if (getCheck4 == true)
                            {
                                boolean getCheck5 = JTextFieldInfo.checkNumberRange(minValue, 1, JTextFieldInfo.getDoubleFromJTextField(maxValue, "..below similarity field"), "..below similarity field");
                                if (getCheck5 == true)
                                {
                                    boolean getCheck6 = JTextFieldInfo.checkNumberRange(maxValue, JTextFieldInfo.getDoubleFromJTextField(minValue, "Jump to over field"), 100, "Jump to over field");
                                    if (getCheck6 == true)
                                    {
                                        boolean getCheck7 = JTextFieldInfo.checkNumberRange(noOfSeqs, 1, seq.size(), "contributing sequences field");
                                        if (getCheck7 == true)
                                        {
                                            finalCheck = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (finalCheck == true)
                {
                    try
                    {
                        RecomSearch search = new RecomSearch(JTextFieldInfo.getDoubleFromJTextField(minValue, "Jump to over field"),
                               JTextFieldInfo.getDoubleFromJTextField(maxValue, "..below similarity field"), file,
                               JTextFieldInfo.getIntFromJTextField(windowField, "Window size"),
                               JTextFieldInfo.getIntFromJTextField(incrementField, "Increment size"),
                               JTextFieldInfo.getIntFromJTextField(noOfSeqs, "contributing sequences field"),
                               JTextFieldInfo.getIntFromJTextField(startField, "Start at position"),
                               JTextFieldInfo.getIntFromJTextField(endField, "End at position"));
                        search.getResults();
                    }
                    catch (OutOfMemoryError e)
                    {
                        JOptionPane.showMessageDialog(null, e.getMessage() + "Out of memory - see help for further details");
                        System.out.println("Recom out of memory");
                    }
                }
            }
        });
        return frame;
    }
    
    /**
     *
     * @param readMePath - takes a String that is the path for the help files
     * @return  JDialog jd - the help file
     */
    
    
    private JFrame getReadmePane(URL readMePath)
    {
        String readmeText = "Documentation File:\n";
        JFrame jframe = new JFrame("RAT: Help");
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        JScrollPane outerScrollPane = new JScrollPane(innerPanel,
               JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
               JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JTextArea jta = new JTextArea("Help", 10, 50);
        jta.setTabSize(2);
        jta.setMargin(new Insets(5, 5, 5, 5));
        jta.setEditable(false);
        jta.setCaretPosition(0);
        
        
        try
        {
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(readMePath.openStream()));
            String str;
            while ((str = in.readLine()) != null)
            {
                readmeText += in.readLine() + "\n";
                // str is one line of text; readLine() strips the newline character(s)
            }
            in.close();
        }
        catch (MalformedURLException e)
        {
            readmeText += "\nURL Exception. Error reading file\n\n";
        }
        catch (IOException e)
        {
            readmeText += "\n IO Exception. Error reading file\n\n";
        }
        
        
        jta.setText(readmeText);
        innerPanel.add(jta);
        jframe.getContentPane().add(outerScrollPane);
        jframe.pack();
        jframe.setVisible(true);
        return jframe;
    }
    
    //sets tool tips to show full name of test sequences
    
    private class MyComboBoxRenderer extends BasicComboBoxRenderer
    {
        /**
         *
         * @param list
         * @param value
         * @param index
         * @param isSelected
         * @param cellHasFocus
         * @return
         */
        @Override
        public Component getListCellRendererComponent(JList list,
               Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if (isSelected)
            {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
                if (-1 < index)
                {
                    list.setToolTipText((String) name.get(index));
                }
            }
            else
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setFont(list.getFont());
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    public static void main(String args [])
    {
        Recom recom = new Recom();
        recom.showRecom();
    }
}
