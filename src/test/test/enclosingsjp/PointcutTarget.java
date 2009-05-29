/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.enclosingsjp;


public class PointcutTarget {
	private Object m_field;
	
	public PointcutTarget() {
	}
	
	public PointcutTarget(int i) {
	}
	
	public void method1() {
	}
	
	public void setFieldValue(Object obj) {
		m_field = obj;
	}
	
	public Object getFieldValue() {
		return m_field;
	}
}
