package cn.nbbandxdd.survey.common.jwt;

public class JwtHolder {

	private final static ThreadLocal<String> jwtHolder = new ThreadLocal<>();
	
	public static void set(String token) {
		
		jwtHolder.set(token);
	}
	
	public static String get() {
		
		return jwtHolder.get();
	}
	
	public static void remove() {
		
		jwtHolder.remove();
	}
}
