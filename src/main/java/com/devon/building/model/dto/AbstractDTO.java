package com.devon.building.model.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractDTO implements Serializable {
  @Serial private static final long serialVersionUID = 7213600440729202783L;

  private Long id;
  private Date createdDate;
  private String createdBy;
  private Date modifiedDate;
  private String modifiedBy;
}
