package com.chiblinx.core.usecase.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BasicEntityView {

  UUID getId();

  LocalDateTime getCreatedAt();

  LocalDateTime getUpdatedAt();

}
