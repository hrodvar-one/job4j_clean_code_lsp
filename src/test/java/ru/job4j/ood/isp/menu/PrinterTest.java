package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class PrinterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void whenPrintMenuThenCorrectOutput() {
        Menu menu = new SimpleMenu();
        MenuPrinter printer = new Printer();
        menu.add(SimpleMenu.ROOT, "Сходить в магазин", System.out::println);
        menu.add(SimpleMenu.ROOT, "Покормить собаку", System.out::println);
        menu.add("Сходить в магазин", "Купить продукты", System.out::println);
        menu.add("Купить продукты", "Купить хлеб", System.out::println);
        menu.add("Купить продукты", "Купить молоко", System.out::println);

        printer.print(menu);

        String expectedOutput = String.join(System.lineSeparator(),
                "1. Сходить в магазин",
                "----1.1. Купить продукты",
                "--------1.1.1. Купить хлеб",
                "--------1.1.2. Купить молоко",
                "2. Покормить собаку",
                "");

        assertThat(outContent.toString().trim()).isEqualTo(expectedOutput.trim());
    }
}