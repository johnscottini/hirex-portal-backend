package com.hirex.portal.config.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.Lists;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateFormatter extends StdSerializer<Date> implements Formatter<Date> {
    private static final List<DateFormat> formats;
    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    static {
        formats = Lists.newArrayList(
                new SimpleDateFormat(FORMAT),
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
                new SimpleDateFormat("yyyy-MM-dd")
        );
    }

    private final DateFormat printer = new SimpleDateFormat(FORMAT);

    public DateFormatter() {
        super(Date.class);
    }

    @Override
    @NonNull
    @SuppressWarnings({"squid:CallToDeprecatedMethod", "squid:S1309"})
    public Date parse(@NonNull String s, @NonNull Locale locale) {
        try {
            return DateFormat.getDateInstance().parse(s);
        } catch (IllegalArgumentException ex) {
            for (DateFormat format : formats) {
                try {
                    return format.parse(s);
                } catch (ParseException ignored) {
                    //Tenta outro formatter
                }
            }
            throw ex;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @NonNull
    public String print(@NonNull Date date, @NonNull Locale locale) {
        return printer.format(date);
    }

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(printer.format(date));
    }
}
