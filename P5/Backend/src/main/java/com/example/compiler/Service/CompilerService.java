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
        // Hent kildekoden fra Code-objektet
        String sourceCode = code.getStringOfCode();

        // Opprett Docker-kommando for å kjøre Python 3.9-alpine i en beholder
        String[] command = {"docker", "run", "-i", "python:3.9-alpine", "python"};

        // Kjør Docker-prosessen
        Process process = Runtime.getRuntime().exec(command);

        // Skriv kildekoden til standardinngangen til Docker-prosessen
        process.getOutputStream().write(sourceCode.getBytes());
        process.getOutputStream().close();

        // Les eventuelle feilmeldinger fra feilstrømmen til Docker-prosessen
        InputStream errorStream = process.getErrorStream();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        StringBuilder errorOutput = new StringBuilder();
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            errorOutput.append(errorLine).append("\n");
        }

        // Les standardutgangen til Docker-prosessen hvis det ikke er noen feil
        StringBuilder output = new StringBuilder();
        if (errorOutput.length() == 0) {
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } else {
            // Hvis det er feil, legg til feilmeldingen til output
            output.append(errorOutput);
        }

        // Vent på at Docker-prosessen fullfører
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Returner output som en streng
        return output.toString();
    }


}
