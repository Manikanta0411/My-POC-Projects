package com.record.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotifyModel {
	private Long id;
	private String originMessageId;
	private String entityId;
	private Boolean isRead;
}
