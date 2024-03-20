package com.chiblinx.core.usecase.projection;

import java.util.Date;
import java.util.UUID;

public interface BasicEntityView {

  UUID getId();

  Date getCreatedAt();

  Date getUpdatedAt();

}
