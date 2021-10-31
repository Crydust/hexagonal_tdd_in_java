package whiteboard.boilerplate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class NihStringUtilTest {
    @ParameterizedTest
    @CsvSource({
        ", ",
        "'', ''",
        "a, a",
        "aa, aa",
        "a&, a&amp;",
        "&a, &amp;a",
        "&, &amp;",
        "&&, &amp;&amp;",
        "&a&, &amp;a&amp;",
        "a&a, a&amp;a",
        "&<>\"', &amp;&lt;&gt;&#034;&#039;"
    })
    void shouldEscapeXml(String in, String expected) {
        assertThat(NihStringUtil.escapeXml(in), is(expected));
    }

    @ParameterizedTest
    @CsvSource({
        ", , , ",
        "original, key, value, original?key=value",
        "'', key, value, ?key=value",
        "original, key, '', original?key=",
        "original?a=b, key, value, original?a=b&key=value",
        "original#xyz, key, value, original?key=value#xyz",
        "original?a=b#xyz, key, value, original?a=b&key=value#xyz",
        "original#xyz?a=b, key, value, original?key=value#xyz?a=b",
        "original, key?=&, value, original?key%3F%3D%26=value",
        "original, key, value?=&, original?key=value%3F%3D%26",
    })
    void shouldAddAttributeToQuery(String original, String key, String value, String expected) {
        assertThat(NihStringUtil.addAttributeToQuery(original, key, value), is(expected));
    }
}
