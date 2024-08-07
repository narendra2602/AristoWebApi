package com.aristowebapi.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StkRepo10Request extends ViewRequest{ 

	
	private int repType;
	private int uv;
	private int pcode;
	private int repTypePgwise;
	private int creditNoteType;
}
