package com.cor.cep.util;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cor.cep.event.UserSimple;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cor.cep.event.TemperatureEvent;

import com.cor.cep.handler.TemperatureEventHandler;

import javax.jws.soap.SOAPBinding;

/**
 * Just a simple class to create a number of Random TemperatureEvents and pass them off to the
 * TemperatureEventHandler.
 */
@Component
public class GenerateLogEvent {

    /** Logger */
    private static Logger LOG = LoggerFactory.getLogger(RandomTemperatureEventGenerator.class);

    /** The TemperatureEventHandler - wraps the Esper engine and processes the Events  */
    @Autowired
    private TemperatureEventHandler temperatureEventHandler;

    private UserSimplePropertiesExtractor SimplePropertiesExtractor = new UserSimplePropertiesExtractor();

    /**
     * Creates simple random Temperature events and lets the implementation class handle them.
     */
    public void startSendingTemperatureReadings(final long noOfTemperatureEvents) {
        //

        String userJson = "{\n" +
                "\t\"__CURSOR\" : \"s=bc9a121f811e48e5afb5b2d2f5357982;i=2;b=73a9cb791be24dec8d3db36b81c9ca52;m=a117fcd6;t=580e8f5c58c9d;x=57e5ab4b16f71792\",\n" +
                "\t\"__REALTIME_TIMESTAMP\" : \"1549112927685789\",\n" +
                "\t\"__MONOTONIC_TIMESTAMP\" : \"2702703830\",\n" +
                "\t\"_BOOT_ID\" : \"73a9cb791be24dec8d3db36b81c9ca52\",\n" +
                "\t\"PRIORITY\" : \"6\",\n" +
                "\t\"_TRANSPORT\" : \"driver\",\n" +
                "\t\"_PID\" : \"5776\",\n" +
                "\t\"_UID\" : \"0\",\n" +
                "\t\"_GID\" : \"0\",\n" +
                "\t\"_COMM\" : \"systemd-journal\",\n" +
                "\t\"_EXE\" : \"/lib/systemd/systemd-journald\",\n" +
                "\t\"_CMDLINE\" : \"/lib/systemd/systemd-journald\",\n" +
                "\t\"_SYSTEMD_CGROUP\" : \"/system/systemd-journald.service\",\n" +
                "\t\"_SYSTEMD_UNIT\" : \"systemd-journald.service\",\n" +
                "\t\"_SELINUX_CONTEXT\" : [ 117, 110, 99, 111, 110, 102, 105, 110, 101, 100, 10 ],\n" +
                "\t\"_MACHINE_ID\" : \"db74b248b488248836da1cc256c2cfa1\",\n" +
                "\t\"_HOSTNAME\" : \"devlh\",\n" +
                "\t\"MESSAGE\" : \"Missed 5400 kernel messages\",\n" +
                "\t\"MESSAGE_ID\" : \"e9bf28e6e834481bb6f48f548ad13606\"\n" +
                "}\n" +
                "{\n" +
                "\t\"__CURSOR\" : \"s=bc9a121f811e48e5afb5b2d2f5357982;i=3;b=73a9cb791be24dec8d3db36b81c9ca52;m=a117fce7;t=580e8f5c58cad;x=b42c5100ebd0b853\",\n" +
                "\t\"__REALTIME_TIMESTAMP\" : \"1549112927685805\",\n" +
                "\t\"__MONOTONIC_TIMESTAMP\" : \"2702703847\",\n" +
                "\t\"_BOOT_ID\" : \"73a9cb791be24dec8d3db36b81c9ca52\",\n" +
                "\t\"_MACHINE_ID\" : \"db74b248b488248836da1cc256c2cfa1\",\n" +
                "\t\"_HOSTNAME\" : \"devlh\",\n" +
                "\t\"_SOURCE_MONOTONIC_TIMESTAMP\" : \"2669940166\",\n" +
                "\t\"_TRANSPORT\" : \"kernel\",\n" +
                "\t\"PRIORITY\" : \"4\",\n" +
                "\t\"SYSLOG_IDENTIFIER\" : \"kernel\",\n" +
                "\t\"MESSAGE\" : \"[NetFilter-I]IN=eth0 OUT= MAC=08:00:27:32:5a:6a:00:08:e3:ff:fc:28:08:00 SRC=151.101.1.69 DST=10.64.119.57 LEN=2824 TOS=0x00 PREC=0x00 TTL=58 ID=35836 DF PROTO=TCP SPT=443 DPT=47612 WINDOW=59 RES=0x00 ACK PSH URGP=0 \"\n" +
                "}\n" +
                "{\n" +
                "\t\"__CURSOR\" : \"s=bc9a121f811e48e5afb5b2d2f5357982;i=4;b=73a9cb791be24dec8d3db36b81c9ca52;m=a117fcf3;t=580e8f5c58cb9;x=a00008777371493b\",\n" +
                "\t\"__REALTIME_TIMESTAMP\" : \"1549112927685817\",\n" +
                "\t\"__MONOTONIC_TIMESTAMP\" : \"2702703859\",\n" +
                "\t\"_BOOT_ID\" : \"73a9cb791be24dec8d3db36b81c9ca52\",\n" +
                "\t\"_MACHINE_ID\" : \"db74b248b488248836da1cc256c2cfa1\",\n" +
                "\t\"_HOSTNAME\" : \"devlh\",\n" +
                "\t\"_TRANSPORT\" : \"kernel\",\n" +
                "\t\"PRIORITY\" : \"4\",\n" +
                "\t\"SYSLOG_IDENTIFIER\" : \"kernel\",\n" +
                "\t\"_SOURCE_MONOTONIC_TIMESTAMP\" : \"2669940174\",\n" +
                "\t\"MESSAGE\" : \"[NetFilter-O]IN= OUT=eth0 SRC=10.64.119.57 DST=151.101.1.69 LEN=52 TOS=0x00 PREC=0x00 TTL=64 ID=48267 DF PROTO=TCP SPT=47612 DPT=443 WINDOW=295 RES=0x00 ACK URGP=0 \"\n" +
                "}\n" +
                "{\n" +
                "\t\"__CURSOR\" : \"s=bc9a121f811e48e5afb5b2d2f5357982;i=5;b=73a9cb791be24dec8d3db36b81c9ca52;m=a117fcfc;t=580e8f5c58cc3;x=ecc48879428b14d0\",\n" +
                "\t\"__REALTIME_TIMESTAMP\" : \"1549112927685827\",\n" +
                "\t\"__MONOTONIC_TIMESTAMP\" : \"2702703868\",\n" +
                "\t\"_BOOT_ID\" : \"73a9cb791be24dec8d3db36b81c9ca52\",\n" +
                "\t\"_MACHINE_ID\" : \"db74b248b488248836da1cc256c2cfa1\",\n" +
                "\t\"_HOSTNAME\" : \"devlh\",\n" +
                "\t\"_TRANSPORT\" : \"kernel\",\n" +
                "\t\"PRIORITY\" : \"4\",\n" +
                "\t\"SYSLOG_IDENTIFIER\" : \"kernel\",\n" +
                "\t\"_SOURCE_MONOTONIC_TIMESTAMP\" : \"2669940979\",\n" +
                "\t\"MESSAGE\" : \"[NetFilter-O]IN= OUT=eth0 SRC=10.64.119.57 DST=151.101.1.69 LEN=145 TOS=0x00 PREC=0x00 TTL=64 ID=48268 DF PROTO=TCP SPT=47612 DPT=443 WINDOW=295 RES=0x00 ACK PSH URGP=0 \"\n" +
                "}\n" +
                "{\n" +
                "\t\"__CURSOR\" : \"s=bc9a121f811e48e5afb5b2d2f5357982;i=6;b=73a9cb791be24dec8d3db36b81c9ca52;m=a117fd0e;t=580e8f5c58cd4;x=37f32daff5ba419a\",\n" +
                "\t\"__REALTIME_TIMESTAMP\" : \"1549112927685844\",\n" +
                "\t\"__MONOTONIC_TIMESTAMP\" : \"2702703886\",\n" +
                "\t\"_BOOT_ID\" : \"73a9cb791be24dec8d3db36b81c9ca52\",\n" +
                "\t\"_MACHINE_ID\" : \"db74b248b488248836da1cc256c2cfa1\",\n" +
                "\t\"_HOSTNAME\" : \"devlh\",\n" +
                "\t\"_TRANSPORT\" : \"kernel\",\n" +
                "\t\"PRIORITY\" : \"4\",\n" +
                "\t\"SYSLOG_IDENTIFIER\" : \"kernel\",\n" +
                "\t\"_SOURCE_MONOTONIC_TIMESTAMP\" : \"2669942479\",\n" +
                "\t\"MESSAGE\" : \"[NetFilter-I]IN=eth0 OUT= MAC=08:00:27:32:5a:6a:00:08:e3:ff:fc:28:08:00 SRC=151.101.1.69 DST=10.64.119.57 LEN=310 TOS=0x00 PREC=0x00 TTL=58 ID=35838 DF PROTO=TCP SPT=443 DPT=47612 WINDOW=59 RES=0x00 ACK PSH URGP=0 \"\n" +
                "}\n" +
                "{\n" +
                "\t\"__CURSOR\" : \"s=bc9a121f811e48e5afb5b2d2f5357982;i=7;b=73a9cb791be24dec8d3db36b81c9ca52;m=a117fd19;t=580e8f5c58ce0;x=dd489174682d637c\",\n" +
                "\t\"__REALTIME_TIMESTAMP\" : \"1549112927685856\",\n" +
                "\t\"__MONOTONIC_TIMESTAMP\" : \"2702703897\",\n" +
                "\t\"_BOOT_ID\" : \"73a9cb791be24dec8d3db36b81c9ca52\",\n" +
                "\t\"_MACHINE_ID\" : \"db74b248b488248836da1cc256c2cfa1\",\n" +
                "\t\"_HOSTNAME\" : \"devlh\",\n" +
                "\t\"_TRANSPORT\" : \"kernel\",\n" +
                "\t\"PRIORITY\" : \"4\",\n" +
                "\t\"SYSLOG_IDENTIFIER\" : \"kernel\",\n" +
                "\t\"_SOURCE_MONOTONIC_TIMESTAMP\" : \"2669942720\",\n" +
                "\t\"MESSAGE\" : \"[NetFilter-O]IN= OUT=eth0 SRC=10.64.119.57 DST=151.101.1.69 LEN=246 TOS=0x00 PREC=0x00 TTL=64 ID=48269 DF PROTO=TCP SPT=47612 DPT=443 WINDOW=317 RES=0x00 ACK PSH URGP=0 \"\n" +
                "}\n" +
                "{\n" +
                "\t\"__CURSOR\" : \"s=bc9a121f811e48e5afb5b2d2f5357982;i=8;b=73a9cb791be24dec8d3db36b81c9ca52;m=a117fd23;t=580e8f5c58cea;x=7a5e50fe197019e3\",\n" +
                "\t\"__REALTIME_TIMESTAMP\" : \"1549112927685866\",\n" +
                "\t\"__MONOTONIC_TIMESTAMP\" : \"2702703907\",\n" +
                "\t\"_BOOT_ID\" : \"73a9cb791be24dec8d3db36b81c9ca52\",\n" +
                "\t\"_MACHINE_ID\" : \"db74b248b488248836da1cc256c2cfa1\",\n" +
                "\t\"_HOSTNAME\" : \"devlh\",\n" +
                "\t\"_TRANSPORT\" : \"kernel\",\n" +
                "\t\"PRIORITY\" : \"4\",\n" +
                "\t\"SYSLOG_IDENTIFIER\" : \"kernel\",\n" +
                "\t\"_SOURCE_MONOTONIC_TIMESTAMP\" : \"2669943433\",\n" +
                "\t\"MESSAGE\" : \"[NetFilter-O]IN= OUT=eth0 SRC=10.64.119.57 DST=151.101.1.69 LEN=83 TOS=0x00 PREC=0x00 TTL=64 ID=48270 DF PROTO=TCP SPT=47612 DPT=443 WINDOW=317 RES=0x00 ACK PSH URGP=0 \"\n" +
                "}\n" +
                "{\n" +
                "\t\"__CURSOR\" : \"s=bc9a121f811e48e5afb5b2d2f5357982;i=9;b=73a9cb791be24dec8d3db36b81c9ca52;m=a117fd2d;t=580e8f5c58cf4;x=dcc07a9faf8d372a\",\n" +
                "\t\"__REALTIME_TIMESTAMP\" : \"1549112927685876\",\n" +
                "\t\"__MONOTONIC_TIMESTAMP\" : \"2702703917\",\n" +
                "\t\"_BOOT_ID\" : \"73a9cb791be24dec8d3db36b81c9ca52\",\n" +
                "\t\"_MACHINE_ID\" : \"db74b248b488248836da1cc256c2cfa1\",\n" +
                "\t\"_HOSTNAME\" : \"devlh\",\n" +
                "\t\"_TRANSPORT\" : \"kernel\",\n" +
                "\t\"PRIORITY\" : \"4\",\n" +
                "\t\"SYSLOG_IDENTIFIER\" : \"kernel\",\n" +
                "\t\"_SOURCE_MONOTONIC_TIMESTAMP\" : \"2669943482\",\n" +
                "\t\"MESSAGE\" : \"[NetFilter-O]IN= OUT=eth0 SRC=10.64.119.57 DST=151.101.1.69 LEN=52 TOS=0x00 PREC=0x00 TTL=64 ID=48271 DF PROTO=TCP SPT=47612 DPT=443 WINDOW=317 RES=0x00 ACK FIN URGP=0 \"\n" +
                "}";


        //Gson gson = new Gson();

        //UserSimple[] enums = gson.fromJson(userJson, UserSimple[].class);

        //List<UserSimple> postsList = Arrays.asList(gson.fromJson(userJson,
        // UserSimple[].class));


        List<UserSimple> userObjects = new ArrayList<UserSimple>();

        Gson gson = new GsonBuilder().create();
        JsonStreamParser p = new JsonStreamParser(userJson);

        while (p.hasNext()) {
            JsonElement e = p.next();
            if (e.isJsonObject()) {
                //Map m = gson.fromJson(e, Map.class);
                UserSimple userObject = gson.fromJson(e, UserSimple.class);
                ////Test SimplePropertiesExtractor

                SimplePropertiesExtractor.PropertiesSetter(userObject.getMESSAGE(), userObject);

                System.out.println(userObject.getMESSAGE());
                System.out.println("And the Src Ip is "+ userObject.getSrcIp());
                System.out.println("And the Dsc Ip is "+ userObject.getDesIp());
                System.out.println("And the Source Port is "+ userObject.getScrPt());
                System.out.println("And the Destination Port is "+ userObject.getDstPt());
                System.out.println("And the Protocol is "+ userObject.getProto());
                System.out.println("And the Flag is "+ userObject.getFlag());
                userObjects.add(userObject);

                /* do something useful with JSON object .. */

            }
            /* handle other JSON data structures */
        }

        System.out.println(userObjects.size());




        //UserSimple userObject = gson.fromJson(userJson, UserSimple.class);
        //LOG.debug(userObject.getMESSAGE());




        ExecutorService xrayExecutor = Executors.newSingleThreadExecutor();

        xrayExecutor.submit(new Runnable() {
            public void run() {

                LOG.debug(getStartingMessage());

                int count = 0;
                while (count < noOfTemperatureEvents) {
                    TemperatureEvent ve = new TemperatureEvent(new Random().nextInt(500), new Date());
                    temperatureEventHandler.handle(ve);
                    count++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        LOG.error("Thread Interrupted", e);
                    }
                }

            }
        });
    }

    private String getStartingMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n************************************************************");
        sb.append("\n* STARTING - ");
        sb.append("\n* PLEASE WAIT - TEMPERATURES ARE RANDOM SO MAY TAKE");
        sb.append("\n* A WHILE TO SEE WARNING AND CRITICAL EVENTS!");
        sb.append("\n************************************************************\n");
        return sb.toString();
    }
}
