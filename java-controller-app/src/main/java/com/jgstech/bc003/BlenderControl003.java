package com.jgstech.bc003;


import com.jgstech.bc003.net.PacketReceiveTester;
import com.jgstech.bc003.net.PacketSendTester;
import jline.AnsiWindowsTerminal;
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

        PacketSendTester sendTester = new PacketSendTester();
        PacketReceiveTester rcvTester = new PacketReceiveTester();

        try {
            sendTester.init();
            rcvTester.init();


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
                handleConsoleCmd(sendTester, rcvTester, console, line);
            }
            console.close();

        }
        catch (Exception e) {
            log.error("Unexpected error.", e);
            sendTester.shutdown();
        }
    }

    private void handleConsoleCmd(PacketSendTester packetSendTester, PacketReceiveTester receiveTester, ConsoleReader console, String line) throws IOException {
        //console.println("Received:" + line);

        if (line.trim().equals("send")) {
            for (int i = 0; i < 10; i++) {
                packetSendTester.send();
            }
        }
        else if (line.trim().equals("") ||
                line.trim().equals("receive")
                ) {
            receiveTester.receive();
        }
        else if (line.equals("reset")) {
            packetSendTester.reset();
        }
        else if (line.equals("quit")) {
            running = false;
        }
    }
}
