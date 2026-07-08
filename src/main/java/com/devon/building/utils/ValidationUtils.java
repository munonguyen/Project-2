package com.devon.building.utils;

import com.devon.building.exception.DataBuildingInvalidException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ValidationUtils {

  private ValidationUtils() {}

  public static List<Long> normalizeIds(List<Long> ids, String fieldName) {
    if (ids == null) {
      return Collections.emptyList();
    }
    if (ids.stream().anyMatch(Objects::isNull)) {
      throw new DataBuildingInvalidException(fieldName + " không hợp lệ");
    }
    return ids.stream().distinct().toList();
  }
}
