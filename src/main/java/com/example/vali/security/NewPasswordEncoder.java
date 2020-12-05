package com.example.vali.security;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义密码的存储器
 */

public class NewPasswordEncoder implements PasswordEncoder{

	@Override
	public String encode(CharSequence arg0) {
		return arg0.toString();
	}

	@Override
	public boolean matches(CharSequence arg0, String arg1) {
		return arg1.equals(arg0.toString());
	}

}
