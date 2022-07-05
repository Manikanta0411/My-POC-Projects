package com.record.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailModelResponse {

	private String email;
	private Boolean status;
	private String salutation;
	private Boolean claim;
	private Boolean underwriting;
	private Boolean accounting;
	private Date expireDate;
	private List<LobModel> lobs;
}
