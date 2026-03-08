package de.caritas.cob.uploadservice.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import de.caritas.cob.uploadservice.api.exception.CustomCryptoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class EncryptionServiceTest {

  private static final String KEY_MASTER = "MasterKeyTestKey";
  private static final String KEY_APPLICATION = "ApplicationTestKey";
  private static final String KEY_SESSION = "SessionTestKey";
  private static final String KEY_SESSION_WRONG = "WrongSessionTestKey";

  private static final String MESSAGE_PLAIN = "Das hier ist jetzt mal eine Test-Message";
  private static final String MESSAGE_ENCRYPTED =
      "enc:uWHNUkWrQJikGnVpknvB3SkzT1RWHJuY0igDT9p7fGFHWECLBpV2+0eIZF6Qi7J0";

  @InjectMocks private EncryptionService encryptionService;

  @Mock private LogService logService;

  @BeforeEach
  public void setup() throws NoSuchFieldException {
    ReflectionTestUtils.setField(encryptionService, "fragment_applicationKey", KEY_APPLICATION);
    encryptionService.updateMasterKey(KEY_MASTER);
  }

  @Test
  public void check_setup() {
    assertEquals(KEY_MASTER, encryptionService.getMasterKey(), "MasterKey was not properly set");
    assertEquals(
        KEY_APPLICATION,
        encryptionService.getApplicationKey(),
        "ApplicationKey was not properly set");
  }

  @Test
  public void updateMasterKey_Should_UpdateMasterKeyFragment() {
    encryptionService.updateMasterKey(KEY_MASTER);
    assertEquals(KEY_MASTER, encryptionService.getMasterKey(), "Cannot properly set MasterKey");
  }

  @Test
  public void encrypt_Should_ReturnEncryptedText_WhenProvidedWithValidParameters()
      throws Exception {
    String encryptMessage = encryptionService.encrypt(MESSAGE_PLAIN, KEY_SESSION);
    assertEquals(MESSAGE_ENCRYPTED, encryptMessage, "Did not get the expected encryption result.");
  }

  @Test
  public void encrypt_Should_ReturnWrongEncryptedText_WhenProvidedWithInvalidParameters()
      throws Exception {
    String encryptMessage = encryptionService.encrypt(MESSAGE_PLAIN, KEY_SESSION_WRONG);
    Assertions.assertNotEquals(
        MESSAGE_ENCRYPTED, encryptMessage, "Did not get the expected encryption result.");
  }

  @Test
  public void decrypt_Should_ReturnDecryptedText_WhenProvidedWithValidParameters()
      throws Exception {
    String decryptedMessage = encryptionService.decrypt(MESSAGE_ENCRYPTED, KEY_SESSION);
    assertEquals(MESSAGE_PLAIN, decryptedMessage, "Did not get the expected decrypted result.");
  }

  @Test
  public void decrypt_Should_ReturnWrongDecryptedText_WhenProvidedWithInvalidParameters()
      throws Exception {
    try {
      encryptionService.decrypt(MESSAGE_ENCRYPTED, KEY_SESSION_WRONG);
      fail("The expected BadPaddingException due to wrong password was not thrown.");
    } catch (CustomCryptoException ex) {
      assertTrue(true, "Expected BadPaddingException thrown");
    }
  }

  @Test
  public void decrypt_Should_ReturnNull_WhenMessageIsNull() throws CustomCryptoException {
    assertNull(encryptionService.decrypt(null, KEY_MASTER));
  }
}
