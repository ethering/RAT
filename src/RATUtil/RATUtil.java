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
 
 
A group of utility methods, used through RAT
 */


package RATUtil;


//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
import javax.imageio.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;


public class RATUtil
{
    
    /**
     * creates a BufferedImage of a give Component
     * @param component the Component of which BufferedImage will be made
     * @return image the BufferedImage
     */
    public static BufferedImage createComponentImage(Component component)
    {
        BufferedImage image = (BufferedImage) component.createImage(component.getWidth(), component.getHeight());
        Graphics graphics = image.getGraphics();
        if (graphics != null)
        {
            //paintAll is the all important method (not just paint)
            component.paintAll(graphics);
        }
        return image;
    }
    
    /**
     * encodes as a .jpg a given BufferedImage
     * @param bufferedImage the Buffered Image to be encoded
     * @param file the File to write to
     */
    public static void encodeImage(BufferedImage bufferedImage, File file)
    {
        
        try
        {
            // Save as JPEG
            ImageIO.write(bufferedImage, "jpg", file);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(null, "Sorry, an error has occured : " + e);
        }
        // Create a buffered image in which to draw
        
        
    }

    
    /**
     * finds the file extension of a given file
     * @param file the File to examine
     * @return string the file extension
     */
    public static String getFileExtension(File file)
    {
        if (file != null)
        {
            //get the file name
            String name = file.getName();
            //finds where the last '.' in the file name is
            int i = name.lastIndexOf('.');
            if (i > 0 && i < name.length() - 1)
            {
                //get the name of the file extentions
                return name.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }
    
    /**
     * Rounds a double to a set number of decimal places
     * @param d - the double
     * @param places - amount of decimal places
     * @return double - the rounded double
     */
    public static double roundDouble(double d, int places)
    {
        return Math.round(d * Math.pow(10, (double) places)) / Math.pow(10,
               (double) places);
    }
    
    
    
    /**
     * identifies the type of sequence/alignment file and reads it into the two arrays
     * @param fileExtension the file extension
     * @param file the file name
     * @param name empty ArrayList - will be filled with names
     * @param seq empty ArrayList - will be filled with sequences
     */
    public static void chooseSequenceType(String fileExtension, File file, ArrayList name, ArrayList seq)
    {
        switch (fileExtension)
        {
            case "aln":
                BioUtil.readClustalwFileToArray(file, name, seq);
                break;
            case "fas":
                {
                    String nameIndicatorChar = ">";
                    BioUtil.readFastaFileToArray(file, nameIndicatorChar, name, seq);
                    break;
                }
            case "fsa":
                {
                    String nameIndicatorChar = ">";
                    BioUtil.readFastaFileToArray(file, nameIndicatorChar, name, seq);
                    break;
                }
            case "fasta":
                {
                    String nameIndicatorChar = ">";
                    BioUtil.readFastaFileToArray(file, nameIndicatorChar, name, seq);
                    
                    break;
                }
            case "fst":
                {
                    String nameIndicatorChar = ">";
                    BioUtil.readFastaFileToArray(file, nameIndicatorChar, name, seq);
                    break;
                }
            case "gde":
                if (file.canRead())
                {

                    BufferedReader bf = null;
                    try
                    {
                        bf = new BufferedReader(new FileReader(file));
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    String s = null;
                    try
                    {
                        s = bf.readLine();
                        
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                    }
                    
                    if (s.startsWith("%"))
                    {
                        BioUtil.readFileGdePercent(file, name, seq);
                    }
                    else if (s.startsWith("#"))
                    {
                        BioUtil.readFileGdeHash(file, name, seq);
                    }
                }
                break;
            case "phy":
                BioUtil.readPhylipFileToArray(file, name, seq);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Can't recognise that as an alignment file");
                System.exit(0);
                break;
        }
    }
    /**
     * Gives a description of the file extension in a JFileChooser
     */
    //thanks to Jenn Conn for this code
    public static class FileFilterExt extends FileFilter
    {
        String description;
        String[] ext;
        
        /**
         *
         * @param description  what sort of file it is
         * @param ext an array of file extensions for that file sort
         */
        
        public FileFilterExt(String description, String[] ext)
        {
            this.description = description;
            this.ext = ext;
        }
        
        public String getDescription()
        {
            return description;
        }
        
        public boolean accept(File f)
        {
            boolean accept = false;
            if (f.isDirectory())
            {
                accept = true;
            }
            else
            {
                for (int i = 0; !accept && i < ext.length; i++)
                {
                    if (f.getName().endsWith(ext[i]))
                    {
                        accept = true;
                    }
                }
            }
            return accept;
        }
    }
}
