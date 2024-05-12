package com.aristowebapi.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MktRepo10Request extends ViewRequest {
	
	

}
