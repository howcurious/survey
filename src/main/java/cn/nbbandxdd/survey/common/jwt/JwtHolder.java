package cn.nbbandxdd.survey.common.jwt;

/**
 * <p>JwtHolder。保存每个请求的openId。
 * 
 * @author howcurious
 */
public class JwtHolder {

	private final static ThreadLocal<String> jwtHolder = new ThreadLocal<>();
	
	/**
	 * <p>设置openId。
	 * 
	 * @param token Jwt
	 */
	public static void set(String token) {
		
		jwtHolder.set(token);
	}
	
	/**
	 * <p>获取openId。
	 * 
	 * @return String openId
	 */
	public static String get() {
		
		return jwtHolder.get();
	}
	
	/**
	 * 清除openId。
	 */
	public static void remove() {
		
		jwtHolder.remove();
	}
}
