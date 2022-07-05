package com.record.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemarksModel {

	private Long id;
    private String remarks;
    private Long layerId;
    private Long headingId;
    private String remarksCode;
    private boolean status;
    
}
