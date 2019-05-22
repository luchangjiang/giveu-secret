package com.giveu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by fox on 2018/8/27.
 */
@RestController
@RequestMapping(value = "/template")
public class TemplateController {

	@RequestMapping(value = "/hi")
	public String creditActiveHandle() throws Exception {
		return "GiveuSecret";
	}

}
