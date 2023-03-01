package com.example.compiler.Service;

import com.example.compiler.Modal.Code;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}
