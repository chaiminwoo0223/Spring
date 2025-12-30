package hello.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "my.datasource")
public record MyDataSourcePropertiesV1(
        String url,
        String username,
        String password,
        Etc etc
) {
    public record Etc(
            int maxConnection,
            Duration timeout,
            List<String> options
    ) {}
}
