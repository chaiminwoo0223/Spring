package hello.typeconverter.converter;


import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {

    @Override
    public @Nullable Integer convert(String source) {
        log.info("convert source = {}", source);
        return Integer.valueOf(source);
    }
}
