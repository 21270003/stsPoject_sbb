package com.mysite.sbb;

import lombok.Getter;

@Getter
public class HelloLombok {
	private String hello;
	private int lombk;
	
	public static void main(String[] args) {
		HelloLombok test = new HelloLombok();
		test.getHello();
	}
}
