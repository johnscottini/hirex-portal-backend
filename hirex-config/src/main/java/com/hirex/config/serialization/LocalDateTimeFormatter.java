package com.hirex.config.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.Lists;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

public class LocalDateTimeFormatter extends StdSerializer<LocalDateTime> implements Formatter<LocalDateTime> {

    private static final DateTimeFormatter printer = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final List<DateTimeFormatter> formats = Lists.newArrayList(
            new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd['T'[HH][:mm][:ss][.SSS]['Z']]")
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter()
    );

    public LocalDateTimeFormatter() {
        super(LocalDateTime.class);
    }

    @NonNull
    @Override
    @SuppressWarnings({"java:S1166"})
    public LocalDateTime parse(@NonNull String s, @NonNull Locale locale) {
        try {
            return LocalDateTime.parse(s);
        } catch (Exception e) {
            for (DateTimeFormatter format : formats) {
                try {
                    return LocalDateTime.from(format.parse(s));
                } catch (Exception ignored) {
                    //Tenta outro formatter
                }
            }
            throw new IllegalArgumentException("N??o foi poss??vel converter a data", e);
        }
    }

    @NonNull
    @Override
    public String print(@NonNull LocalDateTime date, @NonNull Locale locale) {
        return printer.format(date);
    }

    @Override
    public void serialize(LocalDateTime localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(printer.format(localDate));
    }
}
