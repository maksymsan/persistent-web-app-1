package io.github.maksymsan.persistentwebapp1.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TestFileController {

    @Autowired
    private WebApplicationContext webApplicationContext;

    public void testUpload(String filePath, int statusCode) throws Exception {
        File file = new File(filePath);
        InputStream stream = new FileInputStream(file);
        MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/csv", stream);
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/namedObjectFile")
                .file(multipartFile))
                .andExpect(status().is(statusCode));
    }

    @Test
    public void testValid() throws Exception {
        testUpload("src/test/resources/inputFiles/valid.csv", 200);
    }

    @Test
    public void testDuplicateKey() throws Exception {
        testUpload("src/test/resources/inputFiles/duplicateKey.csv", 400);
    }

    @Test
    public void testInvalidHeader() throws Exception {
        testUpload("src/test/resources/inputFiles/invalidHeader.csv", 400);
    }

    @Test
    public void testBlankKey() throws Exception {
        testUpload("src/test/resources/inputFiles/blankKey.csv", 400);
    }

    @Test
    public void testInvalidTimestamp() throws Exception {
        testUpload("src/test/resources/inputFiles/invalidTimestamp.csv", 400);
    }

    @Test
    public void testInconsistentLine() throws Exception {
        testUpload("src/test/resources/inputFiles/inconsistentLine.csv", 400);
    }

}
