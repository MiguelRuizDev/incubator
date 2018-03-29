package org.activiti.incubator.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;



@SpringBootApplication
public class TaskServiceApplication {

    //we create a logger to check things out in the terminal
    private static final Logger log = LoggerFactory.getLogger(TaskServiceApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(TaskServiceApplication.class, args);

        log.info("Everything OK"); //using the log for the first time in the app...

        //DATE TESTS
        Date dateTest = new Date();

        String datestring = new SimpleDateFormat("yyyy-MM-dd hh:mm a").format(dateTest);

        log.info(datestring);


        String formatDueDate = "2022-06-03 13:45 PM";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        try {
            Date duedate = formatter.parse(formatDueDate);
            log.info(duedate.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }




    }
}


