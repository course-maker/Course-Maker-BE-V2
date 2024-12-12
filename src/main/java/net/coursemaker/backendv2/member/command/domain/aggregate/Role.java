package net.coursemaker.backendv2.member.command.domain.aggregate;

public enum Role {
	ROLE_ADMIN("ROLE_ADMIN", "관리자"), // 관리자
	ROLE_PARTNER("ROLE_PARTNER", "파트너사"), // 파트너사
	ROLE_BEGINNER_TRAVELER("ROLE_BEGINNER_TRAVELER", "초보 여행가"), // 일반 여행가
	ROLE_INTERMEDIATE_TRAVELER("ROLE_INTERMEDIATE_TRAVELER", "중급 여행가"), // 중급 여행가
	ROLE_PRO_TRAVELER("ROLE_PRO_TRAVELER", "프로 여행가"); // 프로 여행가

	private final String role;
	private final String roleKor;

	Role(String role, String roleKor) {
		this.role = role;
		this.roleKor = roleKor;
	}

	public String getRole() {
		return role;
	}

	public static String toKor(String role) {
		return switch (role) {
			case "ADMIN", "ROLE_ADMIN" -> ROLE_ADMIN.roleKor;
			case "PARTNER", "ROLE_PARTNER" -> ROLE_PARTNER.roleKor;
			case "BEGINNER_TRAVELER", "ROLE_BEGINNER_TRAVELER" -> ROLE_BEGINNER_TRAVELER.roleKor;
			case "INTERMEDIATE_TRAVELER", "ROLE_INTERMEDIATE_TRAVELER" -> ROLE_INTERMEDIATE_TRAVELER.roleKor;
			case "PRO_TRAVELER", "ROLE_PRO_TRAVELER" -> ROLE_PRO_TRAVELER.roleKor;
			default -> null;
		};
	}
}
