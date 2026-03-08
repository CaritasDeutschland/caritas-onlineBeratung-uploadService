package de.caritas.cob.uploadservice.api.helper;

import static de.caritas.cob.uploadservice.helper.TestConstants.RC_DESCRIPTION;
import static de.caritas.cob.uploadservice.helper.TestConstants.RC_DESCRIPTION_WITH_HTML;
import static de.caritas.cob.uploadservice.helper.TestConstants.RC_MESSAGE;
import static de.caritas.cob.uploadservice.helper.TestConstants.RC_MESSAGE_WITH_HTML;
import static de.caritas.cob.uploadservice.helper.TestConstants.RC_ROOM_ID;
import static de.caritas.cob.uploadservice.helper.TestConstants.RC_ROOM_ID_WITH_HTML;
import static de.caritas.cob.uploadservice.helper.TestConstants.RC_TMID;
import static de.caritas.cob.uploadservice.helper.TestConstants.RC_TMID_WITH_HTML;
import static de.caritas.cob.uploadservice.helper.TestConstants.UPLOAD_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import de.caritas.cob.uploadservice.api.container.RocketChatUploadParameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RocketChatUploadParameterSanitizerTest {

  private RocketChatUploadParameterSanitizer rocketChatUploadParameterSanitizer;

  @BeforeEach
  public void setup() {
    rocketChatUploadParameterSanitizer = new RocketChatUploadParameterSanitizer();
  }

  @Test
  public void sanitize_Should_SanitizeRocketChatParameter() {

    RocketChatUploadParameter rocketChatUploadParameterWithHtml =
        RocketChatUploadParameter.builder()
            .message(RC_MESSAGE_WITH_HTML)
            .description(RC_DESCRIPTION_WITH_HTML)
            .roomId(RC_ROOM_ID_WITH_HTML)
            .file(UPLOAD_FILE)
            .tmId(RC_TMID_WITH_HTML)
            .build();

    RocketChatUploadParameter result =
        rocketChatUploadParameterSanitizer.sanitize(rocketChatUploadParameterWithHtml);

    assertEquals(result.getMessage(), RC_MESSAGE);
    assertEquals(result.getDescription(), RC_DESCRIPTION);
    assertEquals(result.getTmId(), RC_TMID);
    assertEquals(result.getRoomId(), RC_ROOM_ID);
  }
}
