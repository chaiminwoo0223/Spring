package hello.config;

import hello.datasource.MyDataSource;
import hello.datasource.MyDataSourcePropertiesV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableConfigurationProperties(MyDataSourcePropertiesV2.class)
@RequiredArgsConstructor
public class MyDataSourceConfigV2 {
    private final MyDataSourcePropertiesV2 properties;

    @Bean
    public MyDataSource myDataSource() {
        return new MyDataSource(
                properties.url(),
                properties.username(),
                properties.password(),
                properties.etc().maxConnection(),
                properties.etc().timeout(),
                properties.etc().options()
        );
    }
}
