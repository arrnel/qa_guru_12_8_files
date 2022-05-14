package guru.qa;


import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;

import com.codeborne.xlstest.XLS;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

public class MySelenideTests {

    @Test
    void downloadTest() throws FileNotFoundException {
        Selenide.open("https://github.com/junit-team/junit5/blob/main/README.md");
        File file = $("a[id=downloadButton]").shouldBe(visible).download();

        try (InputStream inputStream = new FileInputStream(file)) {
            assertThat(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8).contains("This repository is the home of the next generation of JUnit, JUnit 5."));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void parsePDFTest() throws IOException {

        Selenide.open("https://junit.org/junit5/docs/current/user-guide/");
        File downloadedFile = $("a[href*='guide']").as("Документация JUnit5").shouldBe(visible).download();

        PDF pdf = new PDF(downloadedFile);
        assertThat(pdf.author).contains("Marc Philipp");

    }

    @Test
    void parseXLSXTest() throws IOException {

        Selenide.open("https://les-snab.ru/prices/");
        File downloadedFile = $(byText("Сосна ель")).as("Прайс-лист 'Сосна ель'").shouldBe(visible).download();
        XLS xls = new XLS(downloadedFile);

        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(3)
                .getCell(1)
                .getStringCellValue()
                .contains("Сосна ель"));

    }

    @Test
    void parseCSVTest() throws IOException {

        Selenide.open("https://les-snab.ru/prices/");
        File downloadedFile = $("a[href*='.csv']").as("Прайс-лист csv").shouldBe(visible).download();
        XLS xls = new XLS(downloadedFile);

        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(3)
                .getCell(1)
                .getStringCellValue()
                .contains("Сосна ель"));

    }

}
