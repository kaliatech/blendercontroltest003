package com.jgstech.bc003;


import com.jgstech.bc003.net.PacketSimServer;
import jline.*;
import jline.console.ConsoleReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class BlenderControl003 {

    volatile static boolean running = true;

    public static void main(String[] args) {
        BlenderControl003 app = new BlenderControl003();
        app.run();
    }

    public void run() {

        PacketSimServer srvr = new PacketSimServer();

        try {
            srvr.init();


//            //formatter:on
//            Terminal t = TerminalBuilder .builder()
//                            .name("BlenderControl003")
//                            .system(true)
//                            .streams(System.in, System.out)
//                            .build();
//            //formatter:off

//            Terminal terminal = TerminalBuilder.terminal();


            //JLine3 failed only low level dll. Win32 only?
//            Terminal terminal = TerminalBuilder.builder()
//                    .system(true)
//                    .build();
//
//            LineReader lineReader = LineReaderBuilder.builder()
//                    .terminal(terminal)
//                    .build();
//            String prompt = "prompt >";
//            boolean reading = true;
//            while (reading) {
//                String line = null;
//                try {
//                    line = lineReader.readLine(prompt);
//
//                    terminal.writer().println("You entered:" + line);
//
//                    if (line.equals("quit")) {
//                        reading = false;
//                    }
//                } catch (UserInterruptException e) {
//                    // Ignore
//                } catch (EndOfFileException e) {
//                    return;
//                }
//            }


//            jline.TerminalFactory.registerFlavor(jline.TerminalFactory.Flavor.WINDOWS, UnsupportedTerminal.class);
//            Terminal t = TerminalFactory.create();
//            ConsoleReader console = new ConsoleReader(System.in, System.out, t);

            ConsoleReader console = new ConsoleReader(System.in, System.out, new AnsiWindowsTerminal() {

                @Override
                public void setEchoEnabled(final boolean enabled) {
                    super.setEchoEnabled(false);
                }

                @Override
                public boolean hasWeirdWrap() {
                    return false;
                }


            });
            String line = null;
            //console.setPrompt("cmd: ");
            while (running && (line = console.readLine(">> ")) != null) {
                handleConsoleCmd(srvr, console, line);
            }
            console.close();

        } catch (Exception e) {
            srvr.shutdown();
        }
    }

    private void handleConsoleCmd(PacketSimServer srvr, ConsoleReader console, String line) throws IOException {
        //console.println("Received:" + line);

        if (line.trim().equals("")) {
            for (int i=0;i<10;i++) {
                srvr.send();
            }
        }

        if (line.equals("reset")) {
            srvr.reset();
        }

        if (line.equals("quit")) {
            running = false;
        }
    }
}
