/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.enclosingsjp;


public class EnclosingTarget {
	private Object m_field;
	
	public EnclosingTarget() {
		PointcutTarget pt = new PointcutTarget();
		
		pt.method1();
	}

	public EnclosingTarget(int i) {
		try {
			throw new IllegalAccessException("msg2");
		} catch(IllegalAccessException iae) {
			;
		}
	}
	
	public EnclosingTarget(Object obj) {
		PointcutTarget pt = new PointcutTarget(1);
		
		m_field = obj;
		
		pt.setFieldValue(obj);
		pt.getFieldValue();
	}
}
