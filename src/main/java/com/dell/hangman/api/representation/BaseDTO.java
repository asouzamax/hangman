package com.dell.hangman.api.representation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class BaseDTO {

  private String id;
  
  @JsonProperty(access = Access.READ_ONLY)
  private OffsetDateTime registrationDate;


}
