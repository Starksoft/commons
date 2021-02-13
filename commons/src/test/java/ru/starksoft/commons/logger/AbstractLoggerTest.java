package ru.starksoft.commons.logger;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static ru.starksoft.commons.logger.AbstractLogger.replaceLogPlaceholders;

public class AbstractLoggerTest {

    @Test
    public void replaceSinglePlaceholder() {
        String result = replaceLogPlaceholders("message with placeholder {}", () -> 123);
        assertEquals("message with placeholder 123", result);

        result = replaceLogPlaceholders("message with placeholder {}", () -> new Exception("!"));
        assertEquals("message with placeholder java.lang.Exception: !", result);

        result = replaceLogPlaceholders("message with placeholder {}", () -> "!");
        assertEquals("message with placeholder !", result);

        result = replaceLogPlaceholders("{} message with placeholder at start", () -> "!");
        assertEquals("! message with placeholder at start", result);

        result = replaceLogPlaceholders("message with placeholder {}", () -> null);
        assertEquals("message with placeholder null", result);

        result = replaceLogPlaceholders("proceedOrderIfSplitPayment(): Error sending order={}, e={}",
                                        () -> Arrays.asList(
                                                "Возникла неизвестная ошибка в процессе отправки заказа",
                                                "android.os.NetworkOnMainThreadException\n" +
                                                "\tat android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork(StrictMode.java:1605)\n" +
                                                "\tat java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:116)\n" +
                                                "\tat java.net.SocketOutputStream.write(SocketOutputStream.java:161)\n" +
                                                "\tat okio.OutputStreamSink.write(JvmOkio.kt:53)"
                                        )
        );
        assertEquals(
                "proceedOrderIfSplitPayment(): Error sending order=Возникла неизвестная ошибка в процессе отправки заказа, e=android.os.NetworkOnMainThreadException\n" +
                "\tat android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork(StrictMode.java:1605)\n" +
                "\tat java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:116)\n" +
                "\tat java.net.SocketOutputStream.write(SocketOutputStream.java:161)\n" +
                "\tat okio.OutputStreamSink.write(JvmOkio.kt:53)",
                result
        );
    }

    @Test
    public void replaceMultiplePlaceholders() {
        List<String> data = new ArrayList<>();
        data.add("one");
        data.add("two");
        data.add("three");

        String result = replaceLogPlaceholders("1={}, 2={}, 3={}", () -> data);
        assertEquals("1=one, 2=two, 3=three", result);

        data.clear();
        data.add("four");
        result = replaceLogPlaceholders("4={}", () -> data);
        assertEquals("4=four", result);

        data.clear();
        data.add("one");
        data.add("two");
        data.add(null);
        data.add("four");
        data.add(null);

        result = replaceLogPlaceholders("1={}, 2={}, 3={}, 4={}, 5={}", () -> data);
        assertEquals("1=one, 2=two, 3=null, 4=four, 5=null", result);
    }

    @Test
    public void replaceMultiplePlaceholdersVararg() {
        testVararg("one", "two", "three");
    }

    private void testVararg(String... args) {
        String result = replaceLogPlaceholders("1={}, 2={}, 3={}", () -> args);
        assertEquals("1=one, 2=two, 3=three", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void replaceMultiplePlaceholdersMismatch() {
        List<String> data = new ArrayList<>();
        data.add("one");
        data.add("two");
        data.add("three");

        String result = replaceLogPlaceholders("1={}, 2={}", () -> data);
        assertEquals("1=one, 2=two, 3=three", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void replaceMultiplePlaceholdersMismatch2() {
        List<String> data = new ArrayList<>();
        data.add("one");
        data.add("two");
        data.add("three");

        String result = replaceLogPlaceholders("test", () -> data);
        assertEquals("test", result);
    }

    @Test
    public void findWord() {
        List<Integer> words = AbstractLogger.findWord("{}{}{}{}", "{}");
        assertEquals(4, words.size());

        words = AbstractLogger.findWord("{}", "{}");
        assertEquals(1, words.size());

        words = AbstractLogger.findWord("124", "{}");
        assertEquals(0, words.size());
    }
}
