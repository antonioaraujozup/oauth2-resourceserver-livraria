package base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Import(KeepErrorMessagesWithMockMvcAdvice.class)
public abstract class SpringBootIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    public MockHttpServletRequestBuilder GET(String uri) {
        return get(uri)
                .contentType(APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder GET(String uri, Object...uriVars) {
        return get(uri, uriVars)
                .contentType(APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder GET(String uri, Jwt jwt, Object...uriVars) {
        return get(uri, uriVars)
                .contentType(APPLICATION_JSON)
                .with(jwt().jwt(jwt));
    }

    public MockHttpServletRequestBuilder GET(String uri, Jwt jwt, String scope, Object...uriVars) {
        return get(uri, uriVars)
                .contentType(APPLICATION_JSON)
                .with(jwt()
                        .authorities(new SimpleGrantedAuthority(scope))
                );
    }

    public MockHttpServletRequestBuilder POST(String uri) {
        return post(uri)
                .contentType(APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder POST(String uri, Object payload) throws JsonProcessingException {
        String json = mapper.writeValueAsString(payload);
        return post(uri)
                .contentType(APPLICATION_JSON)
                .content(json);
    }

    public MockHttpServletRequestBuilder POST(String uri, Object payload, Jwt jwt) throws JsonProcessingException {
        String json = mapper.writeValueAsString(payload);
        return post(uri)
                .contentType(APPLICATION_JSON)
                .content(json)
                .with(jwt().jwt(jwt));
    }


    public MockHttpServletRequestBuilder POST(String uri, Object payload, Jwt jwt, String scope) throws JsonProcessingException {
        String json = mapper.writeValueAsString(payload);
        return post(uri)
                .contentType(APPLICATION_JSON)
                .content(json)
                .with(jwt().jwt(jwt)
                        .authorities(new SimpleGrantedAuthority(scope))
                );
    }

    public MockHttpServletRequestBuilder DELETE(String uri, Object...uriVars) {
        return delete(uri, uriVars)
                .contentType(APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder DELETE(String uri, Jwt jwt, Object...uriVars) {
        return delete(uri, uriVars)
                .contentType(APPLICATION_JSON)
                .with(jwt().jwt(jwt));
    }

    public MockHttpServletRequestBuilder DELETE(String uri, Jwt jwt, String scope, Object...uriVars) {
        return delete(uri, uriVars)
                .contentType(APPLICATION_JSON)
                .with(jwt()
                        .authorities(new SimpleGrantedAuthority(scope))
                );
    }

    public Jwt criaAccessToken() {
        Jwt accessToken = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "user")
                .claim("scope", "read")
                .build();

        return accessToken;
    }

}
