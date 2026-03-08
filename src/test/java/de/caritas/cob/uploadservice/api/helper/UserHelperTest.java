package de.caritas.cob.uploadservice.api.helper;

import static de.caritas.cob.uploadservice.helper.TestConstants.USERNAME_DECODED;
import static de.caritas.cob.uploadservice.helper.TestConstants.USERNAME_ENCODED;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserHelperTest {

  private UserHelper userHelper;

  @BeforeEach
  public void setup() {
    this.userHelper = new UserHelper();
  }

  @Test
  public void encodeUsername_Should_ReturnEncodedUsername_WhenDecodedUsernameIsGiven() {
    assertEquals(USERNAME_ENCODED, userHelper.encodeUsername(USERNAME_DECODED));
  }

  @Test
  public void encodeUsername_Should_ReturnEncodedUsername_WhenEncodedUsernameIsGiven() {
    assertEquals(USERNAME_ENCODED, userHelper.encodeUsername(USERNAME_ENCODED));
  }

  @Test
  public void decodeUsername_Should_ReturnDecodedUsername_WhenEncodedUsernameIsGiven() {
    assertEquals(USERNAME_DECODED, userHelper.decodeUsername(USERNAME_ENCODED));
  }

  @Test
  public void decodeUsername_Should_ReturnDecodedUsername_WhenDecodedUsernameIsGiven() {
    assertEquals(USERNAME_DECODED, userHelper.decodeUsername(USERNAME_DECODED));
  }
}
