package com.example.compiler.Service;

import com.example.compiler.Modal.Code;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class CompilerService {

    public String compileCode(Code code) throws IOException {
        String sourceCode = code.getStringOfCode();

        String[] command = {"docker", "run", "-i", "python:3.9-alpine", "python"};

        Process process = Runtime.getRuntime().exec(command);

        process.getOutputStream().write(sourceCode.getBytes());
        process.getOutputStream().close();

        InputStream errorStream = process.getErrorStream();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        StringBuilder errorOutput = new StringBuilder();
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            errorOutput.append(errorLine).append("\n");
        }

        StringBuilder output = new StringBuilder();
        if (errorOutput.length() == 0) {
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } else {
            output.append(errorOutput);
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return output.toString();
    }

}

