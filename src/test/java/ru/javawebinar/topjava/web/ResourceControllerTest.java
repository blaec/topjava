package ru.javawebinar.topjava.web;

import com.sun.xml.internal.ws.api.pipe.ContentType;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceControllerTest extends AbstractControllerTest {

    @Test
    public void testCss() throws Exception{
        mockMvc.perform(get("/resources/css/style.css"))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=ISO-8859-1")))
//                .andExpect(content().contentType("text/css;charset=ISO-8859-1"))
                .andExpect(content().contentTypeCompatibleWith("text/css"));
//                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT>P));
    }
}
