package de.caritas.cob.uploadservice.api.service;

import de.caritas.cob.uploadservice.api.exception.httpresponses.QuotaReachedException;
import de.caritas.cob.uploadservice.api.helper.AuthenticatedUser;
import de.caritas.cob.uploadservice.api.model.UploadByUser;
import de.caritas.cob.uploadservice.api.repository.UploadByUserRepository;
import java.time.LocalDateTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service to track uploaded files per user and validate against configurated limit.
 */
@Service
@RequiredArgsConstructor
public class UploadTrackingService {

  @Value("${upload.file.perday.limit}")
  private int uploadLimit;

  private final @NonNull UploadByUserRepository uploadByUserRepository;
  private final @NonNull AuthenticatedUser authenticatedUser;

  /**
   * Validates the upload limit of files for given user and throws a {@link QuotaReachedException}
   * if limit for day is reached.
   *
   * @param sessionId the id for the current session
   */
  public void validateUploadLimit(String sessionId) {
    String userId = this.authenticatedUser.getUserId();
    Integer uploadCount = this.uploadByUserRepository
        .countAllByUserIdAndSessionId(userId, sessionId);
    if (uploadCount >= this.uploadLimit) {
      throw new QuotaReachedException(LogService::logInfo);
    }
  }

  /**
   * Stores an {@link UploadByUser} entry for given user.
   *
   * @param sessionId the id for the current session
   */
  public void trackUploadedFileForUser(String sessionId) {
    String userId = this.authenticatedUser.getUserId();
    UploadByUser uploadByUser = UploadByUser.builder()
        .userId(userId)
        .sessionId(sessionId)
        .createDate(LocalDateTime.now())
        .build();

    this.uploadByUserRepository.save(uploadByUser);
  }

  /**
   * Cleans all stored file limits. Cron is execuded daily at 00:00 to ensure daily cleanup. Please
   * do not change cron definition!
   */
  @Scheduled(cron = "0 0 0 * * *")
  public void cleanUpFileLimits() {
    this.uploadByUserRepository.deleteAll();
    LogService.logInfo("File restrictions are reset!");
  }

}
