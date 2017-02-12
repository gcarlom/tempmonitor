package com.gcmassari.tempmonitor;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

import com.gcmassari.tempmonitor.main.Main;

public class HelloWorldTest {

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private PrintStream ps = new PrintStream(baos);

    @Before
    public void setup() {
        System.setOut(ps);
    }

    @Test
    public void shouldPrintTimeToConsole() throws IOException {
        Main.main(new String[] { });

        assertThat(output(), containsString("Entry 1"));
        assertThat(output(), containsString("Entry 92"));
    }

    @Test
    public void shouldPrintHelloWorldToConsole() throws IOException {
        Main.main(new String[] { });

        assertThat(output(), containsString("Hello world!"));
    }

    private String output() {
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }
}
