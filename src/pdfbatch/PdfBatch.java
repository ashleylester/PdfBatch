package pdfbatch;

public class PdfBatch
{
    public static void main(String[] args)
    {

        System.out.println("PdfBatch 0.11");
        System.out.println();
        ProcessBatch pb = new ProcessBatch();
        pb.setInput("File for processing");
        pb.setOutput("Specify output directory");
        pb.setSplitNumber("Number of Pages per File");
        pb.start();
    }
}