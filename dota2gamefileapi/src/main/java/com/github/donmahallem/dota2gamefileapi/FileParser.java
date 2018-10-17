package com.github.donmahallem.dota2gamefileapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public abstract class FileParser {

    private final BufferedSource mInputFile;

    public FileParser(String inputFile) throws FileNotFoundException {
        this(new File(inputFile));
    }

    public FileParser(File inputFile) throws FileNotFoundException {
        this(Okio.source(inputFile));
    }

    public FileParser(Source source){
        if(source instanceof BufferedSource){
            this.mInputFile=(BufferedSource)source;
        }else{
            this.mInputFile=Okio.buffer(source);
        }
    }

    public void parse() throws IOException {
        Stack<String> currentPath = new Stack<>();
        String name=null;
        while (true) {
            String line = this.mInputFile.readUtf8Line();
            if (line == null) break;
            line=line.trim();
            if (line.matches("^\\/\\/.*")) {
                //System.out.println("is comment");
                continue;
            } else if (line.matches("^\\{[\\s]*$")) {
                //Begin Block
                //System.out.println("Begin Block");
                this.startBlock(currentPath,name);
                currentPath.add(name);
            } else if (line.matches("^\\}[\\s]*$")) {
                //End Block
                //System.out.println("End Block");
                final String closeName=currentPath.pop();
                this.endBlock(currentPath,closeName);
            } else if (line.matches("^\"[^\"]+\"[\\s]*(\\/\\/.*)?$")) {
                //End Block
                final String trimmedLine = line.trim();
                name=line.substring(1,line.indexOf("\"",2));
                //System.out.println("Block Title: " + trimmedLine.substring(1, trimmedLine.length() - 1));
            } else if (line.matches("^(\"[^\"]+\")([\\s]+)(\"[^\"]*\")([\\s]*\\/\\/.*)?")) {
                //Attribute with comment
                //System.out.println("Attribute");
                final Pattern pattern= Pattern.compile("\"[^\"]*\"");
                final Matcher matcher=pattern.matcher(line);
                matcher.find();
                final String tag=matcher.group().substring(1,matcher.group().length()-1);
                matcher.find();
                final String value=matcher.group().substring(1,matcher.group().length()-1);
                this.parseAttribute(currentPath,tag,value);
            } else if(line.trim().length()==0){
                continue;
            } else{
                System.out.println("Unknown row: " + line.trim());
                continue;
            }
        }
    }
    abstract void parseAttribute(List<String> path,String tag,String value);
    abstract void startBlock(List<String> path,String name);
    abstract void endBlock(List<String> path,String name);
}
