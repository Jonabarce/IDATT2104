package com.example.compiler.Controller;


import com.example.compiler.Modal.Code;
import com.example.compiler.Service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin( origins = "http://localhost:3000/")
public class Controller {

    @Autowired
    private CompilerService service;

    @GetMapping("/Test")
    public String testConnection(){
        return "Sucess!!!!";
    }

   @PostMapping("/Compile")
    public String compileCode(@RequestBody Code code) throws IOException {
       return service.compileCode(code);
    }
}
