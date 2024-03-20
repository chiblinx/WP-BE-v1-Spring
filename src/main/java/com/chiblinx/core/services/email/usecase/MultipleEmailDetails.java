package com.chiblinx.core.services.email.usecase;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MultipleEmailDetails {

  private List<String> recipients;
  private String msgBody;
  private String subject;
  private String attachment; // path to attachment

}
