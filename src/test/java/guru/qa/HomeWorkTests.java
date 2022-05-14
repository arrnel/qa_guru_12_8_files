package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.*;

public class HomeWorkTests {

    @BeforeAll
    static void beforeAll() {
        Configuration.downloadsFolder = "build/downloads/files";
        Configuration.browserSize = "1920x1080";
    }

    @Test
    void jacksonStringTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Selenide.open("https://www.site24x7.com/tools/json-generator.html");
        $("#convert").as("Button 'Generate'").shouldBe(visible).click();
        String jsonString = $("#ocontainer").as("Output JSON Textarea").shouldNotBe(empty).getValue();

        JsonNode json = mapper.readTree(jsonString);
        int actualValue = json.get("min_position").asInt();
        assertTrue(actualValue > 1);

    }

    @Test
    void jacksonParseJSONFromFileTest() throws Exception {

        Selenide.open("https://filesamples.com/formats/json");
        File downloadedFile = $("a[href*='sample2.json']").as("Button 'Download Sample2.json'").shouldBe(visible).download();

        try (InputStream is = new FileInputStream(downloadedFile)) {

            ObjectMapper mapper = new ObjectMapper();
            User user = mapper.readValue(new String(is.readAllBytes(), StandardCharsets.UTF_8), User.class);

            assertAll("Soft assertions", () -> {
                String actualPhoneNumberType = user.getPhoneNumber().get(0).getType();
                assertEquals("home", actualPhoneNumberType);

                String actualFirstName = user.getFirstName();
                assertEquals("Joe", actualFirstName);

                int actualAge = user.getAge();
                assertEquals(28, actualAge);
            });

        }

    }

    @Test
    void checkContainingTextInZipTxtFileTest() throws Exception {

        Selenide.open("https://www.learningcontainer.com/sample-zip-files/");
        File downloadedFile = $("a[href*='sample-zip-file.zip']").shouldBe(visible).download();

        ZipInputStream zis = new ZipInputStream(new FileInputStream(downloadedFile));
        ZipEntry entry;
        ZipFile zipFile = new ZipFile(downloadedFile.getAbsolutePath());

        while ((entry = zis.getNextEntry()) != null) {
            String fileName = entry.getName();
            assertEquals("sample.txt", fileName);

            if (fileName.equals("sample.txt")) {
                try (InputStream is = zipFile.getInputStream(entry)) {
                    String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    assertTrue(text.contains("Thanks!"));
                }
            }
        }

    }

    @Test
    void wrapPDFInZipFileTest() throws Exception {

        String inputFileName = "junit-user-guide-5.8.2.pdf";
        String outputFileName = "output.zip";
        String inputPath = "src/test/resources/pdf/" + inputFileName;
        String outputPath = "src/test/resources/pdf/" + outputFileName;
        File inputFile = new File(inputPath);


        //Test
        try (
                FileOutputStream fos = new FileOutputStream(outputPath);
                ZipOutputStream zos = new ZipOutputStream(fos)
        ) {

            zos.putNextEntry(new ZipEntry(inputFile.getName()));

            byte[] bytes = Files.readAllBytes(Paths.get(inputPath));

            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();

        }


        //Assertion
        try (
                ZipInputStream zis = new ZipInputStream(new FileInputStream(outputPath));
                ZipFile zipFile = new ZipFile(outputPath);
        ) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName();
                assertEquals(inputFileName, fileName);

                if (fileName.equals(inputFileName)) {
                    try (InputStream is = zipFile.getInputStream(entry)) {
                        PDF pdf = new PDF(is);
                        assertEquals(166, pdf.numberOfPages);
                        assertTrue(pdf.text.contains("JUnit 5 User Guide"));
                    }
                }
            }
        }
    }

    boolean isAlive() {
        try {
            WebDriverRunner.getWebDriver().getCurrentUrl();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @AfterEach
    void afterEach() {

        if (isAlive()) {
            Selenide.closeWindow();
            Selenide.closeWebDriver();
        }

    }

}
