package com.architexa.collab.proxy;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;

public class PluginUtils {
	
	
	public static class TripleInt implements Comparable<TripleInt>{
		public long v1=0;
		public long v2=0;
		public long v3=0;
		public TripleInt(long m1, long m2, long m3) {
			v1=m1;
			v2=m2;
			v3=m3;
		}
		@Override
		public int compareTo(TripleInt di) {
			boolean small = v1<di.v1 || (v1==di.v1 && v2<di.v2) || (v1==di.v1 && v2==di.v2 && v3<di.v3);
			boolean eq = v1==di.v1 && v2 == di.v2 && v3 == di.v3;
			return small ? -1 : (eq ? 0 : 1);
		}
		@Override
		public String toString() {
			return v1+"."+v2+"."+v3;
		}
		public static TripleInt of(long... m) {
			switch (m.length) {
			case 0:
				return new TripleInt(0,0,0);
			case 1:
				return new TripleInt(m[0],0,0);
			case 2:
				return new TripleInt(m[0],m[1],0);
			default:
				return new TripleInt(m[0],m[1],m[2]);
			}
			
		}
		
	}
	
	//Example:
	//count = 3
	//pos: 0 1 2 3 4 
	// vs:  0 1 2 3

	private static TripleInt getVersion(String inp) {
		try {
			int count = inp.length() - inp.replace(".", "").length();
			int[] pos = new int[count+2]; 
			long[] vs = new long[count+1];
			for(int i = 0; i< count+1; i++) {
				int next = inp.indexOf('.', pos[i]+1);
				pos[i+1]=  next == -1 ? inp.length() : next;
				vs[i]=Long.parseLong(inp.substring(pos[i], pos[i+1]).replaceAll("[^\\d]", ""));
			}
			return TripleInt.of(vs);
		} catch (Exception e) {
			return TripleInt.of(0,0,0);
		}
	}

	/**
	 * When comparing the plugin version returned by this method to another 
	 * version number, use PluginUtils.versionsEqual()to test for equality
	 * rather than ==. That is, instead of doing if(getPluginVer >= 3.3), do
	 * if(getPluginVer > 3.3 || versionsEqual(getPluginVer, 3.3) 
	 */
	public static final TripleInt getPluginVer(String symbolicName) {
		String ver = Platform.getBundle(symbolicName).getHeaders().get("Bundle-Version").toString();
		return getVersion(ver);
    }
	
	
}
