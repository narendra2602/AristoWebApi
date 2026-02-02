package com.aristowebapi.dto;
import lombok.Data;

@Data
public class Sales {

    private String month_target;
    private String secondary_sales_as_on_30;
    private String expected_sales_next_month;

}
