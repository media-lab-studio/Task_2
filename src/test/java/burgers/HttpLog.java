package burgers;

import io.qameta.allure.Allure;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

final class HttpLog {
    private static final Logger LOG = LoggerFactory.getLogger(HttpLog.class);
    private static final Pattern SECRET_FIELD = Pattern.compile(
            "(?i)(\"(?:password|accessToken|refreshToken)\"\\s*:\\s*)\"(?:\\\\.|[^\"\\\\])*\"");
    private static final Pattern SECRET_HEADER = Pattern.compile(
            "(?im)(\\b(?:Authorization|Cookie|Set-Cookie)\\s*[:=]\\s*)[^\\r\\n]*");

    static void configure(RequestSpecification spec) {
        var request = new ByteArrayOutputStream();
        var response = new ByteArrayOutputStream();
        spec.filter(new RequestLoggingFilter(LogDetail.ALL, true, new PrintStream(request, true, UTF_8)))
                .filter((req, res, context) -> {
                    publish("HTTP-запрос", request);
                    var result = context.next(req, res);
                    publish("HTTP-ответ", response);
                    return result;
                })
                .filter(new ResponseLoggingFilter(LogDetail.ALL, true, new PrintStream(response, true, UTF_8)));
    }

    private static void publish(String title, ByteArrayOutputStream buffer) {
        String text = SECRET_HEADER.matcher(buffer.toString(UTF_8)).replaceAll("$1[скрыто]");
        text = SECRET_FIELD.matcher(text).replaceAll("$1\"[скрыто]\"");
        LOG.info("{}\n{}", title, text);
        Allure.addAttachment(title, "text/plain", text, ".txt");
    }
}
