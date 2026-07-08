package com.devon.building.config;

import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.entity.Building;
import com.devon.building.entity.RentArea;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.Request.BuildingSearchRequest;
import com.devon.building.model.dto.response.BuildingSearchResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper
        .getConfiguration()
        .setMatchingStrategy(MatchingStrategies.STRICT)
        .setSkipNullEnabled(true);

    Converter<List<String>, String> typeCodeToType =
        ctx -> ctx.getSource() == null ? null : String.join(",", ctx.getSource());

    Converter<String, List<String>> typeToTypeCode =
        ctx ->
            ctx.getSource() == null || ctx.getSource().isBlank()
                ? null
                : Arrays.asList(ctx.getSource().split(","));

    Converter<String, byte[]> base64ToBytes =
        ctx -> {
          if (ctx.getSource() == null || ctx.getSource().isBlank()) return null;
          try {
            String base64Image = ctx.getSource();
            if (base64Image.contains(",")) {
              base64Image = base64Image.split(",")[1];
            }
            return Base64.getDecoder().decode(base64Image);
          } catch (Exception e) {
            return null;
          }
        };

    Converter<byte[], String> bytesToBase64 =
        ctx -> ctx.getSource() == null ? null : Base64.getEncoder().encodeToString(ctx.getSource());

    Converter<List<RentArea>, String> rentAreasToString =
        ctx -> {
          if (ctx.getSource() == null || ctx.getSource().isEmpty()) return null;
          return ctx.getSource().stream()
              .map(item -> item.getValue().toString())
              .collect(Collectors.joining(","));
        };

    modelMapper
        .typeMap(BuildingDTO.class, Building.class)
        .addMappings(
            mapper -> {
              mapper.map(BuildingDTO::getDistrictId, Building::setDistrict);
              mapper.map(BuildingDTO::getRentPrice, Building::setPrice);
              mapper.using(typeCodeToType).map(BuildingDTO::getTypeCode, Building::setType);
              mapper.using(base64ToBytes).map(BuildingDTO::getUploadImage, Building::setImage);
            });

    modelMapper
        .typeMap(Building.class, BuildingDTO.class)
        .addMappings(
            mapper -> {
              mapper.map(Building::getDistrict, BuildingDTO::setDistrictId);
              mapper.map(Building::getPrice, BuildingDTO::setRentPrice);
              mapper.using(typeToTypeCode).map(Building::getType, BuildingDTO::setTypeCode);
              mapper.using(bytesToBase64).map(Building::getImage, BuildingDTO::setUploadImage);
              mapper.using(rentAreasToString).map(Building::getRentAreas, BuildingDTO::setRentArea);
            });

    modelMapper
        .typeMap(Building.class, BuildingSearchResponse.class)
        .addMappings(
            mapper -> {
              mapper.map(Building::getPrice, BuildingSearchResponse::setRentPrice);
              mapper
                  .using(rentAreasToString)
                  .map(Building::getRentAreas, BuildingSearchResponse::setRentArea);
            });

    modelMapper
        .typeMap(BuildingSearchRequest.class, BuildingSearchBuilder.class)
        .addMapping(BuildingSearchRequest::getRentAreaFrom, BuildingSearchBuilder::setAreaFrom)
        .addMapping(BuildingSearchRequest::getRentAreaTo, BuildingSearchBuilder::setAreaTo)
        .addMapping(
            BuildingSearchRequest::getManagerPhoneNumber, BuildingSearchBuilder::setManagerPhone)
        .addMapping(BuildingSearchRequest::getTypeCodes, BuildingSearchBuilder::setTypeCode);

    return modelMapper;
  }
}
