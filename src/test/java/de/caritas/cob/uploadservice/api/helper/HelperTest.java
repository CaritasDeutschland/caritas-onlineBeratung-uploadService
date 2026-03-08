package de.caritas.cob.uploadservice.api.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HelperTest {

  private static String TEXT = "Lorem Ipsum";
  private static String TEXT_WITH_NEWLINE = "Lorem Ipsum\nLorem Ipsum";
  private static String TEXT_WITH_NEWLINE_AND_HTML_AND_JS =
      "<b>Lorem Ipsum</b>\nLorem Ipsum<script>alert('1');</script>";
  private static String TEXT_WITH_HTML = "<strong>Lorem Ipsum</strong>";
  private static String TEXT_WITH_JS = "Lorem Ipsum<script>alert('1');</script>";
  private static String DECODED_STRING = "töst#$";
  private static String ENCODED_STRING = "t%C3%B6st%23%24";

  @Test
  public void removeHtmlFromText_Should_RemoveHtmlFromText() {
    assertEquals(TEXT, Helper.removeHtmFromText(TEXT_WITH_HTML));
  }

  @Test
  public void removeHtmlFromText_Should_RemoveJavascriptFromText() {
    assertEquals(TEXT, Helper.removeHtmFromText(TEXT_WITH_JS));
  }

  @Test
  public void removeHtmlFromText_ShouldNot_RemoveNewslinesFromText() {
    assertEquals(TEXT_WITH_NEWLINE, Helper.removeHtmFromText(TEXT_WITH_NEWLINE));
  }

  @Test
  public void
      removeHtmlFromText_Should_RemoveHtmlAndJavascriptFromText_And_ShouldNot_RemoveNewslines() {
    assertEquals(TEXT_WITH_NEWLINE, Helper.removeHtmFromText(TEXT_WITH_NEWLINE_AND_HTML_AND_JS));
  }

  @Test
  public void urlEncodeString_Should_ReturnEncodedString() {
    assertEquals(ENCODED_STRING, Helper.urlEncodeString(DECODED_STRING));
  }

  @Test
  public void urlDecodeString_Should_ReturnEncodedString() {
    assertEquals(DECODED_STRING, Helper.urlDecodeString(ENCODED_STRING));
  }
}
