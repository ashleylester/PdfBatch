
package pdfbatch;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessBatch
{
    
    private String input;
    private String output;
    private String splitNumber;
    private boolean programRunning;
    private String inputFilePath;
    PdfReader reader;
    PdfWriter writer;
    private int numberOfPages;
    Scanner sc;
    
    public ProcessBatch()
    {
        sc = new Scanner(System.in);
    }
    
    public void setInput(String input)
    {
        this.input = input;
    }
    
    public void setOutput(String output)
    {
        this.output = output;
    }
    
    public void setSplitNumber(String splitNumber)
    {
        this.splitNumber = splitNumber;
    }
    
    public void isRunning(boolean programRunning)
    {
        this.programRunning = programRunning;
    }
    
    public String getInput()
    {
        return input;
    }
    
    public String getOutput()
    {
        return output;
    }
    
    public String getSplitNumber()
    {
        return splitNumber;
    }
    
    public void start()
    {   
        programRunning = true;
        while(programRunning)
        {
            System.out.println();
            System.out.println("1      " + getInput());
            System.out.println("2      " + getOutput());
            System.out.println("3      " + getSplitNumber());
            System.out.println("4      " + "Runs batch");
            System.out.println("0      " + "Quit program");
            System.out.println();
            System.out.print("> ");
            String choice = sc.next();
            
            if(isNumber(choice))
            {
                if(Integer.parseInt(choice) == 1)
                {
                    setInputFilePath();
                }
                else if(Integer.parseInt(choice) == 2)
                {
                    setOutputDirPath();
                }
                else if(Integer.parseInt(choice) == 3)
                {
                    setSplitNumber();
                }
                else if(Integer.parseInt(choice) == 0)
                {
                    closeStreams();
                    isRunning(false);
                }
                else if(Integer.parseInt(choice) == 4)
                {
                    readFile();
                    process();
                }
                else
                {
                    System.out.println("Command not found.");
                    start();
                }
            }
            else
            {
                System.out.println("Not a number!");
                start();
            }
        }
    }
    
    public void setInputFilePath()
    {
        System.out.print("Enter filepath: ");
        String filepath = sc.next();
        File file = new File(filepath);
        if(!file.isFile())
        {
            System.out.println("File not found.");
            start();
        }
        setInput(file.getName());
        inputFilePath = filepath;
    }
    
    public void setSplitNumber()
    {
        System.out.print("Set number of pages per file: ");
        String pages = sc.next();
        setSplitNumber(pages);
    }
    
    public void setOutputDirPath()
    {
        System.out.print("Specify directory path: ");
        String directory = sc.next();
        File dir = new File(directory);
        if(!dir.isDirectory())
        {
            System.out.println("Directory not found.");
            start();
        }
        setOutput(directory);
    }
    
    public int getNumberOfPages()
    {
        return numberOfPages;
    }
    
    public void readFile()
    {
        try 
        {
            reader = new PdfReader(inputFilePath);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(ProcessBatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        numberOfPages = reader.getNumberOfPages();
    }
    
    public void process()
    {
        System.out.println("---");
        System.out.println("Using " + inputFilePath);
        System.out.println("Generating output in " + output);
        System.out.println("---");
        for(int i = 1; i <= numberOfPages; i += Integer.parseInt(getSplitNumber()))
        {
            Document document = new Document();
            File file = new File(output + i + ".pdf");
            
            System.out.println("Opened new document at " + file.getPath());
            
            try 
            {
                writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(ProcessBatch.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            document.open();
        
            for(int j = 0; j < Integer.parseInt(getSplitNumber()); j++)
            {
                if(j + i <= numberOfPages) 
                {
                    PdfImportedPage page;
                
                    page = writer.getImportedPage(reader, (i + j));
                    
                    System.out.println("Creating page " + (i + j));
                    try 
                    {
                        document.add(Image.getInstance(page));
                        System.out.println("Image written to page");
                    } 
                    catch (DocumentException ex) 
                    {
                        Logger.getLogger(ProcessBatch.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            System.out.println("Closing document...");
            document.close();
        }
    }
    
    public void closeStreams()
    {
        System.out.println("Closing streams and ending program");
        writer.close();
        reader.close();
    }
    
    public boolean isNumber(String str)
    {
        for(char c : str.toCharArray())
        {
            if(!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }
}