package com.KoreaIT.example.JAM.session;

import com.KoreaIT.example.JAM.Member;

public class Session {
	public int loginedMemberId;
	public Member loginedMember;
	
	public Session() {
		loginedMemberId = -1;
	}

	public boolean isLogined() {
		return loginedMemberId != -1;
	}

	public void doLogout() {
		loginedMember = null;
		loginedMemberId = -1;
	}
	
	public void doLogin(Member member) {
		loginedMember = member;
		loginedMemberId = member.id;
	}

}
