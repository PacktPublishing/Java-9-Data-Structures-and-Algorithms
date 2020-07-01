package com.example.build;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.LinkedList;

/**
 * Created by debasishc on 5/4/17.
 */

public class Builder {
    public static void main(String [] args) throws Exception{
        LinkedList<String> listOfFiles = new LinkedList<>();
        File baseDirectory = new File("");
        Stack<File> dirStack = new Stack<>();
        dirStack.push(baseDirectory);
        System.out.println(baseDirectory.getCanonicalFile());
        while (!dirStack.empty()){
            File currentFile = dirStack.pop().getCanonicalFile();
            if(currentFile.isDirectory()){
                File [] files = currentFile.listFiles();
                for(File f:files){
                    dirStack.push(f);
                }
            }else{
                if(currentFile.toString().endsWith(".java")) {
                    listOfFiles.add(currentFile.getAbsolutePath());
                }
            }
        }
        listOfFiles.addFirst("src");
        listOfFiles.addFirst("--module-source-path");
        listOfFiles.addFirst("mods");
        listOfFiles.addFirst("-d");
        listOfFiles.addFirst("javac");
        runProcessAndWaitForCompletion(listOfFiles.toArray(new String[0]));
        listOfFiles.clear();
    }

    private static void runProcessAndWaitForCompletion(String ... programAndArguments)
            throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(programAndArguments);
        Process process = pb.start();
        process.waitFor();
    }
}
