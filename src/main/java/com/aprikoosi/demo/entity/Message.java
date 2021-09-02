package com.aprikoosi.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Author: liufeng
 * @Date: 2021/8/26 18:49
 * @Description:
 */
@Data
public class Message {
	// 发送者
	public String from;
	// 发送者名称
	public String fromName;
	// 接收者
	public String to;
	// 发送的文本
	public String text;
	// 发送日期
	public Date date;
}
