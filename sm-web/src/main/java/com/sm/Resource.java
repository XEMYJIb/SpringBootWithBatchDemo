package com.sm;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ademus
 *         Date: 11/30/16
 */
@RestController
public class Resource {

    @Autowired
    @Qualifier("userJob")
    private Job job;

    @RequestMapping("/home")
    String home() {
        System.out.println(job.getName());
        return "Hello World!";
    }
}
