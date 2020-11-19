package com.longyi.csjl.stater;

import com.longyi.demo.domain.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StarterService {

    @Autowired
    private ExampleService exampleService;

}
