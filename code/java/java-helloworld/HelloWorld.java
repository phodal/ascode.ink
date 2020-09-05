import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Instant;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World");
        
        Instant in = new Instant();
        System.out.println("Millisecond time:     in.getMillis():           " + in.getMillis());
    }
}
